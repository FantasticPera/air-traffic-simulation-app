package model;
import time.Time;



public class Flight {
	
	private Airport departureAirport, arrivalAirport;
	private int id;
	private Time departureTime;
	private int flightDuration; // in minutes
	
	// getters and setter
	public Airport getDepartureAirport() {
		return departureAirport;
	}
	public void setDepartureAirport(Airport departureAirport) {
		this.departureAirport = departureAirport;
	}
	public Airport getArrivalAirport() {
		return arrivalAirport;
	}
	public void setArrivalAirport(Airport arrivalAirport) {
		this.arrivalAirport = arrivalAirport;
	}
	public int getID() {
		return id;
	}
	public void setID(int id) {
		this.id = id;
	}
	public Time getDepartureTime() {
		return departureTime;
	}
	public void setDepartureTime(Time departureTime) {
		this.departureTime = departureTime;
	}
	public int getFlightDuration() {
		return flightDuration;
	}
	public void setFlightDuration(int flightDuration) {
		this.flightDuration = flightDuration;
	}
	
	//constructor
	public Flight( int id, Airport departureAirport, Airport arrivalAirport, Time departureTime, int flightDuration) {
		
		this.departureAirport = departureAirport;
		this.arrivalAirport = arrivalAirport;
		this.id = id;
		this.departureTime = departureTime;
		this.flightDuration = flightDuration;
	}
	
	
	@Override
	public String toString() {
		return "Flight ID: " + getID() + " - from "
	+ getDepartureAirport().getCode() + " to " + getArrivalAirport().getCode() +", at "+getDepartureTime()+ ", duration: " + getFlightDuration() + "min.";
	}
	
	//------------------------------------------------------
	
	
	
	
	
	
	
	
	
	
	

}
