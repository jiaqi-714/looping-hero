package unsw.loopmania.modes;

import unsw.loopmania.items.*;

public class StandardMode implements ModeBehaviour {
    @Override
    public void increaseLimit(Item item) {
        // System.out.println("Standard mode has no limits");
    }

    @Override
    public boolean canBuy(String item) {
        // System.out.println("Standard mode has no limits");
        return true;
    }

    @Override
    public void setLimit(int limit) {
        // System.out.println("Standard mode has no limits");  
    }

    @Override
    public String getModeName() {
        return "Standard mode";
    }

    @Override
    public int getLimit() {
        return 0;        
    }
    
}
