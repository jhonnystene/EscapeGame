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

import com.asuscomm.johnnystene.escape.InventoryItem;
import com.asuscomm.johnnystene.escape.Window;
import com.asuscomm.johnnystene.escape.World;
import com.asuscomm.johnnystene.escape.WorldItem;

import java.awt.*;

public class EscapeGameComponent {
	public static final boolean DEBUG_BUILD = true;

	public static void main(String[] args) {
		// Setup window
		Window window = new Window("Escape The Robots And Oh Gosh They're Coming Run");
		
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

		World hallwayMap = new World("/res/maps/Hallway.png");
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
		
		while(gameRunning) {
			float moveX = 0;
			float moveY = 0;
			
			if(window.keyboard.KEY_A) moveX -= 10;
			if(window.keyboard.KEY_D) moveX += 10;
			if(window.keyboard.KEY_W) moveY -= 10;
			if(window.keyboard.KEY_S) moveY += 10;
			
			player.moveAndCollide(moveX, moveY, currentMap.items);
			// Check if we should pick up an item
			for(InventoryItem item : currentMap.pickups) {
				if(player.inArea(item.x, item.y, item.width, item.height) && item.inWorld) {
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

			if(currentMapId == 0) { // Outside
				/*
				 * Map concept:
				 * Make the password the year the game takes place,
				 * and as a hint have the needed buttons look worn down.
				 */
				if(outsideMapKeypadSolved) {
					if(player.inArea(400, 1600, 100, 64) && window.keyboard.KEY_E) {
						for(int i = 0; i < 255; i++) {
							window.UIDrawFilledRect(0, 0, window.width, window.height, new Color(0, 0, 0, i));
							window.repaint();
							try {
								Thread.sleep(4);
							} catch(Exception e) {}
						}
						player.x = 415;
						player.y = 1450;
						currentMap = hallwayMap;
						currentMapId = 1;
						window.centerCamera(player);
						for(int i = 255; i > 0; i--) {
							currentMap.renderTo(window.frameBuffer, window.camera.x, window.camera.y, window.width, window.height, DEBUG_BUILD);
							window.UIDrawFilledRect(0, 0, window.width, window.height, new Color(0, 0, 0, i));
							window.repaint();
							try {
								Thread.sleep(2);
							} catch(Exception e) {}
						}
					}
				} else {
					if(player.inArea(560, 1640, 64, 64) && window.keyboard.KEY_E) {
						// Temporary art for puzzle
						String userEntered = "";
						while(!outsideMapKeypadSolved) {
							window.UIDrawFilledRect(0, 0, window.width, window.height, Color.BLACK);

							// Keypad placement
							if (window.UIDrawButton((window.width / 2) - 100, (window.height / 2) - 100, 50, 50, 20, Color.GRAY, Color.LIGHT_GRAY, Color.BLACK, "1"))
								userEntered = userEntered + "1";
							if (window.UIDrawButton((window.width / 2) - 25, (window.height / 2) - 100, 50, 50, 20, Color.GRAY, Color.LIGHT_GRAY, Color.BLACK, "2"))
								userEntered = userEntered + "2";
							if (window.UIDrawButton((window.width / 2) + 50, (window.height / 2) - 100, 50, 50, 20, Color.GRAY, Color.LIGHT_GRAY, Color.BLACK, "3"))
								userEntered = userEntered + "3";

							if (window.UIDrawButton((window.width / 2) - 100, (window.height / 2) - 25, 50, 50, 20, Color.GRAY, Color.LIGHT_GRAY, Color.BLACK, "4"))
								userEntered = userEntered + "4";
							if (window.UIDrawButton((window.width / 2) - 25, (window.height / 2) - 25, 50, 50, 20, Color.GRAY, Color.LIGHT_GRAY, Color.BLACK, "5"))
								userEntered = userEntered + "5";
							if (window.UIDrawButton((window.width / 2) + 50, (window.height / 2) - 25, 50, 50, 20, Color.GRAY, Color.LIGHT_GRAY, Color.BLACK, "6"))
								userEntered = userEntered + "6";

							if (window.UIDrawButton((window.width / 2) - 100, (window.height / 2) + 50, 50, 50, 20, Color.GRAY, Color.LIGHT_GRAY, Color.BLACK, "7"))
								userEntered = userEntered + "7";
							if (window.UIDrawButton((window.width / 2) - 25, (window.height / 2) + 50, 50, 50, 20, Color.GRAY, Color.LIGHT_GRAY, Color.BLACK, "8"))
								userEntered = userEntered + "8";
							if (window.UIDrawButton((window.width / 2) + 50, (window.height / 2) + 50, 50, 50, 20, Color.GRAY, Color.LIGHT_GRAY, Color.BLACK, "9"))
								userEntered = userEntered + "9";

							if (window.UIDrawButton((window.width / 2) - 25, (window.height / 2) + 125, 50, 50, 20, Color.GRAY, Color.LIGHT_GRAY, Color.BLACK, "0"))
								userEntered = userEntered + "0";

							if (window.UIDrawButton((window.width / 2) + 50, (window.height / 2) + 125, 50, 50, 20, Color.GRAY, Color.LIGHT_GRAY, Color.BLACK, "Done")) {
								// TODO: Glow an LED red or green
								if(userEntered.equals("1234")) {
									outsideMapKeypadSolved = true;
								} else userEntered = "";
							}

							while(window.mouse.down) { window.repaint(); }

							window.repaint();

							try {
								Thread.sleep(1000 / 60);
							} catch(Exception e) { }
						}
					}
				}
			} else if(currentMapId == 1) { // Hallway
				/*
				 * Map concept:
				 * Have an elevator leading upstairs, and a locked door leading to the core hallway. The player needs
				 * to get the screwdriver and wire cutters from upstairs to unscrew and rewire the panel.
				 */
			} else if(currentMapId == 2) { // Upstairs
				/*
				 * Map concept:
				 * Rather than a big-ass laser, make it a workshop with lots of various tools the player
				 * can pick up and use. Make sure there are useless ones, as well as a screwdriver and side cutters.
				 */
			} else if(currentMapId == 3) { // Second hallway (Placeholder)
				/*
				 * Map concept:
				 * Leads directly to the core.
				 */
			} else if(currentMapId == 4) { // Free
				/*
				 * Map concept:
				 * Free Map ID (Removed map)
				 */
			} else if(currentMapId == 5) { // Core
				/*
				 * Keep mostly the same but start a timer to escape. Have the hallway door re-lock itself so the
				 * player can't get out.
				 */
			}

			window.centerCamera(player);
			currentMap.renderTo(window.frameBuffer, window.camera.x, window.camera.y, window.width, window.height, DEBUG_BUILD);

			if(DEBUG_BUILD) {
				// TODO: Put this in the engine so we can just pass an ArrayList of strings and have the box size adjust dynamically.
				window.UIDrawFilledRect(0, window.height - 21, window.width, 21, Color.BLACK);
				window.UIDrawCenteredString(window.width / 2, window.height - 10, 12, "Debug Build", Color.WHITE);

				window.UIDrawFilledRect(0, 0, 100, 45, Color.BLACK);
				window.UIDrawString(5, 15, 12, "X: " + player.x);
				window.UIDrawString(5, 25, 12, "Y: " + player.y);
				window.UIDrawString(5, 35, 12, "Version 1.1b");
			}

			window.repaint();
			try {
				Thread.sleep(1000 / 60);
			} catch(Exception e) { }
		}
	}
}
