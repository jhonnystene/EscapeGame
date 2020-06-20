/*
 * CollisionItem.java
 * 
 * Adds collision functions to WorldItem.
 */

package com.asuscomm.johnnystene.escape;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class CollisionItem extends WorldItem {
	public boolean[] collisionLayer = {true, true, true, true, true};
	public boolean[] collisionMask = {true, true, true, true, true};
	
	public CollisionItem(int width, int height, Color color) {
		super(width, height, color);
	}
	
	public CollisionItem(InputStream setSprite) throws IOException {
		super(setSprite);
	}
	
	public CollisionItem(BufferedImage setSprite) {
		super(setSprite);
	}
	
	public boolean collidingWith(CollisionItem item) {
		if(item == null) return false;
		if(item == this) return false;
		
		for(int currentLayer = 0; currentLayer < 4; currentLayer ++) {
			if(item.collisionLayer[currentLayer] && collisionMask[currentLayer]) {
				if(x < item.x + item.width && x + width > item.x && 
						y < item.y + item.height&& y + height > item.y) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public void moveAndCollide(float moveX, float moveY, ArrayList<CollisionItem> collisionItemLayer) {
		x = x + moveX;
		for(CollisionItem item : collisionItemLayer) {
			while(collidingWith(item)) {
				if(moveX > 0) {
					x = x - 1;
				} else {
					x = x + 1;
				}
			}
		}
		
		y = y + moveY;
		for(CollisionItem item : collisionItemLayer) {
			while(collidingWith(item)) {
				while(collidingWith(item)) {
					if(moveY > 0) {
						y = y- 1;
					} else {
						y = y + 1;
					}
				}
			}
		}
		
		if(isometricItem) {
			if(moveX > 0) {
				if(moveY > 0)
					pointInDirection(WorldItem.SE);
				else if(moveY < 0)
					pointInDirection(WorldItem.NE);
				else
					pointInDirection(WorldItem.E);
			} else if(moveX < 0) {
				if(moveY > 0)
					pointInDirection(WorldItem.SW);
				else if(moveY < 0)
					pointInDirection(WorldItem.NW);
				else
					pointInDirection(WorldItem.W);
			} else {
				if(moveY > 0)
					pointInDirection(WorldItem.S);
				else if(moveY < 0)
					pointInDirection(WorldItem.N);
			}
		}
	}
}
