package io.github.icohedron.retrodungeon.menu;

import io.github.icohedron.retrodungeon.Assets;
import io.github.icohedron.retrodungeon.RetroDungeon;
import io.github.icohedron.retrodungeon.graphics.Bitmap;

// Shows the user how to play the game
public class HowToPlayMenu extends Menu {

	@Override
	protected void updateOnInput(RetroDungeon game, boolean up, boolean down, boolean left, boolean right, boolean enter) {
		if (enter) {
			Assets.SELECT_SOUND.play();
			game.getGameStateManager().popState();
		}
	}
	
	@Override
	public void render(Bitmap bitmap) {
		int h12 = bitmap.height / 12;
		
		bitmap.draw("How to Play", (bitmap.width - ("How to Play").length() * Assets.FONT_STRIDE_X) / 2, (int) (h12 * 2), 0xFFFF00);
		bitmap.draw("WASD to move", (bitmap.width - ("WASD to move").length() * Assets.FONT_STRIDE_X) / 2, (int) (h12 * 3.5), 0xFFFFFF);
		bitmap.draw("Mouse to look around", (bitmap.width - ("Mouse to look around").length() * Assets.FONT_STRIDE_X) / 2, (int) (h12 * 4.5), 0xFFFFFF);
		bitmap.draw("Space to interact", (bitmap.width - ("Space to interact").length() * Assets.FONT_STRIDE_X) / 2, (int) (h12 * 5.5), 0xFFFFFF);
		bitmap.draw("Esc to open menu", (bitmap.width - ("Esc to open menu").length() * Assets.FONT_STRIDE_X) / 2, (int) (h12 * 6.5), 0xFFFFFF);
		bitmap.draw("Look for the key", (bitmap.width - ("Look for the key").length() * Assets.FONT_STRIDE_X) / 2, (int) (h12 * 7.5), 0xFFFFFF);
		bitmap.draw("Find the door", (bitmap.width - ("Find the door").length() * Assets.FONT_STRIDE_X) / 2, (int) (h12 * 8.5), 0xFFFFFF);
		
		bitmap.draw("->Back", (bitmap.width - ("->Back").length() * Assets.FONT_STRIDE_X) / 2, (int) (h12 * 10), 0xEEEE99);
	}

}
