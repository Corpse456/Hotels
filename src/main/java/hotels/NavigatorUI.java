package hotels;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.Navigator.ComponentContainerViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import hotels.categoryUI.CategoryView;
import hotels.holelsUI.HotelView;

public class NavigatorUI extends UI {
    private static final long serialVersionUID = -4705632432578039781L;
    public static final String HOTEL_VIEW = "Hotel";
    public static final String CATEGORY_VIEW = "Category";
    public Navigator navigator;

    public Navigator getNavigator () {
        return navigator;
    }

    @Override
    protected void init (VaadinRequest request) {
        final VerticalLayout layout = new VerticalLayout();
        
        setContent(layout);
        
        ComponentContainerViewDisplay viewDisplay = new ComponentContainerViewDisplay(layout);
        navigator = new Navigator(UI.getCurrent(), viewDisplay);

        navigator.addView(HOTEL_VIEW, new HotelView());
        navigator.addView(CATEGORY_VIEW, new CategoryView());
        
        navigator.navigateTo(HOTEL_VIEW);
    }
    
    @WebServlet(urlPatterns = "/*", name = "NavigatorUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = NavigatorUI.class, productionMode = false)
    public static class NavigatorUIServlet extends VaadinServlet {

        private static final long serialVersionUID = -8122768127039857033L;
    }
}
