/*
 * CollisionLineGenerator.java
 * By Johnny
 * 
 * Makes individual CollisionItems for every pixel in a line.
 * Only works for 45 and 135 degree lines.
 */

package com.boiswhodontknowhowtocompsci.escapegame;

import java.awt.Color;
import com.asuscomm.johnnystene.infinity.CollisionItem;
import com.asuscomm.johnnystene.infinity.Window;

public class CollisionLineGenerator {
	public void createLine(int startX, int startY, int endX, int endY, Color color, Window window) {
		// DDA Line Drawing Algorithm
		int dx = endX - startX;
		int dy = endY - startY;
		int steps = 0;
		
		if(Math.abs(dx) > Math.abs(dy)) steps = Math.abs(dx);
		else steps = Math.abs(dy);
		
		float xIncrement = dx / (float) steps;
		float yIncrement = dy / (float) steps;
		
		float x = startX;
		float y = startY;
		
		for(int v = 0; v < steps; v ++) {
			CollisionItem item = new CollisionItem(1, 1, color);
			item.x = x;
			item.y = y;
			window.collisionItemLayer.add(item);
			x += xIncrement;
			y += yIncrement;
		}
	}
}
