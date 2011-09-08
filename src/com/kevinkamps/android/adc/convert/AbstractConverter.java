package com.kevinkamps.android.adc.convert;

import java.io.File;
import java.io.IOException;

import com.kevinkamps.android.adc.util.Settings;


public abstract class AbstractConverter {
	private final Float sourceSize;
	private final Float destinationSize;
	
	public AbstractConverter(String destination) {
		String convertSource = Settings.getInstance().getString(Settings.CONVERT_SOURCE);
		
		sourceSize = Settings.getInstance().getFloat(convertSource);
		destinationSize = Settings.getInstance().getFloat(destination);
	}
	
	protected float getPctResize() {
		return destinationSize/sourceSize * 100;
	}
	/**
	 * Starts converting of files in the source folder into the destination folders
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public abstract void convert(File file, File destinationDir) throws IOException, InterruptedException;
}
