package com.boiswhodontknowhowtocompsci.escapegame;

import java.awt.Color;
import java.net.URL;

import javax.imageio.ImageIO;

import com.asuscomm.johnnystene.infinity.CollisionItem;
import com.asuscomm.johnnystene.infinity.GithubUtils;
import com.asuscomm.johnnystene.infinity.Window;
import com.asuscomm.johnnystene.infinity.WorldItem;

public class Outside {
	Window window;
	CollisionLineGenerator linegen;
	
	public Outside(Window w, CollisionLineGenerator l) {
		window = w;
		linegen = l;
	}
	
	public void createAt(int x, int y) {
		Color color = Color.WHITE;
		
		try {
			WorldItem background = new WorldItem(ImageIO.read(new URL(GithubUtils.getFullPath("img/maps/Outside.png"))));
			window.backgroundLayer.add(background);
		} catch(Exception e) {
			window.crash("Failed to download map background", e);
		}
		
		linegen.createLine(0, 1594, 110, 1485, color, window);
		linegen.createLine(0, 2047, 660, 2047, color, window);
		linegen.createLine(0, 2047, 0, 1594, color, window);
		linegen.createLine(660, 2047, 660, 1710, color, window);
		linegen.createLine(622, 1710, 660, 1710, color, window);
		linegen.createLine(622, 1710, 622, 1645, color, window);
		linegen.createLine(309, 1600, 568, 1600, color, window);
		linegen.createLine(568, 1600, 622, 1645, color, window);
		linegen.createLine(309, 1600, 309, 1528, color, window);
		linegen.createLine(309, 1528, 336, 1528, color, window);
		linegen.createLine(336, 1528, 336, 1485, color, window);
		linegen.createLine(110, 1485, 336, 1485, color, window);
	}
}