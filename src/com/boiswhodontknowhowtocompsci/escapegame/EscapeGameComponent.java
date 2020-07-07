package com.boiswhodontknowhowtocompsci.escapegame;

import net.ddns.johnnystene.infinitytoolkit.engine.Game;
import net.ddns.johnnystene.infinitytoolkit.engine.World;
import net.ddns.johnnystene.infinitytoolkit.engine.WorldItem;

public class EscapeGameComponent {
    public static void main(String[] args) {
        // Setup player
        WorldItem player = new WorldItem("/res/player/static/S.png");
        player.loadSprite("/res/player/static/N.png", WorldItem.N);
        player.loadSprite("/res/player/static/NE.png", WorldItem.NE);
        player.loadSprite("/res/player/static/E.png", WorldItem.E);
        player.loadSprite("/res/player/static/SE.png", WorldItem.SE);
        player.loadSprite("/res/player/static/S.png", WorldItem.S);
        player.loadSprite("/res/player/static/SW.png", WorldItem.SW);
        player.loadSprite("/res/player/static/W.png", WorldItem.W);
        player.loadSprite("/res/player/static/NW.png", WorldItem.NW);
        player.isometricItem = true;
        player.resize(118, 118);
        player.x = 360;
        player.y = 1800;
        player.hitboxX = 40;
        player.hitboxWidth = 30;
        player.hitboxY = 80;
        player.hitboxHeight = 20;

        // Setup all levels
        World outsideMap = new World("/res/old/maps/Outside-Final.png");
        outsideMap.loadMapFile("/res/old/maps/collision/outside.map");
        outsideMap.items.add(player);
        boolean outsideMapKeypadSolved = false;

        World hallwayMap = new World("/res/old/maps/Hallway-B11-Programmer-Art.png");
        hallwayMap.loadMapFile("/res/old/maps/collision/hallway.map");
        hallwayMap.items.add(player);

        World upstairsMap = new World("/res/old/maps/Upstairs.png");
        upstairsMap.loadMapFile("/res/old/maps/collision/upstairs.map");
        upstairsMap.items.add(player);

        World coreMap = new World("/res/old/maps/Core.png");
        coreMap.loadMapFile("/res/old/maps/collision/core.map");
        coreMap.items.add(player);

        World currentMap = outsideMap;
        int currentMapId = 0;

        Game game = new Game("Escape The Robots And Oh Gosh They're Coming Run", 800, 600) {
            @Override
            public void doTick() {
                if(window.keyboard.KEY_ESCAPE) System.exit(0);

                float moveX = 0;
                float moveY = 0;
                if(window.keyboard.KEY_W) moveY -= 10;
                if(window.keyboard.KEY_S) moveY += 10;
                if(window.keyboard.KEY_A) moveX -= 10;
                if(window.keyboard.KEY_D) moveX += 10;

                player.moveAndCollide(moveX, moveY, currentMap.items);
                window.centerCamera(player);
                currentMap.renderTo(window.createGraphics(), window.camera.x, window.camera.y, window.width, window.height, true);
            }
        };

        game.start();
    }
}
