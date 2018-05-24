package hotels.holelsUI;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.TextField;

@SuppressWarnings ("serial")
public class PaymentMethodField extends CustomField<PaymentMethod> {
    
    private static final String CASH = "Cash";
    private static final String CARD = "Credit Card";
    
    private HorizontalLayout layout = new HorizontalLayout();
    private RadioButtonGroup<String> radiobutton = new RadioButtonGroup<>();
    private TextField depositField = new TextField();
    private Label cashLabel = new Label("Payment will be made directly in the hotel");
    private PaymentMethod method;

    @Override
    public PaymentMethod getValue () {
        return method;
    }

    @Override
    protected Component initContent () {
        radiobutton.setItems(CARD, CASH);
 
        radiobutton.addValueChangeListener(changeListener());
        
        layout.addComponent(radiobutton);
        if (method.getCash()) {
            radiobutton.setSelectedItem(CASH);
            layout.addComponent(cashLabel);
        } else {
            radiobutton.setSelectedItem(CARD);
            layout.addComponent(depositField);
        }
        
        return layout;
    }

    private ValueChangeListener<String> changeListener () {
        return event -> {
            if (event.getValue().equals(event.getOldValue())) return;
            
            if (CARD.equals(event.getValue())) {
                layout.removeComponent(cashLabel);
                layout.addComponent(depositField);
            } else {
                layout.removeComponent(depositField);
                layout.addComponent(cashLabel);
            }
            Notification.show("Value changed:", String.valueOf(event.getValue()), Type.TRAY_NOTIFICATION);
        };
    }

    @Override
    protected void doSetValue (PaymentMethod value) {
        
    }
}
