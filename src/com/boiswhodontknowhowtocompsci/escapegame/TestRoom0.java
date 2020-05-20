/*
 * Main.java
 * 
 * Main game class for Escape The Robots And Oh Gosh They're Coming Run
 */

package com.boiswhodontknowhowtocompsci.escapegame;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.asuscomm.johnnystene.infinity.CollisionItem;
import com.asuscomm.johnnystene.infinity.GithubUtils;
import com.asuscomm.johnnystene.infinity.Window;

public class TestRoom0 {
	public static void main(String[] args) throws InterruptedException, MalformedURLException, IOException, LineUnavailableException, UnsupportedAudioFileException {
		Window window = new Window(800, 600, "Escape The Robots And Oh Gosh They're Coming Run");
		
		Clip clip = AudioSystem.getClip();
		AudioInputStream titleInputStream = AudioSystem.getAudioInputStream(new URL(GithubUtils.getFullPath("msc/title.wav")));
		clip.open(titleInputStream);
		clip.loop(Clip.LOOP_CONTINUOUSLY);
		
		boolean inTitleScreen = true;
		while(inTitleScreen) {
			window.drawRectangle(0, 0, 800, 600, Color.BLACK);
			window.drawTextCentered(400, 250, "Escape The Robots And", 40, Color.WHITE);
			window.drawTextCentered(400, 300, "Oh Gosh They're Coming Run", 40, Color.WHITE);
			window.drawTextCentered(400, 325, "Industry Standard Testing Level For Testing Industry Standard Games", 20, Color.BLUE);
			window.drawTextCentered(400, 350, "(Test Room 0, Basic Maze Level)", 20, Color.BLUE);
			
			if(window.drawMenuButton(25, 475, 350, 100, "PLAY", Color.WHITE, new Color(255, 66, 28), new Color(255, 91, 59))) {
				inTitleScreen = false;
			}
			
			if(window.drawMenuButton(425, 475, 350, 100, "QUIT GAME", Color.WHITE, new Color(255, 66, 28), new Color(255, 91, 59))) {
				System.exit(0);
			}
			
			window.repaint();
			Thread.sleep(1000 / 60); // FPS cap needed in menus too
		}
		
		// Create the player object and preload the player sprite
		window.drawLoadingScreen("Downloading player sprites...");
		CollisionItem player = new CollisionItem(GithubUtils.getFullPath("img/player/test_player.png"), true);
		player.x = 64;
		player.y = 960;
		
		// Create the maze bounding box
		CollisionItem mazeBoundingBoxN = new CollisionItem(2048, 512, Color.BLACK);
		mazeBoundingBoxN.x = -512;
		mazeBoundingBoxN.y = -512;
		
		CollisionItem mazeBoundingBoxE = new CollisionItem(512, 1024, Color.BLACK);
		mazeBoundingBoxE.x = -512;
		mazeBoundingBoxE.y = 0;
		
		CollisionItem mazeBoundingBoxS = new CollisionItem(2048, 512, Color.BLACK);
		mazeBoundingBoxS.x = -512;
		mazeBoundingBoxS.y = 1024;
		
		CollisionItem mazeBoundingBoxW = new CollisionItem(512, 1024, Color.BLACK);
		mazeBoundingBoxW.x = 1024;
		mazeBoundingBoxW.y = 0;
		
		// Create Maze Internals
		CollisionItem mazeInside0 = new CollisionItem(832, 64, Color.BLACK);
		mazeInside0.x = 0;
		mazeInside0.y = 896;
		
		CollisionItem mazeInside1 = new CollisionItem(128, 64, Color.BLACK);
		mazeInside1.x = 896;
		mazeInside1.y = 896;
		
		CollisionItem mazeInside2 = new CollisionItem(320, 192, Color.BLACK);
		mazeInside2.x = 384;
		mazeInside2.y = 704;
		
		CollisionItem mazeInside3 = new CollisionItem(320, 128, Color.BLACK);
		mazeInside3.x = 64;
		mazeInside3.y = 704;
		
		CollisionItem mazeInside4 = new CollisionItem(384, 640, Color.BLACK);
		mazeInside4.x = 0;
		mazeInside4.y = 0;
		
		CollisionItem mazeInside5 = new CollisionItem(576, 640, Color.BLACK);
		mazeInside5.x = 448;
		mazeInside5.y = 0;
		
		CollisionItem mazeInside6 = new CollisionItem(192, 128, Color.BLACK);
		mazeInside6.x = 768;
		mazeInside6.y = 704;
		
		CollisionItem mazeFinish = new CollisionItem(64, 64, Color.BLUE);
		mazeFinish.x = 384;
		mazeFinish.y = 0;
		
		// Start the game loop
		boolean gameRunning = true;
		while(gameRunning) {
			// This is all of the items to collide with each other when moving.
			CollisionItem[] worldItems = {player, 
					mazeBoundingBoxN, mazeBoundingBoxE, mazeBoundingBoxS, mazeBoundingBoxW,
					mazeInside0, mazeInside1, mazeInside2, mazeInside3, mazeInside4, mazeInside5,
					mazeInside6};
			
			// Handle inputs
			int moveX = 0;
			int moveY = 0;
			
			if(window.keyListener.KEY_LEFT) moveX -= 10;
			if(window.keyListener.KEY_RIGHT) moveX += 10;
			if(window.keyListener.KEY_UP) moveY -= 10;
			if(window.keyListener.KEY_DOWN) moveY += 10; 
			
			player.moveAndCollide(moveX, moveY, worldItems);
			window.centerCamera(player);
			
			// Draw all objects to the screen and update
			window.drawWorldItems(worldItems);
			window.drawWorldItem(mazeFinish);
			
			// See if player finished
			if(player.collidingWith(mazeFinish)) {
				gameRunning = false;
			}
			
			window.repaint();
			Thread.sleep(1000 / 60);
		}
		
		System.exit(0);
	}
}
