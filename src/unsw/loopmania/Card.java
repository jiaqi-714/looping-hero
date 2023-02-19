package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * a Card in the world
 * which doesn't move
 */
public  class Card extends StaticEntity {
    private String type;

    /**
     * Constructor for Card
     * @param x
     * @param y
     */
    public Card(SimpleIntegerProperty x, SimpleIntegerProperty y, String cardName) {
        super(x, y);
        this.type = cardName;
    }

    /**
     * Getter for type
     * @return type
     */
    public String getType() {
        return this.type;
    }

    /**
     * Setter for type
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }
}
