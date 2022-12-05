package com.s409022mg.mario.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.s409022mg.mario.Game;
import com.s409022mg.mario.Id;
import com.s409022mg.mario.entity.Entity;
import com.s409022mg.mario.tile.Tile;

public class KeyInput implements KeyListener {

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		for (int i = 0; i < Game.handler.entity.size(); i++) {
			Entity en = Game.handler.entity.get(i);
			
			if (en.getId() == Id.player) {
				if (en.goingDownPipe) {
					return;
				}
				switch (key) {
				case KeyEvent.VK_W:
					for (int j = 0; j < Game.handler.tile.size(); j++) {
						Tile t = Game.handler.tile.get(j);
						
						if (t.getId() == Id.pipe) {
							if (en.getBoundsTop().intersects(t.getBounds())) {
								if (!en.goingDownPipe) {
									en.goingDownPipe = true;
								}
							}
						}
					}
					if (!en.jumping) {
						en.jumping = true;
						en.gravity = 8.0;
					}
					break;
				case KeyEvent.VK_S:
					for (int j = 0; j < Game.handler.tile.size(); j++) {
						Tile t = Game.handler.tile.get(j);
						
						if (t.getId() == Id.pipe) {
							if (en.getBoundsBottom().intersects(t.getBounds())) {
								if (!en.goingDownPipe) {
									en.goingDownPipe = true;
								}
							}
						}
					}
					break;
				case KeyEvent.VK_A:
					en.setVelX(-5);
					break;
				case KeyEvent.VK_D:
					en.setVelX(5);
					break;
				}
			}
		}
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		
		for (int i = 0; i < Game.handler.entity.size(); i++) {
			Entity en = Game.handler.entity.get(i);
			
			if (en.getId() == Id.player) {
				switch (key) {
				case KeyEvent.VK_W:
					en.setVelY(0);
					break;
				case KeyEvent.VK_S:
					en.setVelY(0);
					break;
				case KeyEvent.VK_A:
					en.setVelX(0);
					break;
				case KeyEvent.VK_D:
					en.setVelX(0);
					break;
				}
			}
		}
	}
	
	public void keyTyped(KeyEvent e) {

	}
}
