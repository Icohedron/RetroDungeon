package io.github.icohedron.retrodungeon.menu;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import io.github.icohedron.retrodungeon.Assets;
import io.github.icohedron.retrodungeon.Input;
import io.github.icohedron.retrodungeon.RetroDungeon;
import io.github.icohedron.retrodungeon.game.gamestates.PlayState;
import io.github.icohedron.retrodungeon.graphics.Bitmap;
import io.github.icohedron.retrodungeon.level.Level;

// A user interface used for importing levels into the game
public class ImportMenu extends Menu {
	
	private final String[] options = { "Blizzard", "Sandstorm", "Dungeon", "Back" };
	private int selected = 0;
	
	private Bitmap level;
	
	@Override
	protected void updateOnInput(RetroDungeon game, boolean up, boolean down, boolean left, boolean right, boolean enter) {
		
		if (level == null) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));
			int status = fileChooser.showOpenDialog(game);
			if (status == JFileChooser.APPROVE_OPTION) {
				level = Assets.loadBitmap(fileChooser.getSelectedFile().getPath());
			} else {
				game.getGameStateManager().popState();
			}
			return;
		}
		
		if (enter) {
			Assets.SELECT_SOUND.play();
			if (selected == 0) {
				Input.setMouseLock(true);
				game.getGameStateManager().popState();
				game.getGameStateManager().pushState(new PlayState(game, new Level(level, Level.Theme.BLIZZARD)));
			} else if (selected == 1) {
				Input.setMouseLock(true);
				game.getGameStateManager().popState();
				game.getGameStateManager().pushState(new PlayState(game, new Level(level, Level.Theme.SANDSTORM)));
			} else if (selected == 2) {
				Input.setMouseLock(true);
				game.getGameStateManager().popState();
				game.getGameStateManager().pushState(new PlayState(game, new Level(level, Level.Theme.DUNGEON)));
			} else {
				game.getGameStateManager().popState();
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
		
		bitmap.draw("Choose a Theme", (bitmap.width - ("Choose a Theme").length() * Assets.FONT_STRIDE_X) / 2, (int) (h12 * 3), 0xFFFF00);
		for (int i = 0; i < options.length; i++) {
			String text = options[i];
			int color = 0x999999;
			if (i == selected) {
				text = "->" + text;
				color = 0xEEEE99;
			}
			if (i == 3) {
				bitmap.draw(text, (bitmap.width - text.length() * Assets.FONT_STRIDE_X) / 2, (int) (h12 * 8), color);
			} else {
				bitmap.draw(text, (bitmap.width - text.length() * Assets.FONT_STRIDE_X) / 2, (int) (h12 * (4.5 + i)), color);
			}
		}
	}

}
