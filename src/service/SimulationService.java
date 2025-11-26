package service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import model.Airport;
import model.Flight;
import registry.AirportRegistry;
import registry.FlightRegistry;
import time.Time;

public class SimulationService {
	
	private FlightRegistry flightReg;
    private AirportRegistry airportReg;

    public SimulationService(FlightRegistry flightReg, AirportRegistry airportReg) {
        this.flightReg = flightReg;
        this.airportReg = airportReg;
        
        startClockThread();
    }
    
    
    private volatile boolean running = false;
    private volatile double simMinutes = 0; 
    
    private final Timer clockTimer = new Timer("SimClockTimer", true);
    private final long tickMs = 200;              
    private final double minutesPerSecond = 10.0;
    private final double minutesPerTick = minutesPerSecond * (tickMs / 1000.0);
    
    //Making the schedule for flights
    
    private static class ScheduledFlight {
        Flight flight;
        int actualDepartureMin;
        boolean started = false;
        boolean completed = false;

        ScheduledFlight(Flight f, int depMin) {
            this.flight = f;
            this.actualDepartureMin = depMin;
        }
    }
    
    private final List<ScheduledFlight> scheduled = new ArrayList<>();
    private final List<ScheduledFlight> active = new ArrayList<>();
    
    public static class ActiveFlightState {
        public final int fromX, fromY, toX, toY;
        public final double progress; // interval from [0..1]
        public final int flightId;

        public ActiveFlightState(int fromX, int fromY, int toX, int toY, double progress, int flightId) {
            this.fromX = fromX; this.fromY = fromY;
            this.toX = toX; this.toY = toY;
            this.progress = progress;
            this.flightId = flightId;
        }
    }
    
    //CLOCK THREAD===========================================
    
    private void startClockThread() {
        clockTimer.scheduleAtFixedRate(new TimerTask() {
            @Override public void run() {
                if (running) {
                    simMinutes += minutesPerTick;
                }
            }
        }, 0, tickMs);
    }
    //========================================================
    //Methods used by other classes
    
    public synchronized void start() {
    	running = true;
    }

    public synchronized void pause() {
    	running = false;
    }

    public synchronized boolean isRunning() {
    	return running;
    }
    
    public synchronized void reset() {
        running = false;
        simMinutes = 0.0;
        for (ScheduledFlight sf : scheduled) {
            sf.started = false;
            sf.completed = false;
        }
        active.clear();
    }
    
    public synchronized Time currentSimTime() {
        int simMin = (int) Math.floor(simMinutes);
        simMin = simMin % (24 * 60);
        int hh = simMin / 60;
        int mm = simMin % 60;
        return new Time(hh, mm);
    }
    
    public synchronized String simClockString() {
        return currentSimTime().toString();
    }
    	
    public double currentSimMinutes() {
    	return simMinutes;
    }
    
    public synchronized void rebuildSchedule() {
    	scheduled.clear();
        active.clear();
        
        Map<String, Integer> nextFreeSlot = new HashMap<>();
        List<Flight> flights = new ArrayList<>(flightReg.findAll());
        flights.sort(Comparator.comparingInt(this::plannedDepartureMinutes).thenComparingInt(Flight::getID));
        
        for (Flight flight : flights) {
            String depCode = flight.getDepartureAirport().getCode();
            int planned = plannedDepartureMinutes(flight);
            int slotStart = ceilTo10(planned);
            int freeAt = nextFreeSlot.getOrDefault(depCode, 0);
            int actual = Math.max(slotStart, freeAt); 
            scheduled.add(new ScheduledFlight(flight, actual));
            nextFreeSlot.put(depCode, actual + 10);
        }
        
   	
    }
    
    //for painting
    public synchronized List<ActiveFlightState> snapshotActiveFlights() {
        double nowSim = simMinutes;

        // aktiviraj letove koji treba da polete
        for (ScheduledFlight sf : scheduled) {
            if (!sf.started && !sf.completed && nowSim >= sf.actualDepartureMin) {
                sf.started = true;
                active.add(sf);
            }
        }

        List<ActiveFlightState> out = new ArrayList<>();
        Iterator<ScheduledFlight> it = active.iterator();
        while (it.hasNext()) {
            ScheduledFlight sf = it.next();
            Flight f = sf.flight;

            int duration = f.getFlightDuration();
            double elapsed = nowSim - sf.actualDepartureMin;
            
            double prog;
            if (duration <= 0) {
                prog = 1.0;
            } else {
                prog = elapsed / duration;
            }
            
            
            if (prog >= 1.0) {
                sf.completed = true;
                it.remove();
                continue;
            }
            
            
            prog = Math.max(0, Math.min(1, prog));

            Airport from = f.getDepartureAirport();
            Airport to = f.getArrivalAirport();
            out.add(new ActiveFlightState(
                from.getX(), from.getY(),
                to.getX(), to.getY(),
                prog, f.getID()
            ));
        }
        return out;
    }
    
    
    //HELPER METHODS ================================================
    private int plannedDepartureMinutes(Flight f) {
        Time time = f.getDepartureTime();
        return time.getHour() * 60 + time.getMinute();
    }

    private int ceilTo10(int minutes) {
        return ((minutes + 9) / 10) * 10;
    }
    
    
    

}
