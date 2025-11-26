package registry;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import model.Flight;

public class FlightRegistry implements Registrable<Integer, Flight>{
	
	private Map<Integer, Flight> flights = new LinkedHashMap<Integer, Flight>();
	

	@Override
	public void save(Flight flight) {
		flights.put(flight.getID(), flight);
		
	}

	@Override
	public Flight find(Integer id) {
		return flights.get(id);
	}

	@Override
	public List<Flight> findAll() {
		return new ArrayList<Flight>(flights.values());
	}



	@Override
	public boolean exists(Integer id) {
		return flights.containsKey(id);
				
	}

	@Override
	public void remove(Integer id) {
		flights.remove(id);
	}

}
