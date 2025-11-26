package fileOperations;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import exception.FileFormatException;
import model.Airport;
import service.AirportService;

public class AirportFileOperations implements FileOperations<AirportService>{

	@Override
	public void saveFile(File file, AirportService airportService) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for (Airport airport : airportService.listAll()) {
				String line = airport.getCode() + "," + airport.getName() + "," + airport.getX() + "," + airport.getY();
				bw.write(line);
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			throw new FileFormatException("An error occured while saving the file: " + e.getMessage());
		}
		
	}

	@Override
	public void loadFile(File file, AirportService airportService) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			int lineNumber = 1; //For tracking errors
			
			while ((line = br.readLine()) != null) {
				if (line.trim().isEmpty()) continue; //Empty line in CSV file
				String[] values = line.split(",", -1); // Even empty values are included
				if (values.length != 4) {
					throw new FileFormatException("Unexpected number of values in line number " + lineNumber + "(CSV files for airports must only contain following values: [code],[airport name],[x],[y])");
				}
				//triming
				for (int i = 0; i < values.length; i++) {
					values[i] = values[i].trim();
				}
				try {
					airportService.createAirport(values[0], values[1], values[2], values[3]);
				} catch (RuntimeException e) {
					
					throw new FileFormatException("Error in line number " + lineNumber + " : " + e.getMessage());
				}

				lineNumber++; // ovo stoji na kraju
			}
			br.close();
			
		} catch(IOException e) {
			throw new FileFormatException("An error occurred while working with files: " + e.getMessage());
		}
		
	}

}
