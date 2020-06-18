/*
 * ######################################################
 * #  ESCAPE THE ROBOTS AND OH GOSH THEY'RE COMING RUN  #
 * ######################################################
 * # COPYRIGHT 2020 BOIS WHO DON'T KNOW HOW TO COMP SCI #
 * #                                                    #
 * # LEAD DEVELOPER, ENGINE, UI, SFX       JOHNNY STENE #
 * # LEVEL SCRIPTING                    ETHAN STEVENSON #
 * #								   CALEB McCANDLESS #
 * #									 AIDEN SCHLEGEL #
 * # ART, LEVEL DESIGN                      ETHAN MEIER #
 * # MUSIC								  ISAAC MEETSMA #
 * ######################################################
 */

package com.boiswhodontknowhowtocompsci.escapegame;

import com.asuscomm.johnnystene.infinity.CollisionItem;
import com.asuscomm.johnnystene.infinity.Window;
import com.asuscomm.johnnystene.infinity.WorldItem;
import res.FileLoader;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Rectangle;

public class Main {
	private static final boolean DEBUG_BUILD = false;
	private static final int DEBUG_LEVEL = 0; // 0 - Don't include frame-by-frame debug information, 1 - Include all information
	
	private static final boolean ENABLE_TITLE_SCREEN_ANIMATION = true; // Makes loading take longer. Enable for release/demo builds.
	private static final boolean ENABLE_ANNOYING_ASS_MUSIC = true; // Makes me want to shoot myself. Enable for release/demo builds.

	private static void debug(String message) {
		if(DEBUG_BUILD) System.out.println(message);
	}

	private static void drawHackyThing(Window window, String message) throws InterruptedException {
		int hackyframes = 0;

		while(hackyframes < 125) {
			window.drawRectangle(0, 0, window.windowWidth, window.windowHeight, Color.BLACK);
			window.drawRectangle((window.windowWidth / 2) - 150, (window.windowHeight / 2) - 50, 300, 100, Color.DARK_GRAY);
			window.drawOtherRectangle((window.windowWidth / 2) - 155, (window.windowHeight / 2) - 55, 310, 110, Color.LIGHT_GRAY);
			window.drawTextCentered(window.windowWidth / 2, (window.windowHeight / 2), message, 25, Color.WHITE);
			window.drawRectangle((window.windowWidth / 2) - 125, (window.windowHeight / 2), hackyframes * 2, 25, Color.WHITE);

			window.repaint();
			hackyframes ++;
			Thread.sleep(1000 / 30);
		}
	}

	public static void playSound(Clip clip, AudioInputStream sound) throws IOException, LineUnavailableException {
		debug("Playing sound!");
		clip.close();
		clip.open(sound);
		clip.start();
	}
	
