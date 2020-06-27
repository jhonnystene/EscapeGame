package com.asuscomm.johnnystene.escape;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.*;

public class Window extends JPanel {
	public BufferedImage frameBuffer;
	public JFrame window;


	public Keyboard keyboard;
	public Mouse mouse;
	public Camera camera;
	
	public int width;
	public int height;
	public String title;
	
	public long nextFrameTime = 0;
	public int frameGap = 0;
	
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
		window = new JFrame();
		title = t;
		window.setTitle(title);
		
		width = w;
		height = h;
		window.setPreferredSize(new Dimension(width, height));
		setSize(width, height);
		
		// Init submodules
		keyboard = new Keyboard();
		mouse = new Mouse();
		camera = new Camera();
		
		window.addKeyListener(keyboard);
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		
		// Initialize image buffers
		frameBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		// Do JFrame things
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLocationRelativeTo(null);
		window.add(this);
		window.pack();
		window.setVisible(true);

		width = getWidth();
		height = getHeight();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		while(System.nanoTime() < nextFrameTime) {}
		g.drawImage(frameBuffer, 0, 0, this);
		nextFrameTime = System.nanoTime() + frameGap;
	}
	
	public void setFPSCap(int fps) {
		frameGap = (1/fps)*1000000000;
	}
	
	public void setNewSize(int w, int h) {
		width = w;
		height = h;
		setSize(width, height);
	}
	
	public void centerCamera(WorldItem item) {
		camera.x = (int) item.x - ((width / 2) - (item.width / 2));
		camera.y = (int) item.y - ((height / 2) - (item.height / 2));
		
		if(camera.x < 0) camera.x = 0;
		if(camera.y < 0) camera.y = 0;
		if(camera.x + width > 10240) camera.x = 10240 - width;
		if(camera.y + height > 10240) camera.y = 10240 - height;
	}

	public boolean mouseIn(int x, int y, int w, int h) {
		if(mouse.x > x && mouse.x < x + w && mouse.y > y && mouse.y < y + h)
			return true;

		return false;
	}

	public int mouseStatus(int x, int y, int w, int h) {
		if(mouseIn(x, y, w, h)) {
			if(mouse.down) return 2;
			else return 1;
		} else return 0;
	}

	// UI Framework
	public int UIDrawRect(int x, int y, int w, int h, Color color) {
		Graphics2D graphics = frameBuffer.createGraphics();
		graphics.setColor(color);
		graphics.drawRect(x, y, w, h);
		graphics.dispose();
		return mouseStatus(x, y, w, h);
	}

	public int UIDrawRect(int x, int y, int w, int h, Color color, Color hoverColor) {
		Graphics2D graphics = frameBuffer.createGraphics();
		if(mouseStatus(x, y, w, h) == 1) {
			graphics.setColor(hoverColor);
		} else graphics.setColor(color);
		graphics.drawRect(x, y, w, h);
		graphics.dispose();

		return mouseStatus(x, y, w, h);
	}

	public int UIDrawFilledRect(int x, int y, int w, int h, Color color) {
		Graphics2D graphics = frameBuffer.createGraphics();
		graphics.setColor(color);
		graphics.fillRect(x, y, w, h);
		graphics.dispose();
		return mouseStatus(x, y, w, h);
	}

	public int UIDrawFilledRect(int x, int y, int w, int h, Color color, Color hoverColor) {
		Graphics2D graphics = frameBuffer.createGraphics();
		if(mouseStatus(x, y, w, h) == 1) {
			graphics.setColor(hoverColor);
		} else graphics.setColor(color);
		graphics.fillRect(x, y, w, h);
		graphics.dispose();

		return mouseStatus(x, y, w, h);
	}

	public int UIDrawString(int x, int y, int size, String string) {
		Font font = new Font("Sans Serif", Font.PLAIN, size);
		Graphics2D graphics = frameBuffer.createGraphics();
		FontMetrics metrics = graphics.getFontMetrics(font);
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		graphics.setFont(font);
		graphics.drawString(string, x, y);
		graphics.dispose();
		return mouseStatus(x, y, metrics.stringWidth(string), metrics.getHeight());
	}

	public int UIDrawString(int x, int y, int size, String string, Color color) {
		Font font = new Font("Sans Serif", Font.PLAIN, size);
		Graphics2D graphics = frameBuffer.createGraphics();
		FontMetrics metrics = graphics.getFontMetrics(font);
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		graphics.setColor(color);
		graphics.setFont(font);
		graphics.drawString(string, x, y);
		graphics.dispose();
		return mouseStatus(x, y, metrics.stringWidth(string), metrics.getHeight());
	}

	public int UIDrawCenteredString(int x, int y, int size, String string) {
		Font font = new Font("Sans Serif", Font.PLAIN, size);
		Graphics2D graphics = frameBuffer.createGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		FontMetrics metrics = graphics.getFontMetrics(font);
		int width = metrics.stringWidth(string);
		int height = metrics.getHeight();
		x = x - (width / 2);
		y = y - (height / 2);
		graphics.setFont(font);
		graphics.drawString(string, x, y);
		graphics.dispose();
		return mouseStatus(x, y, width, height);
	}

	public int UIDrawCenteredString(int x, int y, int size, String string, Color color) {
		Font font = new Font("Sans Serif", Font.PLAIN, size);
		Graphics2D graphics = frameBuffer.createGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		FontMetrics metrics = graphics.getFontMetrics(font);
		Rectangle2D dimensions = metrics.getStringBounds(string, graphics);
		int width = (int) dimensions.getWidth();
		int height = (int) dimensions.getHeight();
		x = x - (width / 2);
		y = y + metrics.getDescent();
		graphics.setFont(font);
		graphics.setColor(color);
		graphics.drawString(string, x, y);
		graphics.dispose();
		return mouseStatus(x, y, width, height);
	}

	public boolean UIDrawButton(int x, int y, int w, int h, int fontSize, Color backColor, Color hoverColor, Color textColor, String text) {
		if(UIDrawFilledRect(x, y, w, h, backColor, hoverColor) == 2) {
			return true;
		}

		UIDrawCenteredString((x + (w / 2)), (y + (h / 2)), fontSize, text, textColor);
		return false;
	}
}
