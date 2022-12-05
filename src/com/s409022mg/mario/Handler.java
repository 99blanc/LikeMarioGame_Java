package com.s409022mg.mario;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;

import com.s409022mg.mario.entity.Entity;
import com.s409022mg.mario.mob.Goomba;
import com.s409022mg.mario.mob.Player;
import com.s409022mg.mario.powerup.Mushroom;
import com.s409022mg.mario.tile.Coin;
import com.s409022mg.mario.tile.Flag;
import com.s409022mg.mario.tile.Pipe;
import com.s409022mg.mario.tile.PipeIn;
import com.s409022mg.mario.tile.PowerUpBlock;
import com.s409022mg.mario.tile.Tile;
import com.s409022mg.mario.tile.Wall;

public class Handler {
	
	public LinkedList<Entity> entity = new LinkedList<Entity>();
	public LinkedList<Tile> tile = new LinkedList<Tile>();
	
	public void render(Graphics g) {
		for (Entity en:entity) {
			en.render(g);
		}
		
		for (Tile ti:tile) {
			ti.render(g);
		}
	}
	
	public void tick() {
		for (int i = 0; i < entity.size(); i++) {
			Entity e = entity.get(i);
			e.tick();
		}
		
		for (int i = 0; i < tile.size(); i++) {
			Tile t = tile.get(i);
			t.tick();
		}
	}
	
	public void addEntity(Entity en) {
		entity.add(en);
	}
	
	public void removeEntity(Entity en) {
		entity.remove(en);
	}
	
	public void addTile(Tile ti) {
		tile.add(ti);
	}
	
	public void removeTile(Tile ti) {
		tile.remove(ti);
	}
	
	public void createLevel(BufferedImage level) {
		int width = level.getWidth();
		int height = level.getHeight();
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int pixel = level.getRGB(x, y);
				
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;
				
				if (red == 0 && green == 0 && blue == 0) {
					addTile(new Wall(x * 64, y * 64, 64, 64, true, Id.wall, this));
				}
				if (red == 0 && green == 0 && blue == 255) {
					addEntity(new Player(x * 64, y * 64, 64, 64, Id.player, this));
				}
				if (red == 255 && green == 0 && blue == 0) {
					addEntity(new Mushroom(x * 64, y * 64, 64, 64, Id.mushroom, this));
				}
				if (red == 255 && green == 119 && blue == 0) {
					addEntity(new Goomba(x * 64, y * 64, 64, 64, Id.goomba, this));
				}
				if (red == 255 && green == 255 && blue == 0) {
					addTile(new PowerUpBlock(x * 64, y * 64, 64, 64, true, Id.powerUp, this, Game.mushroom));
				}
				if (red == 0 && green == 255 && blue == 0) {
					addTile(new PipeIn(x * 64, y * 64, 64, 64, false, Id.pipeIn, this));
				}
				if (red == 0 && (green > 123 && green < 129) && blue == 0) {
					addTile(new Pipe(x * 64, y * 64, 64, 64 * 9, true, Id.pipe, this, 128 - green));
				}
				if (red == 255 && green == 250 && blue == 0) {
					addTile(new Coin(x * 64, y * 64, 64, 64, true, Id.coin, this));
				}
				if (red == 100 && green == 0 && blue == 255) {
					addTile(new Flag(x * 64, y * 64, 64, 64, true, Id.flag, this));
				}
			}
		}
	}
	
	public void clearLevel() {
		entity.clear();
		tile.clear();
	}
}
