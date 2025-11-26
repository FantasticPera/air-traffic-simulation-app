package gui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.ScrollPane;
import java.awt.TextField;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import engine.AppBuilder;
import exception.ValidationException;
import model.Airport;
import model.Flight;
import service.InactivityService;
import time.Time;

public class ControlPanel extends Panel {
	
	private AppBuilder build;
	private MapPanel mapPanel;
	
	//Airport input
	private TextField tfCode, tfName, tfX, tfY;
	private Button buttonAddAirport;
	
	//Flight input
	private TextField tfDepAirport, tfArrAirport, tfDepTime, tfFlightDur;
	private Button buttonAddFligth;
	
	//Airport panel
	private Panel airportsListPanel;     
    private ScrollPane airportsScroll;
    private Map<String, Checkbox> airportVisibility = new LinkedHashMap<>();
    //Flight panel
    private Panel flightsListPanel;
    private ScrollPane flightsScroll;
    
    //Simulation
    private Button buttonToggleSim;
    private Button buttonResetSim;
    private Label labelClock;
    private Timer clockTimer;
    
    //Contructor
    public ControlPanel(AppBuilder build, MapPanel mapPanel) {
    	this.build = build;
    	this.mapPanel = mapPanel;
    	
    	setLayout(new BorderLayout());
    	setBackground(Color.LIGHT_GRAY);
    	
    	//ADDING NORTH CENTER AND SOUTH
    	add(buildAirportPanel(), BorderLayout.NORTH);
    	add(buildFlightPanel(), BorderLayout.CENTER);
    	add(buildSimulationPanel(), BorderLayout.SOUTH);
    	
    	startClockUpdater();
    }
    
    
    
    //BUILDERS ----------------------------------------------------------
    
    //AIRPORT BUILDER=================================================
    private Panel buildAirportPanel() {
    	
    	Panel airportPanel = new Panel(new BorderLayout());
    	InactivityService.getInstance();
    	
    	
    	Panel titlePanel = new Panel();
    	titlePanel.add(new Label("Airports control panel"));
    	airportPanel.add(titlePanel, BorderLayout.NORTH);
    	
    	Panel inputPanel = new Panel(new BorderLayout());
    	
    	Panel grid = new Panel(new GridLayout(4,2));
    	
    	grid.add(new Label("Code:"));
    	tfCode = new TextField();
    	grid.add(tfCode);
    	
    	grid.add(new Label("Airport name:"));
    	tfName = new TextField();
    	grid.add(tfName);
    	
    	grid.add(new Label("X coordinate:"));
    	tfX = new TextField();
    	grid.add(tfX);
    	
    	grid.add(new Label("Y coordinate:"));
    	tfY = new TextField();
    	grid.add(tfY);
    	
    	inputPanel.add(grid, BorderLayout.CENTER);
    	
    	Panel actions = new Panel();
    	buttonAddAirport = new Button("Add airport");
    	//button function (for airports) 
    	buttonAddAirport.addActionListener(ae -> {
    		try {
    			build.airportService().createAirport(
    					tfCode.getText().trim(), tfName.getText().trim(), tfX.getText().trim(), tfY.getText().trim());
    			InactivityService.getInstance().recordActivity();
    			clearAirportFields();
    			// moram da imam ovo refresh u slucaju dodavanja uklanjanja aerodroma
    			refreshAirports();
    			mapPanel.refreshAirports();
    			mapPanel.repaint();
    			
    			
    		} catch (ValidationException e) {
    			PopUpDialog.show(this, "Invalid input", e.getMessage());
    		}
    	});
    	
    	actions.add(buttonAddAirport);
    	inputPanel.add(actions, BorderLayout.SOUTH);
    	
    	airportPanel.add(inputPanel, BorderLayout.CENTER);
    	
    	//Panel with lists of airports
    	Panel listPanel = new Panel(new BorderLayout());
    	listPanel.add(new Label("List of airports:"), BorderLayout.NORTH);
    	
    	
    	
    	airportsListPanel = new Panel(new GridLayout(0,1)) {
    		@Override public Dimension getPreferredSize() {
                
                int rows = getComponentCount();
                int rowH = 20;
                int h = rows * rowH;
                return new Dimension(280, h);
            }
    	};
    	airportsListPanel.setBackground(Color.WHITE);
    	
    	//Ensuring that the first element of the list stand on top of the list
    	Panel wrapper = new Panel(new BorderLayout());
    	wrapper.add(airportsListPanel, BorderLayout.NORTH);
    	wrapper.setBackground(Color.WHITE);
    	
    	
    	airportsScroll = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
    	airportsScroll.setPreferredSize(new Dimension(300, 85));
    	airportsScroll.add(wrapper);
    	listPanel.add(airportsScroll, BorderLayout.CENTER);
    	
    	airportPanel.add(listPanel, BorderLayout.SOUTH);
    	
    	
    	airportPanel.setPreferredSize(new Dimension(300,240));
    	return airportPanel;
    	
    }
    
