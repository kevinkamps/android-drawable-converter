package com.kevinkamps.android.adc.draw9patch;

public class Pixel {
	private int pixel;

	public Pixel(int pixel) {
		this.pixel = pixel;
	}
	
	public int getAlpha() {
		return (this.pixel >> 24) & 0x000000FF;
	}
	
	public int getRed() {
		return (pixel >> 16) & 0x000000FF;
	}
	
	public int getBlue() {
		return (pixel) & 0x000000FF;
	}
	
	public int getGreen() {
		return (pixel >>8 ) & 0x000000FF;
	}
	
	public boolean isDraw9PatchPixel() {
		if(this.getAlpha() > 0 && getRed() >= 0 && getGreen() >= 0 && getBlue() >= 0) {
			return true;
		}
		return false;
	}
	/**
	 * Image Pixels are Arrays of Integers [32 bits/4Bytes]
	 * Consider a 32 pixel as 11111111-00110011-00111110-00011110
	 *
	 * First Byte From Left [11111111]= Alpha
	 * Second Byte From Left[00110011]= Red
	 * Third Byte From Left [00111110]= Green
	 * Fourth Byte From Left[00011110]= Blue
	 *
	 * The following method will do a proper bit shift and
	 * logical AND operation to extract the correct values
	 * of different color/alpha components.
	 *
	 */
	public String getPixelARGBData() {
		String pixelARGBData = "";
		/**
		 * Shift all pixels 24 bits to the right.
		 * Do a logical and with 0x000000FF
		 * i.e. 0000 0000 0000 0000 0000 0000 1111 1111
		 * You will get the alpha value for the pixel
		 */
		int alpha = getAlpha();

		/**
		 * Shift all pixels 16 bits to the right.
		 * Do a logical and with 0x000000FF
		 * i.e. 0000 0000 0000 0000 0000 0000 1111 1111
		 * You will get the red value for the pixel
		 */

		int red = getRed();

		/**
		 * Shift all pixels 8 bits to the right.
		 * Do a logical and with 0x000000FF
		 * i.e. 0000 0000 0000 0000 0000 0000 1111 1111
		 * You will get the green value for the pixel
		 */
		int green = getGreen();

		/**
		 * Dont do any shift.
		 * Do a logical and with 0x000000FF
		 * i.e. 0000 0000 0000 0000 0000 0000 1111 1111
		 * You will get the blue value for the pixel
		 */
		int blue = getBlue();

		pixelARGBData = "Alpha: " + alpha + ", " + "Red: " + red + ", "
		+ "Green: " + green + ", " + "Blue: " + blue;

		return pixelARGBData;
	}
}
