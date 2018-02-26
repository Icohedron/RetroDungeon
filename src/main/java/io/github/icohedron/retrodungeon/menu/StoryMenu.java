package io.github.icohedron.retrodungeon.menu;

import io.github.icohedron.retrodungeon.Assets;
import io.github.icohedron.retrodungeon.RetroDungeon;
import io.github.icohedron.retrodungeon.graphics.Bitmap;

// Displays the story of this game
public class StoryMenu extends Menu {
	
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
		
		bitmap.draw("Story", (bitmap.width - ("Story").length() * Assets.FONT_STRIDE_X) / 2, (int) (h12 * 3), 0xFFFF00);
		
		bitmap.draw("Lost in a retro game", (bitmap.width - ("Lost in a retro game").length() * Assets.FONT_STRIDE_X) / 2, (int) (h12 * 5), 0xFFFFFF);
		bitmap.draw("find a way back", (bitmap.width - ("find a way back").length() * Assets.FONT_STRIDE_X) / 2, (int) (h12 * 6), 0xFFFFFF);
		bitmap.draw("to the future!", (bitmap.width - ("to the future!").length() * Assets.FONT_STRIDE_X) / 2, (int) (h12 * 7), 0xFFFFFF);
		
		bitmap.draw("->Back", (bitmap.width - ("->Back").length() * Assets.FONT_STRIDE_X) / 2, (int) (h12 * 10), 0xEEEE99);

	}

}
