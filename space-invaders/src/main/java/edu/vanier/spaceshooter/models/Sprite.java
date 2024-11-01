package edu.vanier.spaceshooter.models;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author frostybee
 */
public class Sprite extends Rectangle {

    private boolean dead = false;
    private final String type;

    public Sprite(int x, int y, int width, int height, String type, Color color) {
        super(width, height ,color);

        this.type = type;
        setTranslateX(x);
        setTranslateY(y);
    }

    public void moveLeft() {
        setTranslateX(getTranslateX() - 5);
    }

    public void moveRight() {
        setTranslateX(getTranslateX() + 5);
    }

    public void moveUp() {
        setTranslateY(getTranslateY() - 5);
    }

    public void moveDown() {
        setTranslateY(getTranslateY() + 5);
    }

    public boolean isDead() {
        return dead;
    }

    public String getType() {
        return type;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }
}
