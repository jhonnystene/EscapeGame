/*
 * WorldItem.java
 * 
 * Loads or generates a sprite that can be placed in the world.
 * Used for decorative items. For intractable items, use CollisionItem instead.
 */

package old.com.asuscomm.johnnystene.escape;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class WorldItem {
	public BufferedImage sprite;
	public int height;
	public int width;
	public float x;
	public float y;
	public BufferedImage[] isometricSprites = new BufferedImage[8];
	public boolean isometricItem = false;
	
	// Directions
	public static int SW = 0;
	public static int W = 1;
	public static int NW = 2;
	public static int N = 3;
	public static int NE = 4;
	public static int E = 5;
	public static int SE = 6;
	public static int S = 7;
	
	public WorldItem(int setWidth, int setHeight, Color color) { // Create sprite with just a color
		sprite = new BufferedImage(setWidth, setHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = sprite.createGraphics();
		graphics.setColor(color);
		graphics.fillRect(0, 0, setWidth, setHeight);
		graphics.dispose();
		
		width = sprite.getWidth();
		height = sprite.getHeight();
		
		// Initialize isometric sprites
		for(int i = 0; i < 8; i++) isometricSprites[i] = sprite;
	}
	
	public WorldItem(BufferedImage setSprite) {
		sprite = setSprite;
		width = setSprite.getWidth();
		height = setSprite.getHeight();
		// Initialize isometric sprites
		for(int i = 0; i < 8; i++) isometricSprites[i] = sprite;
	}
	
	public WorldItem(InputStream setSprite) throws IOException {
		sprite = ImageIO.read(setSprite);
		width = sprite.getWidth();
		height = sprite.getHeight();
		for(int i = 0; i < 8; i++) isometricSprites[i] = sprite;
	}
	
	public void pointInDirection(int direction) {
		if(isometricItem) {
			if(direction < 8)
				sprite = isometricSprites[direction];
			else 
				System.out.println("Warning: Tried to point an isometric sprite in a non-existant direction.");
		} else
			System.out.println("Warning: Tried to point a non-isometric sprite in a direction.");
	}
	
	public void refreshDimensions() {
		width = sprite.getWidth();
		height = sprite.getHeight();
	}
	
	public void lookTowards(int moveX, int moveY) {
		if(moveX > 0) {
			if(moveY > 0)
				pointInDirection(WorldItem.SE);
			else if(moveY < 0)
				pointInDirection(WorldItem.NE);
			else
				pointInDirection(WorldItem.E);
		} else if(moveX < 0) {
			if(moveY > 0)
				pointInDirection(WorldItem.SW);
			else if(moveY < 0)
				pointInDirection(WorldItem.NW);
			else
				pointInDirection(WorldItem.W);
		} else {
			if(moveY > 0)
				pointInDirection(WorldItem.S);
			else if(moveY < 0)
				pointInDirection(WorldItem.N);
		}
	}
}