    //FLIGHT PANEL BUILDER================================================
    private Panel buildFlightPanel() {
    	Panel flightPanel = new Panel(new BorderLayout());
    	Panel titlePanel = new Panel();
    	titlePanel.add(new Label("Flights control panel"));
    	flightPanel.add(titlePanel, BorderLayout.NORTH);
    	
    	Panel inputPanel = new Panel(new BorderLayout());
    	
    	Panel grid = new Panel(new GridLayout(4,2));
    	
    	grid.add(new Label("Departure airport (code):"));
    	tfDepAirport = new TextField();
    	grid.add(tfDepAirport);
    	
    	grid.add(new Label("Arrival airport (code):"));
    	tfArrAirport = new TextField();
    	grid.add(tfArrAirport);
    	
    	grid.add(new Label("Departure time (HH:mm):"));
    	tfDepTime = new TextField();
    	grid.add(tfDepTime);
    	
    	grid.add(new Label("Flight duration (in minutes):"));
    	tfFlightDur = new TextField();
    	grid.add(tfFlightDur);
    	
    	inputPanel.add(grid, BorderLayout.CENTER);
    	
    	Panel actions = new Panel();
    	buttonAddFligth = new Button("Add flight");
    	//button function (for flights) 
    	buttonAddFligth.addActionListener(ae -> {
    		try {
    			build.flightService().createFlight(
    					tfDepAirport.getText().trim(), tfArrAirport.getText().trim(),
    					tfDepTime.getText().trim(), tfFlightDur.getText().trim());
    			
    			
    			build.simulationService().rebuildSchedule();
    			InactivityService.getInstance().recordActivity();
    			clearFlightFields();
    			refreshFlights();
    			mapPanel.repaint(); //mozda obrisi?

    		} catch(ValidationException e) {
    			PopUpDialog.show(this, "Invalid input", e.getMessage());
    		}
    	});
    	
    	actions.add(buttonAddFligth);
    	inputPanel.add(actions, BorderLayout.SOUTH);
    	
    	flightPanel.add(inputPanel, BorderLayout.CENTER);
    	
    	//List of flights - panel
    	
    	Panel listPanel = new Panel(new BorderLayout());
    	listPanel.add(new Label("List of flights:"), BorderLayout.NORTH);
    	
    	
    	
    	flightsListPanel = new Panel(new GridLayout(0,1)) {
    		@Override public Dimension getPreferredSize() {
                
                int rows = getComponentCount();
                int rowH = 20;
                int h = rows * rowH;
                return new Dimension(280, h);
            }
    	};
    	
    	flightsListPanel.setBackground(Color.WHITE);
    	
    	//Ensuring that the first element of the list stand on top of the list
    	Panel wrapper = new Panel(new BorderLayout());
    	wrapper.add(flightsListPanel, BorderLayout.NORTH);
    	wrapper.setBackground(Color.WHITE);
    	
    	
    	flightsScroll = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
    	flightsScroll.setPreferredSize(new Dimension(300, 85));
    	flightsScroll.add(wrapper);
    	listPanel.add(flightsScroll, BorderLayout.CENTER);
    	
    	flightPanel.add(listPanel, BorderLayout.SOUTH);
  
    	//size
    	flightPanel.setPreferredSize(new Dimension(300,250));
    	return flightPanel;
    	
    }
    
