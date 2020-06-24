package com.boiswhodontknowhowtocompsci.escapegame;

import com.asuscomm.johnnystene.escape.Window;
import com.asuscomm.johnnystene.escape.World;
import com.asuscomm.johnnystene.escape.WorldItem;

public class EscapeGameComponent {
	public static void main(String[] args) {
		Window window = new Window("Escape The Robots And Oh Gosh They're Coming Run");
		window.setFPSCap(60);
		World gameWorld = new World("/res/maps/Outside-Final.png");
		gameWorld.loadMapFile("/res/maps/collision/outside.map");
		WorldItem player = new WorldItem("/res/player/static/FLStatic.png");
		player.resize(118, 118);
		player.x = 360;
		player.y = 1800;
		player.hitboxX = 44;
		player.hitboxWidth = 30;
		player.hitboxY = 93;
		player.hitboxHeight = 20;
		gameWorld.items.add(player);
		
		while(true) {
			float moveX = 0;
			float moveY = 0;
			
			if(window.keyboard.KEY_A) moveX -= 10;
			if(window.keyboard.KEY_D) moveX += 10;
			if(window.keyboard.KEY_W) moveY -= 10;
			if(window.keyboard.KEY_S) moveY += 10;
			
			player.moveAndCollide(moveX, moveY, gameWorld.items);
			
			window.centerCamera(player);
			gameWorld.renderTo(window.frameBuffer, window.camera.x, window.camera.y, window.width, window.height);
			window.repaint();
			try {
				Thread.sleep(1000 / 60);
			} catch(Exception e) {
				
			}
		}
	}
}
