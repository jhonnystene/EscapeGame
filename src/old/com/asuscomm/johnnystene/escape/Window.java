/*
 * Window.java
 * 
 * This handles most of the actual game logic.
 * TODO: Fix that.
 */

package old.com.asuscomm.johnnystene.escape;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Window extends JFrame implements MouseListener, MouseMotionListener {
	public Clip musicPlayer; // BG Music
	
	// Render layers
	public ArrayList<WorldItem> backgroundLayer;
	public ArrayList<CollisionItem> collisionItemLayer;
	public ArrayList<CollisionItem> hiddenCollisionItemLayer;
	public ArrayList<WorldItem> effectLayer;
	
	public BufferedImage renderedBackground; // Used to improve performance
	public BufferedImage frameBuffer; // Screen is drawn here
	public Keyboard keyListener; // Listens for keyboard inputs
	
	// Window size
	public int windowWidth = 0; 
	public int windowHeight = 0; 
	
	// Mouse properties
	public int mouseX = -1;
	public int mouseY = -1;
	public boolean mouseDown = false;
	
	// Camera properties
	public int cameraX = 0;
	public int cameraY = 0;
	public boolean enableCamera = true;
	
	public long lastFrameTime = 0; // Used for calculating FPS and delta time
	public float delta = 0;
	public float FPS = 0;
	
	private Toolkit t;
	
	public Window(int width, int height, String name) throws LineUnavailableException {
		super(); // Create JFrame
		
		// Set window size
		windowWidth = width;
		windowHeight = height;
		setSize(width, height); // Set window size
		
		// Setup mouse listening
		addMouseListener(this);
		addMouseMotionListener(this);
		
		// Setup keyboard listening
		keyListener = new Keyboard();
		addKeyListener(keyListener);
		
		// Initialize framebuffer
		frameBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); // Create image for framebuffer
		
		// Initialize pre-render of background
		renderedBackground = new BufferedImage(10240, 10240, BufferedImage.TYPE_INT_RGB);
		
		// Setup JFrame stuff
		setTitle(name); // Set window title
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Terminate the game on window close
		setLocationRelativeTo(null); // Center window
		setVisible(true); // Make window visible
		
		// Initialize music system
		musicPlayer = AudioSystem.getClip();
		
		// Initialize layers
		backgroundLayer = new ArrayList<WorldItem>();
		collisionItemLayer = new ArrayList<CollisionItem>();
		hiddenCollisionItemLayer = new ArrayList<CollisionItem>();
		effectLayer = new ArrayList<WorldItem>();
		
		t = Toolkit.getDefaultToolkit();
		
		lastFrameTime = System.nanoTime(); // It was a bitch figuring out i needed nanos rather than millis
	}
	
	public void clear() {
		Raster newFB = renderedBackground.getData(new Rectangle(cameraX, cameraY, windowWidth, windowHeight)).createTranslatedChild(0, 0);
		frameBuffer.setData(newFB);
	}
	
	public void paint(Graphics g) {
		try {
			t.sync();
		} catch(NullPointerException e) {
			// Do nothing -this line fails when the game is first initializing and it's quite annoying
		}
		g.drawImage(frameBuffer, 0, 0, this); // Draw the framebuffer on the screen
		
		// Calculate FPS and delta
		long currentTime = System.nanoTime();
		FPS = 1000000000 / (currentTime - lastFrameTime);
		delta = FPS / 100;
		lastFrameTime = currentTime;
	}

	public void fill(Color color) {
		drawRectangle(0, 0, windowWidth, windowHeight, color);
	}

	public void repaintAndWait(int millis) throws InterruptedException {
		repaint();
		Thread.sleep(millis);
	}

	/*
	 * METHODS FOR DRAWING ITEMS
	 */
	public void drawWorldItem(WorldItem item) {
		Graphics2D graphics = frameBuffer.createGraphics();
		if(enableCamera)
			graphics.drawImage(item.sprite, (int) item.x - cameraX, (int) item.y - cameraY, this);
		else
			graphics.drawImage(item.sprite, (int) item.x, (int) item.y, this);
		graphics.dispose();
	}
	
	public void drawWorldItems(WorldItem[] items) {
		for(WorldItem item : items) {
			drawWorldItem(item);
		}
	}
	
	public void renderBackground() {
		Graphics2D graphics = renderedBackground.createGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, 10240, 10240);
		
		for(WorldItem item : backgroundLayer) {
			graphics.drawImage(item.sprite, (int) item.x, (int) item.y, this);
		}
		graphics.dispose();
	}
	
	public void drawLayers() {
		for(WorldItem item : collisionItemLayer) {
			if(item != null)
				drawWorldItem(item);
		}
		
		for(WorldItem item : effectLayer) {
			if(item != null)
				drawWorldItem(item);
		}
	}
	
	/*
	 * RESIZE FUNCTION
	 */
	
	public BufferedImage resizeImage(BufferedImage img, int width, int height) {
		Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_FAST);
		BufferedImage finalImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D graphics = finalImage.createGraphics();
		graphics.drawImage(scaledImage, 0, 0, null);
		graphics.dispose();
		
		return finalImage;
	}
	
	/*
	 * METHODS FOR CAMERA
	 */
	public void centerCamera(WorldItem item) {
		cameraX = (int) item.x - ((windowWidth / 2) - (item.width / 2));
		cameraY = (int) item.y - ((windowHeight / 2) - (item.height / 2));
		
		if(cameraX < 0) cameraX = 0;
		if(cameraY < 0) cameraY = 0;
		if(cameraX > 10240 - windowWidth) cameraX = 10240 - windowWidth;
		if(cameraY > 10240 - windowHeight) cameraY = 10240 - windowHeight;
	}
	
	/*
	 * METHODS FOR MOUSE LISTENING
	 */
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {
		mouseDown = true;
	}
	public void mouseReleased(MouseEvent e) {
		mouseDown = false;
	}
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}
	public void mouseDragged(MouseEvent e) {}
	
	/*
	 * METHODS FOR UI
	 */
	public void drawText(int x, int y, String text, int fontSize, Color fontColor) {
		Font font = new Font("Sans Serif", Font.BOLD, fontSize);
		Graphics2D graphics = frameBuffer.createGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		graphics.setColor(fontColor);
		graphics.setFont(font);
		graphics.drawString(text, x, y);
	}
	
	// java makes the easy things hard. yay!
	public void drawTextCentered(int x, int y, String text, int fontSize, Color fontColor) {
		Font font = new Font("Sans Serif", Font.PLAIN, fontSize);
		Graphics2D graphics = frameBuffer.createGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		graphics.setColor(fontColor);
		FontMetrics metrics = graphics.getFontMetrics(font);
		x = x - (metrics.stringWidth(text) / 2);
		y = y - (metrics.getHeight() / 2);
		graphics.setFont(font);
		graphics.drawString(text, x, y);
	}
	
	// this is fucked
	public boolean drawMenuButton(int x, int y, int width, int height, String text, Color textColor, Color backgroundColor, Color backgroundColorHover) {
		Font font = new Font("Sans Serif", Font.BOLD, 35);
		Graphics2D graphics = frameBuffer.createGraphics();
		if(mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height) {
			graphics.setColor(backgroundColorHover);
			if(mouseDown) return true;
		} else
			graphics.setColor(backgroundColor);
		
		graphics.fillRect(x, y, width, height);
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		FontMetrics metrics = graphics.getFontMetrics(font);
		int textX = x + (width - metrics.stringWidth(text)) / 2;
		int textY = y + ((height - metrics.getHeight()) / 2) + metrics.getAscent();
		graphics.setColor(textColor);
		graphics.setFont(font);
		graphics.drawString(text, textX, textY);
		graphics.dispose();
		
		return false;
	}
	
	public boolean drawClearMenuButton(int x, int y, int width, int height, int fontSize, String text, Color textColor, Color textColorHover) {
		Font font = new Font("Monospace", Font.BOLD, fontSize);
		Graphics2D graphics = frameBuffer.createGraphics();
		if(mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height) {
			graphics.setColor(textColorHover);
			if(mouseDown) return true;
		} else
			graphics.setColor(textColor);
		
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		FontMetrics metrics = graphics.getFontMetrics(font);
		int textX = x;// + (width - metrics.stringWidth(text)) / 2;
		int textY = y + ((height - metrics.getHeight()) / 2) + metrics.getAscent();
		graphics.setFont(font);
		graphics.drawString(text, textX, textY);
		graphics.dispose();
		
		return false;
	}

	public void drawRectangle(int x, int y, int width, int height, Color color) {
		Graphics2D graphics = frameBuffer.createGraphics();
		graphics.setColor(color);
		graphics.fillRect(x, y, width, height);
		graphics.dispose();
	}

	public void drawOtherRectangle(int x, int y, int width, int height, Color color) {
		Graphics2D graphics = frameBuffer.createGraphics();
		graphics.setColor(color);
		graphics.drawRect(x, y, width, height);
		graphics.dispose();
	}
	
	public void drawLoadingScreen(String hint) {
		drawRectangle(0, 0, windowWidth, windowHeight, new Color(0, 0, 0));
		drawTextCentered(windowWidth / 2, windowHeight / 2, "Loading", 40, Color.WHITE);
		drawTextCentered(windowWidth / 2, (windowHeight / 2) + 40, hint, 30, Color.WHITE);
		repaint();
	}
	
	/*
	 * METHODS FOR MUSIC AND SFX
	 */
	public void loopMusic(AudioInputStream input) throws LineUnavailableException, IOException {
		musicPlayer.stop();
		musicPlayer.open(input);
		musicPlayer.loop(Clip.LOOP_CONTINUOUSLY);
	}
}
