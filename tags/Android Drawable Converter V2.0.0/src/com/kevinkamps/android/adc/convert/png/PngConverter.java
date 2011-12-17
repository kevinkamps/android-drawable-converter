package com.kevinkamps.android.adc.convert.png;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.kevinkamps.android.adc.convert.AbstractConverter;
import com.kevinkamps.android.adc.util.*;

/**
 * PngConverter
 * @author Kevin Kamps
 *
 */
public class PngConverter extends AbstractConverter {

	/**
	 * Constructor
	 * @param destination
	 */
	public PngConverter(String destination) {
		super(destination);
	}

	/**
	 * Starts converting of files in the source folder into the destination folders
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public void convert(File file, File destinationDir) throws IOException, InterruptedException {
		String convertCommand = Settings.getInstance().getString(Settings.CONVERT_COMMAND);

		final String destFile = String.format("%s%s%s",destinationDir.getAbsolutePath(), File.separator, file.getName());
		Runtime rt = Runtime.getRuntime();
		final String cmd = String.format(convertCommand, file.getAbsolutePath(), destFile, this.getPctResize()+"%");
		Process pr = rt.exec(cmd);
		pr.waitFor();
	}
}