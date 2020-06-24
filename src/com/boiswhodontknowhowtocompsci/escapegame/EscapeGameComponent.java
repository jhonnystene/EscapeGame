package com.boiswhodontknowhowtocompsci.escapegame;

import com.asuscomm.johnnystene.escape.Window;
import com.asuscomm.johnnystene.escape.World;
import com.asuscomm.johnnystene.escape.WorldItem;

public class EscapeGameComponent {
	public static void main(String[] args) {
		Window window = new Window("Escape The Robots And Oh Gosh They're Coming Run");
		World gameWorld = new World();
		WorldItem player = new WorldItem("/res/player/static/FLStatic.png");
		player.resize(118, 118);
		gameWorld.items.add(player);
		
		while(true) {
			player.x = window.mouse.x;
			player.y = window.mouse.y;
			gameWorld.renderTo(window.frameBuffer, window.camera.x, window.camera.y, window.width, window.height);
			window.repaint();
		}
	}
}
