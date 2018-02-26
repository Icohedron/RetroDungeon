package io.github.icohedron.retrodungeon.level;

import io.github.icohedron.retrodungeon.Assets;
import io.github.icohedron.retrodungeon.graphics.Bitmap;

public class Sprite {

	// The different types of sprites
	public static enum Type {
		AIR,
		TALL_GRASS,
		DEADBUSH,
		KEY
	}
	
//	// All non-solid sprites
//	public static final Sprite.Type[] NON_SOLID_SPRITES = {Sprite.Type.AIR, Sprite.Type.TALL_GRASS, Sprite.Type.DEADBUSH, Sprite.Type.KEY};
	
	// Get the corresponding bitmap texture of the sprite type
	public static Bitmap getBitmap(Sprite.Type type) {
		switch (type) {
		case TALL_GRASS:
			return Assets.TALL_GRASS;
		case DEADBUSH:
			return Assets.DEADBUSH;
		case KEY:
			return Assets.GOLD_NUGGET;
		default:
			return Assets.DEBUG;
		}
	}
	
	private Sprite.Type type;
	
	public Sprite() {
		type = Sprite.Type.AIR;
	}
	
	public Sprite(Sprite.Type type) {
		this.type = type;
	}
	
//	public boolean isSolid() {
//		for (Sprite.Type t : Sprite.NON_SOLID_SPRITES) {
//			if (type == t) {
//				return false;
//			}
//		}
//		return true;
//	}
	
	// Get this sprite's bitmap texture
	public Bitmap getBitmap() {
		return getBitmap(type);
	}

	// Get this sprite's type
	public Sprite.Type getType() {
		return type;
	}

//	// Set this sprite's type
//	public void setType(Sprite.Type type) {
//		this.type = type;
//	}
}
