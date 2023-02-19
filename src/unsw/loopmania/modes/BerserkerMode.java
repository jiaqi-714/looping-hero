package unsw.loopmania.modes;

import unsw.loopmania.items.*;

public class BerserkerMode implements ModeBehaviour {
    private int currLimit = 0;

    @Override
    public void increaseLimit(Item item) {     
        if ((item instanceof Shield) || (item instanceof BodyArmour) || (item instanceof Helmet)) {
            this.currLimit++;
        }
    }

    @Override
    public boolean canBuy(String item) {
        if ((item.equals("itemShield") || item.equals("itemBodyArmour") || item.equals("itemHelmet")) && this.currLimit < 1) {
            return true;
        } else if (!item.equals("itemShield") && !item.equals("itemBodyArmour") && !item.equals("itemHelmet")) {
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
        return "Berserker mode";
    }

    @Override
    public int getLimit() {
        return currLimit;
    }
  
}
