package com.asuscomm.johnnystene.escape;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class WorldItem {
	public float x;
	public float y;
	public int width;
	public int height;
	
	public int hitboxX;
	public int hitboxY;
	public int hitboxWidth;
	public int hitboxHeight;
	
	boolean[] collisionLayer = {true, false, false};
	boolean[] collisionMask = {true, false, false};
	
	// Directions
	public static int SW = 0;
	public static int W = 1;
	public static int NW = 2;
	public static int N = 3;
	public static int NE = 4;
	public static int E = 5;
	public static int SE = 6;
	public static int S = 7;
	
	BufferedImage sprite;
	public BufferedImage[] isometricSprites = new BufferedImage[8];
	public boolean isometricItem = false;
	
	public WorldItem(String path) {
		x = 0;
		y = 0;
		
		loadSprite(path);
	}
	
	public WorldItem(String path, int sX, int sY) {
		x = sX;
		y = sY;
		
		loadSprite(path);
	}
	
	public WorldItem(String path, int sX, int sY, int hX, int hY, int hW, int hH) {
		x = sX;
		y = sY;
		
		loadSprite(path);
		
		hitboxX = hX;
		hitboxY = hY;
		hitboxWidth = hW;
		hitboxHeight = hH;
	}
	
	public void resize(int w, int h) {
		sprite = ImageUtils.resize(sprite, w, h);
		for(int i = 0; i < 8; i++) {
			isometricSprites[i] = ImageUtils.resize(isometricSprites[i], w, h);
		}
	}
	
	public void loadSprite(String path) {
		try {
			sprite = ImageIO.read(this.getClass().getResourceAsStream(path));
			width = sprite.getWidth();
			height = sprite.getHeight();
			hitboxX = 0;
			hitboxY = 0;
			hitboxWidth = width;
			hitboxHeight = height;
		} catch(Exception e) {
			System.out.println("WorldItem load error! Can't load " + path);
			loadErrorSprite();
		}
		
		for(int i = 0; i < 8; i++) isometricSprites[i] = sprite;
	}
	
	public void loadErrorSprite() {
		// Load in error sprite
		sprite = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = sprite.createGraphics();
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, 64, 64);
		graphics.setColor(Color.MAGENTA);
		graphics.fillRect(0, 0, 32, 32);
		graphics.fillRect(32, 32, 64, 64);
		graphics.dispose();
		
		// Set params to match
		width = 64;
		height = 64;
		hitboxX = 0;
		hitboxY = 0;
		hitboxWidth = 64;
		hitboxHeight = 64;
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
	
	public void lookTowards(int x, int y) {
		if(x > 0) {
			if(y > 0)
				pointInDirection(SE);
			else if(y < 0)
				pointInDirection(NE);
			else
				pointInDirection(E);
		} else if(x < 0) {
			if(y > 0)
				pointInDirection(SW);
			else if(y < 0)
				pointInDirection(NW);
			else
				pointInDirection(W);
		} else {
			if(y > 0)
				pointInDirection(S);
			else if(y < 0)
				pointInDirection(N);
		}
	}
	
	public boolean collidingWith(WorldItem item) {
		if(item == null) return false;
		if(item == this) return false;
		
		for(int currentLayer = 0; currentLayer < 4; currentLayer ++) {
			if(item.collisionLayer[currentLayer] && collisionMask[currentLayer]) {
				if(x < item.x + item.width && x + width > item.x && 
						y < item.y + item.height&& y + height > item.y) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public void moveAndCollide(float moveX, float moveY, ArrayList<WorldItem> collisionItemLayer) {
		x = x + moveX;
		for(WorldItem item : collisionItemLayer) {
			while(collidingWith(item)) {
				if(moveX > 0) {
					x = x - 1;
				} else {
					x = x + 1;
				}
			}
		}
		
		y = y + moveY;
		for(WorldItem item : collisionItemLayer) {
			while(collidingWith(item)) {
				while(collidingWith(item)) {
					if(moveY > 0) {
						y = y- 1;
					} else {
						y = y + 1;
					}
				}
			}
		}
		
		if(isometricItem) {
			lookTowards((int) moveX, (int) moveY);
		}
	}
}
