package com.hotels.view;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.hotels.entities.Category;
import com.hotels.services.CategoryService;
import com.hotels.view.form.CategoryEditForm;
import com.vaadin.event.selection.MultiSelectionListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Notification.Type;

public class CategoryView extends VerticalLayout implements View {

    private static final long serialVersionUID = 890806074356068662L;
    private final Label status = new Label();
    private final CategoryService service = CategoryService.getInstance();
    private final Grid<Category> grid = new Grid<>();
    private final HorizontalLayout controls = new HorizontalLayout();
    private final HorizontalLayout content = new HorizontalLayout();
    private final Button addCategory = new Button("Add category");
    private final Button deleteCategory = new Button("Delete category");
    private final Button editCategory = new Button("Edit category");
    private final TextField filter = new TextField();
    private final CategoryEditForm form = new CategoryEditForm(this);
    
    @Override
    public void enter (ViewChangeEvent event) {
        Notification.show("Showing view: Category");
        updateList();
    }

    public CategoryView () {
        gridSetUp();
        content.addComponents(grid, form);
        
        filtersSetUp();
        deleteSetUp();
        editSetUp();
        addSetup();
        controls.addComponents(filter, addCategory, deleteCategory, editCategory);
        
        statusSetUp();
        addComponents(status, controls, content);
    }

    private void addSetup () {
        addCategory.addClickListener(e -> form.setCategory(new Category()));
        addCategory.setId("Add");
    }
    
    private void gridSetUp () {
        grid.setSelectionMode(SelectionMode.MULTI);
        grid.asMultiSelect().addSelectionListener(listener());
        grid.addColumn(Category::getName).setCaption("Category List");
        
        updateList();
    }

    private MultiSelectionListener<Category> listener () {
        return e -> {
            Set<Category> value = e.getValue();
            
            if (value.size() == 0) deleteCategory.setEnabled(false);
            else deleteCategory.setEnabled(true);
            
            if (value.size() == 1) {
                editCategory.setEnabled(true);
            }
            if (value.size() != 1) {
                editCategory.setEnabled(false);
                form.setVisible(false);
            }
        };
    }

    private void editSetUp () {
        editCategory.setEnabled(false);
        editCategory.addClickListener(e -> {
            Category editCandidate = grid.getSelectedItems().iterator().next();
            form.setCategory(editCandidate);
        });
    }
    
    private void deleteSetUp () {
        deleteCategory.setEnabled(false);
        
        deleteCategory.addClickListener(e -> {
            Iterator<Category> delCandidates = grid.getSelectedItems().iterator();
            while (delCandidates.hasNext()) {
                Category delCandidate = delCandidates.next();
                notification(grid.getSelectedItems(), delCandidate);
                service.delete(delCandidate);
            }
            deleteCategory.setEnabled(false);
            form.setVisible(false);
            updateList();
        });
    }
    
    private void notification (Set<Category> set, Category delCandidate) {
        if (set.size() > 1) Notification.show(set.size() + " categories deleted", Type.TRAY_NOTIFICATION);
        else Notification.show("Category " + delCandidate.getName() + " deleted", Type.TRAY_NOTIFICATION);
    }
    
    public void updateList () {
        List<Category> categoryList = service.findAll(filter.getValue());
        grid.setItems(categoryList);
    }

    private void filtersSetUp () {
        filter.setPlaceholder("Start typing a name...");
        filter.addValueChangeListener(e -> updateList());
        filter.setValueChangeMode(ValueChangeMode.LAZY);
    }

    private void statusSetUp () {
        String notif = "You are now in: Category";
        status.setValue(notif);
    }
}
