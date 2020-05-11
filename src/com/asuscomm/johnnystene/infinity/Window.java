/*
 * Window.java
 * 
 * Handles creating and drawing to the window. Also handles the camera.
 */

package com.asuscomm.johnnystene.infinity;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

public class Window extends JFrame implements MouseListener, MouseMotionListener {
	public BufferedImage frameBuffer;
	public Keyboard keyListener;
	
	public int windowWidth = 0;
	public int windowHeight = 0;
	
	public int mouseX = -1;
	public int mouseY = -1;
	public boolean mouseDown = false;
	
	public int cameraX = 0;
	public int cameraY = 0;
	public boolean enableCamera = true;
	
	public Window(int width, int height, String name) {
		super(); // Create JFrame
		windowWidth = width;
		windowHeight = height;
		setTitle(name); // Set window title
		addMouseListener(this); // Setup mouse listening
		addMouseMotionListener(this);
		keyListener = new Keyboard();
		addKeyListener(keyListener);
		setSize(width, height); // Set window size
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Terminate the game on window close
		setLocationRelativeTo(null); // Center window
		setVisible(true); // Make window visible
		frameBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); // Create image for framebuffer
	}
	
	public void paint(Graphics g) {
		g.drawImage(frameBuffer, 0, 0, this); // Draw the framebuffer on the screen
		
		// Reset the framebuffer
		Graphics2D graphics = frameBuffer.createGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, frameBuffer.getWidth(), frameBuffer.getHeight());
		graphics.dispose();
	}
	
	public void drawWorldItem(WorldItem item) {
		Graphics2D graphics = frameBuffer.createGraphics();
		if(enableCamera)
			graphics.drawImage(item.sprite, item.x - cameraX, item.y - cameraY, this);
		else
			graphics.drawImage(item.sprite, item.x, item.y, this);
		graphics.dispose();
	}
	
	public void centerCamera(WorldItem item) {
		cameraX = item.x - ((windowWidth / 2) - (item.width / 2));
		cameraY = item.y - ((windowHeight / 2) - (item.height / 2));
	}
	
	// MouseListener functions
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
	
	public void drawTextCentered(int x, int y, String text, int fontSize, Color fontColor) {
		Font font = new Font("Sans Serif", Font.PLAIN, fontSize);
		Graphics2D graphics = frameBuffer.createGraphics();
		graphics.setColor(fontColor);
		FontMetrics metrics = graphics.getFontMetrics(font);
		x = x - (metrics.stringWidth(text) / 2);
		y = y - (metrics.getHeight() / 2);
		graphics.setFont(font);
		graphics.drawString(text, x, y);
	}
	
	public boolean drawMenuButton(int x, int y, int width, int height, String text, Color textColor, Color backgroundColor, Color backgroundColorHover) {
		boolean checkForClick = false;
		Font font = new Font("Sans Serif", Font.BOLD, 32);
		Graphics2D graphics = frameBuffer.createGraphics();
		if(mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height) {
			checkForClick = true;
			graphics.setColor(backgroundColorHover);
		} else
			graphics.setColor(backgroundColor);
		graphics.fillRect(x, y, width, height);
		FontMetrics metrics = graphics.getFontMetrics(font);
		int textX = x + (width - metrics.stringWidth(text)) / 2;
		int textY = y + ((height - metrics.getHeight()) / 2) + metrics.getAscent();
		graphics.setColor(textColor);
		graphics.setFont(font);
		graphics.drawString(text, textX, textY);
		graphics.dispose();
		
		if(mouseDown && checkForClick) {
			return true;
		}
		
		return false;
	}
}
