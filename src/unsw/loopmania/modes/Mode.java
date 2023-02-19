package unsw.loopmania.modes;

import unsw.loopmania.items.*;

public class Mode {
    ModeBehaviour modeBehaviour;

    public Mode() {
    }   

    public void setModeBehaviour(ModeBehaviour mb) {
        modeBehaviour = mb;
    }

    public void performIncreaseLimit(Item item) {
        if (!performGetModeName().equals("Confusing mode") && (item instanceof RareItem)) {
            RareItem secondAbility = (RareItem) item;
            secondAbility.setSecondActive(false);
        }
        modeBehaviour.increaseLimit(item);
    }

    public void performSetLimit(int limit) {
        modeBehaviour.setLimit(limit);
    }

    public int performGetLimit() {
        return modeBehaviour.getLimit();
    }

    public String performGetModeName() {
        return modeBehaviour.getModeName();
    }

    public boolean performCanBuy(String item) {
        if (modeBehaviour.canBuy(item)) {
            return true;
        }
        return false;
    }
}
