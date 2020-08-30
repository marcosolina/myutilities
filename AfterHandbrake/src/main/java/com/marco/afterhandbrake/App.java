package com.marco.afterhandbrake;

import java.nio.file.Path;

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
			String handBrakVolder = MarcoUtils.getProperty("com.marco.afterhandbrake.destFolder");
			
			//TODO create factory class to de-couple the instantiacion
			ProcessVideoFolders service = new AfterHandBrake(Path.of(exifToolPath));
			service.exifDataFromSourceFileToMp4Destiantion(Path.of(originalFolder), Path.of(handBrakVolder));
			
			
		} catch (MarcoException e) {
			logger.error(e.getMessage());
		}
		
		
		logger.info("End");
	}
}
