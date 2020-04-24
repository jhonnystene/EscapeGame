package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;

import mygame.Keybinds;

public class Main extends SimpleApplication {
    Node pickables;
    int playerNormalSpeed = 10;
    int playerSpeed = 10;
    int playerSprintSpeed = 20;
    
    public static void main(String[] args) {
        Main app = new Main();
        app.showSettings = false;
        
        // Create a new app settings loaded with defaults
        AppSettings appSettings = new AppSettings(true);
        
        // Game resolution
        appSettings.put("Width",720);
        appSettings.put("Height",480);
        
        // Game title
        appSettings.put("Title", "Escape Game");
        
        app.setSettings(appSettings);
        app.start();
    }
        
    protected Geometry player;
    
    // Init game
    @Override
    public void simpleInitApp() {
        initPickups();
        Keybinds keybinds = new Keybinds();
        keybinds.initKeybinds(inputManager, actionListener, analogListener);
        
        // Disable debug and fps - Press F5 to re-enable in game if you really want
        setDisplayStatView(false); 
        setDisplayFps(false); 
        
        this.flyCam.setMoveSpeed(10); // Raise camera speed to an acceptable level
        
        // Creates test player
        Box b = new Box(1, 1, 1);
        player = new Geometry("Player", b);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Red);
        player.setMaterial(mat);
        rootNode.attachChild(player);
        
        // Create test box my guy
        Geometry box = new Geometry("Box", new Box(1, 1, 1));
        Material boxMaterial = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        boxMaterial.setTexture("ColorMap", assetManager.loadTexture("Textures/Terrain/splat/dirt.jpg"));
        box.setMaterial(boxMaterial);
        
        // Make box respond to raycasting
        // Do the same thing for other objects to make it so they can be picked up
        pickables.attachChild(box);
    }
    
    // Function for initializing pickups and crosshair and stuff
    // Written by Johnny - Don't touch if you don't understand it
    private void initPickups() {
        pickables = new Node(); // Java!
        rootNode.attachChild(pickables);
        
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt"); // Load a font
        
        // Create a text object that says +
        BitmapText ch = new BitmapText(guiFont, false);
        ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        ch.setText("+"); // crosshairs
        ch.setLocalTranslation( // center
        settings.getWidth() / 2 - ch.getLineWidth()/2,
        settings.getHeight() / 2 + ch.getLineHeight()/2, 0);
        
        guiNode.attachChild(ch); // Throw it in the center
    }
    
    // Action input
    private final ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
            if(name.equals("Action") && !keyPressed) { // LMB
                // Written by Johnny - Don't touch if you don't understand it
                // Oh boy, do I hate raycasting!
                CollisionResults results = new CollisionResults();
                Ray ray = new Ray(cam.getLocation(), cam.getDirection());
                pickables.collideWith(ray, results); // Actually cast the ray
                for(int i = 0; i < results.size(); i ++) { // Loop over all items the ray hit
                    float dist = results.getCollision(i).getDistance();
                    Vector3f pt = results.getCollision(i).getContactPoint();
                    String hit = results.getCollision(i).getGeometry().getName();
                    System.out.println("DEBUG: Raycast hit " + hit + " at " + pt + " " + dist + " wu away.");
                }
                
                if(results.size() > 0) {
                    CollisionResult closest = results.getClosestCollision();
                    // TODO: Make item picked up follow camera, Portal-style
                }
            }
            if(name.equals("Sprint")) {
                if(keyPressed) playerSpeed = playerSprintSpeed;
                else playerSpeed = playerNormalSpeed;
            }
        }
    };
    
    // Movement input
    private final AnalogListener analogListener = new AnalogListener() {
        @Override
        public void onAnalog(String name, float value, float tpf) {
            // Written by Ethan S
            if (name.equals("Right")) {
                Vector3f v = player.getLocalTranslation();
                player.setLocalTranslation(v.x + value * playerSpeed, v.y, v.z);
            }
            if (name.equals("Left")) {
                Vector3f v = player.getLocalTranslation();
                player.setLocalTranslation(v.x - value * playerSpeed, v.y, v.z);
            }
            if (name.equals("Forward")) {
                Vector3f v = player.getLocalTranslation();
                player.setLocalTranslation(v.x, v.y, v.z - value * playerSpeed);
            }
            if (name.equals("Backward")) {
                Vector3f v = player.getLocalTranslation();
                player.setLocalTranslation(v.x, v.y, v.z  + value * playerSpeed);
            }
        }
    };    
}