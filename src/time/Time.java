package time;

import exception.ValidationException;

public class Time {
    private int h, m;

    public Time(int h, int m) {
        setTime(h, m);
    }
    
    public Time(String time) {
        setTime(time); // For working with files
    }

    public Time() {
        this(0, 0);
    }

    public int getHour() {
        return h;
    }

    public int getMinute() {
        return m;
    }

    public void setTime(int h, int m) {
        if (h < 0 || h > 23 || m < 0 || m > 59) {
            throw new ValidationException("Invalid time: " + h + ":" + m);
        }
        this.h = h;
        this.m = m;
    }
    
    public void setTime(String time) {
    	if (time == null || !time.matches("^\\d{2}:\\d{2}$")) {
    		throw new ValidationException("Invalid time format. (Time must be in format HH:mm)");
    	}
    	int h = Integer.parseInt(time.substring(0, 2));
        int m = Integer.parseInt(time.substring(3, 5));
        setTime(h, m); 	
    }

    public void addMinutes(int minutes) {
        int totalMinutes = h * 60 + m + minutes;
        totalMinutes = totalMinutes % (24 * 60); // to wrap it around 24 hours
        h = totalMinutes / 60;
        m = totalMinutes % 60;
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d", h, m);
    }

    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false; // ako prosledimo null
    	if (!(obj instanceof Time)) return false; // da li je objekat uopste Time
        Time time = (Time) obj;
        return h == time.h && m == time.m;
    }

}