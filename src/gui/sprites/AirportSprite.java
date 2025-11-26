package gui.sprites;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import gui.MapProjection;

public class AirportSprite {
	
	private String code;
    private int x, y;
    private boolean visible = true;
    private boolean selected = false;
    
    //style of airport sprite
    private int squareSize = 20;
    private Color airportColor = Color.GRAY;
    private Color selectBlinkColor = Color.RED;
    // blink freq.
    private long blinkPeriodMilis = 500;
    
    
	public AirportSprite(String code, int x, int y) {
		this.code = code;
		this.x = x;
		this.y = y;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean hitTest(int mouseX, int mouseY, Dimension panelSize) {
        if (!visible) return false;
        Point p = MapProjection.mapToCanvas(x, y, panelSize);
        int half = squareSize / 2;
        return mouseX >= (p.x - half) && mouseX <= (p.x + half)
            && mouseY >= (p.y - half) && mouseY <= (p.y + half);
    }
	
	public void paint(Graphics g, Dimension panelSize, long systemTimeMilis) {
		
		
		if(!visible) return;
		
		Point point = MapProjection.mapToCanvas(x, y, panelSize);
		
		Color squareColor = airportColor;
		
		//for blinking
		if (selected) {
			long period = (systemTimeMilis / blinkPeriodMilis) % 2; //500ms refresh rate
			if (period == 0) squareColor = selectBlinkColor;
			else squareColor = airportColor;
		}
		
		//drawing a filled square
		g.setColor(squareColor);
		g.fillRect(point.x - squareSize/2, point.y - squareSize/2, squareSize, squareSize);
		
		g.setColor(Color.BLACK);
		g.drawString(code, point.x + 10, point.y + 10);
		
	}
	
	
    
    
    
    

}
