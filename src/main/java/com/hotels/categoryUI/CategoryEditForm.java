package com.hotels.categoryUI;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Notification.Type;

public class CategoryEditForm extends FormLayout {
    private static final long serialVersionUID = -2639909361527714997L;
    
    private CategoryView ui;
    private final CategoryService categoryService = CategoryService.getInstance();
    private TextField name = new TextField("Name");
    private Category category;
    private Button save = new Button("Save");
    private Button close = new Button("Close");
    private HorizontalLayout buttons = new HorizontalLayout();
    private Binder<Category> binder = new Binder<>(Category.class);
    
    public CategoryEditForm (CategoryView ui) {
        this.ui = ui;
        this.setVisible(false);
        
        binder.forField(name).asRequired("Please enter a name").bind(Category::getName, Category::setName);
        name.setDescription("Category name - String, not empty");
        name.setId("CategoryName");
        
        buttons.addComponents(save, close);
        save.setId("Save");
        save.addClickListener(e -> save());
        close.addClickListener(e -> close());
        
        addComponents(name, buttons);
        binder.bindInstanceFields(this);
    }
    
    private void close () {
        setVisible(false);
        category = null;
    }

    private void save () {
        if (binder.isValid()) {
            try {
                binder.writeBean(category);
            } catch (ValidationException e) {
                Notification.show("Unable to save! " + e.getMessage(), Type.ERROR_MESSAGE);
            }
            categoryService.save(category);
            ui.updateList();
            Notification.show("Category " + category.getName() + " saved", Type.TRAY_NOTIFICATION);
            close();
        } else Notification.show("Unable to save! please review errors and fill name field", Type.ERROR_MESSAGE);
    }
    

    public void setCategory (Category category) {
        this.category = category;
        binder.readBean(this.category);
        setVisible(true);
    }
}
