package service;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Timer;
import java.util.TimerTask;

import gui.MainFrame;

public class InactivityService {
	
	private MainFrame mainFrame;
	
	private static InactivityService Instance = new InactivityService(); // So that it is singleton instance
	
	private long shutdownTimeMilis = 60000;
	private long warningMsgMilis = 55000;
	
	private Timer inactivtyTimer = new Timer("InactivityTimerThread", true);
	
	private volatile boolean paused = false;
	private volatile boolean warned = false;
	
	private volatile long lastActivity = System.currentTimeMillis();
	
	// private construcor for singleton instance
	private InactivityService() {
		inactivtyTimer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				if (paused) return;
				long idle = System.currentTimeMillis() - lastActivity; // how long there was no action in application
				
				if (idle >= warningMsgMilis && idle < shutdownTimeMilis && !warned) {
					warned = true;
					
					long secondsLeft = (shutdownTimeMilis - warningMsgMilis) / 1000;
					EventQueue.invokeLater(() -> warningDialog(secondsLeft));
				}
				else if (idle >= shutdownTimeMilis) {
					System.exit(0);
				}
				
			} // runs every 0.5s = 500ms
		}, 0, 500);
	}
	
	public static InactivityService getInstance() {
		return Instance;
	}
	
	public static void setMainFrame(MainFrame frame) {
	    getInstance().mainFrame = frame;
	}
	
	public void recordActivity() {
		
		lastActivity = System.currentTimeMillis();
		warned = false;
	}
	
	public void pause() {
		paused = true;
	}
	
	public void resume() {
		paused = false;
		recordActivity();
	}
	
	private void warningDialog(long secondsLeft) {
		Dialog warningDialog = new Dialog(mainFrame, "Inactivity warning!", true);
		warningDialog.setLayout(new GridLayout(3,1));
		warningDialog.add(new Label("Application will close in " + secondsLeft + " seconds.", new FlowLayout().CENTER));
		warningDialog.add(new Label("Continue using application?", new FlowLayout().CENTER));
		Panel wrapper = new Panel();
		Button buttonContinue = new Button("Continue");
		buttonContinue.addActionListener(ae ->{
			recordActivity();
			warningDialog.dispose();
		});
		
		warningDialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				recordActivity();
				warningDialog.dispose();
			}
		});
		
		wrapper.add(buttonContinue);
		warningDialog.add(wrapper);
		warningDialog.setSize(250,150);
		warningDialog.setLocationRelativeTo(mainFrame);
		warningDialog.setResizable(false);
		warningDialog.setVisible(true);
		
	}
	

}
