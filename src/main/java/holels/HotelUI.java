package holels;

import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.HtmlRenderer;

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
    private final Grid<Hotel> grid = new Grid<>(Hotel.class);
    private final TextField nameFilter = new TextField();
    private final TextField addressFilter = new TextField();
    private final Button addHotel = new Button("Add hotel");
    private final Button deleteHotel = new Button("Delete hotel");
    private final HotelEditForm form = new HotelEditForm(this);

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        grid.setColumnOrder("name", "address", "rating", "category");
        /*grid.setWidth(1500, Unit.PIXELS);
        grid.setHeight(850, Unit.PIXELS);*/
        grid.asSingleSelect().addValueChangeListener(e -> {
            if (e.getValue() != null) {
                deleteHotel.setEnabled(true);
                form.setHotel(e.getValue());
            }
            if (e.getValue() == null && deleteHotel.isEnabled()) {
                deleteHotel.setEnabled(false);
                form.setVisible(false);
            }
        });

        nameFilter.setPlaceholder("Name Filter");
        nameFilter.addValueChangeListener(e -> updateList());
        nameFilter.setValueChangeMode(ValueChangeMode.LAZY);
        
        addressFilter.setPlaceholder("Address filter");
        addressFilter.addValueChangeListener(e -> updateList());
        addressFilter.setValueChangeMode(ValueChangeMode.LAZY);

        addHotel.addClickListener(e -> form.setHotel(new Hotel()));
        
        deleteHotel.setEnabled(false);
        deleteHotel.addClickListener(e -> {
            Hotel delCandidate = grid.getSelectedItems().iterator().next(); 
            service.delete(delCandidate);
            deleteHotel.setEnabled(false);
            updateList();
            form.setVisible(false);
        });
        
        grid.setColumns("name", "address", "rating", "category", "operatesFrom", "description");
        Column<Hotel, String> htmlColumn = grid.addColumn(hotel -> urlAsHtmlLink(hotel.getUrl()),
        new HtmlRenderer());
        htmlColumn.setCaption("URL");
        
        HorizontalLayout controls = new HorizontalLayout();
        controls.addComponents(nameFilter, addressFilter, addHotel, deleteHotel);
        
        HorizontalLayout content = new HorizontalLayout();
        content.addComponents(grid, form);
        grid.setWidth(100, Unit.PERCENTAGE);
        grid.setHeight(600, Unit.PIXELS);
        content.setWidth(100, Unit.PERCENTAGE);
        
        layout.addComponents(controls, content);
        setContent(layout);
        updateList();
        
        Notification.show("Welcome to our website", Type.TRAY_NOTIFICATION);
    }
    
    private String urlAsHtmlLink(String url) {
	int beginIndex = url.indexOf("//") + "//".length();
	String shortUrl = url.substring(beginIndex, url.indexOf("/", beginIndex));
        return "<a href=\"" + url + "\" target=\"_blank\">" + shortUrl + "</a>";
    }

    public int updateList() {
        List<Hotel> hotelList = service.findAll(nameFilter.getValue(), addressFilter.getValue());
        grid.setItems(hotelList);
        return 0;
    }

    @WebServlet(urlPatterns = "/*", name = "HotelUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = HotelUI.class, productionMode = false)
    public static class HotelUIServlet extends VaadinServlet {

        private static final long serialVersionUID = -8122768127039857033L;
    }
}
