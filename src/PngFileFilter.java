import java.io.File;

import java.io.FileFilter;


/**
 * PNG file filter that excludes draw nine patch files
 * 
 * @author Kevin Kamps
 */
public class PngFileFilter implements FileFilter {
	@Override
	public boolean accept(File f) {
		if(f.getAbsolutePath().endsWith(".png") && !f.getAbsolutePath().endsWith(".9.png")) {
			return true;
		}
		return false;
	}
}
