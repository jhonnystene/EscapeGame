package com.boiswhodontknowhowtocompsci.escapegame;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import com.asuscomm.johnnystene.infinity.CollisionItem;
import com.asuscomm.johnnystene.infinity.GithubUtils;
import com.asuscomm.johnnystene.infinity.Window;

public class MapGenerator {
	public void loadMap(String path, Window window) {
		try {
			URL url = new URL(GithubUtils.getFullPath(path));
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			int y = 0;
			String inputline;
			while((inputline = in.readLine()) != null) {
				for(int x = 0; x < inputline.length(); x ++) {
					if(inputline.charAt(x) == '#') {
						CollisionItem item = new CollisionItem(80, 80, Color.BLACK);
						item.x = x * 80;
						item.y = y * 80;
						window.collisionItemLayer.add(item);
					}
				}
				y ++;
			}
		} catch(Exception e) {
			window.crash("Failed to load map file: " + path, e);
		}
	}
}
