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
	
	WorldItem background = new WorldItem(ImageIO.read(fileLoader.load("/res/maps/Core.png")));
	window.backgroundLayer.add(background);

	linegen.createLine(1001, 746, 1126, 746, color, window);
	linegen.createLine(1126, 746, 1191, 772, color, window);
	linegen.createLine(1191, 772, 1229, 808, color, window);
	linegen.createLine(1229, 808, 1251, 841, color, window);
	linegen.createLine(1251, 841, 1251, 884, color, window);
	linegen.createLine(1251, 884, 1229, 917, color, window);
	linegen.createLine(1229, 917, 1191, 951, color, window);
	linegen.createLine(1191, 951, 1130, 976, color, window);
	linegen.createLine(1130, 976, 996, 976, color, window);
	linegen.createLine(996, 976, 932, 950, color, window);
	linegen.createLine(932, 950, 895, 916, color, window);
	linegen.createLine(895, 916, 875, 884, color, window);
	linegen.createLine(875, 884, 875, 843, color, window);
	linegen.createLine(875, 843, 900, 808, color, window);
	linegen.createLine(900, 808, 937, 771, color, window);
	linegen.createLine(937, 771, 1001, 746, color, window);
	}
}
