package unsw.loopmania.modes;

import unsw.loopmania.items.*;

public class SurvivalMode implements ModeBehaviour {
    private int currLimit = 0;

    @Override
    public void increaseLimit(Item item) {
        if (item instanceof HealthPotion) {
            this.currLimit++;
        } 
    }

    @Override
    public boolean canBuy(String item) {
        if (item.equals("itemHealthPotion") && this.currLimit < 1) {
            return true;
        } else if (!item.equals("itemHealthPotion")) {
            return true;
        }
        return false;
    }

    @Override
    public void setLimit(int limit) {
        this.currLimit = limit;      
    }

    @Override
    public String getModeName() {
        return "Survival mode";
    }

    @Override
    public int getLimit() {
        return this.currLimit;
    }
}
