package com.boiswhodontknowhowtocompsci.escapegame;

import java.awt.Color;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.asuscomm.johnnystene.infinity.Window;
import com.asuscomm.johnnystene.infinity.WorldItem;

import res.FileLoader;

public class Outside {
	Window window;
	CollisionLineGenerator linegen;
	FileLoader fileLoader;
	
	public Outside(Window w, CollisionLineGenerator l, FileLoader f) {
		window = w;
		linegen = l;
		fileLoader = f;
	}
	
	public void create() throws IOException {
		Color color = Color.WHITE;
		
		WorldItem background = new WorldItem(ImageIO.read(fileLoader.load("/res/maps/Outside-Basic.png")));
		window.backgroundLayer.add(background);
		
		linegen.createLine(0, 1594, 110, 1485, color, window);
		linegen.createLine(0, 2047, 660, 2047, color, window);
		linegen.createLine(0, 2047, 0, 1594, color, window);
		linegen.createLine(660, 2047, 660, 1710, color, window);
		linegen.createLine(622, 1710, 660, 1710, color, window);
		linegen.createLine(622, 1710, 622, 1645, color, window);
		linegen.createLine(309, 1600, 568, 1600, color, window);
		linegen.createLine(568, 1600, 622, 1645, color, window);
		linegen.createLine(309, 1600, 309, 1528, color, window);
		linegen.createLine(309, 1528, 336, 1528, color, window);
		linegen.createLine(336, 1528, 336, 1485, color, window);
		linegen.createLine(110, 1485, 336, 1485, color, window);
	}
}