package hotels;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.Navigator.ComponentContainerViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.themes.ValoTheme;

import hotels.categoryUI.CategoryView;
import hotels.holelsUI.HotelView;

public class NavigatorUI extends UI {
    private static final long serialVersionUID = -4705632432578039781L;
    public static final String HOTEL_VIEW = "Hotel";
    public static final String CATEGORY_VIEW = "Category";
    public Navigator navigator;
    private MenuBar menu = new MenuBar();
    private MenuItem hotelItem;
    private MenuItem categoryItem;

    public Navigator getNavigator () {
        return navigator;
    }

    @Override
    protected void init (VaadinRequest request) {
        final VerticalLayout layout = new VerticalLayout();
        setContent(layout);
        
        ComponentContainerViewDisplay viewDisplay = new ComponentContainerViewDisplay(layout);
        navigator = new Navigator(UI.getCurrent(), viewDisplay);
        
        menuCreating();

        navigator.addView(HOTEL_VIEW, new HotelView(this));
        navigator.addView(CATEGORY_VIEW, new CategoryView(this));
        
        navigator.navigateTo(HOTEL_VIEW);
    }
    
    public MenuBar menuCreating () {
        menu = new MenuBar();
        MenuBar.Command command = selectedItem -> {
            if (selectedItem.equals(hotelItem)) return;
            hotelItem.setStyleName(null);
            navigator.navigateTo(selectedItem.getText());
        };
        hotelItem = menu.addItem(HOTEL_VIEW, VaadinIcons.BUILDING, command);
        categoryItem = menu.addItem(CATEGORY_VIEW, VaadinIcons.RECORDS, command);

        hotelItem.setCommand(command);
        categoryItem.setCommand(command);

        menu.setStyleName(ValoTheme.MENUBAR_BORDERLESS);
        return menu;
    }
    
    @WebServlet(urlPatterns = "/*", name = "NavigatorUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = NavigatorUI.class, productionMode = false)
    public static class NavigatorUIServlet extends VaadinServlet {

        private static final long serialVersionUID = -8122768127039857033L;
    }
}
