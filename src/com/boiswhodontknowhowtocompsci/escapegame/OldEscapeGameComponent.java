/*
 * Escape The Robots And Oh Gosh They're Coming Run
 * Copyright (c) 2020 Johnny Stene, Ethan Meier, Ethan Stevenson, Isaac Meetsma, Aiden Schlegel, Caleb McCandless
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either Version 3 of the license, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *
 * You may contact Johnny Stene at <jhonnystene@gmail.com>.
 */

package com.boiswhodontknowhowtocompsci.escapegame;

import net.ddns.johnnystene.infinitytoolkit.engine.*;
import net.ddns.johnnystene.infinitytoolkit.engine.Window;

import java.awt.*;
import java.util.Scanner;

public class OldEscapeGameComponent {
	public static final boolean DEBUG_BUILD = true;

	public static void main(String[] args) {
		// Setup window
		Window window = new Window("Escape The Robots And Oh Gosh They're Coming Run", 800, 600);
		Window debugWindow;

		if(DEBUG_BUILD) debugWindow = new Window("Debug Window", 200, 100);
		
		// Setup player
		WorldItem player = new WorldItem("/res/player/static/S.png");
		player.loadSprite("/res/player/static/N.png", WorldItem.N);
		player.loadSprite("/res/player/static/NE.png", WorldItem.NE);
		player.loadSprite("/res/player/static/E.png", WorldItem.E);
		player.loadSprite("/res/player/static/SE.png", WorldItem.SE);
		player.loadSprite("/res/player/static/S.png", WorldItem.S);
		player.loadSprite("/res/player/static/SW.png", WorldItem.SW);
		player.loadSprite("/res/player/static/W.png", WorldItem.W);
		player.loadSprite("/res/player/static/NW.png", WorldItem.NW);
		player.isometricItem = true;
		player.resize(118, 118);
		player.x = 360;
		player.y = 1800;
		player.hitboxX = 40;
		player.hitboxWidth = 30;
		player.hitboxY = 80;
		player.hitboxHeight = 20;
		
		// Setup all levels
		World outsideMap = new World("/res/maps/Outside-Final.png");
		outsideMap.loadMapFile("/res/maps/collision/outside.map");
		outsideMap.items.add(player);
		boolean outsideMapKeypadSolved = false;

		World hallwayMap = new World("/res/maps/Hallway-B11-Programmer-Art.png");
		hallwayMap.loadMapFile("/res/maps/collision/hallway.map");
		hallwayMap.items.add(player);
		
		World upstairsMap = new World("/res/maps/Upstairs.png");
		upstairsMap.loadMapFile("/res/maps/collision/upstairs.map");
		upstairsMap.items.add(player);
		
		World coreMap = new World("/res/maps/Core.png");
		coreMap.loadMapFile("/res/maps/collision/core.map");
		coreMap.items.add(player);
		
		World currentMap = outsideMap;
		int currentMapId = 0;

		boolean gameRunning = true;

		try {
			while (gameRunning) {
				if (window.keyboard.KEY_ESCAPE) gameRunning = false;

				float moveX = 0;
				float moveY = 0;

				if (window.keyboard.KEY_A) moveX -= 10;
				if (window.keyboard.KEY_D) moveX += 10;
				if (window.keyboard.KEY_W) moveY -= 10;
				if (window.keyboard.KEY_S) moveY += 10;

				player.moveAndCollide(moveX, moveY, currentMap.items);
				// Check if we should pick up an item
				for (InventoryItem item : currentMap.pickups) {
					if (player.inArea(item.x, item.y, item.width, item.height) && item.inWorld) {
						// Pickup
						System.out.println("Pickup: " + item.name);
						item.inWorld = false;
					}
				}

				/*
				 * MAP IDS LIST
				 * 0 - Outside building (Goes to Main Hallway)
				 * 1 - Main Hallway (Goes to Upstairs or Keypad Hallway)
				 * 2 - Upstairs (Goes to Vents)
				 * 3 - Secondary (Keypad) Hallway (Goes to Core)
				 * 4 - Unused
				 * 5 - Core
				 */

				if (currentMapId == 0) { // Outside
					/*
					 * Map concept:
					 * Make the password the year the game takes place,
					 * and as a hint have the needed buttons look worn down.
					 */
					if (outsideMapKeypadSolved) {
						if (player.inArea(400, 1600, 100, 64) && window.keyboard.KEY_E) {
							for (int i = 0; i < 255; i++) {
								window.UIDrawFilledRect(0, 0, window.width, window.height, new Color(0, 0, 0, i));
								window.repaint();
								try {
									Thread.sleep(4);
								} catch (Exception ignored) {
								}
							}
							player.x = 415;
							player.y = 1450;
							currentMap = hallwayMap;
							currentMapId = 1;
							window.centerCamera(player);
							for (int i = 255; i > 0; i--) {
								//currentMap.renderTo(window.frameBuffer, window.camera.x, window.camera.y, window.width, window.height, DEBUG_BUILD);
								window.UIDrawFilledRect(0, 0, window.width, window.height, new Color(0, 0, 0, i));
								window.repaint();
								try {
									Thread.sleep(2);
								} catch (Exception ignored) {
								}
							}
						}
					} else {
						if (player.inArea(560, 1640, 64, 64) && window.keyboard.KEY_E) {
							// Temporary art for puzzle
							String userEntered = "";
							while (!outsideMapKeypadSolved) {
								window.UIDrawFilledRect(0, 0, window.width, window.height, Color.BLACK);

								// Keypad placement
								/*
								if (window.UIDrawButton((window.width / 2) - 100, (window.height / 2) - 100, 50, 50, 20, Color.GRAY, Color.LIGHT_GRAY, Color.BLACK, "1"))
									userEntered += "1";
								if (window.UIDrawButton((window.width / 2) - 25, (window.height / 2) - 100, 50, 50, 20, Color.GRAY, Color.LIGHT_GRAY, Color.BLACK, "2"))
									userEntered += "2";
								if (window.UIDrawButton((window.width / 2) + 50, (window.height / 2) - 100, 50, 50, 20, Color.GRAY, Color.LIGHT_GRAY, Color.BLACK, "3"))
									userEntered += "3";

								if (window.UIDrawButton((window.width / 2) - 100, (window.height / 2) - 25, 50, 50, 20, Color.GRAY, Color.LIGHT_GRAY, Color.BLACK, "4"))
									userEntered += "4";
								if (window.UIDrawButton((window.width / 2) - 25, (window.height / 2) - 25, 50, 50, 20, Color.GRAY, Color.LIGHT_GRAY, Color.BLACK, "5"))
									userEntered += "5";
								if (window.UIDrawButton((window.width / 2) + 50, (window.height / 2) - 25, 50, 50, 20, Color.GRAY, Color.LIGHT_GRAY, Color.BLACK, "6"))
									userEntered += "6";

								if (window.UIDrawButton((window.width / 2) - 100, (window.height / 2) + 50, 50, 50, 20, Color.GRAY, Color.LIGHT_GRAY, Color.BLACK, "7"))
									userEntered += "7";
								if (window.UIDrawButton((window.width / 2) - 25, (window.height / 2) + 50, 50, 50, 20, Color.GRAY, Color.LIGHT_GRAY, Color.BLACK, "8"))
									userEntered += "8";
								if (window.UIDrawButton((window.width / 2) + 50, (window.height / 2) + 50, 50, 50, 20, Color.GRAY, Color.LIGHT_GRAY, Color.BLACK, "9"))
									userEntered += "9";

								if (window.UIDrawButton((window.width / 2) - 25, (window.height / 2) + 125, 50, 50, 20, Color.GRAY, Color.LIGHT_GRAY, Color.BLACK, "0"))
									userEntered += "0";

								if (window.UIDrawButton((window.width / 2) + 50, (window.height / 2) + 125, 50, 50, 20, Color.GRAY, Color.LIGHT_GRAY, Color.BLACK, "Done")) {
									// TODO: Glow an LED red or green
									if (userEntered.equals("1234")) {
										outsideMapKeypadSolved = true;
									} else userEntered = "";
								}*/

								while (window.mouse.down) {
									window.repaint();
									Thread.sleep(1000 / 60);
								}

								window.repaint();

								try {
									Thread.sleep(1000 / 60);
								} catch (Exception ignored) {
								}
							}
						}
					}
				} else if (currentMapId == 1) { // Hallway
					/*
					 * Map concept:
					 * Have an elevator leading upstairs, and a locked door leading to the core hallway. The player needs
					 * to get the screwdriver and wire cutters from upstairs to unscrew and rewire the panel.
					 */

					if (player.inArea(382, 1368, 74, 32) && window.keyboard.KEY_E) { // Locked door

					}

					if (player.inArea(1095, 947, 142, 32) && window.keyboard.KEY_E) { // Upstairs elevator
						for (int i = 0; i < 255; i++) {
							window.UIDrawFilledRect(0, 0, window.width, window.height, new Color(0, 0, 0, i));
							window.repaint();
							try {
								Thread.sleep(4);
							} catch (Exception ignored) {
							}
						}
						player.x = 996;
						player.y = 1044;
						currentMap = upstairsMap;
						currentMapId = 2;
						window.centerCamera(player);
						for (int i = 255; i > 0; i--) {
							//currentMap.renderTo(window.frameBuffer, window.camera.x, window.camera.y, window.width, window.height, DEBUG_BUILD);
							window.UIDrawFilledRect(0, 0, window.width, window.height, new Color(0, 0, 0, i));
							window.repaint();
							try {
								Thread.sleep(2);
							} catch (Exception ignored) {
							}
						}
					}
				} else if (currentMapId == 2) { // Upstairs
					/*
					 * Map concept:
					 * Rather than a big-ass laser, make it a workshop with lots of various tools the player
					 * can pick up and use. Make sure there are useless ones, as well as a screwdriver and side cutters.
					 */
					if (player.inArea(823, 1270, 480, 214) && window.keyboard.KEY_E) { // Go back to hallway
						for (int i = 0; i < 255; i++) {
							window.UIDrawFilledRect(0, 0, window.width, window.height, new Color(0, 0, 0, i));
							window.repaint();
							try {
								Thread.sleep(4);
							} catch (Exception ignored) {
							}
						}
						player.x = 1139;
						player.y = 900;
						currentMap = hallwayMap;
						currentMapId = 1;
						window.centerCamera(player);
						for (int i = 255; i > 0; i--) {
							//currentMap.renderTo(window.frameBuffer, window.camera.x, window.camera.y, window.width, window.height, DEBUG_BUILD);
							window.UIDrawFilledRect(0, 0, window.width, window.height, new Color(0, 0, 0, i));
							window.repaint();
							try {
								Thread.sleep(2);
							} catch (Exception ignored) {
							}
						}
					}

					// 823 1270 1253 1484
				} else if (currentMapId == 3) { // Second hallway (Placeholder)
					/*
					 * Map concept:
					 * Leads directly to the core.
					 */
				} else if (currentMapId == 4) { // Free
					/*
					 * Map concept:
					 * Free Map ID (Removed map)
					 */
				} else if (currentMapId == 5) { // Core
					/*
					 * Keep mostly the same but start a timer to escape. Have the hallway door re-lock itself so the
					 * player can't get out.
					 */
				}

				window.centerCamera(player);
				//currentMap.renderTo(window.frameBuffer, window.camera.x, window.camera.y, window.width, window.height, DEBUG_BUILD);

				if (DEBUG_BUILD) {
					window.UIDrawFilledRect(0, window.height - 21, window.width, 21, Color.BLACK);
					window.UIDrawCenteredString(window.width / 2, window.height - 10, 12, "Debug Build", Color.WHITE);

					debugWindow.UIDrawFilledRect(0, 0, window.width, window.height, Color.BLACK);
					debugWindow.UIDrawString(5, 15, 12, "X: " + player.x);
					debugWindow.UIDrawString(5, 25, 12, "Y: " + player.y);
					debugWindow.UIDrawString(5, 35, 12, "Version 1.1b");
					debugWindow.UIDrawString(5, 45, 12, "Collision Objects: " + currentMap.items.size());
					debugWindow.UIDrawString(5, 55, 12, "Pickup Objects: " + currentMap.pickups.size());
					debugWindow.UIDrawString(5, 65, 12, "Map ID: " + currentMapId);

					if (debugWindow.keyboard.KEY_X) {
						Scanner keyboard = new Scanner(System.in);
						System.out.print("New player X? ");
						player.x = keyboard.nextInt();
					}

					if (debugWindow.keyboard.KEY_Y) {
						Scanner keyboard = new Scanner(System.in);
						System.out.print("New player Y? ");
						player.y = keyboard.nextInt();
					}

					if (debugWindow.keyboard.KEY_C) {
						gameRunning = false;
					}

					debugWindow.repaint();
				}

				window.repaint();
				try {
					Thread.sleep(1000 / 60);
				} catch (Exception ignored) {
				}
			}
		} catch(Exception e) {
			if (DEBUG_BUILD) {
				boolean debugging = true;
				debugWindow.setPreferredSize(new Dimension(800, 600));
				while (debugging) {
					debugWindow.UIDrawFilledRect(0, 0, debugWindow.width, debugWindow.height, Color.BLACK);
					debugWindow.UIDrawString(5, 15, 12, "FUCK! THE GAME CRASHED!", Color.WHITE);
					debugWindow.UIDrawString(5, 25, 12, "Game Version: DEBUG 1.1b", Color.WHITE);
					debugWindow.UIDrawString(5, 35, 12, "Map ID " + currentMapId, Color.WHITE);
					debugWindow.UIDrawString(5, 45, 12, "X: " + player.x, Color.WHITE);
					debugWindow.UIDrawString(5, 55, 12, "Y: " + player.y, Color.WHITE);
					debugWindow.UIDrawString(5, 65, 12, "Error: " + e.getMessage(), Color.WHITE);
					debugWindow.UIDrawString(5, 75, 12, "In: " + e.getClass(), Color.WHITE);
					debugWindow.UIDrawString(5, 85, 12, "Press ESC to exit.", Color.WHITE);
					debugWindow.repaint();

					if(debugWindow.keyboard.KEY_ESCAPE) debugging = false;

					try {
						Thread.sleep(1000 / 60);
					} catch(Exception ignored) {}
				}
				System.exit(1);
			} else {
				window.UIDrawFilledRect(0, 0, window.width, window.height, Color.BLACK);
				window.UIDrawCenteredString(window.width / 2, window.height / 2, 34, "Game crashed!", Color.WHITE);
				window.repaint();
				while(true) {}
			}
		}
		System.exit(0);
	}
}
