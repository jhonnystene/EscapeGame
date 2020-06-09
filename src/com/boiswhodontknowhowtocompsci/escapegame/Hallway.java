//don't know what do, this is just beginning for
//code so yeah, now i do lines
package com.boiswhodontknowhowtocompsci.escapegame;

import java.awt.Color;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import com.asuscomm.johnnystene.infinity.Window;
import com.asuscomm.johnnystene.infinity.WorldItem;

import res.FileLoader;

public class Hallway {
	Window window;
	CollisionLineGenerator linegen;
	FileLoader fileLoader;
	
	public Hallway(Window w, CollisionLineGenerator l, FileLoader f) {
		window = w;
		linegen = l;
		fileLoader = f;
	}
	
	public void create() throws IOException {
		Color color = new Color(67, 67, 75);
		
		WorldItem background = new WorldItem(fileLoader.load("/res/maps/Hallway-Final.png"));
		window.backgroundLayer.add(background);
	
	
		//Spawn: x = 415, 7 = 1569
			
		//Barrier lines, this takes too long	
		linegen.createLine(661, 1969, 661, 1680, color, window);
		linegen.createLine(661, 1680, 751, 1680, color, window);
		linegen.createLine(751, 1680, 751, 1510, color, window);
		linegen.createLine(661, 1969, 1130, 1969, color, window);
		linegen.createLine(1130, 1969, 1130, 1680, color, window);
		linegen.createLine(1130, 1680, 871, 1680, color, window);
		linegen.createLine(871, 1510, 871, 1680, color, window);
		linegen.createLine(871, 1510, 1680, 1510, color, window);
		linegen.createLine(1680, 1510, 1680, 1554, color, window);
		linegen.createLine(1680, 1554, 1982, 1554, color, window);
		linegen.createLine(1982, 1554, 1982, 1329, color, window);
		linegen.createLine(1982, 1329, 1678, 1329, color, window);
		linegen.createLine(1678, 1329, 1678, 1359, color, window);
		linegen.createLine(1678, 1359, 1232, 1359, color, window);
		linegen.createLine(1232, 1359, 1232, 1167, color, window);
		linegen.createLine(1232, 1170, 1322, 1170, color, window);
		linegen.createLine(1322, 1170, 1322, 945, color, window);
		linegen.createLine(1322, 945, 1232, 945, color, window);
		//DOORWAY FRONTyes
		linegen.createLine(1232, 945, 1082, 945, color, window);
		
		linegen.createLine(1082, 945, 1011, 945, color, window);
		linegen.createLine(1011, 945, 1011, 1170, color, window);
		linegen.createLine(1011, 1170, 1110, 1170, color, window);
		linegen.createLine(1110, 1170, 1110, 1360, color, window);
		linegen.createLine(1110, 1360, 333, 1360, color, window);
		linegen.createLine(333, 1360, 333, 1600, color, window);
		linegen.createLine(333, 1600, 570, 1600, color, window);
		linegen.createLine(570, 1600, 570, 1510, color, window);
		linegen.createLine(570, 1510, 750, 1510, color, window);
		
	}
}