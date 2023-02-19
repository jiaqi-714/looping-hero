package unsw.loopmania.modes;

import unsw.loopmania.items.Item;

public interface ModeBehaviour {
    public void increaseLimit(Item item);
    public boolean canBuy(String item);
    public void setLimit(int limit);
    public String getModeName();
    public int getLimit();
}
