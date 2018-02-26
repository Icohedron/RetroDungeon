package io.github.icohedron.retrodungeon;

import java.awt.AWTException;
import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

// Responsible for handling all user input
public class Input implements KeyListener, MouseMotionListener {
	
	private static Canvas canvas;
	private static Robot robot;
	
	private static Cursor blankCursor;
	private static boolean lockMouse;
	
	private static boolean inFocus;
	
	public Input(Canvas canvas) {
		Input.canvas = canvas;
		
		BufferedImage cursorImage = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImage, new Point(0, 0), "Blank Cursor");
		
		lockMouse = false;
//		canvas.setCursor(blankCursor);
		
		inFocus = canvas.isFocusOwner();
		
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	//// Keyboard input ////
	
	private static final boolean[] keyDown = new boolean[0xFFFF];
	private static final boolean[] keyPressed = new boolean[0xFFFF];
	
	// Check if a key is held down
	public static boolean isKeyDown(int keyCode) {
		return keyDown[keyCode];
	}
	
	// Check if a key has been pressed and released
	public static boolean isKeyPressed(int keyCode) {
		return keyPressed[keyCode];
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (!keyDown[e.getKeyCode()]) {
			keyDown[e.getKeyCode()] = true;
			keyPressed[e.getKeyCode()] = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keyDown[e.getKeyCode()] = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {}
	
//	//// Mouse Button Input ////
//	
//	private static final boolean[] mouseDown = new boolean[4];
//	private static final boolean[] mousePressed = new boolean[4];
//	private static boolean mouseInWindow = false;
//	
//	// Check if a mouse button is held down
//	public static boolean isMouseButtonDown(int button) {
//		return mouseDown[button];
//	}
//	
//	// Check if a mouse button has been pressed and released
//	public static boolean isMouseButtonPressed(int button) {
//		return mousePressed[button];
//	}
//	
//	public static boolean isMouseInWindow() {
//		return mouseInWindow;
//	}
//
//	@Override
//	public void mouseEntered(MouseEvent e) {
//		mouseInWindow = true;
//	}
//
//	@Override
//	public void mouseExited(MouseEvent e) {
//		mouseInWindow = false;
//	}
//
//	@Override
//	public void mousePressed(MouseEvent e) {
//		if (!mouseDown[e.getButton()]) {
//			mouseDown[e.getButton()] = true;
//			mousePressed[e.getButton()] = true;
//		}
//	}
//
//	@Override
//	public void mouseReleased(MouseEvent e) {
//		mouseDown[e.getButton()] = false;
//	}
//	
//	@Override
//	public void mouseClicked(MouseEvent e) {}
	
	//// Mouse Motion Input ////
	
//	private static int mousePosX;
//	private static int mousePosY;
	
	private static int mouseDiffX;
//	private static int mouseDiffY;
	
//	// Get the mouse's x position
//	public static int getMousePosX() {
//		return mousePosX;
//	}
	
//	// Get the mouse's y position
//	public static int getMousePosY() {
//		return mousePosY;
//	}
	
	// Get the difference in the mouse's x position since the last update
	public static int getMouseDiffX() {
		return mouseDiffX;
	}
	
//	// Get the difference in the mouse's y position since the last update
//	public static int getMouseDiffY() {
//		return mouseDiffY;
//	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		if (lockMouse == true) {
			mouseDiffX = e.getX() - canvas.getWidth() / 2;
//			mouseDiffY = e.getY() - canvas.getHeight() / 2;
		}
//		mousePosX = e.getX();
//		mousePosY = e.getY();
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {}
	
	// Check if the mouse is locked to the center of the window
	public static boolean isMouseLocked() {
		return lockMouse;
	}
	
	// Set whether the mouse should be locked to the center of the window or not
	public static void setMouseLock(boolean lock) {
		lockMouse = lock;
		if (lock == true) {
			canvas.setCursor(blankCursor);
		} else {
			canvas.setCursor(Cursor.getDefaultCursor());
		}
	}
	
	//// Updating ////
	
	// Update the pressed keys, and lock the mouse to the center of the screen if lockMouse == true
	public static void update() {
		inFocus = canvas.isFocusOwner();
		for (int i = 0; i < keyPressed.length; i++) {
			keyPressed[i] = false;
		}
//		for (int i = 0; i < mouseDown.length; i++) {
//			mousePressed[i] = false;
//		}
		mouseDiffX = 0;
//		mouseDiffY = 0;
		if (inFocus && lockMouse) {
			Point p = canvas.getLocationOnScreen();
			robot.mouseMove(p.x + canvas.getParent().getWidth() / 2, p.y + canvas.getParent().getHeight() / 2);
		}
	}
}
