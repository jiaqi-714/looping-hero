package unsw.loopmania.modes;

import unsw.loopmania.items.*;

public class ConfusingMode implements ModeBehaviour {
    private int enhancedItems = 0;

    @Override
    public void increaseLimit(Item item) {
        // If you try to buy this at shop, nothing happens
        if ((item instanceof TheOneRing)) {
            //add Andurils behaviour
            RareItem newItem = (RareItem) item;
            newItem.setSecondActive(true);
            // enhancedItems++;
        } else if ((item instanceof Anduril)) {
            // Add tree stump behaviour
            RareItem newItem = (RareItem) item;
            newItem.setSecondActive(true);
        } else if ((item instanceof TreeStump)) {
            // Add One Ring Behaviour
            RareItem newItem = (RareItem) item; 
            newItem.setSecondActive(true);
        }
    }

    @Override
    public boolean canBuy(String item) {
        return true;
    }

    @Override
    public void setLimit(int limit) {

    }

    @Override
    public String getModeName() {
        return "Confusing mode";
    }

    @Override
    public int getLimit() {
        return enhancedItems;
    }
    
}
