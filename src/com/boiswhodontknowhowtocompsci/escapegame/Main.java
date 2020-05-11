/*
 * Main.java
 * 
 * Example game for the Infinity Engine.
 */

package com.boiswhodontknowhowtocompsci.escapegame;

import java.awt.Color;
import com.asuscomm.johnnystene.infinity.*;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		Window window = new Window(800, 600, "Escape The Robots and Oh Gosh They're Coming Run"); // Create window that is 800x600
		window.enableCamera = true; // Make sure camera is enabled
		
		window.drawRectangle(0, 0, 800, 600, new Color(59, 59, 59));
		window.drawTextCentered(400, 300, "Now Loading...", 40, Color.WHITE);
		window.repaint();
		
		// Create a player object
		// CollisionItem(width, height, color);
		// Can also do CollisionItem(filename);
		CollisionItem player = new CollisionItem("https://github.com/jhonnystene/EscapeGame/raw/master/res/img/test/giphy.gif", true);
		player.x = 100;
		player.y = 200;
		
		// Create a box
		CollisionItem box = new CollisionItem("https://github.com/jhonnystene/EscapeGame/raw/master/res/img/roboboi/tile_00.png", true);
		box.x = 200;
		box.y = 200;
		
		boolean inTitleScreen = true;
		while(inTitleScreen) {
			window.drawTextCentered(400, 250, "Escape The Robots And", 40, Color.BLACK);
			window.drawTextCentered(400, 300, "Oh Gosh They're Coming Run", 40, Color.BLACK);
			
			if(window.drawMenuButton(225, 300, 350, 100, "put me in coach.", Color.WHITE, new Color(255, 66, 28), new Color(255, 91, 59))) {
				inTitleScreen = false;
			}
			
			window.repaint();
			Thread.sleep(1000 / 60); // FPS cap needed in menus too
		}
		
		while(1 == 1) {
			if(!window.keyListener.KEY_ACTION) { 
				// Manage keyboard input
				if(window.keyListener.KEY_LEFT) player.x -= 10; 
				if(window.keyListener.KEY_RIGHT) player.x += 10;
				if(window.keyListener.KEY_UP) player.y -= 10;
				if(window.keyListener.KEY_DOWN) player.y += 10; 
				
				window.centerCamera(player);
			} else {
				int movementX = 0;
				int movementY = 0;
				if(window.keyListener.KEY_LEFT) movementX -= 10; 
				if(window.keyListener.KEY_RIGHT) movementX += 10;
				if(window.keyListener.KEY_UP) movementY -= 10;
				if(window.keyListener.KEY_DOWN) movementY += 10;
				
				box.x += movementX;
				box.y += movementY;
				
				window.centerCamera(box);
			}
			
			window.drawWorldItem(box);
			window.drawWorldItem(player); // Draw the player into the world
			
			if(player.collidingWith(box)) {
				System.out.println("Colliding!");
			}
			
			// This should always be at the end of the game loop when not using an fps cap
			window.repaint();
			Thread.sleep(1000 / 60); // This is how to do an fps cap
		}
	}
}
