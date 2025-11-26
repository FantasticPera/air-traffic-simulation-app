package gui.sprites;

import java.awt.Color;
import java.awt.Graphics;

public class FlightSprite {

    private int radius = 10;
    private Color color = Color.BLUE;

    public void setRadius(int r) {
    	this.radius = r;
    }
    public int getRadius() {
    	return radius;
    }

    public void paint(Graphics g, int pointX, int pointY) {
        g.setColor(color);
        g.fillOval(pointX - radius, pointY - radius, 2 * radius, 2 * radius);
    }
}