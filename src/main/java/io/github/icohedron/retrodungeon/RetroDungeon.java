package io.github.icohedron.retrodungeon;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;
import javax.swing.UIManager;

import io.github.icohedron.retrodungeon.game.GameStateManager;
import io.github.icohedron.retrodungeon.game.gamestates.MainMenuState;
import io.github.icohedron.retrodungeon.graphics.Bitmap;

public class RetroDungeon extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = 180;
	public static final int HEIGHT = 120;
	public static final int SCALE = 6;
	public static final String TITLE = "Retro Dungeon";
	public static final int TEXTURE_SIZE = 16;
	
	private static final long TICKS_PER_SECOND = 60;
	private static final long NS_PER_TICK = 1000000000 / TICKS_PER_SECOND;
	
	private boolean running = false;
	private Thread thread;
	
	private BufferedImage image;
	private int[] pixels;
	
	private GameStateManager gsm;

	public RetroDungeon() {
		Dimension dimension = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
		setMinimumSize(dimension);
		setMaximumSize(dimension);
		setPreferredSize(dimension);
		
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		
		gsm = new GameStateManager();
		gsm.pushState(new MainMenuState(this));
		
		Input input = new Input(this);
		addKeyListener(input);
//		addMouseListener(input);
		addMouseMotionListener(input);
	}
	
	// Start the thread for the program
	public void start() {
		if (running) {
			return;
		}
		
		running = true;
		thread = new Thread(this, TITLE);
		thread.start();
	}
	
	// The game loop
	@Override
	public void run() {
		long statTimer = System.currentTimeMillis();
		int updates = 0;
		int frames = 0;
		
		long lastFrameTime = System.nanoTime();
		long delta = 0;
		
		while (running) {
			long frameTime = System.nanoTime();
			delta += frameTime - lastFrameTime;
			lastFrameTime = frameTime;
			
			while (delta >= NS_PER_TICK) {
				delta -= NS_PER_TICK;
				update();
				Input.update();
				updates++;
			}
			
			render();
			frames++;
			
			if (System.currentTimeMillis() - statTimer >= 1000) {
				statTimer = System.currentTimeMillis();
				System.out.println(frames + " FPS, " + updates + " UPS");
				updates = 0;
				frames = 0;
			}
		}
	}
	
	// The program's update call. Anything that needs to be updated that does not include rendering/drawing goes here.
	// This will get called 60 times per second. More will be called to account for lag if needed.
	private void update() {
		if (Input.isKeyPressed(KeyEvent.VK_ESCAPE)) {
			Assets.CHANGE_SELECTION_SOUND.play();
			if (Input.isMouseLocked()) {
				Input.setMouseLock(false);
			} else {
				Input.setMouseLock(true);
			}
		}
		
		gsm.update();
	}
	
	// The program's render call. Anything that renders/draws to the screen goes here.
	private void render() {
		BufferStrategy buffer = getBufferStrategy();
		if (buffer == null) {
			createBufferStrategy(3);
			return;
		}
		
		Graphics graphics = buffer.getDrawGraphics();
		
		gsm.render();
		
		Bitmap bitmap = gsm.getBitmap();
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = bitmap.pixels[i];
		}
		
		graphics.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		
		graphics.dispose();
		buffer.show();
	}
	
//	// Used for stopping the game.
//	public void stop() {
//		if (!running) {
//			return;
//		}
//		
//		running = false;
//		try {
//			thread.join();
//		} catch (InterruptedException e) {
//			e.printStackTrace();sw s
//		}
//	}
	
	public GameStateManager getGameStateManager() {
		return gsm;
	}
	
	// Main method. Starts everything.
	public static void main(String[] args) {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {} // If it doesn't work, stick with default.
		
		RetroDungeon game = new RetroDungeon();
		JFrame frame = new JFrame(TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.add(game);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setAlwaysOnTop(true); // Just for ease of development on a single monitor
		frame.setVisible(true);
		
		game.start();
	}
}
