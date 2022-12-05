package com.s409022mg.mario;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ConcurrentModificationException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.s409022mg.mario.entity.Entity;
import com.s409022mg.mario.gfx.Sprite;
import com.s409022mg.mario.gfx.SpriteSheet;
import com.s409022mg.mario.gfx.gui.Launcher;
import com.s409022mg.mario.input.KeyInput;
import com.s409022mg.mario.input.MouseInput;
import com.s409022mg.mario.mob.Player;
import com.s409022mg.mario.tile.Wall;

public class Game extends Canvas implements Runnable {
	
	public static final int WIDTH = 270;
	public static final int HEIGHT = WIDTH / 14 * 10;
	public static final int SCALE = 4;
	public static final String TITLE = "Mario Game(s409022mg)";
	public static Handler handler;
	public static SpriteSheet sheet;
	public static Camera cam;
	public static Launcher launcher;
	public static MouseInput mouse;
	public static Sprite grass;
	public static Sprite powerUp;
	public static Sprite usedPowerUp;
	public static Sprite pipeIn;
	public static Sprite flag;
	public static Sprite mushroom;
	public static Sprite coin;
	public static Sprite goomba;
	public static Sprite player;
	public static int coins = 0;
	public static int lives = 5;
	public static int deathScreenTime = 0;
	public static boolean showDeathScreen = true;
	public static boolean showClearScreen = false;
	public static boolean gameOver = false;
	public static boolean gameClear = false;
	public static boolean playing = false;
	private Thread thread;
	private boolean running = false;
	private BufferedImage image;
	
	public Game() {
		Dimension size = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
		setPreferredSize(size);
		setMaximumSize(size);
		setMinimumSize(size);
	}
	
	private void init() {
		handler = new Handler();
		sheet = new SpriteSheet("/spritesheet.png");
		cam = new Camera();
		launcher = new Launcher();
		mouse = new MouseInput();
		addKeyListener(new KeyInput());
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		grass = new Sprite(sheet, 2, 1);
		powerUp = new Sprite(sheet, 6, 1);
		usedPowerUp = new Sprite(sheet, 7, 1);
		pipeIn = new Sprite(sheet, 8, 1);
		flag = new Sprite(sheet, 9, 1);
		mushroom = new Sprite(sheet, 4, 1);
		coin = new Sprite(sheet, 3, 1);
		goomba = new Sprite(sheet, 5, 1);
		player = new Sprite(sheet, 1, 1);
		try {
			image = ImageIO.read(getClass().getResource("/level.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private synchronized void start() {
		if (running) {
			return;
		}
		
		running = true;
		thread = new Thread(this, "Thread");
		thread.start();
	}
	
	private synchronized void stop() {
		if (!running) {
			return;
		}
		
		running = false;
		
		try {
			thread.join();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		init();
		requestFocus();
		
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		double delta = 0.0;
		double ns = 1000000000.0/60.0;
		int frames = 0;
		int ticks = 0;
		
		while (running) {
			long now = System.nanoTime();
			
			delta += (now - lastTime) / ns;
			lastTime = now;
			
			while (delta >= 1) {
				tick();
				ticks++;
				delta--;
			}
			
			render();
			frames++;
			
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println(frames + " Frame Per Second " + ticks + " Updates Per Second");
				frames = 0;
				ticks = 0;
			}
		}
		
		stop();
	}
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		
		if (bs == null) {
			createBufferStrategy(3);
			
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		if (!showDeathScreen) {
			g.setColor(Color.BLACK);
			g.setFont(new Font("Courier", Font.BOLD, 20));
			g.drawImage(Game.coin.getBufferedImage(), 20, 20, 75, 75, null);
			g.drawString("x" + coins, 100, 95);
		}
		else {
			if (!gameOver) {
				g.setColor(Color.BLACK);
				g.setFont(new Font("Courier", Font.BOLD, 50));
				g.drawImage(Game.player.getBufferedImage(), 500, 300, 100, 100, null);
				g.drawString("x" + lives, 610, 370);
			}
			else {
				g.setColor(Color.BLACK);
				g.setFont(new Font("Courier", Font.BOLD, 50));
				g.drawString("Game Over", 300, 400);
			}
		}
		if (playing) {
			g.translate(cam.getX(), cam.getY());
		}
		if (!showDeathScreen && playing) {
			handler.render(g);
		}
		else if (!playing) {
			launcher.render(g);
		}
		if (showClearScreen) {
			gameClear = true;
			g.setColor(Color.BLACK);
			g.setFont(new Font("Courier", Font.BOLD, 50));
			g.drawString("Game Clear", 300, 400);
		}
		g.dispose();
		bs.show();
	}
	
	public void tick() {
		if (playing) {
			handler.tick();
		}

		for (Entity e:handler.entity) {
			if (e.getId() == Id.player) {
				if (!e.goingDownPipe) {
					cam.tick(e);
				}
			}
		}
		if (showDeathScreen) {
			deathScreenTime++;
		}
		if (deathScreenTime >= 180) {
			showDeathScreen = false;
			deathScreenTime = 0;
			handler.clearLevel();
			handler.createLevel(image);
		}
	}
	
	public static int getFrameWidth() {
		return WIDTH * SCALE;
	}
	
	public static int getFrameHeight() {
		return HEIGHT * SCALE;
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		JFrame frame = new JFrame(TITLE);
		frame.add(game);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		game.start();
	}
}
