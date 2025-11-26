package fileOperations;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import exception.FileFormatException;
import model.Flight;
import service.FlightService;

public class FlightFileOperations implements FileOperations<FlightService>{

	
	@Override
	public void saveFile(File file, FlightService flightService) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for (Flight flight : flightService.listAll()) {
				String line = flight.getDepartureAirport().getCode() + "," + flight.getArrivalAirport().getCode() + "," + flight.getDepartureTime() + "," + flight.getFlightDuration();
				bw.write(line);
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			throw new FileFormatException("An error occured while saving the file: " + e.getMessage());
		}
		
	}

	@Override
	public void loadFile(File file, FlightService flightService) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			int lineNumber = 1;
			
			while ((line = br.readLine()) != null) {
				if (line.trim().isEmpty()) continue;
				String[] values = line.split(",", -1); // Even empty values are included
				if (values.length != 4) {
					throw new FileFormatException("Unexpected number of values in line number " + lineNumber + "(CSV files for flights must only contain following values: [departure airport],[arrival airport],[departure time],[flight duration])");
				}
				//triming
				for (int i = 0; i < values.length; i++) {
					values[i] = values[i].trim();
				}
				try {
					flightService.createFlight(values[0], values[1], values[2], values[3]);
				} catch (RuntimeException e) {
					throw new FileFormatException("Error in line number " + lineNumber + " : " + e.getMessage());
				}
				
			}
			br.close();
			
			
		} catch (IOException e) {
			throw new FileFormatException("An error occurred while working with files: " + e.getMessage());
		}
		
	}
	
	

}