    //SIMULATION PANEL BUILDER
    private Panel buildSimulationPanel() {
    	
    	Panel buildPanel = new Panel(new BorderLayout());
    	buttonToggleSim = new Button("Start simulation");
    	buttonResetSim = new Button("Reset");
    	labelClock = new Label("00:00");
    	labelClock.setVisible(true);
    	
    	buttonToggleSim.addActionListener(ae -> {
    		
    		InactivityService.getInstance().recordActivity();
    		
    		if (build.simulationService().isRunning()) {
    			build.simulationService().pause();
    			buttonToggleSim.setLabel("Start simulation");
    			
    		} else {
    			build.simulationService().start();
    			buttonToggleSim.setLabel("Pause simulation");
    			
    		}
    	});
    	
    	buttonResetSim.addActionListener(ae -> {
    		
    		InactivityService.getInstance().recordActivity();
    		
    		build.simulationService().reset();
    		buttonToggleSim.setLabel("Start simulation");
    		
    		mapPanel.repaint();
    	});
    	
    	Panel buttonPanel = new Panel(new FlowLayout(FlowLayout.CENTER));
    	buttonPanel.add(buttonToggleSim);
    	buttonPanel.add(buttonResetSim);
    	
    	buildPanel.add(buttonPanel, BorderLayout.CENTER);
    	buildPanel.add(labelClock, BorderLayout.SOUTH);
    	buildPanel.setPreferredSize(new Dimension(300, 60));
    	return buildPanel;
    }
    
    private void startClockUpdater() {
    	if (clockTimer != null) return;
    	clockTimer = new Timer("SimClock", true);
    	clockTimer.scheduleAtFixedRate(new TimerTask() {
			@Override public void run() {
				// update every 200ms
				String now = build.simulationService().simClockString();
				labelClock.setText(now);
			}
		}, 0, 200);
    }
    
    //HELPER METHODS -----------------------------------------------------
    
    private void clearAirportFields() {
        tfCode.setText("");
        tfName.setText("");
        tfX.setText("");
        tfY.setText("");
    }
    
    private void clearFlightFields() {
    	tfDepAirport.setText("");
    	tfArrAirport.setText("");
    	tfDepTime.setText("");
    	tfFlightDur.setText("");
    	
    }
    
    //Helper methods that are also used by mainframe ---------------------
    
    // adds/removes airports from airport list and related flights from flights list
    public void refreshAirports() {
    	//refreshing all objects we have in this class
    	
    	//reading all airport again
    	List<Airport> airportsCopy = build.airportService().listAll();
    	
    	//saving information about checkboxes
    	Map<String, Boolean> checkboxesCopy = new HashMap<String, Boolean>();
    	
    	for (Map.Entry<String, Checkbox> entry : airportVisibility.entrySet()) {
    		checkboxesCopy.put(entry.getKey(), entry.getValue().getState());
    	}
    	airportVisibility.clear();
    	airportsListPanel.removeAll();
    	
    	for (Airport airport : airportsCopy) {
    		String code = airport.getCode();
    		boolean visible = checkboxesCopy.getOrDefault(code, true);
    		
    		Panel row = new Panel(new BorderLayout());
            Checkbox cbVisible = new Checkbox("", visible);
            Label name = new Label(airport.toString());
            
            cbVisible.addItemListener(event -> {
            	boolean state = cbVisible.getState();
            	airportVisibility.put(code, cbVisible);
            	build.airportService().setVisible(code, state);
            	mapPanel.setAirportVisibility(code, state);
            	//mapPanel.refreshAirports();
            	mapPanel.repaint(); //mozda i ne mora ali sigurica
            	InactivityService.getInstance().recordActivity();
            });
            
            airportVisibility.put(code, cbVisible); //if it was only created, not changed
            
            row.add(cbVisible, BorderLayout.WEST);
            row.add(name, BorderLayout.CENTER);
            airportsListPanel.add(row);
            
            
            mapPanel.setAirportVisibility(code, visible);
    		
    	}
    	//kraj for petlje
    	
    	airportsListPanel.invalidate();
    	//airportsScroll is parent of airportsListPanel
    	airportsScroll.validate();
        airportsScroll.repaint();
        
        mapPanel.refreshAirports();
        mapPanel.repaint();
        
        refreshFlights();
 	
    }
    
    public void refreshFlights() {
    	
    	List<Flight> flightsCopy = build.flightService().listAll();
    	flightsListPanel.removeAll();
    	
    	for (Flight flight : flightsCopy) {
    		flightsListPanel.add(new Label(flight.toString()));
    	}
    	
    	flightsListPanel.invalidate();
    	flightsScroll.validate();
    	flightsScroll.repaint();
    	
    }
    
    public void refreshAll() {
    	refreshAirports();
    	refreshFlights();
    }

    
    
}