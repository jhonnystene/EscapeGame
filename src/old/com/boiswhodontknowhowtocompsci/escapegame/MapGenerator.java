/*
 * MapGenerator.java
 * By Johnny
 * 
 * Downloads and converts .map files from the Github to playable maps.
 * TODO: Make it able to load textures based on paths in the .map file
 * TODO: Finish headers for connecting multiple maps together
 */

package com.boiswhodontknowhowtocompsci.escapegame;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.asuscomm.johnnystene.escape.CollisionItem;
import com.asuscomm.johnnystene.escape.Window;

public class MapGenerator {
	Window window;
	boolean enableDebugCollision;

	public MapGenerator(Window w, boolean d) {
		window = w;
		enableDebugCollision = d;
	}

	public void createLine(int startX, int startY, int endX, int endY) {
		if(startX == endX) { // Vertical Line
			int height = Math.abs(startY - endY);
			CollisionItem item = new CollisionItem(1, height, Color.MAGENTA);
			item.x = endX;
			if(startY > endY) item.y = endY;
			else item.y = startY;
			if(enableDebugCollision)
				window.collisionItemLayer.add(item);
			else
				window.hiddenCollisionItemLayer.add(item);
		} else if(startY == endY) { // Horizontal Line
			int width = Math.abs(startX - endX);
			CollisionItem item = new CollisionItem(width, 1, Color.MAGENTA);
			item.y = startY;
			if(startX > endX) item.x = endX;
			else item.x = startX;
			if(enableDebugCollision)
				window.collisionItemLayer.add(item);
			else
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
				CollisionItem item = new CollisionItem(1, 1, Color.MAGENTA);
				item.x = x;
				item.y = y;
				if(enableDebugCollision)
					window.collisionItemLayer.add(item);
				else
					window.hiddenCollisionItemLayer.add(item);
				x += xIncrement;
				y += yIncrement;
			}
		}
	}

	public void loadMap(InputStream file) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(file, "UTF-8"));
		String inputline;

		// Line format:
		// X1,Y1 X2,Y2

		while((inputline = in.readLine()) != null) {
			System.out.println("New line: " + inputline);
			// We need to get the coordinates for the line - there will be 2 per line.
			String[] fullCoords = inputline.split(" ");
			String coordSet0 = fullCoords[0];
			String coordSet1 = fullCoords[1];

			// Next, we need to split those into ints.
			int[] coords0 = {0, 0};
			int[] coords1 = {0, 0};

			// I'm gonna look back at this in a week and have know clue what the fuck i was doing haha
			coords0[0] = Integer.parseInt(coordSet0.split(",")[0]);
			coords0[1] = Integer.parseInt(coordSet0.split(",")[1]);
			coords1[0] = Integer.parseInt(coordSet1.split(",")[0]);
			coords1[1] = Integer.parseInt(coordSet1.split(",")[1]);

			createLine(coords0[0], coords0[1], coords1[0], coords1[1]);
		}
	}

	// TODO: Make no map use this, ideally make .map v3 work for any type of map
	public void loadMapOld(InputStream file, Window window) throws IOException {
		// Download map file
		BufferedReader in = new BufferedReader(new InputStreamReader(file, "UTF-8"));
		int y = 0;
		
		// Load it in line by line
		String inputline;
		
		boolean inHeader = true;
		int xOffset = 0;
		int yOffset = 0;
		while((inputline = in.readLine()) != null) {
			for(int x = 0; x < inputline.length(); x ++) {
				if(inputline.charAt(x) == '#') {
					// Put a CollisionItem at the proper coords
					CollisionItem item = new CollisionItem(128, 128, Color.BLACK);
					item.x = (x * 128) + xOffset;
					item.y = (y * 128) + yOffset;
					if(enableDebugCollision)
						window.collisionItemLayer.add(item);
					else
						window.hiddenCollisionItemLayer.add(item);
				}
				// TODO make .map files work with CollisionLineGenerator
			}
			y ++;
		}
	}
}
