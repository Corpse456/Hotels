package holels;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public class HotelEditForm extends FormLayout {

    private static final long serialVersionUID = -1966192813627725835L;
    private HotelUI ui;
    private HotelService service = HotelService.getInstance();
    private Hotel hotel;
    private Binder<Hotel> binder = new Binder<>(Hotel.class);
    private TextField name = new TextField("Name");
    private TextField address = new TextField("Address");
    private TextField rating = new TextField("Rating");
    private DateField operatesFrom = new DateField("Date");
    private NativeSelect<HotelCategory> category = new NativeSelect<>("Category");
    private TextArea description = new TextArea("Description");
    private TextField url = new TextField("URL");
    
    private Button save = new Button("Save");
    private Button close = new Button("Close");
    private HorizontalLayout buttons = new HorizontalLayout();
    
    public HotelEditForm(HotelUI ui) {
        this.ui = ui;
        this.setVisible(false);
        
        setSizes();
        preapareFied();
        
        buttons.addComponents(save, close);
        
        save.addClickListener(e -> save());
        
        close.addClickListener(e -> close());
        
        category.setItems(HotelCategory.values());
        
        addComponents(name, address, rating, operatesFrom, category, description, url, buttons);
        binder.bindInstanceFields(this);
    }
    
    private void setSizes () {
        buttons.setWidth(100, Unit.PERCENTAGE);
        save.setWidth(100, Unit.PERCENTAGE);
        close.setWidth(100, Unit.PERCENTAGE);
        name.setWidth(100, Unit.PERCENTAGE);
        address.setWidth(100, Unit.PERCENTAGE);
        rating.setWidth(100, Unit.PERCENTAGE);
        operatesFrom.setWidth(100, Unit.PERCENTAGE);
        category.setWidth(100, Unit.PERCENTAGE);
        description.setWidth(100, Unit.PERCENTAGE);
        url.setWidth(100, Unit.PERCENTAGE);
    }

    private void preapareFied () {
        Validator<String> adressValidator = new Validator<String>() {
            @Override
            public ValidationResult apply (String value, ValueContext context) {
                if (value == null || value.isEmpty())
                    return ValidationResult apply
                return null;
            }
        };
        binder.forField(name).asRequired("Please enter a name").bind(Hotel::getName, Hotel::setName);
        name.setDescription("Hotel name");
        binder.forField(address).bind(Hotel::getAddress, Hotel::setAddress);
        binder.forField(rating).bind(Hotel::getRating, Hotel::setRating);
        binder.forField(operatesFrom).bind(Hotel::getOperatesFrom, Hotel::setOperatesFrom);
        binder.forField(category).bind(Hotel::getCategory, Hotel::setCategory);
        binder.forField(description).bind(Hotel::getDescription, Hotel::setDescription);
        binder.forField(url).bind(Hotel::getUrl, Hotel::setUrl);
    }

    private void close () {
        setVisible(false);
        hotel = null;
    }

    private void save() {
        if (binder.isValid()) {
            try {
                binder.writeBean(hotel);
            } catch (ValidationException e) {
                e.printStackTrace();
            }
            service.save(hotel);
            ui.updateList();
            close();
        }
    }

    public Hotel getHotel() {
        return hotel;
    }

    public synchronized void setHotel(Hotel hotel) {
        try {
	    this.hotel = hotel.clone();
	} catch (CloneNotSupportedException e) {
	    e.printStackTrace();
	}
        binder.readBean(this.hotel);
        setVisible(true);
    }
}
