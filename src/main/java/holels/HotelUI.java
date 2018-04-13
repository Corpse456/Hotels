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
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

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
    private final TextField filter = new TextField();
    private final Button addHotel = new Button("Add hotel");
    private final Button deleteHotel = new Button("Delete hotel");
    private final HotelEditForm form = new HotelEditForm(this);

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        grid.setColumnOrder("name", "address", "rating", "category");
        grid.setWidth(1500, Unit.PIXELS);
        grid.setHeight(850, Unit.PIXELS);
        grid.asSingleSelect().addValueChangeListener(e -> {
            if (e.getValue() != null) {
                deleteHotel.setEnabled(true);
                form.setHotel(e.getValue());
            }
            if (e.getValue() == null) {
                deleteHotel.setEnabled(false);
                form.setVisible(false);
            }
        });

        filter.addValueChangeListener(e -> updateList());
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        updateList();

        addHotel.addClickListener(e -> form.setHotel(new Hotel()));
        
        deleteHotel.setEnabled(false);
        deleteHotel.addClickListener(e -> {
            Hotel delCandidate = grid.getSelectedItems().iterator().next(); 
            service.delete(delCandidate);
            deleteHotel.setEnabled(false);
            updateList();
        });
        
        HorizontalLayout controls = new HorizontalLayout();
        controls.addComponents(filter, addHotel, deleteHotel);
        
        HorizontalLayout content = new HorizontalLayout();
        content.addComponents(grid, form);
        
        layout.addComponents(controls, content);
        setContent(layout);
    }

    public int updateList() {
        List<Hotel> hotelList = service.findAll(filter.getValue());
        grid.setItems(hotelList);
        return 0;
    }

    @WebServlet(urlPatterns = "/*", name = "HotelUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = HotelUI.class, productionMode = false)
    public static class HotelUIServlet extends VaadinServlet {

        private static final long serialVersionUID = -8122768127039857033L;
    }
}
