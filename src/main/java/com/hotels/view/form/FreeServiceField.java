package com.hotels.view.form;

import com.hotels.entities.FreeServices;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;

@SuppressWarnings ("serial")
public class FreeServiceField extends CustomField<FreeServices> {

    private CheckBox breakfast = new CheckBox();
    private CheckBox towels = new CheckBox();
    private CheckBox spirits = new CheckBox();
    
    private FreeServices value;
    private String caption = "Free Services";
    
    public FreeServiceField (String caption) {
        this.caption = caption;
    }

    @Override
    public FreeServices getValue () {
        return value;
    }

    @Override
    protected Component initContent () {
        HorizontalLayout layout = new HorizontalLayout();
        super.setCaption(caption);
        
        breakfast.setIcon(VaadinIcons.CUTLERY);
        towels.setIcon(VaadinIcons.RASTER);
        spirits.setIcon(VaadinIcons.COFFEE);
        
        breakfast.setDescription("Breakfast");
        towels.setDescription("Towels");
        spirits.setDescription("Spirits");
        
        breakfast.addValueChangeListener(l -> value.setBreakfast(l.getValue()));
        towels.addValueChangeListener(l -> value.setTowels(l.getValue()));
        spirits.addValueChangeListener(l -> value.setColdSpirits(l.getValue()));
        
        breakfast.setId("breakfast");
        towels.setId("towels");
        spirits.setId("spirits");
        
        layout.addComponent(breakfast);
        layout.addComponent(towels);
        layout.addComponent(spirits);
        
        value = new FreeServices();
        
        updateValues();
        
        return layout;
    }

    private void updateValues () {
        if (getValue() != null) {
            breakfast.setValue(value.isBreakfast());
            towels.setValue(value.isTowels());
            spirits.setValue(value.isColdSpirits());
        }
    }

    @Override
    protected void doSetValue (FreeServices value) {
        this.value = new FreeServices(value);
        updateValues();
    }

    @Override
    public String toString () {
        return "breakfast=" + breakfast + ", towels=" + towels + ", spirits=" + spirits;
    }
    
    
}
