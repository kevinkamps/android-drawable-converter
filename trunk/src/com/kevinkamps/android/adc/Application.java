package com.kevinkamps.android.adc;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.kevinkamps.android.adc.util.*;

/**
 * Application
 * @author Kevin Kamps
 *
 */
public class Application {
	private File source = null;
	private Map<String, File> destinations = null;

	/**
	 * Inits stuff like source and destination folders
	 * @param args
	 */
	public Application(String[] args) {
		Settings.getInstance().overloadSettings(args);
		String convertSource = Settings.getInstance().getString(Settings.CONVERT_SOURCE);
		String[] convertDestination = Settings.getInstance().getString(Settings.CONVERT_DESITNATION).split(",");

		String convertDestinationPath = Settings.getInstance().getString(Settings.CONVERT_DESITNATION_PATH);
		String convertSourcePath = Settings.getInstance().getString(Settings.CONVERT_SOURCE_PATH);
		if(convertSourcePath != null) {
			source = new File(convertSourcePath);
			source.mkdirs();
			System.out.println("Source folder: "+source.getAbsolutePath());
		}
		destinations = new HashMap<String, File>();

		File f = new File("res");
		if(f.isDirectory()) {
			File[] resFiles = f.listFiles();
			for (File resFile : resFiles) {
				if(resFile.isDirectory()) {
					if(source == null && resFile.getName().contains("drawable") && resFile.getName().contains("-"+convertSource)) {
						source = resFile;
						System.out.println("Source folder: "+resFile.getAbsolutePath());
					}

					if(convertDestinationPath == null) {
						for (String type : convertDestination) {

							if(resFile.getName().contains("drawable") && resFile.getName().contains("-"+type)) {
								addDestination(type, resFile);
							}
						}
					}
				}
			}
			if(convertDestinationPath != null) {
				for (String type : convertDestination) {
					File resFile = new File(convertDestinationPath+File.separator+type);
					resFile.mkdirs();
					addDestination(type, resFile);
				}
			}
		}
	}
	
	/**
	 * Adds a destination folder
	 * @param type
	 * @param resFile
	 */
	private void addDestination(String type, File resFile) {
		destinations.put(type, resFile);
		System.out.println("Destination type:"+type+" | folder:"+resFile.getAbsolutePath());
	}

	/**
	 * Starts converting of files in the source folder into the destination folders
	 */
	public void convert() {
		int counterSuccessful = 0;
		int counterFailed = 0;
		System.out.println("Start convert");

		if(source != null && destinations.size() > 0) {
			String convertCommand = Settings.getInstance().getString(Settings.CONVERT_COMMAND);

			String convertSource = Settings.getInstance().getString(Settings.CONVERT_SOURCE);
			String[] convertDestinations = Settings.getInstance().getString(Settings.CONVERT_DESITNATION).split(",");


			File[] files = source.listFiles(new PngFileFilter());
			final int totalFiles = convertDestinations.length * files.length;

			//Convert the source for each type
			for (String destination: convertDestinations) {

				Float sourceSize = Settings.getInstance().getFloat(convertSource);
				Float destinationSize = Settings.getInstance().getFloat(destination);
				Float pctResize = destinationSize/sourceSize * 100;

				//get the destination folder for the destination type
				File destinationFile = destinations.get(destination);
				try {
					for (File file : files) {
						final String destFile = String.format("%s%s%s",destinationFile.getAbsolutePath(), File.separator, file.getName());
						Runtime rt = Runtime.getRuntime();
						final String cmd = String.format(convertCommand, file.getAbsolutePath(), destFile, pctResize+"%");
						Process pr = rt.exec(cmd);
						pr.waitFor();
						counterSuccessful++;
						System.out.println(String.format("Converting(%s%%): %s", (int)((float)(counterSuccessful+counterFailed)/totalFiles*100), cmd));
					}
				} catch (Exception e) {
					counterFailed++;
					e.printStackTrace();
				}
			}
		} else {
			if(source == null) {
				throw new IllegalStateException("Could not find source folder. Please make sure that the source exists");
			} else {
				throw new IllegalStateException("Could not find destination folders. Please make sure that the destinations exists");
			}
		}

		System.out.println(String.format("Converting finished: %1$s successful - %2$s failed.", counterSuccessful, counterFailed));
	}

	/**
	 * Application starter
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Application app = new Application(args);
			app.convert();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}