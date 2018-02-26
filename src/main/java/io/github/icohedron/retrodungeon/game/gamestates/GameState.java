package io.github.icohedron.retrodungeon.game.gamestates;

import io.github.icohedron.retrodungeon.graphics.Bitmap;

// An interface that defines what a game state does
public interface GameState {
	// Update the game state
	public abstract void update();
	// Draw whatever the game state wants to draw
	public abstract void render();
	// Get the game state's bitmap
	public abstract Bitmap getBitmap();
}
