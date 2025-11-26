package registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import model.Airport;


public class AirportRegistry implements Registrable<String, Airport>{
	
	private Map<String, Airport> airports = new LinkedHashMap<String, Airport>();
	private Map<String, Boolean> visibility = new HashMap<String, Boolean>();
	
	public void setVisible(String code, boolean visible) {
	    visibility.put(code, visible);
	}
	
	public boolean isVisible(String code) {
	    return visibility.getOrDefault(code, true);
	}

	@Override
	public void save(Airport airport) {
		airports.put(airport.getCode(), airport);
	}

	@Override
	public Airport find(String code) { // find by code
		return airports.get(code);
	}

	@Override
	public List<Airport> findAll() {
		return new ArrayList<Airport>(airports.values());
	}

	@Override
	public boolean exists(String code) {
		return airports.containsKey(code);
	}

	@Override
	public void remove(String code) {
		airports.remove(code);
	}
	
	
	
	
}
