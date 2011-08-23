package com.kevinkamps.android.adc;
import java.io.File;

import java.io.FileFilter;


/**
 * PNG file filter that excludes draw nine patch files
 * 
 * @author Kevin Kamps
 */
public class PngFileFilter implements FileFilter {
	/* (non-Javadoc)
	 * @see java.io.FileFilter#accept(java.io.File)
	 */
	@Override
	public boolean accept(File f) {
		if(f.getAbsolutePath().endsWith(".png") && !f.getAbsolutePath().endsWith(".9.png")) {
			return true;
		}
		return false;
	}
}
