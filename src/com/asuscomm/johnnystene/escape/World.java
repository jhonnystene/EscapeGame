package com.asuscomm.johnnystene.escape;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class World {
	public ArrayList<WorldItem> items;
	public BufferedImage backdrop;
	
	public int width;
	public int height;
	
	public World() {
		items = new ArrayList<>();
		width = 10240;
		height = 10240;
		backdrop = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	}
	
	public World(int w, int h) {
		items = new ArrayList<>();
		width = w;
		height = h;
		backdrop = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	}
	
	public World(String path) {
		try {
			backdrop = ImageIO.read(this.getClass().getResourceAsStream(path));
		} catch(Exception e) {
			System.out.println("World load error! Can't load " + path);
			backdrop = new BufferedImage(10240, 10240, BufferedImage.TYPE_INT_RGB);
		}
	}
	
	public void renderTo(BufferedImage image) {
		Graphics2D graphics = image.createGraphics();
		Raster newFB = backdrop.getData(new Rectangle(0, 0, width, height));
		image.setData(newFB);
		
		for(WorldItem item : items) {
			graphics.drawImage(item.sprite, (int) item.x, (int) item.y, null);
		}
	}
	
	public void renderTo(BufferedImage image, int viewportX, int viewportY, int viewportW, int viewportH) {
		Graphics2D graphics = image.createGraphics();
		Raster newFB = backdrop.getData(new Rectangle(viewportX, viewportY, viewportW, viewportH)).createTranslatedChild(0, 0);
		image.setData(newFB);
		
		for(WorldItem item : items) {
			if(item.x + item.width > viewportX && item.x < viewportX + viewportW &&
					item.y + item.height > viewportY && item.y < viewportY + viewportH)
						graphics.drawImage(item.sprite, (int) item.x, (int) item.y, null);
		}
	}
}
