package com.marco.afterhandbrake.services.implementations;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.marco.afterhandbrake.services.interfaces.ProcessVideoFolders;
import com.marco.exifdatamanager.ExifDataManager;
import com.marco.exifdatamanager.ExifDataManagerFactory;
import com.marco.exifdatamanager.enums.ExifTags;
import com.marco.exifdatamanager.resources.GpsData;
import com.marco.utils.DateUtils;
import com.marco.utils.MarcoException;
import com.marco.utils.enums.DateFormats;

public class AfterHandBrake implements ProcessVideoFolders {
	private static final Logger LOGGER = Logger.getLogger(AfterHandBrake.class);
	
	private ExifDataManager edm;
	
	public AfterHandBrake(Path exifToolPath) {
		edm = new ExifDataManagerFactory().setExifToolPath(exifToolPath).getExifDataManager();
	}

	@Override
	public void exifDataFromSourceFileToMp4Destiantion(Path sourceFolder, Path destFolder) {
		List<File> files = getListOfFiles(sourceFolder);
		
		String destFolderName = destFolder.toFile().getAbsolutePath();
		
		files.parallelStream().forEach(f -> {
			try {
				Map<ExifTags, String> originalMetadata = getOriginalExifData(f);
				GpsData gpsData = edm.convertIntoGpsData(originalMetadata);
				if(gpsData != null) {
					originalMetadata.putAll(edm.createGpsExifTags(gpsData));
				}else {
					LOGGER.info("No GPS data");
				}
				
				String dateOriginal = originalMetadata.get(ExifTags.DATE_DATE_TIME_ORIGINAL);
				originalMetadata.put(ExifTags.DATE_MODIFY_DATE, dateOriginal);
				originalMetadata.put(ExifTags.DATE_CREATE_DATE, dateOriginal);
				originalMetadata.put(ExifTags.DATE_FILE_MODIFIED, dateOriginal);
				
				File newFile = Paths.get(destFolderName, getNewFileName(f)).toFile();
				edm.updateExifDataInFile(newFile, originalMetadata);
				LOGGER.info(String.format("Updated file: %s", newFile.getAbsolutePath()));
				
			} catch (MarcoException e) {
				LOGGER.error(e.getMessage());
			}
		});
	}
	
	private String getNewFileName(File oldFile) {
		String oldName = oldFile.getName();
		String newName = oldName.substring(0, oldName.lastIndexOf("."));
		return newName + ".mp4";
	}

	private List<File> getListOfFiles(Path sourceFolder) {
		List<File> files = new ArrayList<>();
		File folder = sourceFolder.toFile();
		if (folder.exists()) {
			String[] filesNames = folder.list();
			for (String fileName : filesNames) {
				File f = Paths.get(folder.getAbsolutePath(), fileName).toFile();
				if (f.isFile()) {
					files.add(f);
				}
			}
		}

		return files;
	}
	
	private Map<ExifTags, String> getOriginalExifData(File file) throws MarcoException{
		List<ExifTags> tagsToRead = new ArrayList<>();
		tagsToRead.add(ExifTags.GPS_LATITUDE);
		tagsToRead.add(ExifTags.GPS_LONGITUTE);
		tagsToRead.add(ExifTags.DATE_DATE_TIME_ORIGINAL);
		tagsToRead.add(ExifTags.MIME_TYPE);
		return edm.readExifData(tagsToRead, file);
	}

	@Override
	public void setDatesFromFileName(Path folder) {
		List<File> files = getListOfFiles(folder);
		files.stream().filter(File::isFile).forEach(f -> {
			String fileName = f.getName().substring(0, f.getName().lastIndexOf("."));
			LocalDateTime fileDt = DateUtils.fromStringToLocalDateTime(fileName, DateFormats.FILE_NAME);
			
			String valueToStore = DateUtils.fromLocalDateTimeToString(fileDt, DateFormats.EXIF_DATE_TIME);
			
			EnumMap<ExifTags, String> map = new EnumMap<>(ExifTags.class);
			
			map.put(ExifTags.DATE_DATE_TIME_ORIGINAL, valueToStore);
			map.put(ExifTags.DATE_MODIFY_DATE, valueToStore);
			map.put(ExifTags.DATE_CREATE_DATE, valueToStore);
			map.put(ExifTags.DATE_FILE_MODIFIED, valueToStore);
			
			try {
				edm.updateExifDataInFile(f, map);
			} catch (MarcoException e) {
				e.printStackTrace();
			}
		});
	}

}
