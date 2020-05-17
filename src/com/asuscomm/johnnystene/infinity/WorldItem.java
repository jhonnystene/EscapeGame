/*
 * WorldItem.java
 * 
 * Loads or generates a sprite that can be placed in the world.
 * Used for decorative items. For intractable items, use CollisionItem instead.
 */

package com.asuscomm.johnnystene.infinity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class WorldItem {
	public BufferedImage sprite;
	public int height;
	public int width;
	public int x;
	public int y;
	
	public WorldItem(int setWidth, int setHeight, Color color) { // Create sprite with just a color
		sprite = new BufferedImage(setWidth, setHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = sprite.createGraphics();
		graphics.setColor(color);
		graphics.fillRect(0, 0, setWidth, setHeight);
		graphics.dispose();
		
		width = sprite.getWidth();
		height = sprite.getHeight();
	}
	
	public WorldItem(BufferedImage setSprite) {
		sprite = setSprite;
		width = setSprite.getWidth();
		height = setSprite.getHeight();
	}
	
	public WorldItem(String filename, boolean url) { // Load in a sprite
		try {
			System.out.print("Loading file " + filename + "... ");
			if(url) sprite = ImageIO.read(new URL(filename));
			else sprite = ImageIO.read(new File(filename));
			width = sprite.getWidth();
			height = sprite.getHeight();
			System.out.println("Success!");
		} catch(Exception e) {
			System.out.print("ERROR: ");
			System.out.println(e);
			System.out.println("Replacing with missing sprite texture.");
			
			// Create missing sprite texture
			sprite = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics = sprite.createGraphics();
			graphics.setColor(new Color(255, 0, 255));
			graphics.fillRect(0, 0, 64, 64);
			graphics.setColor(Color.BLACK);
			graphics.fillRect(0, 0, 32, 32);
			graphics.fillRect(32, 32, 64, 64);
			graphics.dispose();
			
			width = 64;
			height = 64;
		}
	}
}
