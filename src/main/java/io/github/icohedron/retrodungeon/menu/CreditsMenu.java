package io.github.icohedron.retrodungeon.menu;

import io.github.icohedron.retrodungeon.Assets;
import io.github.icohedron.retrodungeon.RetroDungeon;
import io.github.icohedron.retrodungeon.graphics.Bitmap;

// Shows the people/things that made/influenced this product
public class CreditsMenu extends Menu {

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
		
		bitmap.draw("Credits", (bitmap.width - ("Credits").length() * Assets.FONT_STRIDE_X) / 2, (int) (h12 * 2), 0xFFFF00);
		
		bitmap.draw("Inspired by:", (bitmap.width - ("Inspired by").length() * Assets.FONT_STRIDE_X) / 2, (int) (h12 * 3.5), 0xFFFFFF);
		bitmap.draw("Prelude of the Chambered", (bitmap.width - ("Prelude of the Chambered").length() * Assets.FONT_STRIDE_X) / 2, (int) (h12 * 4.5), 0xFFFFFF);
		bitmap.draw("Made by:", (bitmap.width - ("Made by").length() * Assets.FONT_STRIDE_X) / 2, (int) (h12 * 6.5), 0xFFFFFF);
		bitmap.draw("Icohedron", (bitmap.width - ("Icohedron").length() * Assets.FONT_STRIDE_X) / 2, (int) (h12 * 7.5), 0xFFFFFF);
		
		bitmap.draw("->Back", (bitmap.width - ("->Back").length() * Assets.FONT_STRIDE_X) / 2, (int) (h12 * 10), 0xEEEE99);

	}

}
