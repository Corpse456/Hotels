package com.hotels.holelsUI;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings ("serial")
public class PaymentMethodField extends CustomField<PaymentMethod> {
    
    private static final String CASH = "Cash";
    private static final String CARD = "Credit Card";
    
    private VerticalLayout layout = new VerticalLayout();
    private RadioButtonGroup<String> radiobutton = new RadioButtonGroup<>();
    private TextField prepayment = new TextField();
    private Label cashLabel = new Label("Payment will be made directly in the hotel");
    private PaymentMethod method;
    private String caption = "Payment method";
    private Integer oldValue = null;
    
    public PaymentMethodField (String caption) {
        this.caption = caption;
    }

    @Override
    public PaymentMethod getValue () {
        return method;
    }

    @Override
    protected Component initContent () {
        super.setCaption(caption);
        
        radiButtonSetup();
        
        method = new PaymentMethod();
        
        setWidth();
        setIds();
        
        cashLabel.setVisible(false);
        
        prepaymentSetup();
        
        layoutSetup();
        
        updateValues();
        
        return layout;
    }

    private void setIds () {
        radiobutton.setId("HotelRadioButton");
        prepayment.setId("HotelPrepayment");
    }

    private void layoutSetup () {
        layout.setMargin(false);
        layout.addComponents(radiobutton, cashLabel, prepayment);
    }

    private void radiButtonSetup () {
        radiobutton.setItems(CARD, CASH);
        radiobutton.setStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
        radiobutton.setPrimaryStyleName(ValoTheme.OPTIONGROUP_SMALL);
        radiobutton.addValueChangeListener(changeListener());
        radiobutton.setItemDescriptionGenerator((item) -> {
            if (item.equals(CARD)) return "Credit card deposit";
            else return "Cash payment";
        });;
    }

    private void setWidth () {
        radiobutton.setWidth(100, Unit.PERCENTAGE);
        cashLabel.setWidth(100, Unit.PERCENTAGE);
        prepayment.setWidth(100, Unit.PERCENTAGE);
        layout.setWidth(100, Unit.PERCENTAGE);
    }

    private void prepaymentSetup () {
        prepayment.setDescription("Prepayment on the card in percent");
        prepayment.setPlaceholder("Guaranty Deposit");
        prepayment.setVisible(false);
        //prepayment.setValueChangeMode(ValueChangeMode.LAZY);
        prepayment.addValueChangeListener(l -> {
            if (l.getValue() == null) return;
            
            if (oldValue != null && !l.getValue().isEmpty()) {
                Notification.show("Value changed: ", "from " + oldValue + " to " + l.getValue(), Type.TRAY_NOTIFICATION);
            }
            
            try {
                oldValue = new Integer(l.getValue());
                if (oldValue > 100 || oldValue < 0) throw new NumberFormatException();
                method.setCard(oldValue);
            } catch (NumberFormatException e) {
                prepayment.setValue("");
                oldValue = null;
            }
        });
    }

    private ValueChangeListener<String> changeListener () {
        return event -> {
            if (event.getValue().equals(event.getOldValue())) return;
            
            if (CARD.equals(event.getValue())) {
                cashLabel.setVisible(false);
                prepayment.setVisible(true);
                prepayment.setValue(method.getCard() + "");
                method.setCash(false);
            } else {
                cashLabel.setVisible(true);
                prepayment.setValue(0 + "");
                prepayment.setVisible(false);
                method.setCash(true);
            }
        };
    }
    
    private void updateValues () {
        if (getValue() != null) {
            if (method.getCash()) {
                radiobutton.setSelectedItem(CASH);
                cashLabel.setVisible(true);
                prepayment.setVisible(false);
            } else {
                radiobutton.setSelectedItem(CARD);
                cashLabel.setVisible(false);
                prepayment.setVisible(true);
                prepayment.setValue(method.getCard() + "");
            }
        }
    }

    @Override
    protected void doSetValue (PaymentMethod value) {
        method = new PaymentMethod(value);
        updateValues();
    }
}
