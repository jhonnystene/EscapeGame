/*
 * Outside the building
 * To complete, the player needs to "hack" the terminal.
 * This feels like a useless start to the game, and needs to be reworked
 * to be more fun and challenging.
 */

package com.boiswhodontknowhowtocompsci.escapegame;

import java.awt.Color;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.asuscomm.johnnystene.escape.CollisionItem;
import com.asuscomm.johnnystene.escape.Window;
import com.asuscomm.johnnystene.escape.WorldItem;

import res.FileLoader;

public class Outside {
	Window window;
	MapGenerator mapGen;
	FileLoader fileLoader;

	CollisionItem door;
	CollisionItem terminal;

	boolean terminalSolved;
	
	public Outside(Window w, MapGenerator m, FileLoader f) {
		window = w;
		mapGen = m;
		fileLoader = f;
	}
	
	public void create() throws IOException {
		WorldItem background = new WorldItem(ImageIO.read(fileLoader.load("/res/maps/Outside-Final.png")));
		window.backgroundLayer.add(background);
		window.renderBackground();

		// Terminal Hitbox
		terminal = new CollisionItem(64, 64, Color.RED);
		terminal.x = 560;
		terminal.y = 1645;
		terminalSolved = false;

		// Door Loading Zone
		door = new CollisionItem(100, 50, Color.RED);
		door.x = 400;
		door.y = 1600;

		mapGen.loadMap(fileLoader.load("/res/maps/collision/outside.map"));
	}
}