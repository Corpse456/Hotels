package hotels.holelsUI;

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

    public PaymentMethod (Integer card) {
        this.card = card;
        
        if (card > 0) cash = false;
        else cash = true;
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
