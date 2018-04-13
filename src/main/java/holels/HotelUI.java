package holels;

import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Grid;
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

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        grid.setItems(service.findAll());
        grid.setColumnOrder("name", "address", "rating", "category");

        grid.setWidth(100, Unit.PERCENTAGE);
        grid.setHeight(850, Unit.PIXELS);

        filter.addValueChangeListener(e -> updateList());
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        updateList();

        layout.addComponents(filter, grid);
        setContent(layout);
    }

    private int updateList() {
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
