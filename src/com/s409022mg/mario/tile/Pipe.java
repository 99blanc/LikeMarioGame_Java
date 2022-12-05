package com.s409022mg.mario.tile;

import java.awt.Color;
import java.awt.Graphics;

import com.s409022mg.mario.Game;
import com.s409022mg.mario.Handler;
import com.s409022mg.mario.Id;

public class Pipe extends Tile {

	public Pipe(int x, int y, int width, int height, boolean solid, Id id, Handler handler, int facing) {
		super(x, y, width, height, solid, id, handler);
		this.facing = facing;
	}

	public void render(Graphics g) {
		g.setColor(new Color(0, 128, 0));
		g.fillRect(x, y, width, height);
	}

	public void tick() {
		
	}
}
