package com.kevinkamps.android.adc;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.kevinkamps.android.adc.convert.draw9patch.Draw9PatchConverter;
import com.kevinkamps.android.adc.convert.draw9patch.Draw9PatchFileFilter;
import com.kevinkamps.android.adc.convert.png.PngConverter;
import com.kevinkamps.android.adc.convert.png.PngFileFilter;
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
		destinations = new HashMap<String, File>();

		File res = new File("res");
		if(res.isDirectory() && convertDestinationPath == null && convertSourcePath == null) {
			File[] resFiles = res.listFiles();
			for (File resFile : resFiles) {
				if(resFile.isDirectory()) {
					if(source == null && resFile.getName().contains("drawable") && resFile.getName().contains("-"+convertSource)) {
						source = resFile;
						System.out.println("Source folder: "+resFile.getAbsolutePath());
					}

					//get destinations
					for (String type : convertDestination) {
						if(resFile.getName().contains("drawable") && resFile.getName().contains("-"+type)) {
							addDestination(type, resFile);
						}
					}
				}
			}

			String[] convertDestinations = Settings.getInstance().getString(Settings.CONVERT_DESITNATION).split(",");
			for (String destinationType: convertDestinations) {
				File destinationFile = destinations.get(destinationType);
				if(destinationFile == null) {
					destinationFile = new File(res.getAbsoluteFile() + File.separator + "drawable-"+destinationType);
					destinationFile.mkdirs();
					addDestination(destinationType, destinationFile);
				}
			}
		} else if(convertDestinationPath != null && convertSourcePath != null) {
			source = new File(convertSourcePath);
			for (String type : convertDestination) {
				File resFile = new File(convertDestinationPath + File.separator + "drawable-"+type);
				resFile.mkdirs();
				addDestination(type, resFile);
			}
		} else { // error handling
			if(!res.isDirectory() && convertDestinationPath == null && convertSourcePath == null) {
				System.out.println("Res folder could not be found!");
			} else {
				if(convertDestinationPath == null ) {
					System.out.println("convert_destination_path is not filled in");
				}
				if(convertSourcePath == null ) {
					System.out.println("convert_source_path is not filled in!");
				}
			}
		}

		if(!source.exists()) {
			System.out.println("Source folder does not exists!");
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
						try {
							long start = System.currentTimeMillis();
							pngConverter.convert(file, destinationDir);
							counterSuccessful++;
							long time = (System.currentTimeMillis() - start);
							System.out.println(String.format("Converting(%s%%): %s (in %sms)", (int)((float)(counterSuccessful+counterFailed)/totalFiles*100), file.getName(), time));
						} catch (Exception e) {
							counterFailed++;
							System.out.println(String.format("Converting failed: %s to %s", file.getAbsolutePath(), destinationDir.getAbsolutePath()));
							e.printStackTrace();
						}
					}

					Draw9PatchConverter draw9PatchConverter = new Draw9PatchConverter(destination);
					for (File file : draw9PatchFiles) {
						
//						if(file.getAbsolutePath().endsWith("top_bar_background.9.png")) {
						try {
							long start = System.currentTimeMillis();
							draw9PatchConverter.convert(file, destinationDir);
							counterSuccessful++;
							long time = (System.currentTimeMillis() - start);
							System.out.println(String.format("Converting(%s%%): %s (in %sms)", (int)((float)(counterSuccessful+counterFailed)/totalFiles*100), file.getName(), time));
						} catch (Exception e) {
							counterFailed++;
							System.out.println(String.format("Converting(%s%%): conversion failed: %s to %s", (int)((float)(counterSuccessful+counterFailed)/totalFiles*100), file.getAbsolutePath(), destinationDir.getAbsolutePath()));
							e.printStackTrace();
						}
						
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