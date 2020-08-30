package com.marco.afterhandbrake.services.interfaces;

import java.nio.file.Path;

public interface ProcessVideoFolders {

	/**
	 * It copies all the Exif info from the files contained in the source folder
	 * into the files contained into the destination folder. The source folder and
	 * destination must only contain the required files, nothing more. The
	 * destination file must have ".mp4" as extension
	 * 
	 * @param sourceFolder
	 * @param destFolder
	 */
	public void exifDataFromSourceFileToMp4Destiantion(Path sourceFolder, Path destFolder);

}
