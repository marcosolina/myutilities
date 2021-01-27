package com.marco.utilities.csgoserverutils;

import java.nio.file.Paths;

import org.apache.log4j.Logger;

import com.marco.utilities.csgoserverutils.services.implementations.MarcoMoveMapsPictures;
import com.marco.utilities.csgoserverutils.services.interfaces.MoveMapsPictures;
import com.marco.utils.MarcoException;
import com.marco.utils.miscellaneous.MarcoUtils;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	Logger logger = MarcoUtils.configureAndGetLogger(App.class);
		MarcoUtils.setAppProperties(MarcoUtils.retrieveAppProperties(App.class));
		
		try {
			String sourceFolder = MarcoUtils.getProperty("com.marco.utilities.csgoserverutils.sourceFolder");
			String destinationFolder = MarcoUtils.getProperty("com.marco.utilities.csgoserverutils.destFolder");
			
			// TODO create factory class to de-couple the instantiacion
			MoveMapsPictures service = new MarcoMoveMapsPictures();
			service.copyTheMapPictures(Paths.get(sourceFolder), Paths.get(destinationFolder));
			
		} catch (MarcoException e) {
			logger.error(e.getMessage());
		}
		
		logger.info("End");
    }
}
