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
		int directionX = -1;
		int directionY = -1;
		
		// Slightly less code than the alternative
		if(startX - endX < 0) directionX = Math.abs(directionX);
		if(startY - endY < 0) directionY = Math.abs(directionY);
		
		int y = 0;
		
		for(int x = 0; x != Math.abs(startX - endX); x ++) {
			CollisionItem item = new CollisionItem(1, 1, color);
			item.x = startX + (directionX * x);
			item.y = startY + (directionY * y);
			window.collisionItemLayer.add(item);
			y ++;
		}
	}
}
