package hotels.categoryUI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CategoryService {
    private static CategoryService instance;
    private HashMap<Long, Category> category = new HashMap<>();
    private static final Logger LOGGER = Logger.getLogger(CategoryService.class.getName());
    private long nextId = 0;

    private CategoryService () {
    }

    public static CategoryService getInstance () {
        if (instance == null) {
            instance = new CategoryService();
            instance.ensureTestData();
        }
        return instance;
    }

    public synchronized List<Category> findAll () {
        return findAll(null);
    }

    public synchronized List<Category> findAll (String filter) {
        ArrayList<Category> arrayList = new ArrayList<>();
        for (Category cat : category.values()) {
            boolean passesFilter = filter(cat, filter);
            if (passesFilter) {
                arrayList.add(cat);
            }
        }
        Collections.sort(arrayList, Comparator.comparing(Category::getId));
        return arrayList;
    }

    private boolean filter (Category category, String filter) {
        boolean passFilter = (filter == null || filter.isEmpty())
                || category.getName().toLowerCase().contains(filter.toLowerCase());

        return passFilter;
    }

    public synchronized long count () {
        return category.size();
    }

    public synchronized void delete (Category value) {
        category.remove(value.getId());
        value.setId(null);
        value.setName(null);
    }

    public synchronized void save (Category entry) {
        if (entry == null) {
            LOGGER.log(Level.SEVERE, "Category is null.");
            return;
        }
        if (entry.getId() == null) {
            entry.setId(nextId++);
        }
        category.put(entry.getId(), entry);
    }

    private void ensureTestData () {
        if (findAll().isEmpty()) {
            final String[] categoryData = new String[] { "Hotel", "Hostel", "GuestHouse", "Appartments" };

            for (String category : categoryData) {
                Category cat = new Category(category);
                save(cat);
            }
        }
    }
}
