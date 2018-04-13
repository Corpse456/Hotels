package holels;

import com.vaadin.data.Binder;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
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
    private TextField url = new TextField("URL");
    
    private Button save = new Button("Save");
    private Button close = new Button("Close");
    
    public HotelEditForm(HotelUI ui) {
        this.ui = ui;
        this.setVisible(false);
        
        save.addClickListener(e -> save());
        save.addClickListener(e -> setVisible(false));
        
        category.setItems(HotelCategory.values());
        
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.addComponents(save, close);
        
        addComponents(name, address, rating, operatesFrom, category, url, buttons);
        binder.bindInstanceFields(this);
    }

    private void save() {
        service.save(hotel);
        ui.updateList();
        setVisible(false);
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
        binder.setBean(this.hotel);
        setVisible(true);
    }
}
