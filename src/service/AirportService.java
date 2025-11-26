package service;

import java.util.ArrayList;
import java.util.List;

import exception.DuplicateKeyException;
import exception.NotFoundException;
import exception.ValidationException;
import model.Airport;
import model.Flight;
import registry.AirportRegistry;
import registry.FlightRegistry;

public class AirportService {
	
	private AirportRegistry airportReg;
	private FlightRegistry flightReg;
	
	public AirportService(AirportRegistry airportReg, FlightRegistry flightReg) {
		this.airportReg = airportReg;
		this.flightReg = flightReg;
	}
	
	public Airport createAirport(String code, String name, String xString, String yString) {
		
		if (code == null) {
			throw new ValidationException("Airport Code is missing.");
		}
		if (!code.matches("^[A-Z]{3}$")) {
			throw new ValidationException("Invalid code format: Code must contain 3 uppercase characters (ex. ABC)");
		}
		if (airportReg.exists(code)) {
			throw new DuplicateKeyException("Airport", code);
		}
		if (name.isEmpty()) {
			throw new ValidationException("Airport must have a name.");
		}
		
		int x = parseCoordinate(xString, "x");
		int y = parseCoordinate(yString, "y");
		
		Airport tempAirport = new Airport(code, name, x, y);
		airportReg.save(tempAirport);
		airportReg.setVisible(code, true);
		
		return tempAirport;
		
		
	}
	
	public void removeAirport(String code) {
		if(!airportReg.exists(code)) {
			throw new NotFoundException("Airport", code);
		}
		
		List<Flight> flightsForRemoving = new ArrayList<Flight>();
		for (Flight flight : flightReg.findAll()) {
			if (flight.getDepartureAirport().getCode().equals(code) || flight.getArrivalAirport().getCode().equals(code)) {
				flightsForRemoving.add(flight);
			}
		}
		for (Flight flight : flightsForRemoving) {
			flightReg.remove(flight.getID());
		}

		airportReg.remove(code);
		
		
		
	}
	
	public boolean isVisible(String code) {
		return airportReg.isVisible(code);
	}
	
	public void setVisible (String code, boolean state) {
		airportReg.setVisible(code, state);
	}
	
	private int parseCoordinate(String coordinate, String coordinateName) {
		if (coordinate.isEmpty()) {
			throw new ValidationException("Coordinate " + coordinateName + " is required");
		}
		try {
			int temp = Integer.parseInt(coordinate);
			if (!(temp >= -90 && temp <= 90)) {
				throw new ValidationException("Coordinate (" + coordinateName + ") must be in interval [-90,90].");
			}
			return temp;
		} catch(NumberFormatException e) {
			throw new ValidationException("Coordinate " + coordinateName + " must be an integer.");
		}
		
	}
	
	public List<Airport> listAll() { return airportReg.findAll(); }

}
