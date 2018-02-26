package io.github.icohedron.retrodungeon;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import io.github.icohedron.retrodungeon.graphics.Bitmap;
import io.github.icohedron.retrodungeon.sound.Sound;

// The go-to class for all your file loading needs!
public class Assets {
	
	public static final Bitmap STONE = loadBitmap("/pixel_perfection/textures/blocks/stone.png");
	public static final Bitmap SNOW = loadBitmap("/pixel_perfection/textures/blocks/snow.png");
	public static final Bitmap GLASS = loadBitmap("/pixel_perfection/textures/blocks/glass.png");
	public static final Bitmap TALL_GRASS = loadBitmap("/pixel_perfection/textures/blocks/wheat_stage_2.png");
	public static final Bitmap COBBLESTONE = loadBitmap("/pixel_perfection/textures/blocks/cobblestone.png");
	public static final Bitmap SAND = loadBitmap("/pixel_perfection/textures/blocks/sand.png");
	public static final Bitmap SANDSTONE = loadBitmap("/pixel_perfection/textures/blocks/sandstone_normal.png");
	public static final Bitmap DEADBUSH = loadBitmap("/pixel_perfection/textures/blocks/deadbush.png");
	public static final Bitmap DOOR = loadBitmap("/pixel_perfection/textures/blocks/door_iron_upper.png");
	public static final Bitmap GOLD_NUGGET = loadBitmap("/pixel_perfection/textures/items/gold_nugget.png");
	public static final Bitmap DEBUG = loadBitmap("/pixel_perfection/textures/blocks/debug.png");
	
	public static final Bitmap FONT = loadBitmap("/bitmaps/font.png");
	public static final String FONT_CHARS = "" +
			"ABCDEFGHIJKLMNOPQRSTUVWXYZ.,!?\"'/\\<>()[]{}" +
			"abcdefghijklmnopqrstuvwxyz_               " +

			"0123456789+-=*:;#$%^                      " +
			"";
	public static final int FONT_STRIDE_X = 6;
	public static final int FONT_STRIDE_Y = 8;
	public static final int FONT_STRIDE_WIDTH = 42;
	
	public static final Sound CHANGE_SELECTION_SOUND = loadSound("/sound/ChangeSelection.wav");
	public static final Sound HIT_DOOR_SOUND = loadSound("/sound/HitDoor.wav");
	public static final Sound PICKUP_KEY_SOUND = loadSound("/sound/PickupKey.wav");
	public static final Sound SELECT_SOUND = loadSound("/sound/Select.wav");
	
	public static final Bitmap LEVEL_1 = loadBitmap("/levels/Level 1.png");
	public static final Bitmap LEVEL_2 = loadBitmap("/levels/Level 2.png");
	public static final Bitmap LEVEL_3 = loadBitmap("/levels/Level 3.png");
	
	// Load an external image onto a bitmap
	public static Bitmap loadBitmap(String filePath) {
		Bitmap bitmap = null;
		try {
			BufferedImage image = ImageIO.read(Assets.class.getResource(filePath));
			bitmap = new Bitmap(image.getWidth(), image.getHeight());
			image.getRGB(0, 0, image.getWidth(), image.getHeight(), bitmap.pixels, 0, image.getWidth());
		} catch (Exception e) {
			try {
				BufferedImage image = ImageIO.read(new File(filePath));
				bitmap = new Bitmap(image.getWidth(), image.getHeight());
				image.getRGB(0, 0, image.getWidth(), image.getHeight(), bitmap.pixels, 0, image.getWidth());
			} catch (Exception e2) {
				return null;
			}
		}
		return bitmap;
	}
	
	// Load a sound file
	public static Sound loadSound(String filePath) {
		Sound sound = null;
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(Assets.class.getResource(filePath));
			Clip clip = AudioSystem.getClip();
			clip.open(ais);
			sound = new Sound(clip);
		} catch (Exception e) {
			return null;
		}
		
		return sound;
	}
}
