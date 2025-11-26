package engine;

import registry.*;
import service.*;


public class AppBuilder {
	
	private AirportRegistry airportReg;
	private FlightRegistry flightReg;
	private AirportService airportService;
	private FlightService flightService;
	private SimulationService simulationService;
	
	
	public AppBuilder() {
		super();
		this.airportReg = new AirportRegistry();
		this.flightReg = new FlightRegistry();
		this.airportService = new AirportService(airportReg, flightReg);
		this.flightService = new FlightService(flightReg, airportReg);
		this.simulationService = new SimulationService(flightReg, airportReg);

	}


	public AirportRegistry airportReg() {
		return airportReg;
	}


	public FlightRegistry flightReg() {
		return flightReg;
	}


	public AirportService airportService() {
		return airportService;
	}


	public FlightService flightService() {
		return flightService;
	}

	public SimulationService simulationService() {
		return simulationService;
	}


	
	
	

}
