package holels;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.event.selection.MultiSelectionListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.themes.ValoTheme;

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of an HTML page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@Theme("mytheme")
public class HotelUI extends UI {

    private static final long serialVersionUID = -4501291059692502904L;
    private final VerticalLayout layout = new VerticalLayout();
    private final HotelService service = HotelService.getInstance();
    private final Grid<Hotel> grid = new Grid<>();
    private final TextField nameFilter = new TextField();
    private final TextField addressFilter = new TextField();
    private final Button addHotel = new Button("Add hotel");
    private final Button deleteHotel = new Button("Delete hotel");
    private final Button editHotel = new Button("Edit hotel");
    private final Button editCategory = new Button("Edit category");
    private final HotelEditForm form = new HotelEditForm(this);
    private final Label status = new Label();
    private final MenuBar menu = new MenuBar();
    private final HorizontalLayout controls = new HorizontalLayout();
    private final HorizontalLayout content = new HorizontalLayout();

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        menuCreating();
        
        gridSetUp();
        
        formSetUp();

        filtersSetUp();

        addHotel.addClickListener(e -> form.setHotel(new Hotel()));
        
        deleteSetUp();
        editSetUp();
        controls.addComponents(nameFilter, addressFilter, addHotel, deleteHotel, editHotel, editCategory);
        
        contentSetUp();
        
        layout.addComponents(menu, status, controls, content);
        
        setContent(layout);
        
        updateList();
        
        Notification.show("Welcome to our website", Type.TRAY_NOTIFICATION);
    }

    private void editSetUp () {
        editHotel.setEnabled(false);
        editHotel.addClickListener(e -> {
            Hotel editCandidate = grid.getSelectedItems().iterator().next();
            form.setHotel(editCandidate);
        });
    }

    private void contentSetUp () {
        content.addComponents(grid, form);
        content.setWidth(100, Unit.PERCENTAGE);
        content.setExpandRatio(grid, 0.7f);
        content.setExpandRatio(form, 0.3f);
        content.setComponentAlignment(form, Alignment.MIDDLE_CENTER);
    }

    private void deleteSetUp () {
        deleteHotel.setEnabled(false);
        deleteHotel.addClickListener(e -> {
            Iterator<Hotel> delCandidates = grid.getSelectedItems().iterator();
            while (delCandidates.hasNext()) {
                Hotel delCandidate = delCandidates.next();
                notification(grid.getSelectedItems(), delCandidate);
                service.delete(delCandidate);
            }
            deleteHotel.setEnabled(false);
            updateList();
        });
    }

    private void notification (Set<Hotel> set, Hotel delCandidate) {
        if (set.size() > 1) Notification.show(set.size() + " hotels deleted", Type.TRAY_NOTIFICATION);
        else Notification.show("Hotel " + delCandidate.getName() + " deleted", Type.TRAY_NOTIFICATION);
    }

    private void gridSetUp () {
        grid.setSelectionMode(SelectionMode.MULTI);
        grid.asMultiSelect().addSelectionListener(listener());
        grid.setWidth(100, Unit.PERCENTAGE);
        grid.setHeight(700, Unit.PIXELS);
        gridAddColumns();
    }

    private void gridAddColumns () {
        grid.addColumn(Hotel::getName).setCaption("Name");
        grid.addColumn(Hotel::getAddress).setCaption("Address");
        grid.addColumn(Hotel::getRating).setCaption("Rating");
        grid.addColumn(hotel -> hotel.getCategory() != null && !hotel.getCategory().isEmpty() ? hotel.getCategory() : "No category").setCaption("Category");
        grid.addColumn(hotel -> LocalDate.ofEpochDay(hotel.getOperatesFrom())).setCaption("Operates from");
        grid.addColumn(Hotel::getDescription).setCaption("Description");
        grid.addColumn(hotel -> "<a href=\"" + hotel.getUrl() + "\" target=\"_blank\">hotel info</a>", new HtmlRenderer()).setCaption("URL");
    }

    private MultiSelectionListener<Hotel> listener () {
        return e -> {
            Set<Hotel> value = e.getValue();
            
            if (value.size() == 0) {
                deleteHotel.setEnabled(false);
                editHotel.setEnabled(false);
            }
            if (value.size() == 1) {
                editHotel.setEnabled(true);
                deleteHotel.setEnabled(true);
            }
            if (value.size() > 1) {
                editHotel.setEnabled(false);
                deleteHotel.setEnabled(true);
            }
        };
    }
    
    private void formSetUp () {
        form.setWidth("90%");
        form.setHeight(100, Unit.PERCENTAGE);
        form.setVisible(false);
    }

    private void filtersSetUp () {
        nameFilter.setPlaceholder("filter by name");
        nameFilter.addValueChangeListener(e -> updateList());
        nameFilter.setValueChangeMode(ValueChangeMode.LAZY);
        
        addressFilter.setPlaceholder("filter by name");
        addressFilter.addValueChangeListener(e -> updateList());
        addressFilter.setValueChangeMode(ValueChangeMode.LAZY);
    }
    
    private void menuCreating () {
        
        MenuBar.Command command = new MenuBar.Command() {
            private static final long serialVersionUID = -6641046536068795991L;
            MenuItem previous = null;
            
            @Override
            public void menuSelected (MenuItem selectedItem) {
                String notif = "You are now in: " + selectedItem.getText();
                status.setValue(notif);
                
                if (previous != null) previous.setStyleName(null);
                selectedItem.setStyleName("highlight");
                
                previous = selectedItem;
            }
        };
        
        MenuItem hotelItem = menu.addItem("Hotel", VaadinIcons.BUILDING, command);
        MenuItem categoryItem = menu.addItem("Category", VaadinIcons.RECORDS, command);
        
        command.menuSelected(hotelItem);
        
        menu.setStyleName(ValoTheme.MENUBAR_BORDERLESS);
    }

    public void updateList() {
        List<Hotel> hotelList = service.findAll(nameFilter.getValue(), addressFilter.getValue());
        grid.setItems(hotelList);
    }

    @WebServlet(urlPatterns = "/*", name = "HotelUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = HotelUI.class, productionMode = false)
    public static class HotelUIServlet extends VaadinServlet {

        private static final long serialVersionUID = -8122768127039857033L;
    }
}
