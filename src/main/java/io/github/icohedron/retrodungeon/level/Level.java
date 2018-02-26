package io.github.icohedron.retrodungeon.level;

import java.util.Random;

import io.github.icohedron.retrodungeon.Assets;
import io.github.icohedron.retrodungeon.graphics.Bitmap;
import io.github.icohedron.retrodungeon.math.Vector2;
import io.github.icohedron.retrodungeon.math.Vector3;

// Stores data on all the blocks that make up the world
public class Level {
	
	// The theme of this level
	public static enum Theme {
		SANDSTORM,
		BLIZZARD,
		DUNGEON
	}
	
	public final int width;
	public final int height;
	public final Block[] blocks;
	
	private Vector3 playerSpawnPoint; // (x, y, rotation)
	private Level.Theme theme;
	
	public Level(Bitmap bitmap, Level.Theme theme) {
		width = bitmap.width;
		height = bitmap.height;
		blocks = new Block[width * height];
		this.theme = theme;
		
		Block.Type wall;
		Sprite.Type grass;
		switch (theme) {
		case SANDSTORM:
			wall = Block.Type.SANDSTONE;
			grass = Sprite.Type.DEADBUSH;
			break;
		case BLIZZARD:
			wall = Block.Type.SNOW;
			grass = Sprite.Type.TALL_GRASS;
			break;
		case DUNGEON:
			wall = Block.Type.STONE;
			grass = Sprite.Type.TALL_GRASS;
			break;
		default:
			wall = Block.Type.COBBLESTONE;
			grass = Sprite.Type.TALL_GRASS;
		}
		playerSpawnPoint = new Vector3(0);
		
		/*
		 * 	Black (0x000000 or rgb(0, 0, 0)) is a wall.
		 *  Red (0xFF0000 or rgb(255, 0, 0)) is the player's position.
		 *	Cyan (0x00FFFF or rgb(0, 255, 255)) is the player's direction (which way the player is looking). This must be placed adjacent to red. If not provided, the default rotation is north.
		 *	Green (0x00FF00 or rgb(0, 255, 0)) is the door.
		 *	Yellow (0xFFFF00 or rgb(255, 255, 0)) is the key.
		 */
		Random random = new Random(0);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int yTile = (height - 1) - y;
				int xTile = x;
				int pixel = bitmap.getPixel(x, y);
				if (((pixel >> 24) & 0xFF) < 255) {
					if (random.nextInt(10) < 2) {
						setBlock(xTile, yTile, new Block(grass));
					}
					continue;
				}
				if ((pixel & 0xFFFFFF) == 0x000000) {
					if (random.nextInt(10) < 2) {
						setBlock(xTile, yTile, new Block(Block.Type.GLASS));
					} else {
						setBlock(xTile, yTile, new Block(wall));
					}
				} else if ((pixel & 0xFFFFFF) == 0xFF0000) {
					playerSpawnPoint.set(xTile + 0.5, yTile + 0.5, playerSpawnPoint.z);
					
					int north = bitmap.getPixel(x, y - 1);
					int east = bitmap.getPixel(x + 1, y);
					int south = bitmap.getPixel(x, y + 1);
					int west = bitmap.getPixel(x - 1, y);
					
					if ((north & 0xFFFFFF) == 0x00FFFF) {
						playerSpawnPoint.z = Math.PI / 2.0;
					} else if ((east & 0xFFFFFF) == 0x00FFFF) {
						playerSpawnPoint.z = 0.0;
					} else if ((south & 0xFFFFFF) == 0x00FFFF) {
						playerSpawnPoint.z = -Math.PI / 2.0;
					} else if ((west & 0xFFFFFF) == 0x00FFFF) {
						playerSpawnPoint.z = Math.PI;
					} else {
						playerSpawnPoint.z = Math.PI / 2.0;
					}
					
				} else if ((pixel & 0xFFFFFF) == 0xFFFF00) {
					setBlock(xTile, yTile, new Block(Sprite.Type.KEY));
				} else if ((pixel & 0xFFFFFF) == 0x00FF00) {
					setBlock(xTile, yTile, new Block(Block.Type.DOOR));
				}
			}
		}
	}
	
	// Randomly create a level with a given size
	public Level(int width, int height, Level.Theme theme) {
		this.width = width;
		this.height = height;
		this.theme = theme;
		
		Block.Type wall;
		Sprite.Type grass;
		switch (theme) {
		case SANDSTORM:
			wall = Block.Type.SANDSTONE;
			grass = Sprite.Type.DEADBUSH;
			break;
		case BLIZZARD:
			wall = Block.Type.SNOW;
			grass = Sprite.Type.TALL_GRASS;
			break;
		case DUNGEON:
			wall = Block.Type.STONE;
			grass = Sprite.Type.TALL_GRASS;
			break;
		default:
			wall = Block.Type.COBBLESTONE;
			grass = Sprite.Type.TALL_GRASS;
		}
		playerSpawnPoint = new Vector3(-1, -1, 0);
		
		blocks = new Block[width * height];
		Random random = new Random(0);
		for (int i = 0; i < blocks.length; i++) {
			if (random.nextInt(10) < 3) {
				if (random.nextInt(10) < 5) {
					blocks[i] = new Block(wall);
				} else {
					blocks[i] = new Block(Block.Type.GLASS);
				}
			} else {
				if (random.nextInt(10) < 1) {
					blocks[i] = new Block(grass);
				}
			}
		}
		
//		blocks[4] = new Block(Sprite.Type.KEY);
//		blocks[16] = new Block(Block.Type.DOOR);
	}
	
	// Get the block at the given coordinates
	public Block getBlock(Vector2 v) {
		return getBlock(v.x, v.y);
	}
	
	public Block getBlock(double x, double y) {
		
		int xTile = (int) Math.floor(x);
		int yTile = (int) Math.floor(y);
		
		return getBlock(xTile, yTile);
	}
	
	public Block getBlock(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height) { // Make sure it is in the array's bounds
			return null;
		}
		return blocks[x + y * width];
	}
	
	// Set the block at coordinate [x, y] to a different block.
	public void setBlock(Vector2 v, Block block) {
		setBlock(v.x, v.y, block);
	}
	
	public void setBlock(double x, double y, Block block) {
		
		int xTile = (int) Math.floor(x);
		int yTile = (int) Math.floor(y);
		
		setBlock(xTile, yTile, block);
	}
	
	public void setBlock(int x, int y, Block block) {
		blocks[x + y * width] = block;
	}
	
	// Set the block at coordinate [x, y] to null
	public void removeBlock(Vector2 v) {
		setBlock(v.x, v.y, null);
	}
	
	public void removeBlock(double x, double y) {
		setBlock(x, y, null);
	}
	
	public void removeBlock(int x, int y) {
		setBlock(x, y, null);
	}
	
	// Get this level's fog color according to its theme
	public int getFogColor() {
		switch (theme) {
		case SANDSTORM:
			return 0xFCF8BD;
		case BLIZZARD:
			return 0xE7E7E7;
		case DUNGEON:
			return 0;
		default:
			return 0;
		}
	}
	
	// Get this level's floor texture according to its theme
	public Bitmap getFloorTexture() {
		switch (theme) {
		case SANDSTORM:
			return Assets.SAND;
		case BLIZZARD:
			return Assets.SNOW;
		case DUNGEON:
			return Assets.COBBLESTONE;
		default:
			return Assets.DEBUG;
		}
	}
	
	// check whether this level's theme has a ceiling
	public boolean hasCeiling() {
		switch (theme) {
		case SANDSTORM:
			return false;
		case BLIZZARD:
			return false;
		case DUNGEON:
			return true;
		default:
			return true;
		}
	}

	// Get the player's spawn point
	public Vector3 getPlayerSpawnPoint() {
		return new Vector3(playerSpawnPoint);
	}

	// Set the player's spawn point
//	public void setPlayerSpawnPoint(Vector3 playerSpawnPoint) {
//		this.playerSpawnPoint = playerSpawnPoint;
//	}
//	
//	public void setPlayerSpawnPoint(double x, double y, double rotation) {
//		setPlayerSpawnPoint(x, y, rotation);
//	}
//	
//	public void setPlayerSpawnPoint(int x, int y, double rotation) {
//		setPlayerSpawnPoint(x + 0.5, y + 0.5, rotation);
//	}
}
