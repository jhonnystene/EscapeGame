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
import java.io.InputStreamReader;
import java.net.URL;

import com.asuscomm.johnnystene.infinity.CollisionItem;
import com.asuscomm.johnnystene.infinity.GithubUtils;
import com.asuscomm.johnnystene.infinity.Window;

public class MapGenerator {
	public void loadMap(String path, Window window) {
		System.out.println("Loading map from Github: " + path);
		try {
			// Download map file
			URL url = new URL(GithubUtils.getFullPath(path));
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			int y = 0;
			
			// Load it in line by line
			String inputline;
			
			boolean inHeader = true;
			int xOffset = 0;
			int yOffset = 0;
			
			System.out.println("Reading header:");
			while((inputline = in.readLine()) != null) {
				if(inHeader) {
					if(inputline.contains("END HEADER DATA")) {
						System.out.println("Exiting header.");
						inHeader = false;
					} else {
						// parse the line
						String[] command = inputline.split(" ", 2);
						// Todo: Fix this shit
						System.out.println("Malformed header data: " + inputline);
					}
				} else {
					for(int x = 0; x < inputline.length(); x ++) {
						if(inputline.charAt(x) == '#') {
							// Put a CollisionItem at the proper coords
							CollisionItem item = new CollisionItem(80, 80, Color.BLACK);
							item.x = (x * 80) + xOffset;
							item.y = (y * 80) + yOffset;
							window.collisionItemLayer.add(item);
						}
						// TODO add more shit you can put in
					}
					y ++;
				}
			}
		} catch(Exception e) {
			window.crash("Failed to load map file: " + path, e);
		}
	}
}
