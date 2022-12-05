package com.s409022mg.mario.powerup;

import java.awt.Graphics;
import java.util.Random;

import com.s409022mg.mario.Game;
import com.s409022mg.mario.Handler;
import com.s409022mg.mario.Id;
import com.s409022mg.mario.entity.Entity;
import com.s409022mg.mario.tile.Tile;

public class Mushroom extends Entity {

	private Random random = new Random();
	
	public Mushroom(int x, int y, int width, int height, Id id, Handler handler) {
		super(x, y, width, height, id, handler);
		
		int dir = random.nextInt(2);
		
		switch (dir) {
		case 0:
			setVelX(-2);
			break;
		case 1:
			setVelX(2);
			break;
		}
	}

	public void render(Graphics g) {
		g.drawImage(Game.mushroom.getBufferedImage(), x, y, width, height, null);
	}

	public void tick() {
		x += velX;
		y += velY;
		
		for (int i = 0; i < handler.tile.size(); i++) {
			Tile t = handler.tile.get(i);
			
			if (t.solid) {
				if (t.getId() == Id.wall) {
					if (getBoundsBottom().intersects(t.getBounds())) {
						setVelY(0);
						
						if (falling) {
							falling = false;
						}
						else if (!falling) {
							gravity = 0.8;
							falling = true;
						}
					}
					if (getBoundsLeft().intersects(t.getBounds())) {
						setVelX(2);
					}
					if (getBoundsRight().intersects(t.getBounds())) {
						setVelX(-2);
					}
				}
			}
		}
		
		if (falling) {
			gravity += 0.1;
			setVelY((int)gravity);
		}
	}
}
