package io.github.icohedron.retrodungeon.game.gamestates;

import io.github.icohedron.retrodungeon.RetroDungeon;
import io.github.icohedron.retrodungeon.graphics.Bitmap;
import io.github.icohedron.retrodungeon.menu.MainMenu;

// Responsible for displaying the main menu
public class MainMenuState implements GameState {
	
	private RetroDungeon game;
	private Bitmap bitmap;
	private MainMenu menu;

	public MainMenuState(RetroDungeon game) {
		this.game = game;
		bitmap = new Bitmap(RetroDungeon.WIDTH, RetroDungeon.HEIGHT);
		menu = new MainMenu();
	}

	@Override
	public void update() {
		menu.update(game);
	}

	@Override
	public void render() {
		bitmap.clear(0x131313);
		menu.render(bitmap);
	}

	@Override
	public Bitmap getBitmap() {
		return bitmap;
	}

}
