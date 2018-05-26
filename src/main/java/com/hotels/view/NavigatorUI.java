package com.hotels.view;

import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ContextLoaderListener;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.EnableVaadin;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@Theme ("mytheme")
@SpringUI
public class NavigatorUI extends UI {

    @WebListener
    public static class MyContextLoaderListener extends ContextLoaderListener {
    }

    @Configuration
    @EnableVaadin
    public static class MyConfiguration {
    }

    private static final long serialVersionUID = -4705632432578039781L;
    private static final String HOTEL_VIEW = "Hotel";
    private static final String CATEGORY_VIEW = "Category";

    private final Panel panel = new Panel();
    private final Navigator navigator = new Navigator(this, panel);

    private final MenuBar menu = new MenuBar();
    private final VerticalLayout layout = new VerticalLayout();

    @Override
    protected void init (VaadinRequest request) {
        layout.addComponents(menu, panel);
        setContent(layout);

        navigator.addView(CATEGORY_VIEW, new CategoryView());
        navigator.addView(HOTEL_VIEW, new HotelView());

        menuCreating();
        
        Page.getCurrent().setTitle("Hotels");
    }

    @SuppressWarnings ("serial")
    public void menuCreating () {
        MenuBar.Command command = new MenuBar.Command() {
            MenuItem previous = null;

            @Override
            public void menuSelected (MenuItem selectedItem) {
                if (previous != null) previous.setStyleName(null);

                previous = selectedItem;
                selectedItem.setStyleName("highlight");

                navigator.navigateTo(selectedItem.getText());
            }
        };
        MenuItem hotel = menu.addItem(HOTEL_VIEW, VaadinIcons.BUILDING, command);
        menu.addItem(CATEGORY_VIEW, VaadinIcons.RECORDS, command);
        command.menuSelected(hotel);

        menu.setStyleName(ValoTheme.MENUBAR_BORDERLESS);
        panel.setSizeFull();
        panel.setStyleName(ValoTheme.PANEL_BORDERLESS);
        menu.setId("NavigatorMenu");
    }

    @WebServlet (urlPatterns = "/*", name = "NavigatorUIServlet", asyncSupported = true)
    @VaadinServletConfiguration (ui = NavigatorUI.class, productionMode = false)
    public static class NavigatorUIServlet extends VaadinServlet {

        private static final long serialVersionUID = -8122768127039857033L;
    }
}
