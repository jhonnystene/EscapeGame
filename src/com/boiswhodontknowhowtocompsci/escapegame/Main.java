/*
 * Main.java
 * 
 * Example game for the Infinity Engine.
 */

package com.boiswhodontknowhowtocompsci.escapegame;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import com.asuscomm.johnnystene.infinity.*;

public class Main {
	public static void main(String[] args) throws InterruptedException, MalformedURLException, IOException {
		Window window = new Window(800, 600, "Escape The Robots and Oh Gosh They're Coming Run"); // Create window that is 800x600
		window.enableCamera = true; // Make sure camera is enabled
		
		System.out.println("Loading...");
		
		// Create a player object
		// CollisionItem(width, height, color);
		// Can also do CollisionItem(filename);
		CollisionItem player = new CollisionItem(GithubUtils.getFullPath("img/player/test_player.png"), true);
		player.x = 100;
		player.y = 200;
		
		// Create a box
		BufferedImage boxSW = ImageIO.read(new URL(GithubUtils.getFullPath("img/roboboi/tile_00.png")));
		BufferedImage boxW = ImageIO.read(new URL(GithubUtils.getFullPath("img/roboboi/tile_01.png")));
		BufferedImage boxNW = ImageIO.read(new URL(GithubUtils.getFullPath("img/roboboi/tile_02.png")));
		BufferedImage boxN = ImageIO.read(new URL(GithubUtils.getFullPath("img/roboboi/tile_03.png")));
		BufferedImage boxNE = ImageIO.read(new URL(GithubUtils.getFullPath("img/roboboi/tile_04.png")));
		BufferedImage boxE = ImageIO.read(new URL(GithubUtils.getFullPath("img/roboboi/tile_05.png")));
		BufferedImage boxSE = ImageIO.read(new URL(GithubUtils.getFullPath("img/roboboi/tile_06.png")));
		BufferedImage boxS = ImageIO.read(new URL(GithubUtils.getFullPath("img/roboboi/tile_07.png")));
		CollisionItem box = new CollisionItem(boxE);
		box.x = 200;
		box.y = 200;
		
		boolean inTitleScreen = true;
		while(inTitleScreen) {
			window.drawTextCentered(400, 250, "Escape The Robots And", 40, Color.BLACK);
			window.drawTextCentered(400, 300, "Oh Gosh They're Coming Run", 40, Color.BLACK);
			
			if(window.drawMenuButton(25, 475, 350, 100, "PLAY", Color.WHITE, new Color(255, 66, 28), new Color(255, 91, 59))) {
				inTitleScreen = false;
			}
			
			if(window.drawMenuButton(425, 475, 350, 100, "QUIT GAME", Color.WHITE, new Color(255, 66, 28), new Color(255, 91, 59))) {
				System.exit(0);
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
				
				if(movementX > 0) {
					if(movementY > 0) {
						box.sprite = boxSE;
					} else if(movementY < 0) {
						box.sprite = boxNE;
					} else {
						box.sprite = boxE;
					}
				} else if(movementX < 0) {
					if(movementY > 0) {
						box.sprite = boxSW;
					} else if(movementY < 0) {
						box.sprite = boxNW;
					} else {
						box.sprite = boxW;
					}
				} else {
					if(movementY > 0) {
						box.sprite = boxS;
					} else if(movementY < 0) {
						box.sprite = boxN;
					}
				}
				
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
