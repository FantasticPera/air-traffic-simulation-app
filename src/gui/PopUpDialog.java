package gui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import service.InactivityService;

public class PopUpDialog {
	
	
	
	public static void show(Component frame, String title, String message) {
		
		InactivityService.getInstance();
		
		Dialog dialog = new Dialog ((Frame)findParent(frame), title, true);
		dialog.setLayout(new GridLayout(2,1));
		dialog.add(new Label(message, new FlowLayout().CENTER));

		Panel wrapper = new Panel();
		Button buttonOK = new Button("OK");
		buttonOK.addActionListener(ae ->{
			InactivityService.getInstance().recordActivity();
			dialog.dispose();
		});
		
		dialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				InactivityService.getInstance();
				dialog.dispose();
			}
		});
		
		wrapper.add(buttonOK);
		dialog.add(wrapper);
		dialog.setSize(calculateWidth(message), 150);
		dialog.setLocationRelativeTo(frame);
		dialog.setResizable(false);
		dialog.setVisible(true);
		
		
	}
	
	private static Component findParent(Component component) {
		while (component != null && !(component instanceof Frame)) {
			component = component.getParent();
		}
		
		return (Frame) component;
	}
	
	private static int calculateWidth(String s) {
		int charWidth = 6;
		int minWidth = 250;
		int width = Math.max(s.length() * charWidth, minWidth);
		return width;
	}
	
	

}
