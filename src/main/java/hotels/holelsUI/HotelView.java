package hotels.holelsUI;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.vaadin.event.selection.MultiSelectionListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.HtmlRenderer;

import hotels.NavigatorUI;

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of an HTML page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */

public class HotelView extends VerticalLayout implements View {

    private static final long serialVersionUID = -4501291059692502904L;
    private final HotelService hotelService = HotelService.getInstance();
    private final Grid<Hotel> grid = new Grid<>();
    private final TextField nameFilter = new TextField();
    private final TextField addressFilter = new TextField();
    private final Button addHotel = new Button("Add hotel");
    private final Button deleteHotel = new Button("Delete hotel");
    private final Button editHotel = new Button("Edit hotel");
    private final HotelEditForm form = new HotelEditForm(this);
    private final Label status = new Label();
    private final HorizontalLayout controls = new HorizontalLayout();
    private final HorizontalLayout content = new HorizontalLayout();
    private final MenuBar menu;

    @Override
    public void enter (ViewChangeEvent event) {
        Notification.show("Showing view: Hotels");
        updateList();
    }

    public HotelView (NavigatorUI ui) {
        gridSetUp();

        formSetUp();

        filtersSetUp();

        addHotel.addClickListener(e -> form.setHotel(new Hotel()));

        deleteSetUp();
        editSetUp();
        controls.addComponents(nameFilter, addressFilter, addHotel, deleteHotel, editHotel);

        contentSetUp();
        statusSetUp();
        menu = ui.menuCreating();
        addComponents(menu, status, controls, content);

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
                hotelService.delete(delCandidate);
            }
            deleteHotel.setEnabled(false);
            form.setVisible(false);
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
        grid.setHeight(650, Unit.PIXELS);
        gridAddColumns();
    }

    private void gridAddColumns () {
        grid.addColumn(Hotel::getName).setCaption("Name");
        grid.addColumn(Hotel::getAddress).setCaption("Address");
        grid.addColumn(Hotel::getRating).setCaption("Rating");
        grid.addColumn(hotel -> hotel.getCategory().getId() != null ? hotel.getCategory().getName() : "No category")
                .setCaption("Category");
        grid.addColumn(hotel -> LocalDate.ofEpochDay(hotel.getOperatesFrom())).setCaption("Operates from");
        grid.addColumn(Hotel::getDescription).setCaption("Description");
        grid.addColumn(hotel -> "<a href=\"" + hotel.getUrl() + "\" target=\"_blank\">hotel info</a>",
                new HtmlRenderer()).setCaption("URL");
    }

    private MultiSelectionListener<Hotel> listener () {
        return e -> {
            Set<Hotel> value = e.getValue();

            if (value.size() == 0) deleteHotel.setEnabled(false);
            else deleteHotel.setEnabled(true);
            
            if (value.size() == 1) editHotel.setEnabled(true);
            
            if (value.size() != 1) {
                editHotel.setEnabled(false);
                form.setVisible(false);
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

        addressFilter.setPlaceholder("filter by address");
        addressFilter.addValueChangeListener(e -> updateList());
        addressFilter.setValueChangeMode(ValueChangeMode.LAZY);
    }

    private void statusSetUp () {
        String notif = "You are now in: Hotel";
        status.setValue(notif);
    }

    public void updateList () {
        List<Hotel> hotelList = hotelService.findAll(nameFilter.getValue(), addressFilter.getValue());
        grid.setItems(hotelList);
    }
}
