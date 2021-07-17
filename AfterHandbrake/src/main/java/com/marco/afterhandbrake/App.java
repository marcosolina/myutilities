package com.marco.afterhandbrake;

import java.nio.file.Paths;

import org.apache.log4j.Logger;

import com.marco.afterhandbrake.services.implementations.AfterHandBrake;
import com.marco.afterhandbrake.services.interfaces.ProcessVideoFolders;
import com.marco.utils.MarcoException;
import com.marco.utils.miscellaneous.MarcoUtils;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		Logger logger = MarcoUtils.configureAndGetLogger(App.class);
		MarcoUtils.setAppProperties(MarcoUtils.retrieveAppProperties(App.class));
		try {
			String exifToolPath = MarcoUtils.getProperty("com.marco.exiftool.full.path");
			String originalFolder = MarcoUtils.getProperty("com.marco.afterhandbrake.sourceFolder");
			String handBrakFolder = MarcoUtils.getProperty("com.marco.afterhandbrake.destFolder");
			String modality = MarcoUtils.getProperty("com.marco.afterhandbrake.modality");
			
			//TODO create factory class to de-couple the instantiacion
			ProcessVideoFolders service = new AfterHandBrake(Paths.get(exifToolPath));
			
			switch (modality) {
			case "SET_FROM_FILE_NAME":
				service.setDatesFromFileName(Paths.get(handBrakFolder));
				break;
			default:
				service.exifDataFromSourceFileToMp4Destiantion(Paths.get(originalFolder), Paths.get(handBrakFolder));
				break;
			}
			
			
		} catch (MarcoException e) {
			logger.error(e.getMessage());
		}
		
		
		logger.info("End");
	}
}
