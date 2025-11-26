package service;

import java.util.List;

import exception.NotFoundException;
import exception.ValidationException;
import model.Airport;
import model.Flight;
import model.IdGenerator;
import registry.AirportRegistry;
import registry.FlightRegistry;
import time.Time;

public class FlightService {

	private FlightRegistry flightReg;
	private AirportRegistry airportReg;
	private IdGenerator idGen = new IdGenerator();
	
	public FlightService(FlightRegistry flightReg, AirportRegistry airportReg) {
		this.flightReg = flightReg;
		this.airportReg = airportReg;
	}

	
	public Flight createFlight(String departureCode, String arrivalCode, String departureTimeStr, String flightDurationStr) {
		
		if (!departureCode.matches("^[A-Z]{3}$")) {
			throw new ValidationException("Invalid code format for departure Airport. Code must contain 3 uppercase characters (ex. ABC)");
		}
		if (!arrivalCode.matches("^[A-Z]{3}$")) {
			throw new ValidationException("Invalid code format for arrival Airport. Code must contain 3 uppercase characters (ex. ABC)");
		}
		
		Airport departureAirport = airportReg.find(departureCode);
		if (departureAirport == null) {
			throw new NotFoundException("Departure airport", departureCode);
		}
		
		Airport arrivalAirport = airportReg.find(arrivalCode);
		if (arrivalAirport == null) {
			throw new NotFoundException("Arrival airport", arrivalCode);
		}
		
		Time departureTime = new Time(departureTimeStr); // checks time format in class Time
		
		int flightDuration;
		try {
			flightDuration = Integer.parseInt(flightDurationStr);
			if (flightDuration < 0) {
				throw new ValidationException("Flight duration value must be a positive integer");
			}
			
		} catch (NumberFormatException e) {
			throw new ValidationException("Flight duration value must be a positive integer");
		}
		
		Flight tempFlight = new Flight(idGen.generateID(), departureAirport, arrivalAirport, departureTime, flightDuration);
		flightReg.save(tempFlight);
		
		
		return tempFlight;
	}
	
	public void removeFlight(int id) {
		if(!flightReg.exists(id)) {
			throw new NotFoundException("Flight", Integer.toString(id));
		}
		
		flightReg.remove(id);
	}
	
	public List<Flight> listAll() {
		return flightReg.findAll();
	}
	
	
}
