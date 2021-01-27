package com.marco.utilities.csgoserverutils.services.implementations;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

import org.apache.log4j.Logger;

import com.marco.utilities.csgoserverutils.services.interfaces.MoveMapsPictures;
import com.marco.utils.MarcoException;

public class MarcoMoveMapsPictures implements MoveMapsPictures {
	private static final Logger LOGGER = Logger.getLogger(MarcoMoveMapsPictures.class);

	public boolean copyTheMapPictures(Path from, Path to) throws MarcoException {
		LOGGER.debug("Clearing destination folder");
		Arrays.stream(to.toFile().listFiles()).forEach(f -> {
			try {
				Files.delete(f.toPath());
			} catch (IOException e) {
				LOGGER.error(e.getMessage());
			}
		});

		Arrays.stream(from.toFile().listFiles()).filter(f -> f.isFile() && f.getName().endsWith(".jpg")).forEach(f -> {
			try {
				Path target = Path.of(to.toString(), f.getName());
				Files.copy(f.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				LOGGER.error(e.getMessage());
			}
		});

		Path workshopPath = Path.of(from.toString(), "workshop");
		if (Files.exists(workshopPath)) {

			Arrays.stream(workshopPath.toFile().listFiles()).filter(File::isDirectory).forEach(d -> {

				String mapName = "";
				File picture = null;
				for (File f : d.listFiles()) {
					if (f.getName().endsWith(".bsp")) {
						mapName = f.getName().replace(".bsp", "");
						continue;
					}

					if (f.getName().endsWith(".jpg") && !f.getName().endsWith("s.jpg")) {
						picture = f;
						break;
					}

				}
				Path target = Path.of(to.toString(), "workshop-" + d.getName() + "-" + mapName + ".jpg");
				try {
					Files.copy(picture.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}

		return true;
	}

}
