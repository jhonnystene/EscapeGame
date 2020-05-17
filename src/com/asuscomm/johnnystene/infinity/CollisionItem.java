/*
 * CollisionItem.java
 * 
 * Adds collision functions to WorldItem.
 */

package com.asuscomm.johnnystene.infinity;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class CollisionItem extends WorldItem {
	public Line[] collisionShape; // Sets itself to be a box around the whole image by default
	
	public CollisionItem(int width, int height, Color color) {
		super(width, height, color);
		Line[] tempCollisionShape = {new Line(0, 0, width, 0), new Line(0, 0, 0, height), new Line(width, 0, 0, height), new Line(0, height, width, 0)};
		collisionShape = tempCollisionShape;
	}
	
	public CollisionItem(String filename, boolean url) {
		super(filename, url);
		Line[] tempCollisionShape = {new Line(0, 0, width, 0), new Line(0, 0, 0, height), new Line(width, 0, 0, height), new Line(0, height, width, 0)};
		collisionShape = tempCollisionShape;
	}
	
	public CollisionItem(BufferedImage setSprite) {
		super(setSprite);
	}
	
	public boolean collidingWith(CollisionItem item) {
		if(item == null) return false;
		if(x < item.x + item.width && x + width > item.x && y < item.y + item.height&& y + height > item.y) {
			return true;
		}
		return false;
	}
	
	public void moveAndCollide(int moveX, int moveY, CollisionItem[] items) {
		x = x + moveX;
		for(CollisionItem item : items) {
			while(collidingWith(item)) {
				if(moveX > 0) {
					x = x - 1;
				} else {
					x = x + 1;
				}
			}
		}
		
		y = y + moveY;
		for(CollisionItem item : items) {
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
	}
	
	public void moveAndCollide(int moveX, int moveY, CollisionItem item) {
		// We move horizontally and vertically separately so we can actually tell how we need to move if we collide.
		// Horizontally first, because I can.
		x = x + moveX;
		while(collidingWith(item)) {
			if(moveX > 0) {
				x = x - 1;
			} else {
				x = x + 1;
			}
		}
		
		// Then we do the same thing vertically.
		y = y + moveY;
		while(collidingWith(item)) {
			if(moveY > 0) {
				y = y- 1;
			} else {
				y = y + 1;
			}
		}
	}
}
