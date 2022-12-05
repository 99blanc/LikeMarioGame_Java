package com.s409022mg.mario.tile;

import java.awt.Graphics;

import com.s409022mg.mario.Game;
import com.s409022mg.mario.Handler;
import com.s409022mg.mario.Id;
import com.s409022mg.mario.gfx.Sprite;
import com.s409022mg.mario.powerup.Mushroom;

public class PowerUpBlock extends Tile {
	
	private Sprite powerUp;
	private boolean poppedUp = false;
	private int spriteY = getY();

	public PowerUpBlock(int x, int y, int width, int height, boolean solid, Id id, Handler handler, Sprite powerUp) {
		super(x, y, width, height, solid, id, handler);
		this.powerUp = powerUp;
	}

	public void render(Graphics g) {
		if (!poppedUp) {
			g.drawImage(powerUp.getBufferedImage(), x, spriteY, width, height, null);
		}
		if (!activated) {
			g.drawImage(Game.powerUp.getBufferedImage(), x, y, width, height, null);
		}
		else {
			g.drawImage(Game.usedPowerUp.getBufferedImage(), x, y, width, height, null);
		}
	}

	public void tick() {
		if (activated && !poppedUp) {
			spriteY--;
			
			if (spriteY <= y - height) {
				handler.addEntity(new Mushroom(x, spriteY, width, height, Id.mushroom, handler));
				poppedUp = true;
			}
		}
	}

}
