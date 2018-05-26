package com.hotels.entities;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@SuppressWarnings ("serial")
@Embeddable
public class FreeServices implements Serializable {
    
    @Basic
    private Boolean breakfast = Boolean.FALSE;
    
    @Basic
    private Boolean towels = Boolean.FALSE;
    
    @Column (name = "COLD_SPIRITS")
    private Boolean coldSpirits = Boolean.FALSE;
    
    public FreeServices () {
    }
    
    public FreeServices (FreeServices value) {
        if (value == null) return;
        
        this.breakfast = value.breakfast;
        this.towels = value.towels;
        this.coldSpirits = value.coldSpirits;
    }

    public Boolean isBreakfast () {
        return breakfast;
    }

    public void setBreakfast (Boolean breakfast) {
        this.breakfast = breakfast;
    }

    public Boolean isTowels () {
        return towels;
    }

    public void setTowels (Boolean towels) {
        this.towels = towels;
    }

    public Boolean isColdSpirits () {
        return coldSpirits;
    }

    public void setColdSpirits (Boolean coldSpirits) {
        this.coldSpirits = coldSpirits;
    }
}
