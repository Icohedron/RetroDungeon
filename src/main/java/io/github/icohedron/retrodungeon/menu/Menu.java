package io.github.icohedron.retrodungeon.menu;

import java.awt.event.KeyEvent;
import io.github.icohedron.retrodungeon.Input;
import io.github.icohedron.retrodungeon.RetroDungeon;
import io.github.icohedron.retrodungeon.graphics.Bitmap;

// Displays information and gives options that the user can interact with
public abstract class Menu {
	
	// Updates the menu
	public void update(RetroDungeon game) {
		boolean up, down, left, right, enter;
		up = Input.isKeyPressed(KeyEvent.VK_W) || Input.isKeyPressed(KeyEvent.VK_UP);
		down = Input.isKeyPressed(KeyEvent.VK_S) || Input.isKeyPressed(KeyEvent.VK_DOWN);
		left = Input.isKeyPressed(KeyEvent.VK_A) || Input.isKeyPressed(KeyEvent.VK_LEFT);
		right = Input.isKeyPressed(KeyEvent.VK_D) || Input.isKeyPressed(KeyEvent.VK_RIGHT);
		enter = Input.isKeyPressed(KeyEvent.VK_SPACE) || Input.isKeyPressed(KeyEvent.VK_E) || Input.isKeyPressed(KeyEvent.VK_ENTER);
		if (up || down || left || right || enter) {
			updateOnInput(game, up, down, left, right, enter);
		}
	}
	
	// Update that gets called whenever the user as made an input
	protected abstract void updateOnInput(RetroDungeon game, boolean up, boolean down, boolean left, boolean right, boolean enter);
	// Renders the menu onto the screen
	public abstract void render(Bitmap bitmap);
}
