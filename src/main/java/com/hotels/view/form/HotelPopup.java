package com.hotels.view.form;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.hotels.constants.HotelFieldDescription;
import com.hotels.constants.HotelFieldNames;
import com.hotels.entities.Category;
import com.hotels.entities.Hotel;
import com.hotels.services.CategoryService;
import com.hotels.services.HotelService;
import com.hotels.view.HotelView;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings ("serial")
public class HotelPopup extends PopupView {

    private final String defaultSelectText = "Please select field";
    private final VerticalLayout popupContent;
    private final Label title = new Label("Bulk update");
    private final TextField fieldValue = new TextField();
    private final DateField operatesFrom = new DateField();
    private final NativeSelect<String> categorySelect = new NativeSelect<>();
    private final NativeSelect<String> selection = new NativeSelect<>();
    private final HorizontalLayout buttons = new HorizontalLayout();
    private final HorizontalLayout fields = new HorizontalLayout();
    private final Button update = new Button("Update");
    private final Button cancel = new Button("Cancel");

    //private Binder<Hotel> binder;
    private String currentSelectedField;
    private CategoryService categoryService = CategoryService.getInstance();
    private HotelService hotelService = HotelService.getInstance();
    private Set<Hotel> hotelList;
    private HotelView ui;

    public HotelPopup (HotelView ui, VerticalLayout verticalLayout) {
        super(null, verticalLayout);
        this.ui = ui;
        this.popupContent = verticalLayout;

        fieldValueSetup();
        operatesFromSetup();
        categorySelectSetup();
        
        selectionSetup();

        updateSetup();

        cancelSetup();
        update.setEnabled(false);
        buttons.addComponents(update, cancel);
        //binder = ui.getForm().getBinder();

        popupContent.addComponents(title, selection, fields, buttons);

        setHideOnMouseOut(false);
    }

    private void categorySelectSetup () {
        categorySelect.addSelectionListener(select -> categoryChangeListener(select.getSelectedItem()));
    }

    private void categoryChangeListener (Optional<String> optional) {
        if (!optional.isPresent()) {
            update.setEnabled(false);
        } else {
            update.setEnabled(true);
        }
    }

    private void operatesFromSetup () {
        operatesFrom.addValueChangeListener(choise -> dateChangeListener());
    }

    private void dateChangeListener () {
        if (operatesFrom.getValue() == null) return;
        
        if (LocalDate.now().isAfter(operatesFrom.getValue())) {
            update.setEnabled(true);
        } else {
            update.setEnabled(false);
        }
    }

    private void fieldValueSetup () {
        fieldValue.setPlaceholder("Field value");
        fieldValue.addValueChangeListener(input -> textValueChangeListener());
    }

    private void textValueChangeListener () {
        String value = fieldValue.getValue();
        if (value.isEmpty() && !HotelFieldNames.Description.toString().equals(currentSelectedField)) {
            update.setEnabled(false);
            return;
        }

        if (HotelFieldNames.Address.toString().equals(currentSelectedField)) {
            if (value.length() < 5) {
                update.setEnabled(false);
                return;
            }
        }

        if (HotelFieldNames.Rating.toString().equals(currentSelectedField)) {
            int parseInt = -1;
            try {
                parseInt = Integer.parseInt(value);
            } catch (NumberFormatException e) {
            }
            if (!(parseInt >= 0 && parseInt < 6)) {
                update.setEnabled(false);
                return;
            }
        }
        update.setEnabled(true);
    }

    private void cancelSetup () {
        cancel.addClickListener(click -> {
            selection.setSelectedItem(selection.getEmptySelectionCaption());
            fields.removeAllComponents();
            currentSelectedField = null;
            fieldValue.clear();
            operatesFrom.setValue(null);
            update.setEnabled(false);
            setPopupVisible(false);
        });
    }

    private void selectionSetup () {
        selection.setItems(Stream.of(HotelFieldNames.values()).map(Enum::name).collect(Collectors.toList()));
        selection.setEmptySelectionCaption(defaultSelectText);
        selection.setValue(selection.getEmptySelectionCaption());
        selection.addSelectionListener(event -> {
            fields.removeAllComponents();
            currentSelectedField = event.getValue();

            if (currentSelectedField == null) {
                update.setEnabled(false);
            } else if (HotelFieldNames.OperatesFrom.toString().equals(currentSelectedField)) {
                fields.addComponent(operatesFrom);
                dateChangeListener();
                operatesFrom.setDescription(HotelFieldDescription.getDescription(HotelFieldNames.OperatesFrom));
            } else if (HotelFieldNames.Category.toString().equals(currentSelectedField)) {
                categoryAdd();
            } else {
                fields.addComponent(fieldValue);
                textValueChangeListener();
                if (currentSelectedField != null && !defaultSelectText.equals(currentSelectedField)) {
                    fieldValue.setDescription(HotelFieldDescription.getDescription(HotelFieldNames.valueOf(currentSelectedField)));
                }
            }
        });
    }

    private void categoryAdd () {
        categorySelect.setItems(categoryNames());
        fields.addComponent(categorySelect);
        categoryChangeListener(categorySelect.getSelectedItem());
        categorySelect.setDescription(HotelFieldDescription.getDescription(HotelFieldNames.Category));
    }

    private void updateSetup () {
        update.addClickListener(click -> {   
            cltkfnm dataprovider
            b binder
            input.setComponentError(new UserError("This field is never satisfied."));
            /*if (!binder.isValid()) {
                binder.getFields().forEach(a -> System.err.println(a.getValue()));
                Notification.show("Wrong");
                return;
            }*/
            
            if (HotelFieldNames.OperatesFrom.toString().equals(currentSelectedField)) {
                hotelFiledChange("operatesFrom", operatesFrom.getValue().getLong(ChronoField.EPOCH_DAY));
            } else if (HotelFieldNames.Category.toString().equals(currentSelectedField)) {
                String categoryName = categorySelect.getValue();
                List<Category> categories = categoryService.findAll();
                categories.forEach(category -> {
                    if (category.getName().equals(categoryName)) {
                        hotelFiledChange(currentSelectedField.toLowerCase(), category);
                    }
                });
            } else if (HotelFieldNames.Rating.toString().equals(currentSelectedField)) {
                hotelFiledChange(currentSelectedField.toLowerCase(), Integer.parseInt(fieldValue.getValue()));
            } else {
                hotelFiledChange(currentSelectedField.toLowerCase(), fieldValue.getValue());
            }
            cancel.click();
            ui.updateList();
        });
    }

    private void hotelFiledChange (String fieldName, Object value) {
        Iterator<Hotel> iterator = hotelList.iterator();
        while (iterator.hasNext()) {
            Hotel editable = iterator.next();
            Class<? extends Hotel> hotelClass = editable.getClass();
            try {
                Field hotelField = hotelClass.getDeclaredField(fieldName);
                hotelField.setAccessible(true);
                hotelField.set(editable, value);
                hotelService.save(editable);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setHotelList (Set<Hotel> hotelList) {
        this.hotelList = hotelList;
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
