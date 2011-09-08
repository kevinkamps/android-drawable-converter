package com.kevinkamps.android.adc.convert.draw9patch;
import java.io.File;

import java.io.FileFilter;


/**
 * draw nine patch file filter
 * 
 * @author Kevin Kamps
 */
public class Draw9PatchFileFilter implements FileFilter {
	/* (non-Javadoc)
	 * @see java.io.FileFilter#accept(java.io.File)
	 */
	@Override
	public boolean accept(File f) {
		if(f.getAbsolutePath().endsWith(".9.png")) {
			return true;
		}
		return false;
	}
}