	public static void main(String[] args) throws LineUnavailableException {
		debug("Escape The Robots And Oh Gosh They're Coming Run");
		debug("DEBUG BUILD");

		debug("Initializing Window...");
		Window window = new Window(1136, 640, "Escape The Robots And Oh Gosh They're Coming Run"); // Create the game window

		try {
			debug("Initializing MapGenerator...");
			MapGenerator mapgen = new MapGenerator();

			debug("Initializing CollisionLineGenerator...");
			CollisionLineGenerator linegen = new CollisionLineGenerator();

			debug("Initializing FileLoader...");
			FileLoader fileLoader = new FileLoader();

			boolean goodToGo = true;

			// Download and play the music
			debug("Init sound system");
			AudioInputStream titleInputStream;
			AudioInputStream sfxBeep0;
			AudioInputStream sfxBeep1;
			AudioInputStream sfxBeep2;
			AudioInputStream sfxBeep3;
			AudioInputStream explode;
			Clip clip = AudioSystem.getClip();
			try {
				titleInputStream = AudioSystem.getAudioInputStream(fileLoader.load("/res/music/Nebulae_Wind.wav"));
				sfxBeep0 = AudioSystem.getAudioInputStream(fileLoader.load("/res/snd/beep0.wav"));
				sfxBeep1 = AudioSystem.getAudioInputStream(fileLoader.load("/res/snd/beep1.wav"));
				sfxBeep2 = AudioSystem.getAudioInputStream(fileLoader.load("/res/snd/beep2.wav"));
				sfxBeep3 = AudioSystem.getAudioInputStream(fileLoader.load("/res/snd/beep3.wav"));
				explode = AudioSystem.getAudioInputStream(fileLoader.load("/res/snd/explode0.wav"));
			} catch(Exception e) {
				goodToGo = false;
				titleInputStream = AudioSystem.getAudioInputStream(fileLoader.load("/res/snd/missingCodec.wav"));
				sfxBeep0 = AudioSystem.getAudioInputStream(fileLoader.load("/res/snd/missingCodec.wav"));
				sfxBeep1 = AudioSystem.getAudioInputStream(fileLoader.load("/res/snd/missingCodec.wav"));
				sfxBeep2 = AudioSystem.getAudioInputStream(fileLoader.load("/res/snd/missingCodec.wav"));
				sfxBeep3 = AudioSystem.getAudioInputStream(fileLoader.load("/res/snd/missingCodec.wav"));
				explode = AudioSystem.getAudioInputStream(fileLoader.load("/res/snd/missingCodec.wav"));
			}

			debug("Starting playback of title screen music...");
			if (ENABLE_ANNOYING_ASS_MUSIC)
				window.loopMusic(titleInputStream);
			//Menu Button sizes
			int menuButtonWidth = 100;
			int menuButtonHeight = 20;
			int pauseMenuButtonWidth = 250;
			int pauseMenuButtonHeight = 100;
			int menuButtonTextSize = 16;
			int menuButtonSpacing = 10;

			window.drawLoadingScreen("Rendering intro animation...");
			ArrayList<BufferedImage> titleVideo = new ArrayList<>();
			if (ENABLE_TITLE_SCREEN_ANIMATION) {
				debug("Loading title screen video...");
				for (int i = 1; i < 101; i++) {
					//      ^       ^
					// ffmpeg named the files starting at one so i gotta do this shit
					// it hurts

					titleVideo.add(window.resizeImage(ImageIO.read(fileLoader.load("/res/title/out" + i + ".png")), window.windowWidth, window.windowHeight));
				}
			}

			// Title screen loop
			boolean inTitleScreen = true;
			debug("Starting title screen...");
			int frame = 0;
			double frameLength = 83333333.33;
			double nextFrameTime = System.nanoTime() + frameLength;

			while (inTitleScreen) {
				if (ENABLE_TITLE_SCREEN_ANIMATION) {
					Raster newFB = titleVideo.get(frame).getData(new Rectangle(0, 0, window.windowWidth, window.windowHeight));
					window.frameBuffer.setData(newFB);
					if (System.nanoTime() >= nextFrameTime) {
						frame++;
						nextFrameTime = System.nanoTime() + frameLength;
					}
					if (frame == 100) frame = 0;
				} else {
					window.drawRectangle(0, 0, window.windowWidth, window.windowHeight, Color.BLACK); // Black background
				}

				// Title Text
				window.drawText(20, window.windowHeight - 15 - ((menuButtonHeight + menuButtonSpacing) * 6), "Escape The Robots And", 20, Color.WHITE);
				window.drawText(20, window.windowHeight - 15 - ((menuButtonHeight + menuButtonSpacing) * 5), "Oh Gosh They're Coming Run", 20, Color.WHITE);

				// NEW GAME button (duh)
				if (window.drawClearMenuButton(20, window.windowHeight - 20 - ((menuButtonHeight + menuButtonSpacing) * 4), menuButtonWidth, menuButtonHeight, menuButtonTextSize, "NEW GAME", Color.WHITE, Color.LIGHT_GRAY))
					inTitleScreen = false;

				// QUIT button (duh)
				if (window.drawClearMenuButton(20, window.windowHeight - 20 - ((menuButtonHeight + menuButtonSpacing) * 3), menuButtonWidth, menuButtonHeight, menuButtonTextSize, "QUIT GAME", Color.WHITE, Color.LIGHT_GRAY))
					System.exit(0);

				if(!goodToGo) {
					window.drawClearMenuButton(20, window.windowHeight - 20 - ((menuButtonHeight + menuButtonSpacing) * 2), menuButtonWidth, menuButtonHeight, menuButtonTextSize, "YOUR COMPUTER DOES NOT MEET THE MINIMUM REQUIREMENTS", Color.WHITE, Color.LIGHT_GRAY);
				}

				window.repaint();
				Thread.sleep(1000 / 30); // FPS cap needed in menus too
			}

			// Create the player object and preload the player sprite
			debug("Loading player...");
			WorldItem playerSprite = new CollisionItem(window.resizeImage(ImageIO.read(fileLoader.load("/res/player/static/BStatic.png")), 118, 118));
			playerSprite.x = 360;
			playerSprite.y = 1800;
			playerSprite.isometricItem = true;
			window.effectLayer.add(playerSprite);

			playerSprite.isometricSprites[WorldItem.SW] = window.resizeImage(ImageIO.read(fileLoader.load("/res/player/static/FLStatic.png")), 118, 118);
			playerSprite.isometricSprites[WorldItem.S] = window.resizeImage(ImageIO.read(fileLoader.load("/res/player/static/FStatic.png")), 118, 118);
			playerSprite.isometricSprites[WorldItem.SE] = window.resizeImage(ImageIO.read(fileLoader.load("/res/player/static/FRStatic.png")), 118, 118);
			playerSprite.isometricSprites[WorldItem.E] = window.resizeImage(ImageIO.read(fileLoader.load("/res/player/static/RStatic.png")), 118, 118);
			playerSprite.isometricSprites[WorldItem.NE] = window.resizeImage(ImageIO.read(fileLoader.load("/res/player/static/BRStatic.png")), 118, 118);
			playerSprite.isometricSprites[WorldItem.N] = window.resizeImage(ImageIO.read(fileLoader.load("/res/player/static/BStatic.png")), 118, 118);
			playerSprite.isometricSprites[WorldItem.NW] = window.resizeImage(ImageIO.read(fileLoader.load("/res/player/static/BLStatic.png")), 118, 118);
			playerSprite.isometricSprites[WorldItem.W] = window.resizeImage(ImageIO.read(fileLoader.load("/res/player/static/LStatic.png")), 118, 118);

			CollisionItem player = new CollisionItem(30, 20, Color.BLACK);
			player.x = 401;
			player.y = 1875;

			// Load in first level
			debug("Loading first level contents...");
			Outside outside = new Outside(window, linegen, fileLoader);
			outside.create();

			// Terminal Hitbox
			CollisionItem terminal = new CollisionItem(64, 64, Color.RED);
			terminal.x = 560;
			terminal.y = 1645;
			boolean terminalSolved = false;

			// Door Loading Zone
			CollisionItem outsideDoor = new CollisionItem(100, 50, Color.RED);
			outsideDoor.x = 400;
			outsideDoor.y = 1600;

			window.renderBackground();

			WorldItem terminalSprite;
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
			CollisionItem laserControlPanel = new CollisionItem(430, 64, Color.RED);
			laserControlPanel.x = 825;
			laserControlPanel.y = 500;

			//maze button
			boolean mazeButtonPushed = false;
			CollisionItem mazeButton = new CollisionItem(32, 17, Color.GREEN);
			hallwayButton5.x = 1256;
			hallwayButton5.y = 1105;

			// Core Button Hitbox
			CollisionItem core = new CollisionItem(200, 32, Color.BLACK);
			core.x = 1036;
			core.y = 745;
			boolean coreButtonPressed = false;

			CollisionItem laserRoomVent = new CollisionItem(60, 48, Color.BLACK);
			laserRoomVent.x = 532;
			laserRoomVent.y = 592;

			CollisionItem ventCollision = new CollisionItem(128, 128, Color.BLACK);
			ventCollision.x = 1152;
			ventCollision.y = 768;

			window.drawLoadingScreen("Downloading laser sprites...");
			CollisionItem laser = new CollisionItem(fileLoader.load("/res/laser/Static/0001.png"));
			laser.x = 935;
			laser.y = 629;
			//210 215
			laser.isometricItem = true;//26
			laser.isometricSprites[WorldItem.SW] = window.resizeImage(ImageIO.read(fileLoader.load("/res/laser/Static/0008.png")), 210, 215);
			laser.isometricSprites[WorldItem.S] = window.resizeImage(ImageIO.read(fileLoader.load("/res/laser/Static/0001.png")), 210, 215);
			laser.isometricSprites[WorldItem.SE] = window.resizeImage(ImageIO.read(fileLoader.load("/res/laser/Static/0002.png")), 210, 215);
			laser.isometricSprites[WorldItem.E] = window.resizeImage(ImageIO.read(fileLoader.load("/res/laser/Static/0003.png")), 210, 215);
			laser.isometricSprites[WorldItem.NE] = window.resizeImage(ImageIO.read(fileLoader.load("/res/laser/Static/0004.png")), 210, 215);
			laser.isometricSprites[WorldItem.N] = window.resizeImage(ImageIO.read(fileLoader.load("/res/laser/Static/0005.png")), 210, 215);
			laser.isometricSprites[WorldItem.NW] = window.resizeImage(ImageIO.read(fileLoader.load("/res/laser/Rotate/Counter-Clockwise/0026.png")), 210, 215);
			laser.isometricSprites[WorldItem.W] = window.resizeImage(ImageIO.read(fileLoader.load("/res/laser/Static/0007.png")), 210, 215);

			laser.pointInDirection(WorldItem.S);
			laser.refreshDimensions();

			// Start the game loop
			debug("Loading finished. Entering game loop...");

			boolean gameRunning = true;
			int currentLevel = 0;
			boolean freecam = false;

			boolean playSoundOnPuzzleFinish = true;
			boolean laserFinished = false;

			boolean drawHint = false;
			String hintText = "Test";

			nextFrameTime = System.nanoTime();
			frameLength = 16700000;

			while (gameRunning) {
				// Frame limiter
				do {
				} while (System.nanoTime() < nextFrameTime); // I get compiler warnings with just a while() loop
				nextFrameTime = System.nanoTime() + frameLength;

				window.clear();

				// Movement Code
				float moveX = 0;
				float moveY = 0;

				if (window.keyListener.KEY_LEFT) moveX -= 5;
				if (window.keyListener.KEY_RIGHT) moveX += 5;
				if (window.keyListener.KEY_UP) moveY -= 5;
				if (window.keyListener.KEY_DOWN) moveY += 5;

				playerSprite.lookTowards((int) moveX, (int) moveY);

				//moveX = moveX * window.delta;
				//moveY = moveY * window.delta;

				if (window.keyListener.KEY_FREECAM) {
					Thread.sleep(200);
					debug("Toggling freecam...");
					freecam = !freecam;
				}

				if (freecam) {
					window.cameraX += moveX;
					window.cameraY += moveY;
				} else {
					if (!controllingLaser) {
						ArrayList<CollisionItem> tempList = new ArrayList<>();
						tempList.addAll(window.collisionItemLayer);
						tempList.addAll(window.hiddenCollisionItemLayer);
						player.moveAndCollide(moveX, moveY, tempList);
						window.centerCamera(playerSprite);
					}
				}

				// Level-specific code
				if (currentLevel == 0) {
					if(!terminalSolved && player.collidingWith(terminal)) {
						drawHint = true;
						hintText = "(E) Hack";
					} else if(terminalSolved && player.collidingWith(outsideDoor)) {
						drawHint = true;
						hintText = "(E) Enter";
					} else {
						drawHint = false;
					}
					if (window.keyListener.KEY_ACTION) {
						if (terminalSolved && player.collidingWith(outsideDoor)) {
							window.drawLoadingScreen("");
							window.collisionItemLayer.clear(); // Remove all collision items
							window.hiddenCollisionItemLayer.clear();
							window.backgroundLayer.clear();
							window.effectLayer.clear(); // TODO: Fade in/out
							window.effectLayer.add(playerSprite);
							//window.collisionItemLayer.add(player); // Re-add player

							// Put player where they need to go
							player.x = 415;
							player.y = 1450;

							// Load in next level
							Hallway hallway = new Hallway(window, linegen, fileLoader);
							hallway.create();
							window.renderBackground();
							currentLevel = 1;
						}

						if (!terminalSolved && player.collidingWith(terminal)) {
							playSound(clip, sfxBeep0);
							drawHackyThing(window, "Hacking Door...");
							playSound(clip, sfxBeep1);
							terminalSolved = true;
						}
					}
				} else if (currentLevel == 1) {
					if(hallwayComplete && player.collidingWith(hallwayLoadingZone)) {
						drawHint = true;
						hintText = "(E) Enter Elevator";
					} else {
						drawHint = false;
					}
					if (hallwayButton0Pushed) window.drawWorldItem(hallwayButton0);
					if (hallwayButton1Pushed) window.drawWorldItem(hallwayButton1);
					if (hallwayButton2Pushed) window.drawWorldItem(hallwayButton2);
					if (hallwayButton3Pushed) window.drawWorldItem(hallwayButton3);
					if (hallwayButton4Pushed) window.drawWorldItem(hallwayButton4);
					if (hallwayButton5Pushed) window.drawWorldItem(hallwayButton5);

					if (!hallwayComplete) {
						if (player.collidingWith(hallwayButton0)) {
							hallwayButton0Pushed = true;
						}
						if (player.collidingWith(hallwayButton1)) {
							hallwayButton0Pushed = false;
							hallwayButton1Pushed = false;
							hallwayButton2Pushed = false;
							hallwayButton3Pushed = false;
							hallwayButton4Pushed = false;
							hallwayButton5Pushed = false;

						}
						if (player.collidingWith(hallwayButton2)) hallwayButton2Pushed = true;
						if (player.collidingWith(hallwayButton3)) {
							hallwayButton0Pushed = false;
							hallwayButton1Pushed = false;
							hallwayButton2Pushed = false;
							hallwayButton3Pushed = false;
							hallwayButton4Pushed = false;
							hallwayButton5Pushed = false;
						}
						if (player.collidingWith(hallwayButton4)) hallwayButton4Pushed = true;
						if (player.collidingWith(hallwayButton5)) {
							hallwayButton0Pushed = false;
							hallwayButton1Pushed = false;
							hallwayButton2Pushed = false;
							hallwayButton3Pushed = false;
							hallwayButton4Pushed = false;
							hallwayButton5Pushed = false;
						}


						if (hallwayButton0Pushed && hallwayButton2Pushed && hallwayButton4Pushed)
							hallwayComplete = true;
					} else {
						if (playSoundOnPuzzleFinish) { //935, 629
							playSound(clip, sfxBeep1);
							playSoundOnPuzzleFinish = false;
						}
						hallwayButton0Pushed = true;
						hallwayButton1Pushed = true;
						hallwayButton2Pushed = true;
						hallwayButton3Pushed = true;
						hallwayButton4Pushed = true;
						hallwayButton5Pushed = true;

						if (player.collidingWith(hallwayLoadingZone) && window.keyListener.KEY_ACTION) {
							// Next level
							window.drawLoadingScreen("");
							window.collisionItemLayer.clear(); // Remove all collision items
							window.hiddenCollisionItemLayer.clear();
							window.backgroundLayer.clear();
							window.effectLayer.clear(); // TODO: Fade in/out
							window.effectLayer.add(playerSprite);
							//window.collisionItemLayer.add(player); // Re-add player

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
				} else if (currentLevel == 2) {
					if(!laserFinished && player.collidingWith(laserControlPanel)) {
						drawHint = true;
						hintText = "(E) Hack";
					} else if(laserFinished && player.collidingWith(laserRoomVent)) {
						drawHint = true;
						hintText = "(E) Enter vent";
					} else {
						drawHint = false;
					}
					if (window.keyListener.KEY_ACTION && !laserFinished && player.collidingWith(laserControlPanel)) {
						drawHackyThing(window, "Moving laser...");
						laserFinished = true;
						laser.lookTowards(-1, -1);
						window.backgroundLayer.clear();
						WorldItem newBG = new WorldItem(fileLoader.load("/res/maps/Upstairs-Melted.png"));
						window.backgroundLayer.add(newBG);
						window.renderBackground();
					}

					if(laserFinished && window.keyListener.KEY_ACTION && player.collidingWith(laserRoomVent)) {
						window.drawLoadingScreen("");
						window.collisionItemLayer.clear(); // Remove all collision items
						window.hiddenCollisionItemLayer.clear();
						window.backgroundLayer.clear();
						window.effectLayer.clear(); // TODO: Fade in/out
						window.effectLayer.add(playerSprite);
						window.renderBackground();

						// Put player where they need to go
						player.x = 4000;
						player.y = 1152;

						WorldItem vent = new WorldItem(fileLoader.load("/res/maps/vent.png"));
						vent.x = 1152;
						vent.y = 768;
						window.backgroundLayer.add(vent);
						window.renderBackground();

						// Load in next level
						mapgen.loadMap(fileLoader.load("/res/maps/maze.map"), window);
						currentLevel = 3;
					}


				} else if (currentLevel == 3) {
					if(player.collidingWith(ventCollision)) {
						hintText = "(E) Enter vent";
						drawHint = true;

						if(window.keyListener.KEY_ACTION) {
							//load next level
							window.drawLoadingScreen("");
							window.collisionItemLayer.clear(); // Remove all collision items
							window.hiddenCollisionItemLayer.clear();
							window.backgroundLayer.clear();
							window.effectLayer.clear(); // TODO: Fade in/out
							window.effectLayer.add(playerSprite);

							WorldItem background = new WorldItem(fileLoader.load("/res/maps/Core.png"));

							window.backgroundLayer.add(background);
							window.renderBackground();
							player.x = 1046;
							player.y = 818;

							Core coreLevel = new Core(window, linegen, fileLoader);
							coreLevel.create();
							window.renderBackground();
							currentLevel = 4;
						}
					}

				} else if (currentLevel == 4) {
					if (!coreButtonPressed && player.collidingWith(core)) {
						hintText = "(E) Push Button";
						drawHint = true;
					}
					if (window.keyListener.KEY_ACTION) {
						if (!coreButtonPressed && player.collidingWith(core)) {
							// Core level
							coreButtonPressed = true;
						}
					}
				}
				
				if(coreButtonPressed) {
					window.drawRectangle(0, 0, window.windowWidth, window.windowHeight, new Color(0,0,0)); // Black background
					window.repaint();
					Thread.sleep(2000);
					window.drawText((window.windowWidth / 2) - 50, window.windowHeight - 300, "Congrats.", 20, Color.WHITE);
					window.repaint();
					Thread.sleep(3000);
					window.drawRectangle(0, 0, window.windowWidth, window.windowHeight, new Color(0,0,0)); // Black background
					window.repaint();
					Thread.sleep(1000);
					window.drawText((window.windowWidth / 2) - 100, window.windowHeight - 300, "You saved the world.", 20, Color.WHITE);
					window.repaint();
					Thread.sleep(3000);
					window.drawRectangle(0, 0, window.windowWidth, window.windowHeight, new Color(0,0,0)); // Black background
					window.repaint();
					Thread.sleep(1000);
					window.drawText((window.windowWidth / 2) - 150, window.windowHeight - 300, "However, it was at a great cost.", 20, Color.WHITE);
					window.repaint();
					Thread.sleep(3000);
					window.drawRectangle(0, 0, window.windowWidth, window.windowHeight, new Color(0,0,0)); // Black background
					window.repaint();
					Thread.sleep(1000);
					window.drawText((window.windowWidth / 2) - 150, window.windowHeight - 300, "Your sacrifice wont be forgotten.", 20, Color.WHITE);
					window.repaint();
					Thread.sleep(3000);
					window.drawRectangle(0, 0, window.windowWidth, window.windowHeight, new Color(0,0,0)); // Black background
					window.repaint();
					Thread.sleep(100);

					playSound(clip, explode);
					int width = 1136;
					int height = 640;
					for( int i = 1; i < 70; i++) {
						window.drawText(40, i * 10, "CONSOLE ERROR", 20, Color.RED);
						window.repaint();

						if(i % 5 == 0) {
							width -= 7;
							height -= 5;
						} else if(i % 2 == 0) {
							width += 10;
							height += 4;
						} else if(i % 3 == 0) {
							width = 1136;
							height = 640;
						}

						window.setSize(width, height);
						Thread.sleep(40);
					}

					System.exit(0);
				}

				// Pause menu
				if (window.keyListener.KEY_MENU) {
					while (true) {
						window.drawRectangle(0, 0, window.windowWidth, window.windowHeight, new Color(0, 0, 0)); // Black background

						if (window.drawMenuButton((window.windowWidth / 2) - (pauseMenuButtonWidth / 2), window.windowHeight - 400, pauseMenuButtonWidth, pauseMenuButtonHeight, "RESUME", Color.WHITE, new Color(255, 66, 28), new Color(255, 91, 59))) {
							break;
						}

						if (window.drawMenuButton((window.windowWidth / 2) - (pauseMenuButtonWidth / 2), window.windowHeight - 280, pauseMenuButtonWidth, pauseMenuButtonHeight, "QUIT GAME", Color.WHITE, new Color(255, 66, 28), new Color(255, 91, 59))) {
							gameRunning = false;
							System.exit(0);
						}

						window.repaint();
						Thread.sleep(1000 / 60);
					}
				}

				// Print debug information
				if (DEBUG_BUILD && DEBUG_LEVEL == 0)
					System.out.println("FPS: " + (int) window.FPS + ", Delta: " + window.delta +
							", X: " + (int) player.x + ", Y: " + (int) player.y);

				// Drawing Code
				playerSprite.x = player.x - 41;
				playerSprite.y = player.y - 71;

				if(drawHint)
					window.drawTextCentered((int) (player.x + 15) - window.cameraX, (int) playerSprite.y - window.cameraY, hintText, 16, Color.WHITE);

				window.drawLayers();
				window.repaint();
			}
			System.exit(0);
		} catch(NullPointerException e) {
			e.printStackTrace();
			while(true) {
				window.drawLoadingScreen("ERROR! Game assets missing.");
			}
		} catch(IOException e) {
			e.printStackTrace();
			while(true) {
				window.drawLoadingScreen("ERROR! Game assets missing.");
			}
		} catch(InterruptedException e) {
			e.printStackTrace();
			while(true) {
				window.drawLoadingScreen("ERROR! Interrupted.");
			}
		} catch(LineUnavailableException e) {
			e.printStackTrace();
			while(true) {
				window.drawLoadingScreen("ERROR! Line unavailable.");
			}
		} catch(UnsupportedAudioFileException e) {
			e.printStackTrace();
			while(true) {
				window.drawLoadingScreen("ERROR! Missing audio codecs");
			}
		} catch(Exception e) {
			e.printStackTrace();
			while(true) {
				window.drawLoadingScreen("ERROR! Unknown error.");
			}
		}
	}
}

	
