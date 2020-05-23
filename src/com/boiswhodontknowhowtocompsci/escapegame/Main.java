/*
 * Main.java
 * By Johnny, (Add your names here when you fuckers finally decide to do work)
 * 
 * Main game class for Escape The Robots And Oh Gosh They're Coming Run
 */

package com.boiswhodontknowhowtocompsci.escapegame;

import java.awt.Color;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.asuscomm.johnnystene.infinity.CollisionItem;
import com.asuscomm.johnnystene.infinity.GithubUtils;
import com.asuscomm.johnnystene.infinity.Window;
import com.asuscomm.johnnystene.infinity.WorldItem;

public class Main {
	public static void main(String[] args) {
		Window window = new Window(1136, 640, "Escape The Robots And Oh Gosh They're Coming Run"); // Create the game window
		
		// Download and play the music
		AudioInputStream titleInputStream;
		try {
			titleInputStream = AudioSystem.getAudioInputStream(new URL(GithubUtils.getFullPath("msc/title.wav")));
			window.loopMusic(titleInputStream);
		} catch (UnsupportedAudioFileException | IOException e) {
			window.crash("Couldn't load title screen music", e);
		}
		
		// Title screen loop
		boolean inTitleScreen = true;
		while(inTitleScreen) {
			try {
				window.drawRectangle(0, 0, window.windowWidth, window.windowHeight, Color.BLACK); // Black background
				
				// Title Text
				window.drawTextCentered(window.windowWidth / 2, (window.windowHeight / 2) - 50, "Escape The Robots And", 40, Color.WHITE);
				window.drawTextCentered(window.windowWidth / 2, window.windowHeight / 2, "Oh Gosh They're Coming Run", 40, Color.WHITE);
				
				// PLAY button (duh)
				if(window.drawMenuButton(25, window.windowHeight - 125, (window.windowWidth / 2) - 50, 100, "PLAY", Color.WHITE, new Color(255, 66, 28), new Color(255, 91, 59)))
					inTitleScreen = false;
				
				// QUIT button (duh)
				if(window.drawMenuButton((window.windowWidth / 2) + 25, window.windowHeight - 125, (window.windowWidth / 2) - 50, 100, "QUIT GAME", Color.WHITE, new Color(255, 66, 28), new Color(255, 91, 59)))
					System.exit(0);
				
				window.repaint();
				Thread.sleep(1000 / 60); // FPS cap needed in menus too
			} catch(Exception e) {
				window.crash("Error while drawing title screen", e);
			}
		}
		
		// Create the player object and preload the player sprite
		window.drawLoadingScreen("Downloading player sprites...");
		CollisionItem player = new CollisionItem(GithubUtils.getFullPath("img/player/test_player.png"), true);
		player.x = player.y = 0; // X and Y are the same here so we can condense it to one line
		window.collisionItemLayer.add(player);
		
		// Do the same for Roboboi (commented all the sprites out to improve loading times while i get isometric support baked in)
		window.drawLoadingScreen("Downloading Roboboi sprites...");
		CollisionItem roboboi = new CollisionItem(64, 64, Color.BLACK);
		roboboi.x = roboboi.y = 200;
		roboboi.isometricItem = true; // My lazy ass finally implemented this
		
		try {
			// Download all sprites for Roboboi
			roboboi.isometricSprites[WorldItem.SW] = ImageIO.read(new URL(GithubUtils.getFullPath("img/roboboi/tile_00.png")));
			roboboi.isometricSprites[WorldItem.W] = ImageIO.read(new URL(GithubUtils.getFullPath("img/roboboi/tile_01.png")));
			roboboi.isometricSprites[WorldItem.NW] = ImageIO.read(new URL(GithubUtils.getFullPath("img/roboboi/tile_02.png")));
			roboboi.isometricSprites[WorldItem.N] = ImageIO.read(new URL(GithubUtils.getFullPath("img/roboboi/tile_03.png")));
			roboboi.isometricSprites[WorldItem.NE] = ImageIO.read(new URL(GithubUtils.getFullPath("img/roboboi/tile_04.png")));
			roboboi.isometricSprites[WorldItem.E] = ImageIO.read(new URL(GithubUtils.getFullPath("img/roboboi/tile_05.png")));
			roboboi.isometricSprites[WorldItem.SE] = ImageIO.read(new URL(GithubUtils.getFullPath("img/roboboi/tile_06.png")));
			roboboi.isometricSprites[WorldItem.S] = ImageIO.read(new URL(GithubUtils.getFullPath("img/roboboi/tile_07.png")));
		} catch(Exception e) {
			window.crash("Failed downloading Roboboi sprites.", e);
		}
		
		// Set the sprite to the proper one
		roboboi.sprite = roboboi.isometricSprites[WorldItem.E];
		window.collisionItemLayer.add(roboboi);
		
		CollisionItem currentPlayer = player;
		
		// Start the game loop
		boolean gameRunning = true;
		while(gameRunning) {
			try { 
				// Handle inputs
				int moveX = 0;
				int moveY = 0;
				
				if(window.keyListener.KEY_LEFT) moveX -= 10;
				if(window.keyListener.KEY_RIGHT) moveX += 10;
				if(window.keyListener.KEY_UP) moveY -= 10;
				if(window.keyListener.KEY_DOWN) moveY += 10; 
				
				// Should we switch between player and roboboi?
				if(window.keyListener.KEY_ACTION) {
					Thread.sleep(200); // Debounce
					
					// Toggle currently controlled object
					if(currentPlayer == player) currentPlayer = roboboi;
					else currentPlayer = player;
				}
				
				// Move currently controlled object
				currentPlayer.moveAndCollide(moveX, moveY, window.collisionItemLayer);
				window.centerCamera(currentPlayer);
				
				// Draw all objects to the screen and update
				window.drawLayers();
				window.repaint();
				
				Thread.sleep(1000 / 60); // This is needed to stop the game from running WAAAAAAAYYY too fast, delta timer would be a better idea though
			} catch(Exception e) {
				window.crash("Unrecoverable error in main game loop", e);
			}
		}
		
		// We've crashed.
		// TODO: Draw crash screen
	}
}
