package gui;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;

import engine.AppBuilder;
import exception.FileFormatException;
import fileOperations.AirportFileOperations;
import fileOperations.FlightFileOperations;

public class FileOperationsDialog {
	
	private static File chooseFile(Frame frame, String title, int mode) {		
		FileDialog fileDialog = new FileDialog(frame, title, mode);
		
		fileDialog.setVisible(true);
		
		if (fileDialog.getFile() == null) return null;
		return new File(fileDialog.getDirectory(), fileDialog.getFile()); // path i ime fajla		
	}
	
	public static void loadAirports(Frame frame, AppBuilder build) {
		File file = chooseFile(frame, "Read airports from a CSV file", FileDialog.LOAD);
		if (file == null) return;
		try {
			new AirportFileOperations().loadFile(file, build.airportService());
			//potvrdna poruka mozda?
		} catch (FileFormatException e) {
			PopUpDialog.show(frame, "Loading file error", e.getMessage());
		}
	}
	
	public static void loadFlights(Frame frame, AppBuilder build) {
		File file = chooseFile(frame, "Read flights from a CSV file", FileDialog.LOAD);
		if (file == null) return;
		try {
			new FlightFileOperations().loadFile(file, build.flightService());
			//potvrdna poruka mozda isto?
		} catch (FileFormatException e) {
			PopUpDialog.show(frame, "Loading file error", e.getMessage());
		}
	}
	
	public static void saveAirports(Frame frame, AppBuilder build) {
		File file = chooseFile(frame, "Save all airports in a csv file", FileDialog.SAVE);
		if (file == null) return;
		new AirportFileOperations().saveFile(file, build.airportService());
	}
	
	public static void saveFlights(Frame frame, AppBuilder build) {
		File file = chooseFile(frame, "Save all flights in a csv file", FileDialog.SAVE);
		if (file == null) return;
		new FlightFileOperations().saveFile(file, build.flightService());
	}
	
	public static void saveAll(Frame frame, AppBuilder build) {
		saveAirports(frame, build);
		saveFlights(frame, build);
	}
	
	
	

}
