package hotels.holelsUI;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;

import hotels.categoryUI.Category;

@Entity
@Table(name = "HOTEL")
@NamedQueries({
        @NamedQuery(name = "Hotel.byFilter", query = "SELECT e FROM Hotel AS e " +
                "WHERE LOWER(e.name) LIKE :namefilter AND LOWER(e.address) LIKE :addressfilter")
})
@SuppressWarnings ("serial")
public class Hotel implements Serializable, Cloneable {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Version
    @Column(name = "OPTLOCK", columnDefinition = "bigint DEFAULT 0")
    private Long version = 0L;

    private String name = "";

    private String address = "";

    private Integer rating;

    @Column (name = "OPERATES_FROM")
    private Long operatesFrom;

    @JoinColumn (name = "CATEGORY_ID", updatable = true)
    @ManyToOne (cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH}, fetch = FetchType.EAGER)
    private Category category;
    
    @Embedded
    private FreeServices freeServices;

    private String url;

    private String description;

    public boolean isPersisted () {
        return id != null;
    }

    @Override
    public String toString () {
        return name + " " + rating + "stars " + address;
    }

    @Override
    public boolean equals (Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode () {
        return super.hashCode();
    }

    public Hotel () {
    }

    public Long getId () {
        return id;
    }

    public void setId (Long id) {
        this.id = id;
    }
    
    public Long getVersion () {
        return version;
    }
    
    public void setVersion (Long version) {
        this.version = version;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getAddress () {
        return address;
    }

    public void setAddress (String address) {
        this.address = address;
    }

    public Integer getRating () {
        return rating;
    }

    public void setRating (Integer rating) {
        this.rating = rating;
    }

    public Long getOperatesFrom () {
        return operatesFrom;
    }

    public void setOperatesFrom (Long operatesFrom) {
        this.operatesFrom = operatesFrom;
    }

    public Category getCategory () {
        return category;
    }

    public void setCategory (Category category) {
        this.category = category;
    }

    public String getUrl () {
        return url;
    }

    public void setUrl (String url) {
        this.url = url;
    }

    public String getDescription () {
        return description;
    }

    public void setDescription (String description) {
        this.description = description;
    }
    
    public FreeServices getFreeServices () {
        return freeServices;
    }
    
    public void setFreeServices (FreeServices freeServices) {
        this.freeServices = freeServices;
    }

    public Hotel (String name, String address, Integer rating, Long operatesFrom, Category category, String url,
            String description) {
        super();
        this.name = name;
        this.address = address;
        this.rating = rating;
        this.operatesFrom = operatesFrom;
        this.category = category;
        this.url = url;
        this.description = description;
    }
}
