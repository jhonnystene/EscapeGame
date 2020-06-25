package com.boiswhodontknowhowtocompsci.escapegame;

import com.asuscomm.johnnystene.escape.Window;
import com.asuscomm.johnnystene.escape.World;
import com.asuscomm.johnnystene.escape.WorldItem;

public class EscapeGameComponent {
	public static void main(String[] args) {
		// Setup window
		Window window = new Window("Escape The Robots And Oh Gosh They're Coming Run");
		window.setFPSCap(60);
		
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
		outsideMap.items.add(player);
		
		World ventMap = new World("/res/maps/NewVents.png");
		ventMap.loadMapFile("/res/maps/collision/vents.map");
		ventMap.items.add(player);
		
		World upstairsMap = new World("/res/maps/Upstairs.png");
		upstairsMap.loadMapFile("/res/maps/collision/upstairs.map");
		outsideMap.items.add(player);
		
		World coreMap = new World("/res/maps/Core.png");
		coreMap.loadMapFile("/res/maps/collision/core.map");
		outsideMap.items.add(player);
		
		World currentMap = outsideMap;
		int currentMapId = 0;
		
		while(true) {
			float moveX = 0;
			float moveY = 0;
			
			if(window.keyboard.KEY_A) moveX -= 10;
			if(window.keyboard.KEY_D) moveX += 10;
			if(window.keyboard.KEY_W) moveY -= 10;
			if(window.keyboard.KEY_S) moveY += 10;
			
			player.moveAndCollide(moveX, moveY, currentMap.items);

			/*
			 * MAP IDS LIST
			 * 0 - Outside building (Goes to Main Hallway)
			 * 1 - Main Hallway (Goes to Upstairs or Keypad Hallway)
			 * 2 - Upstairs (Goes to Vents)
			 * 3 - Secondary (Keypad) Hallway (Goes to Core)
			 * 4 - Vents (Goes to Core)
			 */

			if(currentMapId == 0) { // Outside
				/*
				 * Map concept:
				 * Have a standard T-9 keypad rather than the terminal. Make the password the year the game takes place,
				 * and as a hint have the needed buttons look worn down.
				 */
				if(outsideMapKeypadSolved) {

				} else {

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
			} else if(currentMapId == 4) { // Vents
				/*
				 * Map concept:
				 * Remove this map.
				 */
			} else if(currentMapId == 5) { // Core
				/*
				 * Keep mostly the same but start a timer to escape. Have the hallway door re-lock itself so the
				 * player can't get out.
				 */
			}

			window.centerCamera(player);
			currentMap.renderTo(window.frameBuffer, window.camera.x, window.camera.y, window.width, window.height);
			window.repaint();
			try {
				Thread.sleep(1000 / 60);
			} catch(Exception e) { }
		}
	}
}
