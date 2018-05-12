package hotels.holelsUI;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.data.Binder;
import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValidationException;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

import hotels.categoryUI.Category;
import hotels.categoryUI.CategoryService;

import com.vaadin.ui.Notification.Type;

public class HotelEditForm extends FormLayout {

    private static final long serialVersionUID = -1966192813627725835L;
    private HotelView ui;
    private HotelService hotelService = HotelService.getInstance();
    private CategoryService categoryService = CategoryService.getInstance();
    private Hotel hotel;
    private Binder<Hotel> binder = new Binder<>(Hotel.class);
    private TextField name = new TextField(HotelFieldNames.Name.toString());
    private TextField address = new TextField(HotelFieldNames.Address.toString());
    private TextField rating = new TextField(HotelFieldNames.Rating.toString());
    private DateField operatesFrom = new DateField(HotelFieldNames.OperatesFrom.toString());
    private NativeSelect<String> category = new NativeSelect<>(HotelFieldNames.Category.toString());
    private TextArea description = new TextArea(HotelFieldNames.Description.toString());
    private TextField url = new TextField(HotelFieldNames.URL.toString());

    private Button save = new Button("Save");
    private Button close = new Button("Close");
    private HorizontalLayout buttons = new HorizontalLayout();

    public HotelEditForm (HotelView ui) {
        this.ui = ui;
        this.setVisible(false);

        setSizes();
        preapareFied();

        buttons.addComponents(save, close);

        save.addClickListener(e -> save());
        close.addClickListener(e -> close());

        category.setItems(categoryNames());

        addComponents(name, address, rating, operatesFrom, category, description, url, buttons);
        binder.bindInstanceFields(this);
    }

    private List<String> categoryNames () {
        List<Category> categories = categoryService.findAll();
        List<String> categoryNames = new ArrayList<>();
        for (Category category : categories) {
            categoryNames.add(category.getName());
        }
        return categoryNames;
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
        String required = "Please enter a ";
        binder.forField(name).asRequired(required + "name").bind(Hotel::getName, Hotel::setName);
        binder.forField(address).asRequired(required + "address").withValidator(adressValidator()).bind(Hotel::getAddress, Hotel::setAddress);
        binder.forField(rating).asRequired(required + "rating").withConverter(ratingConverter()).bind(Hotel::getRating, Hotel::setRating);
        binder.forField(operatesFrom).asRequired(required + "opening date").withConverter(dateConverter()).bind(Hotel::getOperatesFrom, Hotel::setOperatesFrom);
        binder.forField(category).asRequired(required + "category").withConverter(categoryConverter()).bind(Hotel::getCategory, Hotel::setCategory);
        binder.forField(description).bind(Hotel::getDescription, Hotel::setDescription);
        binder.forField(url).asRequired(required + "url").bind(Hotel::getUrl, Hotel::setUrl);

        setToolTips();
    }

    private Converter<String, Category> categoryConverter () {
        return new Converter<String, Category>() {
            private static final long serialVersionUID = 4894727679522256179L;

            @Override
            public Result<Category> convertToModel (String value, ValueContext context) {
                List<Category> categories = categoryService.findAll();
                for (Category category : categories) {
                    if (category.getName().equals(value)) return Result.ok(category);
                }
                return Result.error("No such category");
            }

            @Override
            public String convertToPresentation (Category value, ValueContext context) {
                if (value == null) return "";
                return value.getName();
            }
        };
    }

    private void setToolTips () {
        name.setDescription(HotelFieldDescription.getDescription(HotelFieldNames.Name));
        address.setDescription(HotelFieldDescription.getDescription(HotelFieldNames.Address));
        rating.setDescription(HotelFieldDescription.getDescription(HotelFieldNames.Rating));
        operatesFrom.setDescription(HotelFieldDescription.getDescription(HotelFieldNames.OperatesFrom));
        category.setDescription(HotelFieldDescription.getDescription(HotelFieldNames.Category));
        description.setDescription(HotelFieldDescription.getDescription(HotelFieldNames.Description));
        url.setDescription(HotelFieldDescription.getDescription(HotelFieldNames.URL));
    }

    private Converter<String, Integer> ratingConverter () {
        return new Converter<String, Integer>() {
            private static final long serialVersionUID = 1561986299308364347L;

            @Override
            public Result<Integer> convertToModel (String value, ValueContext context) {
                int parseInt = -1;
                try {
                    parseInt = Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    Result.error("Wrong number");
                }
                if (!(parseInt >= 0 && parseInt < 6)) return Result.error("value must be beetween 0 and 5 inclusive");
                return Result.ok(parseInt);
            }

            @Override
            public String convertToPresentation (Integer value, ValueContext context) {
                if (value == null) return "";
                return value + "";
            }
        };
    }

    private Converter<LocalDate, Long> dateConverter () {
        return new Converter<LocalDate, Long>() {
            private static final long serialVersionUID = 4373291445119905756L;

            @Override
            public Result<Long> convertToModel (LocalDate value, ValueContext context) {
                if (!value.isBefore(LocalDate.now()))
                    return Result.error("Wrong date. Should be until the current day");
                return Result.ok(value.getLong(ChronoField.EPOCH_DAY));
            }

            @Override
            public LocalDate convertToPresentation (Long value, ValueContext context) {
                if (value == null) return null;
                return LocalDate.ofEpochDay(value);
            }
        };
    }

    private Validator<String> adressValidator () {
        return (value, context) -> {
            if (value.length() < 5) return ValidationResult.error("The adress is too short");
            return ValidationResult.ok();
        };
    }

    private void close () {
        setVisible(false);
        hotel = null;
    }

    private void save () {
        if (binder.isValid()) {
            try {
                binder.writeBean(hotel);
            } catch (ValidationException e) {
                Notification.show("Unable to save! " + e.getMessage(), Type.ERROR_MESSAGE);
            }
            hotelService.save(hotel);
            ui.updateList();
            Notification.show("Hotel " + hotel.getName() + " saved", Type.TRAY_NOTIFICATION);
            close();
        } else Notification.show("Unable to save! please review errors and fill in all the required fields",
                Type.ERROR_MESSAGE);
    }

    public Hotel getHotel () {
        return hotel;
    }

    public void setHotel (Hotel hotel) {
        category.setItems(categoryNames());
        this.hotel = hotel;
        binder.readBean(this.hotel);
        setVisible(true);
    }
}
