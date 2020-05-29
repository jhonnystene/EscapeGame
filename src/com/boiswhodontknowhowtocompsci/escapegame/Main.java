/*
 * Main.java
 * By Johnny, Ethan S, Caleb M (Add your names here when you fuckers finally decide to do work)
 * 
 * Main game class for Escape The Robots And Oh Gosh They're Coming Run
 */

package com.boiswhodontknowhowtocompsci.escapegame;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.Graphics2D;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.asuscomm.johnnystene.infinity.CollisionItem;
import com.asuscomm.johnnystene.infinity.GithubUtils;
import com.asuscomm.johnnystene.infinity.Window;
import com.asuscomm.johnnystene.infinity.WorldItem;

public class Main {
	
	private static boolean DEBUG_BUILD = true;
	private static int DEBUG_LEVEL = 0; // 0 - Don't include frame-by-frame debug information, 1 - Include all information
	
	private static void debug(String message) {
		if(DEBUG_BUILD) System.out.println(message);
	}
	
	public static void main(String[] args) {
		debug("Escape The Robots And Oh Gosh They're Coming Run");
		debug("Beta Version");
		
		debug("Initializing Window...");
		Window window = new Window(1136, 640, "Escape The Robots And Oh Gosh They're Coming Run"); // Create the game window
		
		debug("Initializing MapGenerator...");
		MapGenerator mapgen = new MapGenerator();

		debug("Initializing CollisionLineGenerator...");
		CollisionLineGenerator linegen = new CollisionLineGenerator();
		
		// Download and play the music
		debug("Initializing AudioInputStream...");
		AudioInputStream titleInputStream;
		try {
			debug("Downloading title screen music...");
			titleInputStream = AudioSystem.getAudioInputStream(new URL(GithubUtils.getFullPath("msc/title.wav")));
			debug("Starting playback of title screen music...");
			window.loopMusic(titleInputStream);
		} catch (UnsupportedAudioFileException | IOException e) {
			window.crash("Couldn't load title screen music", e);
		}
		
		//Menu Button sizes
		int menuButtonWidth = (window.windowWidth / 2) - 50;
		int menuButtonHeight = 100;
		
		// Title screen loop
		boolean inTitleScreen = true;
		debug("Starting title screen...");
		while(inTitleScreen) {
			try {
				window.drawRectangle(0, 0, window.windowWidth, window.windowHeight, Color.BLACK); // Black background
				
				// Title Text
				window.drawTextCentered(window.windowWidth / 2, (window.windowHeight / 2) - 50, "Escape The Robots And", 40, Color.WHITE);
				window.drawTextCentered(window.windowWidth / 2, window.windowHeight / 2, "Oh Gosh They're Coming Run", 40, Color.WHITE);
				
				// PLAY button (duh)
				if(window.drawMenuButton((window.windowWidth / 2) - (menuButtonWidth / 2), window.windowHeight - 300, menuButtonWidth, menuButtonHeight, "PLAY", Color.WHITE, new Color(255, 66, 28), new Color(255, 91, 59)))
					inTitleScreen = false;
				
				// QUIT button (duh)
				if(window.drawMenuButton((window.windowWidth / 2) - (menuButtonWidth / 2), window.windowHeight - 180, menuButtonWidth, menuButtonHeight, "QUIT GAME", Color.WHITE, new Color(255, 66, 28), new Color(255, 91, 59)))
					System.exit(0);
				
				window.repaint();
				Thread.sleep(1000 / 10); // FPS cap needed in menus too
			} catch(Exception e) {
				window.crash("Error while drawing title screen", e);
			}
		}
		
		// Create the player object and preload the player sprite
		debug("Downloading player sprites and initializing player...");
		window.drawLoadingScreen("Downloading player sprites...");
		CollisionItem player = new CollisionItem(GithubUtils.getFullPath("img/player/test_player.png"), true);
		player.x = 360;//24 * 80;
		player.y = 1800;//30 * 80;
		window.collisionItemLayer.add(player);
		
		/*w// Do the same for Roboboi (commented all the sprites out to improve loading times while i get isometric support baked in)
		debug("Initializing Roboboi...");
		window.drawLoadingScreen("Downloading Roboboi sprites...");
		CollisionItem roboboi = new CollisionItem(64, 64, Color.BLACK);
		roboboi.x = 18 * 80;
		roboboi.y = 30 * 80;
		roboboi.isometricItem = true; // My lazy ass finally implemented this
		
		debug("Downloading isometric sprites for Roboboi...");
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
		//window.collisionItemLayer.add(roboboi);
		*/
		
		// Load in first level
		Outside outside = new Outside(window, linegen);
		outside.create();
		
		// Terminal Hitbox
		CollisionItem terminal = new CollisionItem(64,64, Color.RED);
		terminal.x = 560;
		terminal.y = 1645;
		boolean terminalSolved = false;
		
		// Door Loading Zone
		CollisionItem outsideDoor = new CollisionItem(100,32, Color.RED);
		outsideDoor.x = 400;
		outsideDoor.y = 1600;
		
		window.renderBackground();
		
		WorldItem terminalSprite = null;
		try {
			terminalSprite = new WorldItem(ImageIO.read(new URL(GithubUtils.getFullPath("img/terminal.png"))));
		} catch(Exception e) {
			window.crash("Failed to load terminal background sprite.", e);
		}
			
		// Start the game loop
		debug("Loading finished. Entering game loop...");
		
		boolean gameRunning = true;
		int currentLevel = 0;
		boolean freecam = false;
		while(gameRunning) {
			try {
				window.clear();
				
				// Level-specific code
				if(currentLevel == 0) {
					if(window.keyListener.KEY_ACTION) {
						if(terminalSolved && player.collidingWith(outsideDoor)) {
							window.drawLoadingScreen("");
							window.collisionItemLayer.clear(); // Remove all collision items
							window.backgroundLayer.clear();
							window.effectLayer.clear(); // TODO: Fade in/out
							window.collisionItemLayer.add(player); // Re-add player
							
							// Put player where they need to go
							player.x = 415;
							player.y = 1569;
							
							// Load in next level
							Hallway hallway = new Hallway(window, linegen);
							hallway.create();
							window.renderBackground();
							currentLevel = 1;
						}
						
						if(!terminalSolved && player.collidingWith(terminal)) {
							// Terminal level
							while(!terminalSolved) {
								window.drawRectangle(0, 0, window.windowWidth, window.windowHeight, Color.BLACK); // Black background
								
								Graphics2D graphics = window.frameBuffer.createGraphics();
								graphics.drawImage(terminalSprite.sprite, 0, 0, window);
								graphics.dispose();
								
								if(window.drawMenuButton(25, window.windowHeight - 125, 25, 50, "", Color.WHITE, new Color(0, 0, 0), new Color(5, 5, 5))) {
									terminalSolved = true;
									break;
								}
								window.repaint();
								Thread.sleep(1000 / 15); 
							}
						}
					}
				}
				
				// Pause menu
				if(window.keyListener.KEY_MENU) {
					while(1==1) {
						window.drawRectangle(0, 0, window.windowWidth, window.windowHeight, new Color(0,0,0)); // Black background
						
						if(window.drawMenuButton((window.windowWidth / 2) - (menuButtonWidth / 2), window.windowHeight - 400, menuButtonWidth, menuButtonHeight, "RESUME", Color.WHITE, new Color(255, 66, 28), new Color(255, 91, 59))) {
							break;
						}
							
						if(window.drawMenuButton((window.windowWidth / 2) - (menuButtonWidth / 2), window.windowHeight - 280, menuButtonWidth, menuButtonHeight, "QUIT GAME", Color.WHITE, new Color(255, 66, 28), new Color(255, 91, 59))) {
							System.exit(0);
						}
							
						window.repaint();
						Thread.sleep(1000 / 60); 
					}
				}
				
				// Print debug information
				if(DEBUG_BUILD && DEBUG_LEVEL == 0)
					System.out.println("FPS: " + Integer.toString((int) window.FPS) + ", Delta: " + Float.toString(window.delta) + 
							", X: " + Integer.toString((int) player.x) + ", Y: " + Integer.toString((int) player.y));
				
				// Movement Code
				float moveX = 0;
				float moveY = 0;
				
				if(window.keyListener.KEY_LEFT) moveX -= 15;
				if(window.keyListener.KEY_RIGHT) moveX += 15;
				if(window.keyListener.KEY_UP) moveY -= 15;
				if(window.keyListener.KEY_DOWN) moveY += 15; 
				
				moveX = moveX * window.delta;
				moveY = moveY * window.delta;
				
				if(window.keyListener.KEY_FREECAM) {
					Thread.sleep(200);
					debug("Toggling freecam...");
					freecam = !freecam;
				}
				
				if(freecam) {
					window.cameraX += moveX;
					window.cameraY += moveY;
				} else {
					player.moveAndCollide(moveX, moveY, window.collisionItemLayer);
					window.centerCamera(player);
				}
				
				// Drawing Code
				window.drawLayers();
				window.repaint();
				//Thread.sleep(1000 / 60);
			} catch(Exception e) {
				window.crash("Unrecoverable error in main game loop", e);
			}
		}
		System.exit(0);
	}
}
