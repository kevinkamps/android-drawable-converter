package com.kevinkamps.android.adc.convert.draw9patch;

import java.awt.image.BufferedImage;

/**
 * This class represents a line of draw 9 patch pixels
 * @author Kevin Kamps
 *
 */
public class Line {
	/**
	 * The coordinates
	 */
	private Integer startX, startY, endX, endY;

	/**
	 * Is a horizontal line
	 */
	private boolean isHorizontal;

	/**
	 * @param isHorizontal
	 */
	public Line(boolean isHorizontal) {
		this.isHorizontal = isHorizontal;
	}

	/**
	 * @return true if this line is horizontal
	 */
	public boolean isHorizontal() {
		return isHorizontal;
	}

	/**
	 * @return true if this line is vertical
	 */
	public boolean isVertical() {
		return !isHorizontal;
	}

	/**
	 * Sets the start coordinates
	 * @param x
	 * @param y
	 */
	public void setStart(int x, int y) {
		this.startX = x;
		this.startY = y;
	}

	/**
	 * Sets the end coordinates
	 * @param x
	 * @param y
	 */
	public void setEnd(int x, int y) {
		this.endX = x;
		this.endY = y;
	}

	/**
	 * Checks if the start coordinates have been set
	 * @return true if been set
	 */
	public boolean hasStartCoordinates() {
		if(startX != null && startY != null) {
			return true;
		}
		return false;
	}


	/**
	 * Checks if the end coordinates have been set
	 * @return true if been set
	 */
	public boolean hasEndCoordinates() {
		if(endX != null && endY != null) {
			return true;
		}
		return false;
	}

	/**
	 * Gets the start x coordinate
	 * @return
	 */
	public int getStartX() {
		return startX;
	}

	/**
	 * Gets the start y coordinate
	 * @return
	 */
	public int getStartY() {
		return startY;
	}

	/**
	 * Gets the end x coordinate
	 * @return
	 */
	public int getEndX() {
		return endX;
	}

	/**
	 * Gets the end y coordinate
	 * @return
	 */
	public int getEndY() {
		return endY;
	}

	/**
	 * Gets a string with the line data. (For debug use)
	 * @return
	 */
	public String getLineData() {
		return String.format("indexed: start=(%s, %s) - end=(%s, %s)"
				, this.startX, this.startY, this.endX, this.endY) +
				String.format("\t\tpixel: start=(%s, %s) - end=(%s, %s)"
						, this.startX+1, this.startY+1, this.endX+1, this.endY+1);
	}

	/**
	 * Recalculates the line based an a scale percentage. This is the same percentage that has been
	 * used with the image scaling.
	 * @param pct
	 * @return
	 */
	private Line recalculatePct(float pct) {
		pct = pct/100;
		Line l = new Line(this.isHorizontal);
		if(this.isHorizontal) {
			l.setStart(recalculate(this.startX, pct, true), recalculate(this.startY, pct, true));
			l.setEnd(recalculate(this.endX, pct, true), recalculate(this.endY, pct, true));
		} else {
			l.setStart(recalculate(this.startX, pct, true), recalculate(this.startY, pct, true));
			l.setEnd(recalculate(this.endX, pct, true), recalculate(this.endY, pct, true));
		}
		return l;
	}

	/**
	 * Recalculates the actual value
	 * @param value
	 * @param pct
	 * @param roundUp
	 * @return
	 */
	private int recalculate(int value, float pct, boolean roundUp) {
		if(!roundUp) {
			return (int) Math.floor(value*pct);
		} else {
			return (int) Math.ceil(value*pct);
		}
	}

	/**
	 * Draws the line on the image.
	 * @param pct
	 * @param image
	 */
	public void drawOnImage(float pct, BufferedImage image) {
		final int maxX = image.getWidth()-1;
		final int maxY = image.getHeight()-1;
		final int maxWithoutCornersX = image.getWidth()-2;
		final int maxWithoutCornersY = image.getHeight()-2;
		
		Line line = this.recalculatePct(pct);
//		System.out.println("=" + line.isHorizontal());
//		System.out.println(this.getLineData());
//		System.out.println("=" + line.getLineData());
//		System.out.println("draw pixel xxx=" + line.getStartX() + " - " + line.getEndX() );
		for (int x = line.getStartX(); x <= line.getEndX(); x++) {
			for (int y = line.getStartY(); y <= line.getEndY(); y++) {
				//set no alpha with colour black;
				int tmpX = x;
				int tmpY = y;
				if(line.isVertical() && tmpY > maxWithoutCornersY) {
					tmpY = maxWithoutCornersY;
				} else if(tmpY > maxY) {
					tmpY = maxY;
				}
				
				if(line.isHorizontal() && tmpX > maxWithoutCornersX) {
					tmpX = maxWithoutCornersX;
				} else if(tmpX > maxX) {
					tmpX = maxX;
				}
				
//				System.out.println("draw pixel x=" + tmpX + "y=" +tmpY);
				image.setRGB(tmpX, tmpY, 0xFF000000);
			}
		}
//		System.out.println("--------------------------------");
//		System.out.println("--------------------------------");
	}
}

