package io.github.icohedron.retrodungeon.level;

import io.github.icohedron.retrodungeon.Assets;
import io.github.icohedron.retrodungeon.graphics.Bitmap;

// A physical object in the world. A cubic structure.
public class Block {
	
	// Defines the different types of blocks
	public static enum Type {
		STATIC_SPRITE,
		STONE,
		SAND,
		COBBLESTONE,
		SANDSTONE,
		GLASS,
		BEDROCK,
		SNOW,
		DOOR
	}
	
	// All transparent blocks
	public static final Block.Type[] TRANSPARENT_BLOCKS = {Block.Type.STATIC_SPRITE, Block.Type.GLASS, Block.Type.DOOR};
	
	// Get the block's texture bitmap
	public static Bitmap getBitmap(Block.Type type) {
		switch (type) {
		case STONE:
			return Assets.STONE;
		case SAND:
			return Assets.SAND;
		case COBBLESTONE:
			return Assets.COBBLESTONE;
		case SANDSTONE:
			return Assets.SANDSTONE;
		case GLASS:
			return Assets.GLASS;
		case SNOW:
			return Assets.SNOW;
		case DOOR:
			return Assets.DOOR;
		default:
			return Assets.DEBUG;
		}
	}
	
	private Block.Type type;
	private Sprite sprite;
	
	public Block(Block.Type type) {
		this.type = type;
		sprite = new Sprite();
	}
	
	public Block(Sprite.Type type) {
		this.type = Block.Type.STATIC_SPRITE;
		sprite = new Sprite(type);
	}
	
	// Check if this block is solid. The only non-solid block is a sprite.
	public boolean isSolid() {
		return type != Block.Type.STATIC_SPRITE;
	}
	
	// Check if this block is transparent
	public boolean isTransparent() {
		for (Block.Type t : Block.TRANSPARENT_BLOCKS) {
			if (type == t) {
				return true;
			}
		}
		return false;
	}
	
	public Bitmap getBitmap() {
		return getBitmap(type);
	}
	
	// Get this block's sprite
	public Sprite getSprite() {
		return sprite;
	}

	// Set this block's sprite
	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	// Get the type of this block
	public Block.Type getType() {
		return type;
	}

	// Set the type of this block
	public void setType(Block.Type type) {
		this.type = type;
	}
}
