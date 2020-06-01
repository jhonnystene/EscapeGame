/*
 * CollisionLineGenerator.java
 * By Johnny
 * 
 * Makes individual CollisionItems for every pixel in a line.
 */

package com.boiswhodontknowhowtocompsci.escapegame;

import java.awt.Color;
import com.asuscomm.johnnystene.infinity.CollisionItem;
import com.asuscomm.johnnystene.infinity.Window;

public class CollisionLineGenerator {
	public void createLine(int startX, int startY, int endX, int endY, Color color, Window window) {
		if(startX == endX) { // Vertical Line
			int height = Math.abs(startY - endY);
			CollisionItem item = new CollisionItem(1, height, color);
			item.x = endX;
			if(startY > endY) item.y = endY;
			else item.y = startY;
			window.hiddenCollisionItemLayer.add(item);
		} else if(startY == endY) { // Horizontal Line
			int width = Math.abs(startX - endX);
			CollisionItem item = new CollisionItem(width, 1, color);
			item.y = startY;
			if(startX > endX) item.x = endX;
			else item.x = startX;
			window.hiddenCollisionItemLayer.add(item);
		} else { // Diagonal line
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
				window.hiddenCollisionItemLayer.add(item);
				x += xIncrement;
				y += yIncrement;
			}
		}
	}
}
