package com.marco.utilities.csgoserverutils.services.interfaces;

import java.nio.file.Path;

import com.marco.utils.MarcoException;

public interface MoveMapsPictures {
	/**
	 * It copies the maps pictures files
	 * 
	 * @param from
	 * @param to
	 * @return
	 * @throws MarcoException
	 */
	public boolean copyTheMapPictures(Path from, Path to) throws MarcoException;
}
