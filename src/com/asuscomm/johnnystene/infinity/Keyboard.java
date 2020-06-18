/*
 * Keyboard.java
 * 
 * Manages the keyboard. This is almost certainly not the
 * most efficient way to do keyboard management, but I'm
 * quite frankly sick of Java's shit.
 */

package com.asuscomm.johnnystene.infinity;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Keyboard extends KeyAdapter {
	public boolean KEY_UP = false;
	public boolean KEY_DOWN = false;
	public boolean KEY_LEFT = false;
	public boolean KEY_RIGHT = false;
	public boolean KEY_ACTION = false;
	public boolean KEY_MENU = false;
	public boolean KEY_FREECAM = false;
	
	public int KEYBIND_UP = KeyEvent.VK_W;
	public int KEYBIND_DOWN = KeyEvent.VK_S;
	public int KEYBIND_LEFT = KeyEvent.VK_A;
	public int KEYBIND_RIGHT = KeyEvent.VK_D;
	public int KEYBIND_ACTION = KeyEvent.VK_E;
	public int KEYBIND_MENU = KeyEvent.VK_ESCAPE;
	public int KEYBIND_FREECAM = KeyEvent.VK_U;
	
	public void keyPressed(KeyEvent event) {
		int keycode = event.getKeyCode();
		if(keycode == KEYBIND_UP) KEY_UP = true;
		if(keycode == KEYBIND_DOWN) KEY_DOWN = true;
		if(keycode == KEYBIND_LEFT) KEY_LEFT = true;
		if(keycode == KEYBIND_RIGHT) KEY_RIGHT = true;
		if(keycode == KEYBIND_ACTION) KEY_ACTION = true;
		if(keycode == KEYBIND_MENU) KEY_MENU = true;
		if(keycode == KEYBIND_FREECAM) KEY_FREECAM = true;
	}
	
	public void keyReleased(KeyEvent event) {
		int keycode = event.getKeyCode();
		if(keycode == KEYBIND_UP) KEY_UP = false;
		if(keycode == KEYBIND_DOWN) KEY_DOWN = false;
		if(keycode == KEYBIND_LEFT) KEY_LEFT = false;
		if(keycode == KEYBIND_RIGHT) KEY_RIGHT = false;
		if(keycode == KEYBIND_ACTION) KEY_ACTION = false;
		if(keycode == KEYBIND_MENU) KEY_MENU = false;
		if(keycode == KEYBIND_FREECAM) KEY_FREECAM = false;
	}
}