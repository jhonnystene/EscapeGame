package com.asuscomm.johnnystene.escape;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Window extends JFrame {
	public BufferedImage frameBuffer;
	
	public Keyboard keyboard;
	public Mouse mouse;
	public Camera camera;
	
	public int width;
	public int height;
	public String title;
	
	public Window(String t) {
		super();
		init(t, 800, 600);
	}
	
	public Window(String t, int w, int h) {
		super();
		init(t, w, h);
	}
	
	private void init(String t, int w, int h) {
		// Set basic window params
		title = t;
		setTitle(title);
		
		width = w;
		height = h;
		setSize(width, height);
		
		// Init submodules
		keyboard = new Keyboard();
		mouse = new Mouse();
		camera = new Camera();
		
		addKeyListener(keyboard);
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		
		// Initialize image buffers
		frameBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		// Do JFrame things
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void paint(Graphics g) {
		g.drawImage(frameBuffer, 0, 0, this);
	}
	
	public void setNewSize(int w, int h) {
		width = w;
		height = h;
		setSize(width, height);
	}
}
