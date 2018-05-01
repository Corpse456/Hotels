package hotels.categoryUI;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@SuppressWarnings("serial")
public class Category implements Serializable, Cloneable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

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
