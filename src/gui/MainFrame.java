package gui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import engine.AppBuilder;
import service.InactivityService;

public class MainFrame extends Frame {

	private AppBuilder build;
	
	private ControlPanel controlPanel;
	private MapPanel mapPanel;
	
	
	
	
	
	
	public MainFrame(AppBuilder build) {
		
		this.build = build;
		setTitle("Airport Logistics Application");
		setSize(900,600);
		setResizable(false);
		setLayout(new BorderLayout());
		setLocationRelativeTo(null); // App starts in the middle of the screen
		buildLayout();
		buildMenuBar();
		
		
		
		
		
		
		
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});
		
		
		
		setVisible(true);
		
	}
	
	
	
	private void buildLayout() {
		//left side, center
		mapPanel = new MapPanel(build);
		add(mapPanel, BorderLayout.CENTER);
		
		
		//right side
		controlPanel = new ControlPanel(build, mapPanel); 
		add(controlPanel, BorderLayout.EAST);
		
	}
	
	private void buildMenuBar() {
		 MenuBar menuBar = new MenuBar();
	     Menu menufiles = new Menu("Files");
	     MenuItem menuLoadAirports = new MenuItem("Load file (airports)");
	     MenuItem menuLoadFlights = new MenuItem("Load file (flights)");
	     MenuItem menuSaveAirports = new MenuItem("Save airports");
	     MenuItem menuSaveFlights = new MenuItem("Save flights");
	     MenuItem menuSaveAll = new MenuItem("Save all");
	     MenuItem menuExit = new MenuItem("Exit");
	     
	     menuLoadAirports.addActionListener(ae -> {
	    	 FileOperationsDialog.loadAirports(this, build);
	    	 InactivityService.getInstance().recordActivity();
	    	 controlPanel.refreshAll();
	    	 build.simulationService().rebuildSchedule();
	    	 mapPanel.refreshAirports();
	    	 mapPanel.repaint();
	     });

	     menuLoadFlights.addActionListener(ae -> {
	    	 FileOperationsDialog.loadFlights(this, build);
	    	 InactivityService.getInstance().recordActivity();
	    	 controlPanel.refreshAll();
	    	 build.simulationService().rebuildSchedule();
	    	 mapPanel.refreshAirports();
	    	 mapPanel.repaint();
	     });
	     
	     menuSaveAirports.addActionListener(ae -> {
	    	 FileOperationsDialog.saveAirports(this, build);
	    	 InactivityService.getInstance().recordActivity();
	     });
	     
	     menuSaveFlights.addActionListener(ae -> {
	    	 FileOperationsDialog.saveFlights(this, build);
	    	 InactivityService.getInstance().recordActivity();
	     });
	     
	     menuSaveAll.addActionListener(ae -> {
	    	 FileOperationsDialog.saveAll(this, build);
	    	 InactivityService.getInstance().recordActivity();
	     });
	     
	     menuExit.addActionListener(ae -> {
	    	 dispose();
	    	 System.exit(0);
	     });
	     
	     menufiles.add(menuLoadAirports);
	     menufiles.add(menuLoadFlights);
	     menufiles.add(menuSaveAirports);
	     menufiles.add(menuSaveFlights);
	     menufiles.add(menuSaveAll);
	     menufiles.add(menuExit);
	     
	     menuBar.add(menufiles);
	     setMenuBar(menuBar);
	     
	     
	     
	     
	}
	
}
