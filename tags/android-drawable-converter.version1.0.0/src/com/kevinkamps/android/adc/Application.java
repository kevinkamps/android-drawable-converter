package com.kevinkamps.android.adc;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.kevinkamps.android.adc.util.*;



public class Application {
	private File source = null;
	private Map<String, File> destinations = null;

	/**
	 * Inits stuff like source and destination folders
	 * @param args
	 */
	public Application(String[] args) {
		Settings.getInstance().overloadSettings(args);
		String convertFrom = Settings.getInstance().getString(Settings.CONVERT_SOURCE);
		String[] convertTo = Settings.getInstance().getString(Settings.CONVERT_DESITNATION).split(",");

		String convertDestination = Settings.getInstance().getString(Settings.CONVERT_DESITNATION_PATH);
		String convertSource = Settings.getInstance().getString(Settings.CONVERT_SOURCE_PATH);
		if(convertSource != null) {
			source = new File(convertSource);
			source.mkdirs();
			System.out.println("Source folder: "+source.getAbsolutePath());
		}
		destinations = new HashMap<String, File>();

		File f = new File("res");
		if(f.isDirectory()) {
			File[] resFiles = f.listFiles();
			for (File resFile : resFiles) {
				if(resFile.isDirectory()) {
					if(source == null && resFile.getName().contains("drawable") && resFile.getName().contains("-"+convertFrom)) {
						source = resFile;
						System.out.println("Source folder: "+resFile.getAbsolutePath());
					}

					if(convertDestination == null) {
						for (String type : convertTo) {

							if(resFile.getName().contains("drawable") && resFile.getName().contains("-"+type)) {
								addDestination(type, resFile);
							}
						}
					}
				}
			}
			if(convertDestination != null) {
				for (String type : convertTo) {
					File resFile = new File(convertDestination+File.separator+type);
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

			String convertFrom = Settings.getInstance().getString(Settings.CONVERT_SOURCE);
			String[] convertTo = Settings.getInstance().getString(Settings.CONVERT_DESITNATION).split(",");


			File[] files = source.listFiles(new PngFileFilter());
			final int totalFiles = convertTo.length * files.length;

			for (String item: convertTo) {

				Float convertSizeBase = Settings.getInstance().getFloat(convertFrom);
				Float convertSizeTo = Settings.getInstance().getFloat(item);
				Float pctResize = convertSizeTo/convertSizeBase * 100;

				File destination = destinations.get(item);
				try {

					for (File file : files) {
						final String destFile = String.format("%s%s%s",destination.getAbsolutePath(), File.separator, file.getName());
						Runtime rt = Runtime.getRuntime();
						final String cmd = String.format(convertCommand, file.getAbsolutePath(), destFile, pctResize+"%");
						System.out.println(String.format("Converting(%s%%): %s", (int)((float)(counterSuccessful+counterFailed)/totalFiles*100), cmd));
						Process pr = rt.exec(cmd);
						pr.waitFor();
						counterSuccessful++;
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

		System.out.println(String.format("Converting(100%%): %1$s successful - %2$s failed.", counterSuccessful, counterFailed));
	}

	/**
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