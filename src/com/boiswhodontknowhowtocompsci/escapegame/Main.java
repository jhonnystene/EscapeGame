/*
 * Main.java
 * By Johnny, Ethan S, Caleb M, Aiden S, Ethan M
 * 
 * Main game class for Escape The Robots And Oh Gosh They're Coming Run
 */

package com.boiswhodontknowhowtocompsci.escapegame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.asuscomm.johnnystene.infinity.CollisionItem;
import com.asuscomm.johnnystene.infinity.Window;
import com.asuscomm.johnnystene.infinity.WorldItem;

import res.FileLoader;

public class Main {
	
	private static boolean DEBUG_BUILD = true;
	private static int DEBUG_LEVEL = 0; // 0 - Don't include frame-by-frame debug information, 1 - Include all information
	
	private static boolean ENABLE_TITLE_SCREEN_ANIMATION = false; // Makes loading take longer. Enable for release/demo builds.
	
	private static void debug(String message) {
		if(DEBUG_BUILD) System.out.println(message);
	}
	
	public static void main(String[] args) throws IOException, InterruptedException, LineUnavailableException, UnsupportedAudioFileException {
		debug("Escape The Robots And Oh Gosh They're Coming Run");
		debug("DEBUG BUILD");
		
		debug("Initializing Window...");
		Window window = new Window(1136, 640, "Escape The Robots And Oh Gosh They're Coming Run"); // Create the game window
		
		debug("Initializing MapGenerator...");
		MapGenerator mapgen = new MapGenerator();

		debug("Initializing CollisionLineGenerator...");
		CollisionLineGenerator linegen = new CollisionLineGenerator();
		
		debug("Initializing FileLoader...");
		FileLoader fileLoader = new FileLoader();
		
		// Download and play the music
		debug("Initializing AudioInputStream...");
		AudioInputStream titleInputStream;
		debug("Loading title screen music...");
		titleInputStream = AudioSystem.getAudioInputStream(fileLoader.load("/res/music/title.wav"));
		debug("Starting playback of title screen music...");
		window.loopMusic(titleInputStream);
		
		//Menu Button sizes
		int menuButtonWidth = 100;
		int menuButtonHeight = 20;
		int menuButtonTextSize = 16;
		int menuButtonSpacing = 10;
		
		window.drawLoadingScreen("Rendering intro animation...");
		ArrayList<BufferedImage> titleVideo = new ArrayList<BufferedImage>();
		if(ENABLE_TITLE_SCREEN_ANIMATION) {
			debug("Loading title screen video...");
			for(int i = 1; i < 101; i++) {
				//      ^       ^
				// ffmpeg named the files starting at one so i gotta do this shit
				// it hurts
				
				titleVideo.add(window.resizeImage(ImageIO.read(fileLoader.load("/res/title/out" + Integer.toString(i) + ".png")), window.windowWidth, window.windowHeight));
			}
		}
		
		// Title screen loop
		boolean inTitleScreen = true;
		debug("Starting title screen...");
		int frame = 0;
		double frameLength = 83333333.33;
		double nextFrameTime = System.nanoTime() + frameLength;
		
		while(inTitleScreen) {
			if(ENABLE_TITLE_SCREEN_ANIMATION) {
				Raster newFB = titleVideo.get(frame).getData(new Rectangle(0, 0, window.windowWidth, window.windowHeight));
				window.frameBuffer.setData(newFB);
				if(System.nanoTime() >= nextFrameTime) {
					frame ++;
					nextFrameTime = System.nanoTime() + frameLength;
				}
				if(frame == 100) frame = 0;
			} else {
				window.drawRectangle(0, 0, window.windowWidth, window.windowHeight, Color.BLACK); // Black background
			}
			
			// Title Text
			window.drawText(20, window.windowHeight - 15 - ((menuButtonHeight + menuButtonSpacing) * 6), "Escape The Robots And", 20, Color.WHITE);
			window.drawText(20, window.windowHeight - 15 - ((menuButtonHeight + menuButtonSpacing) * 5), "Oh Gosh They're Coming Run", 20, Color.WHITE);
			
			// NEW GAME button (duh)
			if(window.drawClearMenuButton(20, window.windowHeight - 20 - ((menuButtonHeight + menuButtonSpacing) * 4), menuButtonWidth, menuButtonHeight, menuButtonTextSize, "NEW GAME", Color.WHITE, Color.LIGHT_GRAY))
				inTitleScreen = false;
			
			// LOAD GAME button (duh)
			//window.drawClearMenuButton(20, window.windowHeight - 15 - ((menuButtonHeight + menuButtonSpacing) * 3), menuButtonWidth, menuButtonHeight, menuButtonTextSize, "LOAD GAME", Color.DARK_GRAY, Color.DARK_GRAY);
			
			//window.drawClearMenuButton(20, window.windowHeight - 15 - ((menuButtonHeight + menuButtonSpacing) * 2), menuButtonWidth, menuButtonHeight, menuButtonTextSize, "OPTIONS", Color.DARK_GRAY, Color.DARK_GRAY);
			
			// QUIT button (duh)
			if(window.drawClearMenuButton(20, window.windowHeight - 20 - ((menuButtonHeight + menuButtonSpacing) * 3), menuButtonWidth, menuButtonHeight, menuButtonTextSize, "QUIT GAME", Color.WHITE, Color.LIGHT_GRAY))
				System.exit(0);
			
			window.repaint();
			Thread.sleep(1000 / 30); // FPS cap needed in menus too
		}
		
		// Create the player object and preload the player sprite
		debug("Loading player...");
		CollisionItem player = new CollisionItem(window.resizeImage(ImageIO.read(fileLoader.load("/res/player/static/BStatic.png")), 64, 64));
		player.x = 360;
		player.y = 1800;
		player.isometricItem = true;
		window.collisionItemLayer.add(player);
		
		player.isometricSprites[WorldItem.SW]= window.resizeImage(ImageIO.read(fileLoader.load("/res/player/static/FLStatic.png")), 64, 64);
		player.isometricSprites[WorldItem.S]= window.resizeImage(ImageIO.read(fileLoader.load("/res/player/static/FStatic.png")), 64, 64);
		player.isometricSprites[WorldItem.SE]= window.resizeImage(ImageIO.read(fileLoader.load("/res/player/static/FRStatic.png")), 64, 64);
		player.isometricSprites[WorldItem.E]= window.resizeImage(ImageIO.read(fileLoader.load("/res/player/static/RStatic.png")), 64, 64);
		player.isometricSprites[WorldItem.NE]= window.resizeImage(ImageIO.read(fileLoader.load("/res/player/static/BRStatic.png")), 64, 64);
		player.isometricSprites[WorldItem.N]= window.resizeImage(ImageIO.read(fileLoader.load("/res/player/static/BStatic.png")), 64, 64);
		player.isometricSprites[WorldItem.NW]= window.resizeImage(ImageIO.read(fileLoader.load("/res/player/static/BLStatic.png")), 64, 64);
		player.isometricSprites[WorldItem.W]= window.resizeImage(ImageIO.read(fileLoader.load("/res/player/static/LStatic.png")), 64, 64);
		
		// Load in first level
		debug("Loading first level contents...");
		Outside outside = new Outside(window, linegen, fileLoader);
		outside.create();
		
		// Terminal Hitbox
		CollisionItem terminal = new CollisionItem(64,64, Color.RED);
		terminal.x = 560;
		terminal.y = 1645;
		boolean terminalSolved = false;
		
		// Door Loading Zone
		CollisionItem outsideDoor = new CollisionItem(100, 50, Color.RED);
		outsideDoor.x = 400;
		outsideDoor.y = 1600;
		
		window.renderBackground();
		
		WorldItem terminalSprite = null;
		terminalSprite = new WorldItem(fileLoader.load("/res/ui/terminal.png"));
		terminalSprite.sprite = window.resizeImage(terminalSprite.sprite, 1136, 640);
			
		// Hallway buttons
		boolean hallwayButton0Pushed = false;
		CollisionItem hallwayButton0 = new CollisionItem(32, 17, Color.GREEN); 
		hallwayButton0.x = 1044;
		hallwayButton0.y = 991;
		
		boolean hallwayButton1Pushed = false;
		CollisionItem hallwayButton1 = new CollisionItem(32, 17, Color.GREEN); 
		hallwayButton1.x = 1044;
		hallwayButton1.y = 1048;
		
		boolean hallwayButton2Pushed = false;
		CollisionItem hallwayButton2 = new CollisionItem(32, 17, Color.GREEN); 
		hallwayButton2.x = 1044;
		hallwayButton2.y = 1105;
		
		boolean hallwayButton3Pushed = false;
		CollisionItem hallwayButton3 = new CollisionItem(32, 17, Color.GREEN); 
		hallwayButton3.x = 1256;
		hallwayButton3.y = 991;
		
		boolean hallwayButton4Pushed = false;
		CollisionItem hallwayButton4 = new CollisionItem(32, 17, Color.GREEN); 
		hallwayButton4.x = 1256;
		hallwayButton4.y = 1048;
		
		boolean hallwayButton5Pushed = false;
		CollisionItem hallwayButton5 = new CollisionItem(32, 17, Color.GREEN); 
		hallwayButton5.x = 1256;
		hallwayButton5.y = 1105;
		
		boolean hallwayComplete = false;
		
		CollisionItem hallwayLoadingZone = new CollisionItem(153, 64, Color.BLACK);
		hallwayLoadingZone.x = 1094;
		hallwayLoadingZone.y = 947;
		
		boolean controllingLaser = false;//418 848 y256
		CollisionItem laserControlPanel = new CollisionItem(430, 64, Color.BLACK);
		laserControlPanel.x = 418;
		laserControlPanel.y = 256;
		
		// Core Button Hitbox
		CollisionItem core = new CollisionItem(88,10, Color.RED);
		core.x = 980;
		core.y = 714;
		boolean coreButtonPressed = false;
		
		window.drawLoadingScreen("Downloading laser sprites...");
		CollisionItem laser = new CollisionItem(fileLoader.load("/res/laser/Static/0001.png"));
		laser.x = 527;
		laser.y = 518;
		//210 215
		laser.isometricItem = true;
		laser.isometricSprites[WorldItem.SW]= window.resizeImage(ImageIO.read(fileLoader.load("/res/laser/Static/0008.png")), 210, 215);
		laser.isometricSprites[WorldItem.S]= window.resizeImage(ImageIO.read(fileLoader.load("/res/laser/Static/0001.png")), 210, 215);
		laser.isometricSprites[WorldItem.SE]= window.resizeImage(ImageIO.read(fileLoader.load("/res/laser/Static/0002.png")), 210, 215);
		laser.isometricSprites[WorldItem.E]= window.resizeImage(ImageIO.read(fileLoader.load("/res/laser/Static/0003.png")), 210, 215);
		laser.isometricSprites[WorldItem.NE]= window.resizeImage(ImageIO.read(fileLoader.load("/res/laser/Static/0004.png")), 210, 215);
		laser.isometricSprites[WorldItem.N]= window.resizeImage(ImageIO.read(fileLoader.load("/res/laser/Static/0005.png")), 210, 215);
		laser.isometricSprites[WorldItem.NW]= window.resizeImage(ImageIO.read(fileLoader.load("/res/laser/Static/0006.png")), 210, 215);
		laser.isometricSprites[WorldItem.W]= window.resizeImage(ImageIO.read(fileLoader.load("/res/laser/Static/0007.png")), 210, 215);
	
		laser.pointInDirection(WorldItem.S);
		laser.refreshDimensions();
		
		// Start the game loop
		debug("Loading finished. Entering game loop...");
		
		boolean gameRunning = true;
		int currentLevel = 0;
		boolean freecam = false;
		
		nextFrameTime = System.nanoTime();
		frameLength = 16700000;
		
		while(gameRunning) {
			// Frame limiter
			while(System.nanoTime() < nextFrameTime) {}
			nextFrameTime = System.nanoTime() + frameLength;
			
			window.clear();
			
			// Movement Code
			float moveX = 0;
			float moveY = 0;
			
			if(window.keyListener.KEY_LEFT) moveX -= 5;
			if(window.keyListener.KEY_RIGHT) moveX += 5;
			if(window.keyListener.KEY_UP) moveY -= 5;
			if(window.keyListener.KEY_DOWN) moveY += 5; 
			
			//moveX = moveX * window.delta;
			//moveY = moveY * window.delta;
			
			if(window.keyListener.KEY_FREECAM) {
				Thread.sleep(200);
				debug("Toggling freecam...");
				freecam = !freecam;
			}
			
			if(freecam) {
				window.cameraX += moveX;
				window.cameraY += moveY;
			} else {
				if(!controllingLaser) {
					ArrayList<CollisionItem> tempList = new ArrayList<CollisionItem>();
					tempList.addAll(window.collisionItemLayer);
					tempList.addAll(window.hiddenCollisionItemLayer);
					player.moveAndCollide(moveX, moveY, tempList);
					window.centerCamera(player);
				}
			}
			
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
						player.y = 1450;
						
						// Load in next level
						Hallway hallway = new Hallway(window, linegen, fileLoader);
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
							if(window.drawMenuButton(501, 300, 50, 15, "", Color.WHITE, new Color(0, 0, 0), new Color(5, 5, 5))) {
								terminalSolved = true;
								break;
							}
							window.repaint();
							Thread.sleep(1000 / 15); 
						}
					}
				}
			} else if(currentLevel == 1) {
				if(hallwayButton0Pushed) window.drawWorldItem(hallwayButton0);
				if(hallwayButton1Pushed) window.drawWorldItem(hallwayButton1);
				if(hallwayButton2Pushed) window.drawWorldItem(hallwayButton2);
				if(hallwayButton3Pushed) window.drawWorldItem(hallwayButton3);
				if(hallwayButton4Pushed) window.drawWorldItem(hallwayButton4);
				if(hallwayButton5Pushed) window.drawWorldItem(hallwayButton5);
				
				if(!hallwayComplete) {
					if(player.collidingWith(hallwayButton0)) hallwayButton0Pushed = true;
					if(player.collidingWith(hallwayButton1)) {
						hallwayButton0Pushed = false;
						hallwayButton1Pushed = false;
						hallwayButton2Pushed = false;
						hallwayButton3Pushed = false;
						hallwayButton4Pushed = false;
						hallwayButton5Pushed = false;
					}
					if(player.collidingWith(hallwayButton2)) hallwayButton2Pushed = true;
					if(player.collidingWith(hallwayButton3)) {
						hallwayButton0Pushed = false;
						hallwayButton1Pushed = false;
						hallwayButton2Pushed = false;
						hallwayButton3Pushed = false;
						hallwayButton4Pushed = false;
						hallwayButton5Pushed = false;
					}
					if(player.collidingWith(hallwayButton4)) hallwayButton4Pushed = true;
					if(player.collidingWith(hallwayButton5)) {
						hallwayButton0Pushed = false;
						hallwayButton1Pushed = false;
						hallwayButton2Pushed = false;
						hallwayButton3Pushed = false;
						hallwayButton4Pushed = false;
						hallwayButton5Pushed = false;
					}
					
					
					if(hallwayButton0Pushed && hallwayButton2Pushed && hallwayButton4Pushed) hallwayComplete = true;
				} else {
					hallwayButton0Pushed = true;
					hallwayButton1Pushed = true;
					hallwayButton2Pushed = true;
					hallwayButton3Pushed = true;
					hallwayButton4Pushed = true;
					hallwayButton5Pushed = true;
					
					if(player.collidingWith(hallwayLoadingZone) && window.keyListener.KEY_ACTION) {
						// Next level
						window.drawLoadingScreen("");
						window.collisionItemLayer.clear(); // Remove all collision items
						window.backgroundLayer.clear();
						window.effectLayer.clear(); // TODO: Fade in/out
						window.collisionItemLayer.add(player); // Re-add player
						
						// Put player where they need to go
						player.x = 600;
						player.y = 1100;
						
						// Load in next level
						BigCircularRoom bigCircularRoom = new BigCircularRoom(window, linegen, fileLoader);
						bigCircularRoom.createAt(0, 0);
						window.renderBackground();
						window.collisionItemLayer.add(laser);
						currentLevel = 2;
					}
				}
			} else if(currentLevel == 2) {
				if(controllingLaser) {
					laser.lookTowards((int) moveX, (int) moveY);
					window.centerCamera(laser);
				}
				if(window.keyListener.KEY_ACTION && !controllingLaser && player.collidingWith(laserControlPanel)) controllingLaser = true;
				
				window.drawWorldItem(laserControlPanel);
				
			} else if(currentLevel == 4) {
				if(window.keyListener.KEY_ACTION) {
					if(!coreButtonPressed && player.collidingWith(core)) {
						// Core level
						coreButtonPressed = true;
					}
				}
			}
			
			// Pause menu
			if(window.keyListener.KEY_MENU) {
				while(true) {
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
			
			// Drawing Code
			window.drawLayers();
			window.repaint();
		}
		System.exit(0);
	}
}
