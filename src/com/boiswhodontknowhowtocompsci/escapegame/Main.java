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

import com.asuscomm.johnnystene.escape.CollisionItem;
import com.asuscomm.johnnystene.escape.Window;
import com.asuscomm.johnnystene.escape.WorldItem;
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
	private static final boolean RELEASE_BUILD = false;

	private static void debug(String message) {
		if(!RELEASE_BUILD) System.out.println(message);
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
	
	public static void main(String[] args) throws LineUnavailableException, InterruptedException {
		System.out.println("Escape The Robots And Oh Gosh They're Coming Run");
		System.out.println("Version 1.1b");
		debug("DEBUG BUILD");

		debug("Initializing Window...");
		Window window = new Window(1136, 640, "Escape The Robots And Oh Gosh They're Coming Run"); // Create the game window

		try {
			debug("Initializing MapGenerator...");
			MapGenerator mapgen = new MapGenerator(window, false);

			debug("Initializing CollisionLineGenerator...");
			CollisionLineGenerator linegen = new CollisionLineGenerator();

			debug("Initializing FileLoader...");
			FileLoader fileLoader = new FileLoader();

			boolean goodToGo = true;

			// Download and play the music
			debug("Init sound system");
			Clip clip = AudioSystem.getClip();

			debug("Load sound files");
			AudioInputStream titleMusic = AudioSystem.getAudioInputStream(fileLoader.loadBuffered("/res/music/Nebulae_Wind.wav"));
			AudioInputStream sfxBeep0 = AudioSystem.getAudioInputStream(fileLoader.loadBuffered("/res/snd/beep0.wav"));
			AudioInputStream sfxBeep1 = AudioSystem.getAudioInputStream(fileLoader.loadBuffered("/res/snd/beep1.wav"));
			AudioInputStream sfxBeep2 = AudioSystem.getAudioInputStream(fileLoader.loadBuffered("/res/snd/beep2.wav"));
			AudioInputStream sfxBeep3 = AudioSystem.getAudioInputStream(fileLoader.loadBuffered("/res/snd/beep3.wav"));
			AudioInputStream explode = AudioSystem.getAudioInputStream(fileLoader.loadBuffered("/res/snd/explode0.wav"));

			//Menu Button sizes
			int menuButtonWidth = 100;
			int menuButtonHeight = 20;
			int pauseMenuButtonWidth = 250;
			int pauseMenuButtonHeight = 100;
			int menuButtonTextSize = 16;
			int menuButtonSpacing = 10;

			window.drawLoadingScreen("Rendering intro animation...");
			ArrayList<BufferedImage> titleVideo = new ArrayList<>();
			if (RELEASE_BUILD) {
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

			if(RELEASE_BUILD)
				window.loopMusic(titleMusic);

			while (inTitleScreen) {
				if (RELEASE_BUILD) {
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
			debug("Loading levels...");
			Outside outside = new Outside(window, mapgen, fileLoader);
			outside.create();

			Hallway hallway = new Hallway(window, mapgen, fileLoader);
			BigCircularRoom upstairs = new BigCircularRoom(window, linegen, fileLoader);
			// TODO: Add maze here
			Core core = new Core(window, linegen, fileLoader);

			WorldItem terminalSprite;
			terminalSprite = new WorldItem(fileLoader.load("/res/ui/terminal.png"));
			terminalSprite.sprite = window.resizeImage(terminalSprite.sprite, 1136, 640);

			boolean controllingLaser = false;//418 848 y256
			CollisionItem laserControlPanel = new CollisionItem(430, 64, Color.RED);
			laserControlPanel.x = 825;
			laserControlPanel.y = 500;

			//maze button
			boolean mazeButtonPushed = false;
			CollisionItem mazeButton = new CollisionItem(32, 17, Color.GREEN);

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
					// First level, outside the building
					// TODO: Make the terminal a pinpad the player needs to unscrew and rewire.

					if(outside.terminalSolved) {
						if(player.collidingWith(outside.door)) {
							drawHint = true;
							hintText = "(E) Enter";
							if(window.keyListener.KEY_ACTION) {
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
								hallway.create();
								window.renderBackground();
								currentLevel = 1;
							}
						} else {
							drawHint = false;
						}
					} else {
						if(player.collidingWith(outside.terminal)) {
							drawHint = true;
							hintText = "(E) Hack";

							if(window.keyListener.KEY_ACTION) {
								playSound(clip, sfxBeep0);
								drawHackyThing(window, "Hacking...");
								playSound(clip, sfxBeep1);
								outside.terminalSolved = true;
							}
						} else {
							drawHint = false;
						}
					}
				} else if (currentLevel == 1) {
					if(hallway.complete && player.collidingWith(hallway.loadingZone)) {
						drawHint = true;
						hintText = "(E) Enter Elevator";
					} else {
						drawHint = false;
					}
					if (hallway.button0Pushed) window.drawWorldItem(hallway.button0);
					if (hallway.button1Pushed) window.drawWorldItem(hallway.button1);
					if (hallway.button2Pushed) window.drawWorldItem(hallway.button2);
					if (hallway.button3Pushed) window.drawWorldItem(hallway.button3);
					if (hallway.button4Pushed) window.drawWorldItem(hallway.button4);
					if (hallway.button5Pushed) window.drawWorldItem(hallway.button5);

					if (!hallway.complete) {
						if (player.collidingWith(hallway.button0)) hallway.button0Pushed = true;
						if (player.collidingWith(hallway.button1)) hallway.setAll(false);
						if (player.collidingWith(hallway.button2)) hallway.button2Pushed = true;
						if (player.collidingWith(hallway.button3)) hallway.setAll(false);
						if (player.collidingWith(hallway.button4)) hallway.button4Pushed = true;
						if (player.collidingWith(hallway.button5)) hallway.setAll(false);

						if (hallway.button0Pushed && hallway.button2Pushed && hallway.button4Pushed)
							hallway.complete = true;
					} else {
						if (playSoundOnPuzzleFinish) { //935, 629
							playSound(clip, sfxBeep1);
							playSoundOnPuzzleFinish = false;
						}
						hallway.setAll(true);

						if (player.collidingWith(hallway.loadingZone) && window.keyListener.KEY_ACTION) {
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
						mapgen.loadMapOld(fileLoader.load("/res/maps/maze.map"), window);
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
					if (player.collidingWith(core.buttonHitbox)) {
						hintText = "(E) Push Button";
						drawHint = true;
						if (window.keyListener.KEY_ACTION) {
							window.fill(Color.BLACK);
							window.repaintAndWait(2000);
							window.drawText((window.windowWidth / 2) - 50, window.windowHeight - 300, "Congratulations.", 20, Color.WHITE);
							window.repaintAndWait(3000);
							window.fill(Color.BLACK);
							window.repaintAndWait(1000);
							window.drawText((window.windowWidth / 2) - 100, window.windowHeight - 300, "You saved the world.", 20, Color.WHITE);
							window.repaintAndWait(3000);
							window.fill(Color.BLACK);
							window.repaintAndWait(1000);
							window.drawText((window.windowWidth / 2) - 150, window.windowHeight - 300, "However, it was at a great cost.", 20, Color.WHITE);
							window.repaintAndWait(3000);
							window.fill(Color.BLACK);
							window.repaintAndWait(1000);
							window.drawText((window.windowWidth / 2) - 150, window.windowHeight - 300, "Your sacrifice won't be forgotten.", 20, Color.WHITE);
							window.repaintAndWait(3000);
							window.fill(Color.BLACK);
							window.repaintAndWait(100);

							playSound(clip, explode);
							int width = 1136;
							int height = 640;
							for( int i = 1; i < 70; i++) {
								window.drawText(40, i * 10, "CONSOLE ERROR", 20, Color.RED);
								window.repaint();

								// Makes the window shake a bunch
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
					}
				}

				// Pause menu
				if (window.keyListener.KEY_MENU) {
					while (true) {
						window.drawRectangle(0, 0, window.windowWidth, window.windowHeight, new Color(0, 0, 0)); // Black background

						if (window.drawMenuButton((window.windowWidth / 2) - (pauseMenuButtonWidth / 2), window.windowHeight - 400, pauseMenuButtonWidth, pauseMenuButtonHeight, "RESUME", Color.WHITE, new Color(255, 66, 28), new Color(255, 91, 59)))
							break;

						if (window.drawMenuButton((window.windowWidth / 2) - (pauseMenuButtonWidth / 2), window.windowHeight - 280, pauseMenuButtonWidth, pauseMenuButtonHeight, "QUIT GAME", Color.WHITE, new Color(255, 66, 28), new Color(255, 91, 59))) {
							gameRunning = false;
							break;
						}

						window.repaintAndWait(1000 / 60);
					}
				}

				// Print debug information
				if (!RELEASE_BUILD)
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
		} catch(Exception e) {
			e.printStackTrace();
			while(true) {
				window.drawLoadingScreen("Game crashed!");
				window.repaintAndWait(1000 / 60);
			}
		}
	}
}

	
