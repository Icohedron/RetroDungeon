package io.github.icohedron.retrodungeon.menu;

import io.github.icohedron.retrodungeon.Assets;
import io.github.icohedron.retrodungeon.Input;
import io.github.icohedron.retrodungeon.RetroDungeon;
import io.github.icohedron.retrodungeon.game.gamestates.CreditsState;
import io.github.icohedron.retrodungeon.game.gamestates.HowToPlayState;
import io.github.icohedron.retrodungeon.game.gamestates.ImportState;
import io.github.icohedron.retrodungeon.game.gamestates.PlayState;
import io.github.icohedron.retrodungeon.game.gamestates.StoryState;
import io.github.icohedron.retrodungeon.graphics.Bitmap;
import io.github.icohedron.retrodungeon.level.Level;

// The menu that opens more menus! Also lets the user start playing the built-in levels.
public class MainMenu extends Menu {

	private final String[] options = { "Play", "Import", "Story", "How to Play", "Credits", "Quit" };
	private int selected = 0;

	@Override
	protected void updateOnInput(RetroDungeon game, boolean up, boolean down, boolean left, boolean right, boolean enter) {
		if (enter) {
			Assets.SELECT_SOUND.play();
			if (selected == 0) {
				Input.setMouseLock(true);
				game.getGameStateManager().pushState(new PlayState(game, new Level(Assets.LEVEL_3, Level.Theme.DUNGEON)));
				game.getGameStateManager().pushState(new PlayState(game, new Level(Assets.LEVEL_2, Level.Theme.SANDSTORM)));
				game.getGameStateManager().pushState(new PlayState(game, new Level(Assets.LEVEL_1, Level.Theme.BLIZZARD)));
//				game.getGameStateManager().pushState(new PlayState(game, new Level(32, 32, Level.Theme.DUNGEON)));
			} else if (selected == 1) {
				game.getGameStateManager().pushState(new ImportState(game));
			} else if (selected == 2) {
				game.getGameStateManager().pushState(new StoryState(game));
			} else if (selected == 3) {
				game.getGameStateManager().pushState(new HowToPlayState(game));
			} else if (selected == 4) {
				game.getGameStateManager().pushState(new CreditsState(game));
			} else {
				System.exit(0);
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
		int h12 = bitmap.height / 12;
		
		bitmap.draw("Main Menu", (bitmap.width - ("Main Menu").length() * Assets.FONT_STRIDE_X) / 2, (int) (h12 * 2.5), 0xFFFF00);
		for (int i = 0; i < options.length; i++) {
			String text = options[i];
			int color = 0x999999;
			if (i == selected) {
				text = "->" + text;
				color = 0xEEEE99;
			}
			bitmap.draw(text, (bitmap.width - text.length() * Assets.FONT_STRIDE_X) / 2, (int) (h12 * (4 + i)), color);
		}
	}

}
