//don't know what do, this is just beginning for
//code so yeah, now i do lines
package com.boiswhodontknowhowtocompsci.escapegame;

import java.awt.Color;
import java.io.IOException;

import com.asuscomm.johnnystene.escape.CollisionItem;
import com.asuscomm.johnnystene.escape.Window;
import com.asuscomm.johnnystene.escape.WorldItem;

import res.FileLoader;

public class Hallway {
	Window window;
	MapGenerator mapGen;
	FileLoader fileLoader;

	boolean button0Pushed = false;
	boolean button1Pushed = false;
	boolean button2Pushed = false;
	boolean button3Pushed = false;
	boolean button4Pushed = false;
	boolean button5Pushed = false;
	boolean complete = false;

	CollisionItem button0;
	CollisionItem button1;
	CollisionItem button2;
	CollisionItem button3;
	CollisionItem button4;
	CollisionItem button5;

	CollisionItem loadingZone;

	public Hallway(Window w, MapGenerator m, FileLoader f) {
		window = w;
		mapGen = m;
		fileLoader = f;

		button0 = new CollisionItem(32, 17, Color.GREEN);
		button1 = new CollisionItem(32, 17, Color.GREEN);
		button2 = new CollisionItem(32, 17, Color.GREEN);
		button3 = new CollisionItem(32, 17, Color.GREEN);
		button4 = new CollisionItem(32, 17, Color.GREEN);
		button5 = new CollisionItem(32, 17, Color.GREEN);

		loadingZone = new CollisionItem(153, 64, Color.BLACK);
	}
	
	public void create() throws IOException {
		Color color = new Color(67, 67, 75);

		WorldItem background = new WorldItem(fileLoader.load("/res/maps/Hallway-Final.png"));
		window.backgroundLayer.add(background);

		// Hallway buttons
		button0.x = 1044;
		button0.y = 991;

		button1.x = 1044;
		button1.y = 1048;

		button2.x = 1044;
		button2.y = 1105;

		button3.x = 1256;
		button3.y = 991;

		button4.x = 1256;
		button4.y = 1048;

		button5.x = 1256;
		button5.y = 1105;

		loadingZone.x = 1094;
		loadingZone.y = 947;

		mapGen.loadMap(fileLoader.load("/res/maps/collision/hallway.map"));
	}

	public void setAll(boolean value) {
		button0Pushed = value;
		button1Pushed = value;
		button2Pushed = value;
		button3Pushed = value;
		button4Pushed = value;
		button5Pushed = value;
	}
}