package com.boiswhodontknowhowtocompsci.escapegame;

import net.ddns.johnnystene.infinitytoolkit.engine.AnimatedSprite;
import net.ddns.johnnystene.infinitytoolkit.engine.Game;
import net.ddns.johnnystene.infinitytoolkit.engine.World;
import net.ddns.johnnystene.infinitytoolkit.engine.WorldItem;

import java.awt.*;

public class EscapeGameComponent extends Game {
    public WorldItem player;

    public World outsideMap;
    public boolean outsideMapKeypadSolved;

    public World hallwayMap;

    public World upstairsMap;

    public World coreMap;

    public World currentMap;
    public int currentMapId;

    public AnimatedSprite[] playerIdleSprites = new AnimatedSprite[8];
    public AnimatedSprite[] playerWalkSprites = new AnimatedSprite[8];

    public EscapeGameComponent() {
        super("Escape Game", 800, 600);
        // Setup player
        playerIdleSprites[WorldItem.N] = new AnimatedSprite("/res/player/Idle/Back");
        playerIdleSprites[WorldItem.NE] = new AnimatedSprite("/res/player/Idle/Back-Right");
        playerIdleSprites[WorldItem.NW] = new AnimatedSprite("/res/player/Idle/Back-Left");
        playerIdleSprites[WorldItem.E] = new AnimatedSprite("/res/player/Idle/Right");
        playerIdleSprites[WorldItem.W] = new AnimatedSprite("/res/player/Idle/Left");
        playerIdleSprites[WorldItem.SE] = new AnimatedSprite("/res/player/Idle/Front-Right");
        playerIdleSprites[WorldItem.S] = new AnimatedSprite("/res/player/Idle/Front");
        playerIdleSprites[WorldItem.SW] = new AnimatedSprite("/res/player/Idle/Front-Left");
        for(AnimatedSprite sprite : playerIdleSprites) {
            sprite.frameInterval = 5;
            sprite.resize(118, 118);
        }

        playerWalkSprites[WorldItem.N] = new AnimatedSprite("/res/player/Walk/Back");
        playerWalkSprites[WorldItem.NE] = new AnimatedSprite("/res/player/Walk/Back-Right");
        playerWalkSprites[WorldItem.NW] = new AnimatedSprite("/res/player/Walk/Back-Left");
        playerWalkSprites[WorldItem.E] = new AnimatedSprite("/res/player/Walk/Right");
        playerWalkSprites[WorldItem.W] = new AnimatedSprite("/res/player/Walk/Left");
        playerWalkSprites[WorldItem.SE] = new AnimatedSprite("/res/player/Walk/Front-Right");
        playerWalkSprites[WorldItem.S] = new AnimatedSprite("/res/player/Walk/Front");
        playerWalkSprites[WorldItem.SW] = new AnimatedSprite("/res/player/Walk/Front-Left");
        for(AnimatedSprite sprite : playerWalkSprites) {
            sprite.frameInterval = 5;
            sprite.resize(118, 118);
        }

        player = new WorldItem("/res/player/static/S.png");
        player.isometricSprites = playerWalkSprites;
        player.isometricItem = true;
        player.resize(118, 118);
        player.x = 360;
        player.y = 1800;
        player.hitboxX = 40;
        player.hitboxWidth = 30;
        player.hitboxY = 80;
        player.hitboxHeight = 20;

        // Setup all levels
        outsideMap = new World("/res/old/maps/Outside-Final.png");
        outsideMap.loadMapFile("/res/old/maps/collision/outside.map");
        outsideMap.items.add(player);
        outsideMapKeypadSolved = false;

        hallwayMap = new World("/res/old/maps/Hallway-B11-Programmer-Art.png");
        hallwayMap.loadMapFile("/res/old/maps/collision/hallway.map");
        hallwayMap.items.add(player);

        upstairsMap = new World("/res/old/maps/Upstairs.png");
        upstairsMap.loadMapFile("/res/old/maps/collision/upstairs.map");
        upstairsMap.items.add(player);

        coreMap = new World("/res/old/maps/Core.png");
        coreMap.loadMapFile("/res/old/maps/collision/core.map");
        coreMap.items.add(player);

        currentMap = outsideMap;
        currentMapId = 0;
    }

