/*
 * ######################################################
 * #  ESCAPE THE ROBOTS AND OH GOSH THEY'RE COMING RUN  #
 * ######################################################
 * # COPYRIGHT 2020 BOIS WHO DON'T KNOW HOW TO COMP SCI #
 * #                                                    #
 * # LEAD DEVELOPER, ENGINE                JOHNNY STENE #
 * # LEVEL SCRIPTING                    ETHAN STEVENSON #
 * # ART, LEVEL DESIGN                      ETHAN MEIER #
 * #              NOBODY ELSE DID FUCK ALL              #
 * ######################################################
 */

package com.boiswhodontknowhowtocompsci.escapegame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.asuscomm.johnnystene.infinity.CollisionItem;
import com.asuscomm.johnnystene.infinity.Window;
import com.asuscomm.johnnystene.infinity.WorldItem;

import res.FileLoader;

public class Test {
    private static boolean DEBUG_BUILD = true;
    private static int DEBUG_LEVEL = 0; // 0 - Don't include frame-by-frame debug information, 1 - Include all information
    private static void debug(String message) {
        if(DEBUG_BUILD) System.out.println(message);
    }

    public static void main(String[] args) throws IOException, InterruptedException, LineUnavailableException, UnsupportedAudioFileException {
        debug("Escape The Robots And Oh Gosh They're Coming Run");
        debug("TEST BUILD");

        debug("Initializing Window...");
        Window window = new Window(1136, 640, "TEST LEVEL"); // Create the game window

        debug("Initializing MapGenerator...");
        MapGenerator mapgen = new MapGenerator();

        debug("Initializing CollisionLineGenerator...");
        CollisionLineGenerator linegen = new CollisionLineGenerator();

        debug("Initializing FileLoader...");
        FileLoader fileLoader = new FileLoader();

        //Menu Button sizes
        int menuButtonWidth = 100;
        int menuButtonHeight = 20;
        int menuButtonTextSize = 16;
        int menuButtonSpacing = 10;

        // Title screen loop
        boolean inTitleScreen = true;
        debug("Starting title screen...");
        int frame = 0;
        double frameLength = 83333333.33;
        double nextFrameTime = System.nanoTime() + frameLength;

        boolean enablePlayerMovement = true;

        int hackyframes = 0;

        while(hackyframes < 125) {
            window.drawRectangle(0, 0, window.windowWidth, window.windowHeight, Color.BLACK);
            window.drawRectangle((window.windowWidth / 2) - 150, (window.windowHeight / 2) - 50, 300, 100, Color.DARK_GRAY);
            window.drawOtherRectangle((window.windowWidth / 2) - 155, (window.windowHeight / 2) - 55, 310, 110, Color.LIGHT_GRAY);
            window.drawTextCentered(window.windowWidth / 2, (window.windowHeight / 2), "Hacking door", 25, Color.WHITE);
            window.drawRectangle((window.windowWidth / 2) - 125, (window.windowHeight / 2), hackyframes * 2, 25, Color.WHITE);

            window.repaint();
            hackyframes ++;
            Thread.sleep(1000 / 30);
        }
        System.exit(0);

        /*while(inTitleScreen) {

                window.drawRectangle(0, 0, window.windowWidth, window.windowHeight, Color.BLACK);

            // Title Text
            window.drawText(20, window.windowHeight - 15 - ((menuButtonHeight + menuButtonSpacing) * 6), "Escape The Robots And", 20, Color.WHITE);
            window.drawText(20, window.windowHeight - 15 - ((menuButtonHeight + menuButtonSpacing) * 5), "Oh Gosh They're Coming Run", 20, Color.WHITE);

            // NEW GAME button (duh)
            if(window.drawClearMenuButton(20, window.windowHeight - 20 - ((menuButtonHeight + menuButtonSpacing) * 4), menuButtonWidth, menuButtonHeight, menuButtonTextSize, "NEW GAME", Color.WHITE, Color.LIGHT_GRAY))
                inTitleScreen = false;

            // QUIT button (duh)
            if(window.drawClearMenuButton(20, window.windowHeight - 20 - ((menuButtonHeight + menuButtonSpacing) * 3), menuButtonWidth, menuButtonHeight, menuButtonTextSize, "QUIT GAME", Color.WHITE, Color.LIGHT_GRAY))
                System.exit(0);

            window.repaint();
            Thread.sleep(1000 / 30); // FPS cap needed in menus too
        }*/

        // Create the player object and preload the player sprite
        /*debug("Loading player...");
        WorldItem playerSprite = new CollisionItem(window.resizeImage(ImageIO.read(fileLoader.load("/res/player/static/BStatic.png")), 118, 118));
        playerSprite.x = 360;
        playerSprite.y = 1800;
        playerSprite.isometricItem = true;
        window.effectLayer.add(playerSprite);

        playerSprite.isometricSprites[WorldItem.SW]= window.resizeImage(ImageIO.read(fileLoader.load("/res/player/static/FLStatic.png")), 118, 118);
        playerSprite.isometricSprites[WorldItem.S]= window.resizeImage(ImageIO.read(fileLoader.load("/res/player/static/FStatic.png")), 118, 118);
        playerSprite.isometricSprites[WorldItem.SE]= window.resizeImage(ImageIO.read(fileLoader.load("/res/player/static/FRStatic.png")), 118, 118);
        playerSprite.isometricSprites[WorldItem.E]= window.resizeImage(ImageIO.read(fileLoader.load("/res/player/static/RStatic.png")), 118, 118);
        playerSprite.isometricSprites[WorldItem.NE]= window.resizeImage(ImageIO.read(fileLoader.load("/res/player/static/BRStatic.png")), 118, 118);
        playerSprite.isometricSprites[WorldItem.N]= window.resizeImage(ImageIO.read(fileLoader.load("/res/player/static/BStatic.png")), 118, 118);
        playerSprite.isometricSprites[WorldItem.NW]= window.resizeImage(ImageIO.read(fileLoader.load("/res/player/static/BLStatic.png")), 118, 118);
        playerSprite.isometricSprites[WorldItem.W]= window.resizeImage(ImageIO.read(fileLoader.load("/res/player/static/LStatic.png")), 118, 118);

        CollisionItem player = new CollisionItem(30, 20, Color.BLACK);
        player.x = 401;
        player.y = 1875;

        // Load in first level
        debug("Loading first level contents...");
        Outside outside = new Outside(window, linegen, fileLoader);
        outside.create();

        // Terminal Hitbox
        CollisionItem terminal = new CollisionItem(64,64, Color.RED);
        terminal.x = 560;
        terminal.y = 1645;
        boolean terminalSolved = false;

        // Door Loading Zone
        CollisionItem outsideDoor = new CollisionItem(100, 50, Color.RED);
        outsideDoor.x = 400;
        outsideDoor.y = 1600;

        window.renderBackground();

        WorldItem terminalSprite = null;
        terminalSprite = new WorldItem(fileLoader.load("/res/ui/terminal.png"));
        terminalSprite.sprite = window.resizeImage(terminalSprite.sprite, 1136, 640);

        // Start the game loop
        debug("Loading finished. Entering game loop...");

        boolean gameRunning = true;
        int currentLevel = 0;
        boolean freecam = false;

        nextFrameTime = System.nanoTime();
        frameLength = 16700000;

        while(gameRunning) {
            // Frame limiter
            while(System.nanoTime() < nextFrameTime) {}
            nextFrameTime = System.nanoTime() + frameLength;

            window.clear();

            // Movement Code
            float moveX = 0;
            float moveY = 0;

            if(window.keyListener.KEY_LEFT) moveX -= 5;
            if(window.keyListener.KEY_RIGHT) moveX += 5;
            if(window.keyListener.KEY_UP) moveY -= 5;
            if(window.keyListener.KEY_DOWN) moveY += 5;

            playerSprite.lookTowards((int) moveX, (int) moveY);

            //moveX = moveX * window.delta;
            //moveY = moveY * window.delta;

            if(window.keyListener.KEY_FREECAM) {
                Thread.sleep(200);
                debug("Toggling freecam...");
                freecam = !freecam;
            }

            if(freecam) {
                window.cameraX += moveX;
                window.cameraY += moveY;
            } else {
                if(enablePlayerMovement) {
                    ArrayList<CollisionItem> tempList = new ArrayList<CollisionItem>();
                    tempList.addAll(window.collisionItemLayer);
                    tempList.addAll(window.hiddenCollisionItemLayer);
                    player.moveAndCollide(moveX, moveY, tempList);
                    window.centerCamera(playerSprite);
                }
            }

            // Level-specific code
            if(currentLevel == 0) {
                if(window.keyListener.KEY_ACTION) {
                    if(terminalSolved && player.collidingWith(outsideDoor)) {
                        System.exit(0);
                    }

                    if(!terminalSolved && player.collidingWith(terminal)) {
                        // Terminal level
                        while(!terminalSolved) {
                            window.drawRectangle(0, 0, window.windowWidth, window.windowHeight, Color.BLACK); // Black background

                            Graphics2D graphics = window.frameBuffer.createGraphics();
                            graphics.drawImage(terminalSprite.sprite, 0, 0, window);
                            graphics.dispose();
                            if(window.drawMenuButton(501, 300, 50, 15, "", Color.WHITE, new Color(0, 0, 0), new Color(5, 5, 5))) {
                                terminalSolved = true;
                                break;
                            }
                            window.repaint();
                            Thread.sleep(1000 / 15);
                        }
                    }
                }
            }

            // Pause menu
            if(window.keyListener.KEY_MENU) {
                while(true) {
                    window.drawRectangle(0, 0, window.windowWidth, window.windowHeight, new Color(0,0,0)); // Black background

                    if(window.drawMenuButton((window.windowWidth / 2) - (menuButtonWidth / 2), window.windowHeight - 400, menuButtonWidth, menuButtonHeight, "RESUME", Color.WHITE, new Color(255, 66, 28), new Color(255, 91, 59))) {
                        break;
                    }

                    if(window.drawMenuButton((window.windowWidth / 2) - (menuButtonWidth / 2), window.windowHeight - 280, menuButtonWidth, menuButtonHeight, "QUIT GAME", Color.WHITE, new Color(255, 66, 28), new Color(255, 91, 59))) {
                        System.exit(0);
                    }

                    window.repaint();
                    Thread.sleep(1000 / 60);
                }
            }

            // Print debug information
            if(DEBUG_BUILD && DEBUG_LEVEL == 0)
                System.out.println("FPS: " + Integer.toString((int) window.FPS) + ", Delta: " + Float.toString(window.delta) +
                        ", X: " + Integer.toString((int) player.x) + ", Y: " + Integer.toString((int) player.y));

            // Drawing Code
            playerSprite.x = player.x - 41;
            playerSprite.y = player.y - 71;
            window.drawLayers();
            window.repaint();
        }
        System.exit(0);*/
    }
}
