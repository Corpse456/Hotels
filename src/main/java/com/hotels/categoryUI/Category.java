package com.hotels.categoryUI;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "CATEGORY")
@SuppressWarnings("serial")
@NamedQueries({
    @NamedQuery(name = "Category.byName", query = "SELECT e FROM Category e WHERE LOWER(e.name) LIKE :filter")
})
public class Category implements Serializable, Cloneable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Version
    @Column(name = "OPTLOCK", columnDefinition = "bigint DEFAULT 0")
    private Long version = 0L;;

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
    
    public Long getVersion () {
        return version;
    }
    
    public void setVersion (Long version) {
        this.version = version;
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
