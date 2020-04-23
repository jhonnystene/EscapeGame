package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;

public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        
        // Config window shown on start up?
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

    @Override
    public void simpleInitApp() {
        
        // Disable fly cam
        this.flyCam.setEnabled(false);
        
        // Debug info and FPS Shown on start up?
        this.setDisplayFps(false);
        this.setDisplayStatView(false);
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
