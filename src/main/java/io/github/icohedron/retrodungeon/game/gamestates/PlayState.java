package io.github.icohedron.retrodungeon.game.gamestates;

import java.util.List;

import io.github.icohedron.retrodungeon.Assets;
import io.github.icohedron.retrodungeon.Input;
import io.github.icohedron.retrodungeon.RetroDungeon;
import io.github.icohedron.retrodungeon.graphics.Bitmap;
import io.github.icohedron.retrodungeon.graphics.Bitmap3D;
import io.github.icohedron.retrodungeon.level.Block;
import io.github.icohedron.retrodungeon.level.Level;
import io.github.icohedron.retrodungeon.level.Player;
import io.github.icohedron.retrodungeon.level.Sprite;
import io.github.icohedron.retrodungeon.menu.PauseMenu;
import io.github.icohedron.retrodungeon.menu.WinMenu;

// Responsible for the gameplay
public class PlayState implements GameState {
	
	private RetroDungeon game;
	
	private enum State {
		PAUSED,
		PLAYING,
		WIN
	}
	
	private PlayState.State state = PlayState.State.PLAYING;
	
	private Bitmap3D bitmap;
	private Player player;
	private Level level;
	
	private PauseMenu pauseMenu;
	private WinMenu winMenu;
	
	private boolean won;
	private boolean hasKey;
	private String keyStatus;
	
	public PlayState(RetroDungeon game, Level level) {
		this.game = game;
		pauseMenu = new PauseMenu();
		winMenu = new WinMenu();
		this.level = level;
		player = new Player(level.getPlayerSpawnPoint().x, level.getPlayerSpawnPoint().y, level.getPlayerSpawnPoint().z);
		bitmap = new Bitmap3D(RetroDungeon.WIDTH, RetroDungeon.HEIGHT);
		won = false;
		hasKey = false;
		keyStatus = "Not Acquired";
	}

	@Override
	public void update() {
		if (!Input.isMouseLocked()) {
			state = PlayState.State.PAUSED;
			pauseMenu.update(game);
			return;
		} else if (won) {
			state = PlayState.State.WIN;
			winMenu.update(game);
			return;
		} else {
			state = PlayState.State.PLAYING;
			player.update(level);
		}
		
		if (hasKey) {
			if (player.hasCollided()) {
				List<Block> collisions = player.getCollidedBlocks();
				for (Block b : collisions) {
					if (!won && b.getType() == Block.Type.DOOR) {
						Assets.HIT_DOOR_SOUND.play();
						won = true;
					}
				}
			}
		} else {
			Block block = player.getCurrentBlock(level);
			if (!hasKey && block != null && block.getSprite().getType() == Sprite.Type.KEY) {
				Assets.PICKUP_KEY_SOUND.play();
				hasKey = true;
				keyStatus = "Acquired!";
				level.removeBlock(player.getWorldPosition());
			}
		}
	}

	@Override
	public void render() {
		int h12 = bitmap.height / 12;
		
		bitmap.renderLevel(level, player);
		bitmap.fill(0, (int) (h12 * 11), bitmap.width, bitmap.height, 0x131313);
		bitmap.draw("^", 3, bitmap.height - 8, 0xFFFF00);
		bitmap.draw(keyStatus, 12, bitmap.height - 8, 0xE7E7E7);
		
		if (state == PlayState.State.PAUSED) {
			pauseMenu.render(bitmap);
		} else if (state == PlayState.State.WIN) {
			winMenu.render(bitmap);
		}
	}

	@Override
	public Bitmap getBitmap() {
		return bitmap;
	}
}
