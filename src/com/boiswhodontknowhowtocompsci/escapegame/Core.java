package com.boiswhodontknowhowtocompsci.escapegame;

import java.awt.Color;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.asuscomm.johnnystene.infinity.Window;
import com.asuscomm.johnnystene.infinity.WorldItem;

import res.FileLoader;

public class Core {
	Window window;
	CollisionLineGenerator linegen;
	FileLoader fileLoader;
	
	// Spawn X: 1030, Y: 915
	
	public Core(Window w, CollisionLineGenerator l, FileLoader f) {
		window = w;
		linegen = l;
		fileLoader = f;
	}
	
public void create() throws IOException {
	Color color = Color.WHITE;
	
	WorldItem background = new WorldItem(ImageIO.read(fileLoader.load("/res/maps/Core-Basic.png")));
	window.backgroundLayer.add(background);
	
	linegen.createLine(970, 975, 1100, 975, color, window);
	linegen.createLine(1100, 975, 1196, 945, color, window);
	linegen.createLine(1196, 945, 1260, 830, color, window);
	linegen.createLine(1260, 830, 1271, 767, color, window);
	linegen.createLine(1271 , 767, 1093, 698, color, window);
	linegen.createLine(1093, 698, 968, 698, color, window);
	linegen.createLine(968, 698, 843, 787, color, window);
	linegen.createLine(837, 788, 837, 838, color, window);
	linegen.createLine(837, 838, 906, 931, color, window);
	}
}
