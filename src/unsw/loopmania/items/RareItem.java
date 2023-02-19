package unsw.loopmania.items;

import javafx.beans.property.SimpleIntegerProperty;

public abstract class RareItem extends Item {
    private RareItem secondAbility = null;
    private boolean secondActive = false;

    public RareItem(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    public void setSecondAbility(RareItem item) {
        this.secondAbility = item;
    }

    public RareItem getSecondAbility() {
        return this.secondAbility;
    }

    public boolean getSecondActive() {
        return this.secondActive;
    }

    public void setSecondActive(boolean secondActive) {
        this.secondActive = secondActive;
    }
       
}
