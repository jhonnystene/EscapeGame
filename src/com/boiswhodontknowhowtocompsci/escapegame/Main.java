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

public class Main {
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
		player.x = 100;
		player.y = 200;
		
		// Do the same for Roboboi
		window.drawLoadingScreen("Downloading Roboboi sprites...");
		BufferedImage roboboiSW = ImageIO.read(new URL(GithubUtils.getFullPath("img/roboboi/tile_00.png")));
		BufferedImage roboboiW = ImageIO.read(new URL(GithubUtils.getFullPath("img/roboboi/tile_01.png")));
		BufferedImage roboboiNW = ImageIO.read(new URL(GithubUtils.getFullPath("img/roboboi/tile_02.png")));
		BufferedImage roboboiN = ImageIO.read(new URL(GithubUtils.getFullPath("img/roboboi/tile_03.png")));
		BufferedImage roboboiNE = ImageIO.read(new URL(GithubUtils.getFullPath("img/roboboi/tile_04.png")));
		BufferedImage roboboiE = ImageIO.read(new URL(GithubUtils.getFullPath("img/roboboi/tile_05.png")));
		BufferedImage roboboiSE = ImageIO.read(new URL(GithubUtils.getFullPath("img/roboboi/tile_06.png")));
		BufferedImage roboboiS = ImageIO.read(new URL(GithubUtils.getFullPath("img/roboboi/tile_07.png")));
		CollisionItem roboboi = new CollisionItem(roboboiE);
		roboboi.x = 200;
		roboboi.y = 200;
		
		// Start the game loop
		boolean gameRunning = true;
		while(gameRunning) {
			// This is all of the items to collide with each other when moving.
			CollisionItem[] worldItems = {roboboi, player};
			
			// Handle inputs
			int moveX = 0;
			int moveY = 0;
			
			if(window.keyListener.KEY_LEFT) moveX -= 10;
			if(window.keyListener.KEY_RIGHT) moveX += 10;
			if(window.keyListener.KEY_UP) moveY -= 10;
			if(window.keyListener.KEY_DOWN) moveY += 10; 
			
			// Move the player or Roboboi, depending on whether the action key is held
			if(window.keyListener.KEY_ACTION) { // Roboboi
				// TODO: Bake isometric sprite support into the engine
				// TODO: Make Roboboi collide as well
				roboboi.moveAndCollide(moveX, moveY, worldItems);
				window.centerCamera(roboboi);
				
				if(moveX > 0) {
					if(moveY > 0) {
						roboboi.sprite = roboboiSE;
					} else if(moveY < 0) {
						roboboi.sprite = roboboiNE;
					} else {
						roboboi.sprite = roboboiE;
					}
				} else if(moveX < 0) {
					if(moveY > 0) {
						roboboi.sprite = roboboiSW;
					} else if(moveY < 0) {
						roboboi.sprite = roboboiNW;
					} else {
						roboboi.sprite = roboboiW;
					}
				} else {
					if(moveY > 0) {
						roboboi.sprite = roboboiS;
					} else if(moveY < 0) {
						roboboi.sprite = roboboiN;
					}
				}
			} else { // Player
				player.moveAndCollide(moveX, moveY, worldItems);
				window.centerCamera(player);
			}
			
			// Draw all objects to the screen and update
			window.drawWorldItem(player);
			window.drawWorldItem(roboboi);
			window.repaint();
			Thread.sleep(1000 / 60);
		}
		
		// We've crashed.
		// TODO: Draw crash screen
	}
}
