package unsw.loopmania.items;

import java.util.Random;

import javafx.beans.property.SimpleIntegerProperty;

public class Gold extends Item {
    /**
     * Constructor for Item
     * @param x
     * @param y
     */
    public Gold(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        Random rand = new Random();
        this.setGoldWorth(rand.nextInt(10));
        this.setType("Gold");
    }    

}
