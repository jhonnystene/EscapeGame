package mygame;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;

public class Keybinds {
    // Function for initializing keybinds
    public void initKeybinds(InputManager inputManager, ActionListener actionListener, AnalogListener analogListener) {
        inputManager.addMapping("Action", new KeyTrigger(KeyInput.KEY_E), new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("Forward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Backward", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Sprint", new KeyTrigger(KeyInput.KEY_SPACE));
        
        // Digital Listeners
        inputManager.addListener(actionListener, "Action");
        inputManager.addListener(actionListener, "Sprint");
        
        // Analog Listeners
        inputManager.addListener(analogListener, "Forward", "Backward");
        inputManager.addListener(analogListener, "Left", "Right");
        inputManager.addListener(analogListener, "RotateLeft");
    }
}