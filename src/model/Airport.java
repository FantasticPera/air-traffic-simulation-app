package model;

public class Airport {
	
	private String name;
	private String code;
	private int x;
	private int y;
	private boolean isVisible = true;
	
	public Airport(String code, String name, int x, int y) {
		
		this.name = name;
		this.code = code;
		this.x = x;
		this.y = y;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	
	
	@Override
    public String toString() {
        return "Airport: " + code + " " + name + " (x = " + x + ", y = " + y + ")";
    }
	
	
	

}
