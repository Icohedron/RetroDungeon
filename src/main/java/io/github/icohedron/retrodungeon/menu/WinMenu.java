package io.github.icohedron.retrodungeon.menu;

import io.github.icohedron.retrodungeon.Assets;
import io.github.icohedron.retrodungeon.RetroDungeon;
import io.github.icohedron.retrodungeon.game.gamestates.MainMenuState;
import io.github.icohedron.retrodungeon.graphics.Bitmap;

// The menu that displays when the player beats the level
public class WinMenu extends Menu {
	
	private final String[] options = { "Next", "Quit" };
	private int selected = 0;

	@Override
	protected void updateOnInput(RetroDungeon game, boolean up, boolean down, boolean left, boolean right, boolean enter) {
		if (enter) {
			Assets.SELECT_SOUND.play();
			if (selected == 0) {
				game.getGameStateManager().popState();
			} else {
				game.getGameStateManager().emptyStack();
				game.getGameStateManager().pushState(new MainMenuState(game));
			}
			return;
		}
		if (up) {
			Assets.CHANGE_SELECTION_SOUND.play();
			selected--;
			if (selected < 0) {
				selected = options.length - 1;
			}
		}
		if (down) {
			Assets.CHANGE_SELECTION_SOUND.play();
			selected++;
			selected %= options.length;
		}
	}

	@Override
	public void render(Bitmap bitmap) {
		int w12 = bitmap.width / 12;
		int h12 = bitmap.height / 12;
		
		bitmap.fill(w12 * 4, h12 * 3, w12 * 8, (int) (h12 * 7.5), 0x131313);
		bitmap.draw("You Won!", (bitmap.width - ("You Won").length() * Assets.FONT_STRIDE_X) / 2, (int) (h12 * 3.5), 0xFFFF00);
		for (int i = 0; i < options.length; i++) {
			String text = options[i];
			int color = 0x999999;
			if (i == selected) {
				text = "->" + text;
				color = 0xEEEE99;
			}
			bitmap.draw(text, (bitmap.width - text.length() * Assets.FONT_STRIDE_X) / 2, h12 * (5 + i), color);
		}
	}

}
