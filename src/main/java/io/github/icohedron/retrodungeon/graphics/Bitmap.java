package io.github.icohedron.retrodungeon.graphics;

import io.github.icohedron.retrodungeon.Assets;

// A class for holding, accessing, and manipulating an array of integers representing pixels.
public class Bitmap {

	public final int width;
	public final int height;
	public final int[] pixels;
	
	public Bitmap(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
	}
	
	public void clear() {
		clear(0);
	}
	
	// Clears the pixel array using a color.
	public void clear(int color) {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = color;
		}
	}
	
	// Draw other bitmaps in an offset position of this pixel array.
	public void draw(Bitmap bitmap, int xOffset, int yOffset) {
		for (int bitmapY = 0; bitmapY < bitmap.height; bitmapY++) {
			int y = yOffset + bitmapY;
			if (y < 0 || y >= height) {
				continue;
			}
			for (int bitmapX = 0; bitmapX < bitmap.width; bitmapX++) {
				int x = xOffset + bitmapX;
				if (x < 0 || x >= width) {
					continue;
				}
				
				int pixel = bitmap.pixels[bitmapX + bitmapY * bitmap.width];
				if (((pixel >> 24) & 0xFF) > 0) {
					pixels[x + y * width] = pixel;
				}
			}
		}
	}
	
//	// This method is useful for drawing from sprite sheets.
//	// Draw a segment of a bitmap, separated by strides in the x and y coordinate.
//	// x/yStrideSize is the size of a single stride in pixels in the x/y directions.
//	// x/yStrides is the number of strides to offset by.
//	// x/yOffset is the offset of the pixels to be drawn after applying the strides.
//	public void draw(Bitmap bitmap, int xStrideSize, int yStrideSize, int xStrides, int yStrides, int xOffset, int yOffset) {
//		
//		int startX = xStrideSize * xStrides;
//		int endX = startX + xStrideSize;
//		int startY = yStrideSize * yStrides;
//		int endY = startY + yStrideSize;
//		
//		for (int bitmapY = startY; bitmapY < endY; bitmapY++) {
//			int y = yOffset + (bitmapY - startY);
//			if (y < 0 || y >= height) {
//				continue;
//			}
//			for (int bitmapX = startX; bitmapX < endX; bitmapX++) {
//				int x = xOffset + (bitmapX - startX);
//				if (x < 0 || x >= width) {
//					continue;
//				}
//				
//				int pixel = bitmap.pixels[bitmapX + bitmapY * bitmap.width];
//				if (((pixel >> 24) & 0xFF) > 0) {
//					pixels[x + y * width] = pixel;
//				}
//			}
//		}
//	}
	
//	// This method is useful for drawing from sprite sheets.
//	// Draw a segment of a bitmap, separated by strides in the x and y coordinate.
//	// x/yStrideSize is the size of a single stride in pixels in the x/y directions.
//	// x/yStrides is the number of strides to offset by.
//	// x/yOffset is the offset of the pixels to be drawn after applying the strides.
//  // color is the color to draw instead of the bitmap's color.
	public void draw(Bitmap bitmap, int strideX, int strideY, int xStrides, int yStrides, int xOffset, int yOffset, int color) {
		
		int startX = strideX * xStrides;
		int endX = startX + strideX;
		int startY = strideY * yStrides;
		int endY = startY + strideY;
		
		for (int bitmapY = startY; bitmapY < endY; bitmapY++) {
			int y = yOffset + (bitmapY - startY);
			if (y < 0 || y >= height) {
				continue;
			}
			for (int bitmapX = startX; bitmapX < endX; bitmapX++) {
				int x = xOffset + (bitmapX - startX);
				if (x < 0 || x >= width) {
					continue;
				}
				
				int pixel = bitmap.pixels[bitmapX + bitmapY * bitmap.width];
				if (((pixel >> 24) & 0xFF) > 0) {
					pixels[x + y * width] = color;
				}
			}
		}
	}
	
	// Draw text onto this bitmap
	public void draw(String text, int xOffset, int yOffset, int color) {
		for (int i = 0; i < text.length(); i++) {
			int pos = Assets.FONT_CHARS.indexOf(text.charAt(i));
			if (pos < 0) {
				return;
			}
			
			int xStrides = pos % Assets.FONT_STRIDE_WIDTH;
			int yStrides = pos / Assets.FONT_STRIDE_WIDTH;
			
			draw(Assets.FONT, Assets.FONT_STRIDE_X, Assets.FONT_STRIDE_Y, xStrides, yStrides, xOffset + Assets.FONT_STRIDE_X * i, yOffset, color);
		}
	}
	
	// Fill a rectangle in this bitmap with a specified color.
	public void fill(int startX, int startY, int endX, int endY, int color) {
		for (int y = startY; y < endY; y++) {
			for (int x = startX; x < endX; x++) {
				pixels[x + y * width] = color;
			}
		}
	}
	
	// Get a pixel from this bitmap.
	public int getPixel(int x, int y) {
		try {
			return pixels[x + y * width];
		} catch (IndexOutOfBoundsException e) {
			return -1;
		}
	}
	
//	// Get a pixel from this bitmap using strides.
//	// Useful for getting pixels if this bitmap stores a sprite sheet.
//	public int getPixel(int xStrideSize, int yStrideSize, int xStrides, int yStrides, int x, int y) {
//		int startX = xStrideSize * xStrides;
//		int startY = yStrideSize * yStrides;
//		
//		int bitX = startX + x;
//		int bitY = startY + y;
//		
//		return pixels[bitX + bitY * width];
//	}
}
