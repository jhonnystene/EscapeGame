package com.boiswhodontknowhowtocompsci.escapegame;

import java.awt.Color;
import java.io.IOException;

import com.asuscomm.johnnystene.escape.Window;
import com.asuscomm.johnnystene.escape.WorldItem;

import res.FileLoader;

public class BigCircularRoom {
	Window window;
	CollisionLineGenerator linegen;
	FileLoader fileLoader;
	
	public BigCircularRoom(Window w, CollisionLineGenerator l, FileLoader f) {
		window = w;
		linegen = l;
		fileLoader = f;
	}
	
	public void createAt(int x, int y) throws IOException {
		Color color = new Color(67, 67, 75);
		
		WorldItem background = new WorldItem(fileLoader.load("/res/maps/Upstairs.png"));
		window.backgroundLayer.add(background);

		/*
		//0, 574 to 78, 462
		linegen.createLine(0, 574, 78, 462, color, window);
		//78, 462 to 202, 345
		linegen.createLine(78, 462, 202, 345, color, window);
		//202, 345 to 418, 256
		linegen.createLine(202, 345, 418, 256, color, window);
		//418, 256 to 847, 256
		linegen.createLine(418, 256, 847, 256, color, window);
		//847, 256 to 1066, 345
		linegen.createLine(847, 256, 1066, 345, color, window);
		//1066, 345 to 1191, 462
		linegen.createLine(1066, 345, 1191, 462, color, window);
		//1191, 462 to 1264, 572
		linegen.createLine(1191, 462, 1264, 572, color, window);
		//1264, 572 to 1264, 717
		linegen.createLine(1264, 572, 1264, 717, color, window);
		//1264, 717 to 1188, 828
		linegen.createLine(1264, 717, 1188, 828, color, window);
		//1188, 828 to 1060, 947
		linegen.createLine(1188, 828, 1060, 947, color, window);
		//1060, 947 to 848, 1035
		linegen.createLine(1060, 947, 848, 1035, color, window);
		//848, 1035 to 848, 1251
		linegen.createLine(848, 1035, 848, 1251, color, window);
		//848, 1251 to 416, 1251
		linegen.createLine(848, 1251, 416, 1252, color, window);
		//416, 1251 to 416, 1035
		linegen.createLine(416, 1251, 416, 1035, color, window);
		//416, 1035 to 198, 946
		linegen.createLine(416, 1035, 198, 946, color, window);
		//198, 946 to 72, 828
		linegen.createLine(198, 946, 72, 828, color, window);
		//72, 828 to 0, 722
		linegen.createLine(72, 828, 0, 722, color, window);
		//0, 722 to 0, 574
		linegen.createLine(0, 722, 0, 574, color, window);*/
		linegen.createLine(824, 1484, 824, 1269, color, window);
		linegen.createLine(824, 1269, 606, 1179, color, window);
		linegen.createLine(606, 1179, 482, 1063, color, window);
		linegen.createLine(482, 1063, 408, 955, color, window);
		linegen.createLine(408, 955, 408, 804, color, window);
		linegen.createLine(408, 804, 486, 696, color, window);
		linegen.createLine(486, 696, 611, 579, color, window);
		linegen.createLine(611, 579, 836, 485, color, window);
		linegen.createLine(836, 485, 866, 508, color, window);
		linegen.createLine(866, 508, 1217, 508, color, window);
		linegen.createLine(1217, 508, 1247, 485, color, window);
		linegen.createLine(1247, 485, 1473, 580, color, window);
		linegen.createLine(1473, 580, 1593, 693, color, window);
		linegen.createLine(1593, 693, 1671, 805, color, window);
		linegen.createLine(1671, 805, 1671, 954, color, window);
		linegen.createLine(1671, 954, 1591, 1061, color, window);
		linegen.createLine(1591, 1061, 1467, 1180, color, window);
		linegen.createLine(1467, 1180, 1254, 1268, color, window);
		linegen.createLine(1254, 1268, 1254, 1484, color, window);
		linegen.createLine(1254, 1484, 824, 1484, color, window);

	}
	
}
