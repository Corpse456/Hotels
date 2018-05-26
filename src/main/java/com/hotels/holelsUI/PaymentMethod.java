package com.hotels.holelsUI;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Embeddable;

@SuppressWarnings ("serial")
@Embeddable
public class PaymentMethod implements Serializable {
    
    @Basic
    private Integer card = 0;
    
    @Basic
    private Boolean cash = Boolean.TRUE;

    public PaymentMethod () {
    }

    public PaymentMethod (PaymentMethod value) {
        if (value == null) return;
        
        card = value.getCard();
        cash = value.getCash() != null ? value.getCash() : false;
    }

    public Integer getCard () {
        return card;
    }

    public void setCard (Integer card) {
        this.card = card;
    }

    public Boolean getCash () {
        return cash;
    }

    public void setCash (Boolean cash) {
        this.cash = cash;
    }
}