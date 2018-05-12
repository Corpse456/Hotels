package hotels.holelsUI;

public class HotelFieldDescription {
    
    public static String getDescription (HotelFieldNames field) {
        if (field == null) return null;
        
        if (HotelFieldNames.Name.equals(field)) {
            return "Hotel name - String, not empty";
        }
        if (HotelFieldNames.Address.equals(field)) {
            return "Hotel address - String, more that 5 simbols, not empty";
        }
        if (HotelFieldNames.Rating.equals(field)) {
            return "Hotel rating - 0, 1, 2, 3, 4 or 5 stars, not empty";
        }
        if (HotelFieldNames.OperatesFrom.equals(field)) {
            return "Hotel opening date - date before today, not empty";
        }
        if (HotelFieldNames.Category.equals(field)) {
            return "Category from list to which the hotel belongs, not empty";
        }
        if (HotelFieldNames.Description.equals(field)) {
            return "Hotel description";
        }
        if (HotelFieldNames.URL.equals(field)) {
            return "HTML hotel page";
        }
        
        return null;
    }
}
