/*
 * CollisionItem.java
 * 
 * Adds collision functions to WorldItem.
 */

package com.asuscomm.johnnystene.infinity;

import java.awt.Color;

public class CollisionItem extends WorldItem {
	public Line[] collisionShape; // Sets itself to be a box around the whole image by default
	
	public CollisionItem(int width, int height, Color color) {
		super(width, height, color);
		Line[] tempCollisionShape = {new Line(0, 0, width, 0), new Line(0, 0, 0, height), new Line(width, 0, 0, height), new Line(0, height, width, 0)};
		collisionShape = tempCollisionShape;
	}
	
	public CollisionItem(String filename) {
		super(filename);
		Line[] tempCollisionShape = {new Line(0, 0, width, 0), new Line(0, 0, 0, height), new Line(width, 0, 0, height), new Line(0, height, width, 0)};
		collisionShape = tempCollisionShape;
	}
	
	public boolean collidingWith(CollisionItem item) {
		if(item == null) return false;
		
		for(Line line : item.collisionShape) {
			if(collidingWith(line)) {
				return true;
			}
		}
		
		return false;
		
		/* Old, sad box collision detection
			if(x < item.x + item.width && x + width > item.x && y < item.y + item.width && y + height > item.y)
				return true;
			return false;
		*/
	}
	
	public boolean collidingWith(Line line) {
		// TODO: Check vertical/horizontal line collision
		
		for(Line myline : collisionShape) {
			// Are the lines parallel?
			if(myline.slope == line.slope)
				return false;
			
			// Do we have a vertical or horizontal line?
			if(myline.slope != 0 && line.slope != 0 && myline.slope != 25565 && line.slope != 25565) {
				// Get where the lines would intercept.
				double interceptX = (line.yIntercept - myline.yIntercept) / (myline.slope - line.slope);
				double interceptY = myline.slope * interceptX + myline.yIntercept;
				
				// Check if it's a real point on both lines
				if(interceptY == myline.slope * interceptX + myline.yIntercept && interceptY == line.slope * interceptX + line.yIntercept) {
					return true;
				}
			} else {
				// We do.
				//TODO: this shit
			}
		}
		return false;
	}
}
