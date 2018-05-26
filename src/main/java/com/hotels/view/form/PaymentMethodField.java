package com.hotels.view.form;

import com.hotels.entities.PaymentMethod;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.converter.StringToIntegerConverter;
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
    private Binder<PaymentMethod> binder = new Binder<>(PaymentMethod.class);
    
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
        
        binder.bindInstanceFields(this);
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
            if (l.getOldValue() != null && l.getValue() != null && !l.getOldValue().isEmpty() && !l.getValue().isEmpty()) {
                Notification.show("Value changed: ", "from " + l.getOldValue() + " to " + l.getValue(), Type.TRAY_NOTIFICATION);
            }
        });
        binder.forField(prepayment).withValidator(prepaymentValidator()).withConverter(new StringToIntegerConverter("Wrong Value")).bind(PaymentMethod::getCard, PaymentMethod::setCard);
    }

    private Validator<? super String> prepaymentValidator () {
        return (value, context) -> {
            if (method.isCash()) return ValidationResult.ok();

            int parseInt = -1;
            try {
                parseInt = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                ValidationResult.error("Not a number");
            }
            if (!(parseInt >= 0 && parseInt <= 100)) return ValidationResult.error("value must be beetween 0 and 100 inclusive");

            return ValidationResult.ok();
        };
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
            if (method.isCash()) {
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

    public Binder<PaymentMethod> getBinder () {
        return binder;
    }

    @Override
    protected void doSetValue (PaymentMethod value) {
        method = new PaymentMethod(value);
        updateValues();
    }
}
