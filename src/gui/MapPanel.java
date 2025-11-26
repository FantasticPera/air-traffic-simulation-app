package gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import engine.AppBuilder;
import gui.sprites.AirportSprite;
import gui.sprites.FlightSprite;
import model.Airport;
import service.InactivityService;
import service.SimulationService;
import service.SimulationService.ActiveFlightState;

public class MapPanel extends Canvas {
	
	private AppBuilder build;

	public MapPanel(AppBuilder build) {
		this.build = build;
		setBackground(new Color(131, 242, 161));
		
		startRepaintLoop();
		mouseListener();
	}
	
	
	//Airports and Flights sprites:
	
	private Map<String, AirportSprite> airportSprites = new HashMap<String, AirportSprite>();
	private final FlightSprite flightSprite = new FlightSprite();
	private Set<String> visibleAirports = new HashSet<String>();
	
	
	//TODO Simulation info
	
	//Timer---------------------------------------------------------
	private Timer repaintTimer = new Timer("MapPanelTimer",true);
	private TimerTask repaintTask;
	private int repaintPeriodMilis = 100; //
	
	//paint methods that is always called=======================================
	@Override
	public void paint(Graphics g) {
		Dimension d = getSize();
		long sysTimeMilis = System.currentTimeMillis();
		
		//background
		g.setColor(new Color(131, 242, 161));
		g.drawRect(0, 0, d.width, d.height);
		
		//Airports
		for (AirportSprite aSprite : airportSprites.values()) {
			aSprite.paint(g, d, sysTimeMilis);
		}
		
		//Flights
		List<ActiveFlightState> flights = build.simulationService().snapshotActiveFlights();
		for (ActiveFlightState f : flights) {
			Point p1 = MapProjection.mapToCanvas(f.fromX, f.fromY, d);
			Point p2 = MapProjection.mapToCanvas(f.toX, f.toY, d);
			double t = Math.max(0.0, Math.min(1.0, f.progress));
			int px = (int)Math.round(p1.x + t * (p2.x - p1.x));
			int py = (int)Math.round(p1.y + t * (p2.y - p1.y));
			flightSprite.paint(g, px, py);
		}
		
	}
	//=================================================================
	//mouse actions
	
	private void mouseListener() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				processClick(e.getX(), e.getY());
			}
		});
	}
	
	private void processClick(int mouseX, int mouseY) {
		Dimension d = getSize();
		List<AirportSprite> spriteList = new ArrayList<>(airportSprites.values());
		
		AirportSprite hit = null;
		
		for (AirportSprite sprite : spriteList) {
			if(!sprite.isVisible()) continue;
			
			if (sprite.hitTest(mouseX, mouseY, d)) {
				hit = sprite;
				break;
			}
		}
		
		if (hit != null) {
            boolean wasSelected = hit.isSelected();
            if (wasSelected) {
                hit.setSelected(false);
            } else {
                for (AirportSprite s : airportSprites.values()) s.setSelected(false);
                hit.setSelected(true);
            }
        } 
//		else { // click anywhere to deselect
//            for (AirportSprite s : airportSprites.values()) s.setSelected(false);
//        }
		
		
		updateInactivity();
		repaint();
		
	}
	
	//repaint loop==================================================
	
	private synchronized void startRepaintLoop() {
		if (repaintTask != null) return; //already working
		repaintTask = new TimerTask() {
			
			@Override
			public void run() {
				repaint();
			}
		};
		repaintTimer.scheduleAtFixedRate(repaintTask, 0, repaintPeriodMilis);
	}
	
//	private synchronized void stopRepaintLoop() {
//		if (repaintTask != null) {
//			repaintTask.cancel();
//			repaintTask = null;
//            repaintTimer.purge(); //mozda mora
//		}
//	}
	
	//Airports methods ==================================================
	
	public void setAirportVisibility(String code, boolean visible) {
        if (visible) visibleAirports.add(code);
        else visibleAirports.remove(code);

        AirportSprite sprite = airportSprites.get(code);
        if (sprite != null) {
            sprite.setVisible(visible);
        }
        repaint();
    }
	
	public void setAirportSelected(String code, boolean selected) {
        AirportSprite sprite = airportSprites.get(code);
        if (sprite != null) {
            sprite.setSelected(selected);
            repaint();
        }
        
        updateInactivity();
        repaint();
    }
	
	public void refreshAirports() {
        
        List<Airport> airportsCopy = new ArrayList<>(build.airportService().listAll());

        
        Set<String> seen = new HashSet<>();
        for (Airport a : airportsCopy) {
            seen.add(a.getCode());
            AirportSprite sprite = airportSprites.get(a.getCode());
            if (sprite == null) {
                sprite = new AirportSprite(a.getCode(), a.getX(), a.getY());
                
                if (!visibleAirports.contains(a.getCode())) {
                    visibleAirports.add(a.getCode());
                }
                sprite.setVisible(visibleAirports.contains(a.getCode()));
                airportSprites.put(a.getCode(), sprite);
            } else {
                sprite.setPosition(a.getX(), a.getY());
                sprite.setVisible(visibleAirports.contains(a.getCode()));
            }
        }

        
        airportSprites.keySet().removeIf(code -> !seen.contains(code));
        
        
        updateInactivity();
        repaint();
    }
	//=================================================================================
	
	private void updateInactivity() {
		boolean anySelected = false;
		for (AirportSprite sprite : airportSprites.values()) {
			if (sprite.isSelected()) {
				anySelected = true;
				break;
			}
		}
		
		if (anySelected) InactivityService.getInstance().pause();
		else InactivityService.getInstance().resume();
	}
	
	

}