    @Override
    public void doTick() {
        for(AnimatedSprite sprite : playerIdleSprites) {
            sprite.update();
        }
        for(AnimatedSprite sprite : playerWalkSprites) {
            sprite.update();
        }
        if(window.keyboard.KEY_ESCAPE) System.exit(0);

        float moveX = 0;
        float moveY = 0;
        if(window.keyboard.KEY_W) moveY -= 10;
        if(window.keyboard.KEY_S) moveY += 10;
        if(window.keyboard.KEY_A) moveX -= 10;
        if(window.keyboard.KEY_D) moveX += 10;

        if(moveX != 0) player.isometricSprites = playerWalkSprites;
        else if(moveY != 0) player.isometricSprites = playerWalkSprites;
        else {
            player.isometricSprites = playerIdleSprites;
            player.pointInDirection(player.direction);
        }

        player.moveAndCollide(moveX, moveY, currentMap.items);
        window.centerCamera(player);

        /*
         * MAP IDS LIST
         * 0 - Outside building (Goes to Main Hallway)
         * 1 - Main Hallway (Goes to Upstairs or Keypad Hallway)
         * 2 - Upstairs (Goes to Vents)
         * 3 - Secondary (Keypad) Hallway (Goes to Core)
         * 4 - Unused
         * 5 - Core
         */

        if (currentMapId == 0) { // Outside
            /*
             * Map concept:
             * Make the password the year the game takes place,
             * and as a hint have the needed buttons look worn down.
             */
            if (outsideMapKeypadSolved) {
                if (player.inArea(400, 1600, 100, 64) && window.keyboard.KEY_E) {
                    player.x = 415;
                    player.y = 1450;
                    currentMap = hallwayMap;
                    currentMapId = 1;
                    window.centerCamera(player);
                }
            } else {
                if (player.inArea(560, 1640, 64, 64) && window.keyboard.KEY_E) {
                    // Temporary art for puzzle
                    String userEntered = "";
                    while (!outsideMapKeypadSolved) {
                        if(window.mouse.down) {
                            window.finishedDrawing = true;
                            doNothing();
                        } else {
                            while (window.finishedDrawing) {
                                doNothing();
                            }
                            window.UIDrawFilledRect(0, 0, window.width, window.height, Color.BLACK);

                            // Keypad placement
                            if (window.UIDrawButton((window.width / 2) - 100, (window.height / 2) - 100, 50, 50, 20, Color.GRAY, Color.LIGHT_GRAY, Color.BLACK, "1", true))
                                userEntered += "1";
                            if (window.UIDrawButton((window.width / 2) - 25, (window.height / 2) - 100, 50, 50, 20, Color.GRAY, Color.LIGHT_GRAY, Color.BLACK, "2", true))
                                userEntered += "2";
                            if (window.UIDrawButton((window.width / 2) + 50, (window.height / 2) - 100, 50, 50, 20, Color.GRAY, Color.LIGHT_GRAY, Color.BLACK, "3", true))
                                userEntered += "3";

                            if (window.UIDrawButton((window.width / 2) - 100, (window.height / 2) - 25, 50, 50, 20, Color.GRAY, Color.LIGHT_GRAY, Color.BLACK, "4", true))
                                userEntered += "4";
                            if (window.UIDrawButton((window.width / 2) - 25, (window.height / 2) - 25, 50, 50, 20, Color.GRAY, Color.LIGHT_GRAY, Color.BLACK, "5", true))
                                userEntered += "5";
                            if (window.UIDrawButton((window.width / 2) + 50, (window.height / 2) - 25, 50, 50, 20, Color.GRAY, Color.LIGHT_GRAY, Color.BLACK, "6", true))
                                userEntered += "6";

                            if (window.UIDrawButton((window.width / 2) - 100, (window.height / 2) + 50, 50, 50, 20, Color.GRAY, Color.LIGHT_GRAY, Color.BLACK, "7", true))
                                userEntered += "7";
                            if (window.UIDrawButton((window.width / 2) - 25, (window.height / 2) + 50, 50, 50, 20, Color.GRAY, Color.LIGHT_GRAY, Color.BLACK, "8", true))
                                userEntered += "8";
                            if (window.UIDrawButton((window.width / 2) + 50, (window.height / 2) + 50, 50, 50, 20, Color.GRAY, Color.LIGHT_GRAY, Color.BLACK, "9", true))
                                userEntered += "9";

                            if (window.UIDrawButton((window.width / 2) - 25, (window.height / 2) + 125, 50, 50, 20, Color.GRAY, Color.LIGHT_GRAY, Color.BLACK, "0", true))
                                userEntered += "0";

                            if (window.UIDrawButton((window.width / 2) + 50, (window.height / 2) + 125, 50, 50, 20, Color.GRAY, Color.LIGHT_GRAY, Color.BLACK, "Done", true)) {
                                // TODO: Glow an LED red or green
                                if (userEntered.equals("1234")) {
                                    outsideMapKeypadSolved = true;
                                } else userEntered = "";
                            }

                            window.finishedDrawing = true;
                        }
                    }
                }
            }
        } else if (currentMapId == 1) { // Hallway
            /*
             * Map concept:
             * Have an elevator leading upstairs, and a locked door leading to the core hallway. The player needs
             * to get the screwdriver and wire cutters from upstairs to unscrew and rewire the panel.
             */

            if (player.inArea(382, 1368, 74, 32) && window.keyboard.KEY_E) { // Locked door

            }

            if (player.inArea(1095, 947, 142, 32) && window.keyboard.KEY_E) { // Upstairs elevator
                player.x = 996;
                player.y = 1044;
                currentMap = upstairsMap;
                currentMapId = 2;
                window.centerCamera(player);
            }
        } else if (currentMapId == 2) { // Upstairs
            /*
             * Map concept:
             * Rather than a big-ass laser, make it a workshop with lots of various tools the player
             * can pick up and use. Make sure there are useless ones, as well as a screwdriver and side cutters.
             */
            if (player.inArea(823, 1270, 480, 214) && window.keyboard.KEY_E) { // Go back to hallway
                player.x = 1139;
                player.y = 900;
                currentMap = hallwayMap;
                currentMapId = 1;
                window.centerCamera(player);
            }

            // 823 1270 1253 1484
        } else if (currentMapId == 3) { // Second hallway (Placeholder)
            /*
             * Map concept:
             * Leads directly to the core.
             */
        } else if (currentMapId == 4) { // Free
            /*
             * Map concept:
             * Free Map ID (Removed map)
             */
        } else if (currentMapId == 5) { // Core
            /*
             * Keep mostly the same but start a timer to escape. Have the hallway door re-lock itself so the
             * player can't get out.
             */
        }

        currentMap.renderTo(window.createGraphics(), window.camera.x, window.camera.y, window.width, window.height, true);
    }

    public static void main(String[] args) {
        EscapeGameComponent game = new EscapeGameComponent();
        game.start();
    }
}
