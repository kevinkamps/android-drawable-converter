package com.kevinkamps.android.adc.draw9patch;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.kevinkamps.android.adc.Converter;
import com.kevinkamps.android.adc.util.*;

/**
 * PngConverter
 * @author Kevin Kamps
 *
 */
public class Draw9PatchConverter extends Converter {

	private String tmpDir;

	/**
	 * Constructor
	 * @param destination
	 */
	public Draw9PatchConverter(String destination) {
		super(destination);
		tmpDir = Settings.getInstance().getString(Settings.TMP_DIR);
	}

	/**
	 * Starts converting of files in the source folder into the destination folders
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public void convert(File file, File destinationDir) throws IOException, InterruptedException {
		BufferedImage image = readImage(file);
		createTmpDir();


		//getting the 9 patch lines
		ArrayList<Line> lines = new ArrayList<Line>();
		lines.addAll(getHorizontalLines(image));
		lines.addAll(getVerticalLines(image));
		
		//creating a clean image without 9 patch info
		File stripped = createStrippedPng(image);

		//resize clean image
		File resizedStripped = new File(resize(stripped));
		
		//add 9 patch info to resized image
		File recreatedResized9Patch = recreateResized9Patch(resizedStripped, lines);
		
		//copy resize image with 9 patch info
		File destFile = new File(destinationDir, file.getName());
		destFile.delete();
		recreatedResized9Patch.renameTo(destFile);

		//cleanup tmp data
		cleanupTmpDir();
	}
	
	private File recreateResized9Patch(File resizedStripped, List<Line> lines ) {
		BufferedImage resizedStrippedImage = readImage(resizedStripped);
		final int width = resizedStrippedImage.getWidth();
		final int height = resizedStrippedImage.getHeight();
		final int widthDraw9Patch = width + 2;
		final int heightDraw9Patch = height + 2;
		
		int[] argbArray = new int[width*height];
		resizedStrippedImage.getRGB(0, 0, width, height, argbArray, 0, width);
		
		BufferedImage resizeddraw9Patch = new BufferedImage(widthDraw9Patch, heightDraw9Patch, BufferedImage.TYPE_INT_ARGB);
		resizeddraw9Patch.setRGB(1, 1, width, height, argbArray, 0, width);
		
		for (Line line : lines) {
			line.drawOnImage(this.getPctResize(), resizeddraw9Patch);
		}
		File recreated = new File(tmpDir + File.separator + "recreated.png");
		try {
			ImageIO.write(resizeddraw9Patch, "png", recreated);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return recreated;
	}

	/**
	 * Resizes the incoming file to the required size
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private String resize(File file) throws IOException, InterruptedException {
		String convertCommand = Settings.getInstance().getString(Settings.CONVERT_COMMAND);
		
		final String destFile = String.format("%s%s%s",file.getParentFile().getAbsolutePath(), File.separator, "converted_"+file.getName());
		Runtime rt = Runtime.getRuntime();
		final String cmd = String.format(convertCommand, file.getAbsolutePath(), destFile, this.getPctResize()+"%");
		Process pr = rt.exec(cmd);
		pr.waitFor();
		
		return destFile;
	}
	
	/**
	 * Create a 9 patch free png so we can resize it later
	 * @param image
	 * @return
	 */
	private File createStrippedPng(BufferedImage image) {
		final int width = image.getWidth();
		final int height = image.getHeight();

		BufferedImage stripped = new BufferedImage(width-2, height-2, BufferedImage.TYPE_INT_ARGB);
		int[] argb = new int[width*height];
		image.getRGB(1, 1, width-2, height-2, argb, 0, width);
		stripped.setRGB(0, 0, width-2, height-2, argb, 0, width);

		File strippedOutputFile = new File(tmpDir + File.separator + "stripped.png");

		try {
			ImageIO.write(stripped, "png", strippedOutputFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return strippedOutputFile;
	}

	/**
	 * Creates a temp dir locations
	 */
	private void createTmpDir() {
		File tmpDirFile = new File(tmpDir);
		tmpDirFile.mkdirs();
	}

	/**
	 * Cleans up the temp dir
	 */
	private void cleanupTmpDir() {
		File tmpDirFile = new File(tmpDir);
		File[] tmpFiles = tmpDirFile.listFiles();
		for (File tmpFile : tmpFiles) {
			tmpFile.delete();
		}
		tmpDirFile.delete();
	}

	/**
	 * Gets all vertical 9 patch lines
	 * @param image
	 * @return
	 */
	private static ArrayList<Line> getVerticalLines(BufferedImage image) {
		final int width = image.getWidth();
		final int height = image.getHeight();
		ArrayList<Line> lines = new ArrayList<Line>();
		//get horizontal lines.
		for (int x = 0; x < width; x++) {
			if(x == width-1 || x == 0) {
				Line tmpLine = new Line(false);
				for (int y = 0; y < height; y++) {

					Pixel pixel = new Pixel(image.getRGB(x, y));
					if(!tmpLine.hasStartCoordinates() && pixel.isDraw9PatchPixel()) {
						tmpLine.setStart(x, y);
					} else if(tmpLine.hasStartCoordinates() && !tmpLine.hasEndCoordinates() 
							&& (!pixel.isDraw9PatchPixel() || y == height-1)) {
						tmpLine.setEnd(x, y-1);
						lines.add(tmpLine);
						tmpLine = new Line(false);
					}
				}
			}
		}
		return lines;
	}

	/**
	 * Gets all horizontal 9 patch lines
	 * @param image
	 * @return
	 */
	private static ArrayList<Line> getHorizontalLines(BufferedImage image) {
		final int width = image.getWidth();
		final int height = image.getHeight();
		ArrayList<Line> lines = new ArrayList<Line>();
		//get horizontal lines.
		for (int y = 0; y < height; y++) {
			if(y == height-1 || y == 0) {
				Line tmpLine = new Line(true);
				for (int x = 0; x < width; x++) {

					Pixel pixel = new Pixel(image.getRGB(x, y));
					if(!tmpLine.hasStartCoordinates() && pixel.isDraw9PatchPixel()) {
						tmpLine.setStart(x, y);
					} else if(tmpLine.hasStartCoordinates() && !tmpLine.hasEndCoordinates() 
							&& (!pixel.isDraw9PatchPixel() || x == width-1)) {
						tmpLine.setEnd(x-1, y);
						lines.add(tmpLine);
						tmpLine = new Line(true);
					}
				}
			}
		}
		return lines;
	}

	/**
	 * This method reads an image from a file
	 * @return BufferedImage of the file read
	 */
	public static BufferedImage readImage(File file) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}
}