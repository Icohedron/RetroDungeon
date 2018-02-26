package io.github.icohedron.retrodungeon.game.gamestates;

import io.github.icohedron.retrodungeon.RetroDungeon;
import io.github.icohedron.retrodungeon.graphics.Bitmap;
import io.github.icohedron.retrodungeon.menu.HowToPlayMenu;

// Responsible for displaying the How to Play menu.
public class HowToPlayState implements GameState {
	
	private RetroDungeon game;
	private Bitmap bitmap;
	private HowToPlayMenu menu;

	public HowToPlayState(RetroDungeon game) {
		this.game = game;
		bitmap = new Bitmap(RetroDungeon.WIDTH, RetroDungeon.HEIGHT);
		menu = new HowToPlayMenu();
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
