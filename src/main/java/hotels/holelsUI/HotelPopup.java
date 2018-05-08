package hotels.holelsUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import hotels.categoryUI.Category;
import hotels.categoryUI.CategoryService;

@SuppressWarnings ("serial")
public class HotelPopup extends PopupView {
    
    private final VerticalLayout popupContent;
    private final Label title = new Label("Bulk update");
    private final TextField fieldValue = new TextField();
    private final DateField operatesFrom = new DateField();
    private final NativeSelect<String> category = new NativeSelect<>();
    private final NativeSelect<String> selection = new NativeSelect<>();
    private final HorizontalLayout buttons = new HorizontalLayout();
    private final HorizontalLayout fields = new HorizontalLayout();
    private final Button update = new Button("Update");
    private final Button cancel = new Button("Cancel");
    
    private String currentSelectedField;
    private List<String> fieldNames = Arrays.asList("Name", "Address", "Rating", "Date", "Category", "Description", "Url");
    private CategoryService categoryService = CategoryService.getInstance();
    
    public HotelPopup (VerticalLayout verticalLayout) {
        super(null, verticalLayout);
        this.popupContent = verticalLayout;
        
        fieldValue.setPlaceholder("Field value");
        
        selectionSetup();
        
        updateSetup();
        
        cancelSetup();
        buttons.addComponents(update, cancel);
        
        popupContent.addComponents(title, selection, fields, buttons);
        
        setHideOnMouseOut(false);
        addListener(event -> {
           if (!isPopupVisible()) {
               fields.removeAllComponents();
               selection.setValue(selection.getEmptySelectionCaption());
               System.out.println(fields.getComponentCount());
           }
        });
    }

    private void cancelSetup () {
        cancel.addClickListener(click -> {
            fields.removeAllComponents();
            selection.setValue(selection.getEmptySelectionCaption());
            setPopupVisible(false);
        });
    }

    private void selectionSetup () {
        selection.setItems(fieldNames);
        selection.setEmptySelectionCaption("Please select field");
        selection.setValue(selection.getEmptySelectionCaption());
        selection.addSelectionListener(event -> {
           currentSelectedField = event.getValue();
           fields.removeAllComponents();
           
           if (currentSelectedField == null) {
               fields.removeAllComponents();
           } else if (fieldNames.get(3).equals(currentSelectedField)) {
               fields.addComponent(operatesFrom);
           } else if (fieldNames.get(4).equals(currentSelectedField)) {
               categoryAdd();
           } else {
               fields.addComponent(fieldValue);
           }
        });
    }

    private void categoryAdd () {
        category.setItems(categoryNames());
        fields.addComponent(category);
    }

    private void updateSetup () {
        update.addClickListener(click -> {
            switch (selection.getValue()) {
            case "Name":
                
                break;

            default:
                break;
            }
        });
    }
    
    private List<String> categoryNames () {
        List<Category> categories = categoryService.findAll();
        List<String> categoryNames = new ArrayList<>();
        for (Category category : categories) {
            categoryNames.add(category.getName());
        }
        return categoryNames;
    }
}
