package io.github.icohedron.retrodungeon.game;

import java.util.Stack;

import io.github.icohedron.retrodungeon.game.gamestates.GameState;
import io.github.icohedron.retrodungeon.graphics.Bitmap;

// Handles the states of the game using a FSM
public class GameStateManager {

	private Stack<GameState> gameStates;
	
	public GameStateManager() {
		gameStates = new Stack<GameState>();
	}
	
	// Update the current game state
	public void update() {
		if (getCurrentState() != null) {
			getCurrentState().update();
		}
	}
	
	// Render the current game state
	public void render() {
		if (getCurrentState() != null) {
			getCurrentState().render();
		}
	}
	
	// Get the instance of the current game state
	public GameState getCurrentState() {
		if (gameStates.isEmpty()) {
			return null;
		}
		return gameStates.peek();
	}
	
	// Push a new game state onto the stack
	public void pushState(GameState gameState) {
		gameStates.push(gameState);
	}
	
	// Pop a game state off the stack
	public void popState() {
		if (gameStates.isEmpty()) {
			return;
		}
		gameStates.pop();
	}
	
	// Empty the stack of game states
	public void emptyStack() {
		while (!gameStates.isEmpty()) {
			popState();
		}
	}
	
	// Get the current game state's bitmap
	public Bitmap getBitmap() {
		return getCurrentState().getBitmap();
	}
}
