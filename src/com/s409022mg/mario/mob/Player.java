package com.s409022mg.mario.mob;

import java.awt.Color;
import java.awt.Graphics;

import com.s409022mg.mario.Game;
import com.s409022mg.mario.Handler;
import com.s409022mg.mario.Id;
import com.s409022mg.mario.entity.Entity;
import com.s409022mg.mario.states.PlayerState;
import com.s409022mg.mario.tile.Tile;

public class Player extends Entity {

	private PlayerState state;
	private int pixelsTravelled = 0;
	private boolean cnt = false;
	
	public Player(int x, int y, int width, int height, Id id, Handler handler) {
		super(x, y, width, height, id, handler);
		state = PlayerState.SMALL;
	}

	public void render(Graphics g) {
		g.drawImage(Game.player.getBufferedImage(), x, y, width, height, null);
	}

	public void tick() {
		x += velX;
		y += velY;
		
		for (int i = 0; i < handler.tile.size(); i++) {
			Tile t = handler.tile.get(i);
			
			if (t.isSolid() && !goingDownPipe) {
				if (getBounds().intersects(t.getBounds()) && t.getId() == Id.flag) {
					Game.showClearScreen = true;
					return;
				}
				if (getBounds().intersects(t.getBounds()) && t.getId() == Id.coin) {
					Game.coins++;
					t.die();
					return;
				}
				if (getBoundsTop().intersects(t.getBounds())) {
					setVelY(0);
					
					if (jumping && !goingDownPipe) {
						jumping = false;
						gravity = 0.8;
						falling = true;
					}
					if (t.getId() == Id.powerUp) {
						if (getBoundsTop().intersects(t.getBounds())) {
							t.activated = true;
						}
					}
				}
				if (getBoundsBottom().intersects(t.getBounds())) {
					setVelY(0);
					
					if (falling) {
						falling = false;
					}
					else {
						if (!falling && !jumping) {
							gravity = 0.8;
							falling = true;
						}
					}
				}
				if (getBoundsLeft().intersects(t.getBounds())) {
					setVelX(0);
					x = t.getX() + t.width;
				}
				if (getBoundsRight().intersects(t.getBounds())) {
					setVelX(0);
					x = t.getX() - t.width;
				}
			}
		}
		
		for (int i = 0; i < handler.entity.size(); i++) {
			Entity e = handler.entity.get(i);
			
			if (e.getId() == Id.mushroom) {
				if (getBounds().intersects(e.getBounds())) {
					int tpX = getX();
					int tpY = getY();
					
					if (state == PlayerState.SMALL) {
						state = PlayerState.BIG;
						width *= 1.5;
						height *= 1.5;
						setX(tpX - width);
						setY(tpY - height);
					}
					
					e.die();
				}
			}
			else if (e.getId() == Id.goomba) {
				if (getBoundsBottom().intersects(e.getBoundsTop())) {
					e.die();
				}
				else if (getBounds().intersects(e.getBounds())) {
					if (state == PlayerState.BIG) {
						state = PlayerState.SMALL;
						width /= 1.5;
						height /= 1.5;
						x += width;
						y += height;
					}
					else if (state == PlayerState.SMALL) {
						die();
					}
				}
			}
		}
		
		if (jumping && !goingDownPipe) {
			gravity -= 0.1;
			setVelY((int)-gravity);
			
			if (gravity <= 0.0) {
				jumping = false;
				falling = true;
			}
		}
		if (falling) {
			gravity += 0.1;
			setVelY((int)gravity);
		}
		if (goingDownPipe) {
			for (int i = 0; i < Game.handler.tile.size(); i++) {
				Tile t = Game.handler.tile.get(i);
				
				if (t.getId() == Id.pipe) {
					if (pixelsTravelled >= t.height) {
						goingDownPipe = false;
						pixelsTravelled = 0;
						cnt = !cnt;
					}
					if (getBoundsBottom().intersects(t.getBounds()) && !cnt) {
						switch (t.facing) {
						case 0:
							setVelY(5);
							setVelX(0);
							pixelsTravelled += velY;
							break;
						case 2:
							setVelY(-5);
							setVelX(0);
							break;
						}
					}
					if (getBoundsTop().intersects(t.getBounds()) && cnt) {
						switch (t.facing) {
						case 0:
							setVelY(-5);
							setVelX(0);
							pixelsTravelled += -velY;
							break;
						case 2:
							setVelY(5);
							setVelX(0);
							pixelsTravelled += velY;
							break;
						}
					}
				}
			}
		}
	}
}
