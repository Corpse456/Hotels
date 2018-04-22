package hotels.categoryUI;

import java.io.Serializable;

public class Category implements Serializable, Cloneable {
    private static final long serialVersionUID = 2268034213087099552L;
    
    private String name;
    private Long id;

    public boolean isPersisted() {
        return id != null;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
    
    public Category () {
    }
    
    public Category (String category) {
        this.name = category;
    }

    public String getName () {
        return name;
    }

    public void setName (String category) {
        this.name = category;
    }

    public Long getId () {
        return id;
    }
    
    public void setId (Long id) {
        this.id = id;
    }
}
