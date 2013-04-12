package com.kevinkamps.android.adc.convert.draw9patch;

/**
 * This class represents a pixel in a draw 9 patch file.
 * @author Kevin Kamps
 *
 */
public class Pixel {
	private int pixel;

	/**
	 * @param pixel - the color
	 */
	public Pixel(int pixel) {
		this.pixel = pixel;
	}
	
	/**
	 * Returns the alpha value
	 * @return
	 */
	public int getAlpha() {
		return (this.pixel >> 24) & 0x000000FF;
	}
	
	/**
	 * Returns the red value
	 * @return
	 */
	public int getRed() {
		return (pixel >> 16) & 0x000000FF;
	}
	
	/**
	 * Returns the blue value
	 * @return
	 */
	public int getBlue() {
		return (pixel) & 0x000000FF;
	}
	
	/**
	 * Returns the green value
	 * @return
	 */
	public int getGreen() {
		return (pixel >>8 ) & 0x000000FF;
	}
	
	/**
	 * @return true if this is a draw 9 patch pixel
	 */
	public boolean isDraw9PatchPixel() {
		if(this.getAlpha() > 0 && getRed() >= 0 && getGreen() >= 0 && getBlue() >= 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns all color values as a readable string (for debug use only)
	 * @return 
	 */
	public String getPixelARGBData() {
		String pixelARGBData = "";
		int alpha = getAlpha();
		int red = getRed();
		int green = getGreen();
		int blue = getBlue();

		pixelARGBData = "Alpha: " + alpha + ", " + "Red: " + red + ", "
		+ "Green: " + green + ", " + "Blue: " + blue;

		return pixelARGBData;
	}
}
