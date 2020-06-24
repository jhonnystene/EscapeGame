package com.asuscomm.johnnystene.escape;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class ImageUtils {
	public static BufferedImage resize(BufferedImage image, int width, int height) {
		Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_FAST);
		BufferedImage finalImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D graphics = finalImage.createGraphics();
		graphics.drawImage(scaledImage, 0, 0, null);
		graphics.dispose();
		
		return finalImage;
	}
}
