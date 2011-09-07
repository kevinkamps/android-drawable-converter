package com.kevinkamps.android.adc;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.kevinkamps.android.adc.draw9patch.Draw9PatchConverter;
import com.kevinkamps.android.adc.draw9patch.Draw9PatchFileFilter;
import com.kevinkamps.android.adc.png.PngConverter;
import com.kevinkamps.android.adc.png.PngFileFilter;
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
					File resFile = new File(convertDestinationPath + File.separator + type);
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
	public void start() {
		int counterSuccessful = 0;
		int counterFailed = 0;
		long startedConverting = System.currentTimeMillis();
		System.out.println("Start convert");

		if(source != null && destinations.size() > 0) {
			//			String convertCommand = Settings.getInstance().getString(Settings.CONVERT_COMMAND);
			//
			//			String convertSource = Settings.getInstance().getString(Settings.CONVERT_SOURCE);
			String[] convertDestinations = Settings.getInstance().getString(Settings.CONVERT_DESITNATION).split(",");


			File[] pngFiles = source.listFiles(new PngFileFilter());
			File[] draw9PatchFiles = source.listFiles(new Draw9PatchFileFilter());
			final int totalFiles = convertDestinations.length * (pngFiles.length + draw9PatchFiles.length);

			//Convert the source for each type
			for (String destination: convertDestinations) {
				//get the destination folder for the destination type
				File destinationDir = destinations.get(destination);
				try {
					PngConverter pngConverter = new PngConverter(destination);					
					for (File file : pngFiles) {
						long start = System.currentTimeMillis();
						pngConverter.convert(file, destinationDir);
						counterSuccessful++;
						long time = (System.currentTimeMillis() - start);
						System.out.println(String.format("Converting(%s%%): %s (in %sms)", (int)((float)(counterSuccessful+counterFailed)/totalFiles*100), file.getName(), time));
					}

					Draw9PatchConverter draw9PatchConverter = new Draw9PatchConverter(destination);
					for (File file : draw9PatchFiles) {
						long start = System.currentTimeMillis();
//						if(file.getAbsolutePath().endsWith("top_bar_background.9.png")) {
						draw9PatchConverter.convert(file, destinationDir);
						counterSuccessful++;
						long time = (System.currentTimeMillis() - start);
						System.out.println(String.format("Converting(%s%%): %s (in %sms)", (int)((float)(counterSuccessful+counterFailed)/totalFiles*100), file.getName(), time));
//						}
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

		long time = (System.currentTimeMillis() - startedConverting);
		System.out.println(
				String.format(
						"Converting finished: %1$s successful - %2$s failed. (in %3$sms)"
						, counterSuccessful, counterFailed, time
				)
		);
	}

	/**
	 * Application starter
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Application app = new Application(args);
			app.start();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}