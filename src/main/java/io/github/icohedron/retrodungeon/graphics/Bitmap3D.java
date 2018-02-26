package io.github.icohedron.retrodungeon.graphics;

import io.github.icohedron.retrodungeon.Assets;
import io.github.icohedron.retrodungeon.RetroDungeon;
import io.github.icohedron.retrodungeon.level.Block;
import io.github.icohedron.retrodungeon.level.Level;
import io.github.icohedron.retrodungeon.level.Player;
import io.github.icohedron.retrodungeon.level.Sprite;
import io.github.icohedron.retrodungeon.math.Vector2;

// An extension of Bitmap that is used for rendering in 3D
public class Bitmap3D extends Bitmap {
	
	public static final int VOXEL_SIZE = RetroDungeon.TEXTURE_SIZE;
	public static final int VOXEL_SIZE_1M = VOXEL_SIZE - 1;
	public static final int HALF_VOXEL_SIZE = VOXEL_SIZE / 2;
	public static final double RENDER_DISTANCE = 6.0;
	public static final double NEAR_CLIPPING_DISTANCE = 0.001;
	public static final double FOV = Math.toRadians(60.0);
	
	private int fogColor;
	private Bitmap floorTexture;
	
	private final double fov = Math.tan(FOV / 2.0);
	
	private int xCenter;
	private int yCenter;
	private final double aspect_ratio;
	
	private final double[] depthBuffer;

	public Bitmap3D(int width, int height) { 
		super(width, height);
		xCenter = (int) (width / 2.0);
		yCenter = (int) (height / (2.0 + Player.PITCH));
		aspect_ratio = (double) width / height;
		depthBuffer = new double[pixels.length];
		floorTexture = Assets.COBBLESTONE;
		fogColor = 0;
	}
	
	// Supplying the level and the player, this method will render all elements that need to be displayed from the player's perspective
	public void renderLevel(Level level, Player player) {
		fogColor = level.getFogColor();
		floorTexture = level.getFloorTexture();
		renderFloor(player, level.hasCeiling());
		renderBlocks(level, player);
		renderFog();
	}
	
	// Renders all the walls/blocks in the level from the player's perspective
	public void renderBlocks(Level level, Player player) {
		for (int y = 0; y < level.height; y++) {
			for (int x = 0; x < level.width; x++) {
				
				// Get the current block
				Block block = level.getBlock(x, y);
				if (block == null) {
					continue; // Nothing here to render
				}
				
				// Check if it should be rendered as a sprite
				if (block.getType() == Block.Type.STATIC_SPRITE) {
					Sprite sprite = block.getSprite();
					if (sprite.getType() == Sprite.Type.AIR) {
						continue;  // Nothing to render
					}
					// Render the sprite!
					renderSprite(player, x + 0.5, y + 0.5, sprite);
					continue;
				}
				
				// Get all adjacent blocks
				Block north = level.getBlock(x, y + 1);
				Block east = level.getBlock(x + 1, y);
				Block south = level.getBlock(x, y - 1);
				Block west = level.getBlock(x - 1, y);
				
				// Render differently depending on if this is a transparent block
				if (block.isTransparent()) {
					// Render the north-facing wall only if the north-adjacent block is null or a sprite
					if (north == null || north.getType() == Block.Type.STATIC_SPRITE) {
						renderWall(player, x + 1, y + 1, x, y + 1, block);
					}
					// ... So on and so forth for the other directions...
					if (east == null || east.getType() == Block.Type.STATIC_SPRITE) {
						renderWall(player, x + 1, y, x + 1, y + 1, block);
					}
					
					if (south == null || south.getType() == Block.Type.STATIC_SPRITE) {
						renderWall(player, x, y, x + 1, y, block);
					}
					
					if (west == null || west.getType() == Block.Type.STATIC_SPRITE) {
						renderWall(player, x, y + 1, x, y, block);
					}
				} else {
					// Render the north-facing wall only if the north-adjacent block is null or transparent
					if (north == null || north.isTransparent()) {
						renderWall(player, x + 1, y + 1, x, y + 1, block);
					}
					// ... So on and so forth for the other directions...
					if (east == null || east.isTransparent()) {
						renderWall(player, x + 1, y, x + 1, y + 1, block);
					}
					
					if (south == null || south.isTransparent()) {
						renderWall(player, x, y, x + 1, y, block);
					}
					
					if (west == null || west.isTransparent()) {
						renderWall(player, x, y + 1, x, y, block);
					}
				}
			}
		}
	}
	
	// Render the floor of the level from the player's perspective. THIS MUST BE RENDERED FIRST.
	public void renderFloor(Player player, boolean drawCeiling) {
		
		// Subtract 90 degrees to align player rotation 0 to the x-axis. From this rendering, the player would face down the world y axis by default.
		double playerCos = Math.cos(player.getRotation() - Math.PI / 2);
		double playerSin = Math.sin(player.getRotation() - Math.PI / 2);
		
		for (int y = 0; y < height; y++) {
			double nsy = (2.0 * (y - yCenter) / height) * fov; // "Normalized" screen y [-1, 1] * fov
			
			boolean floor;
			double vsz; // View-space z-coordinate (depth)
			if (nsy == 0) {
				floor = false; // Doesn't matter, this will be discarded anyway
				vsz = RENDER_DISTANCE;
			} else if (nsy > 0) {
				floor = true;
				vsz = (player.getY() + 0.5) / nsy; // Towards the center of the screen, ny gets closer to 0. Dividing by ny will simulate depth.
																// The floor is 0.5 units below the player.
			} else {
				floor = false; // It's a ceiling!
				vsz = (player.getY() - 0.5) / nsy; // The ceiling is 0.5 units above the player.
			}
			
			for (int x = 0; x < width; x++) {
				
				if (vsz > RENDER_DISTANCE) { // It's out of the render distance. Just discard this pixel.
					depthBuffer[x + y * width] = RENDER_DISTANCE;
					pixels[x + y * width] = 0;
					continue;
				}
				
				double vsx = (2.0 * (x - xCenter) / width) * aspect_ratio * fov; // "Normalized" screen x coordinate [-1, 1] * aspect_ratio * fov
				vsx *= vsz; // View-space x coordinate. The view-space x coordinate is directly proportional to the depth / z coordinate.
				
				// Convert view-space coordinates to world-space by applying the appropriate transformations.
				double worldX = vsx * playerCos - vsz * playerSin + player.getX();
				double worldY = vsx * playerSin + vsz * playerCos + player.getZ();
				
				// Scale the world coordinates by the voxel size, and cast to an int. This is required for texturing.
				int sWorldX = (int) Math.floor(worldX * VOXEL_SIZE);
				int sWorldY = (int) Math.floor(worldY * VOXEL_SIZE);
				
				// Get the texture coordinate (texel) of the current world coordinates by taking the remainder of the division by the texture size.
				// Texture size must be a power of two. (x & (2^y - 1)) is equal to (x % 2^y), except for negative values.
				int xTexel = sWorldX & VOXEL_SIZE_1M;
				int yTexel = sWorldY & VOXEL_SIZE_1M;
				
				// Write the depth value of the pixel to the depth buffer.
				depthBuffer[x + y * width] = vsz;
//				pixels[x + y * width] = xTexel << 4 | yTexel << 12; // Used for visualizing the texels. This works best when the texture size / voxel size is 16 because it fits best in a single hexadecimal digit.
				
				// Color the pixel!
				if (floor || (!floor && drawCeiling)) {
					int color = floorTexture.getPixel(xTexel, yTexel);
					if (((color >> 24) & 0xFF) > 0) { // If the alpha component is greater than 0, draw the pixel.
						pixels[x + y * width] = color;
					} else {
						pixels[x + y * width] = fogColor;
					}
				} else {
					pixels[x + y * width] = fogColor;
				}
				
				// Specific floor tile texturing
//				int tileX = 0;
//				int tileY = 0;
//				
//				int tileWX = tileX * VOXEL_SIZE;
//				int tileWY = tileY * VOXEL_SIZE;
//				
//				if (worldX > tileWX && worldX < tileWX + VOXEL_SIZE && worldY > tileWY && worldY < tileWY + VOXEL_SIZE && floor) {
//					int[] textureCoordinate = TextureParser.getTexture(Block.Type.BEDROCK);
//					int color = ResourceLoader.minecraft.getPixel(textureCoordinate[0], textureCoordinate[1], xTexel, yTexel);
//					if (((color >> 24) & 0xFF) > 0) { // If the alpha component is greater than 0, draw the pixel.
//						pixels[x + y * width] = color;
//					}
//				}
				
			}
		}
	}
	
	// Renders "sprites", flat image-objects that always face the player.
	public void renderSprite(Player player, double x, double y, Sprite sprite) {
		
		// Check if the sprite is out of the render distance before rendering.
		Vector2 d = new Vector2(x, y);
		d.subtract(new Vector2(player.getX(), player.getZ()));
		if (d.length() > RENDER_DISTANCE) {
			return;
		}
		
		// Get the player's rotation
		double playerCos = Math.cos(player.getRotation() - Math.PI / 2);
		double playerSin = Math.sin(player.getRotation() - Math.PI / 2);
		
		// Convert from world space to view space
		double xs = x - player.getX();
		double ys = player.getY();
		double zs = y - player.getZ();
		
		double rxs =  xs * playerCos + zs * playerSin;
		double rzs = -xs * playerSin + zs * playerCos;
		
		xs = rxs;
		zs = rzs;
		
		// If the sprite is behind the player, don't render it
		if (zs < 0) {
			return;
		}
		
		// Project the view-space vectors onto the screen
		double xProjection = 1.0 / (aspect_ratio * fov) * (width / 2);
		int leftXPixel = (int) ((xs - 0.5) / zs * xProjection + xCenter); // Left side of the sprite
		int rightXPixel = (int) ((xs + 0.5) / zs * xProjection + xCenter); // Right side of the sprite
		
		double yProjection = 1.0 / fov * (height / 2);
		int topYPixel = (int) ((ys - 0.5) / zs * yProjection + yCenter); // Top of the sprite
		int bottomYPixel = (int) ((ys + 0.5) / zs * yProjection + yCenter); // Bottom of the sprite
		
		// Store the unclamped values for interpolation
		int unclampedLeftXPixel = leftXPixel;
		int unclampedRightXPixel = rightXPixel;
		
		int unclampedTopYPixel = topYPixel;
		int unclampedBottomYPixel = bottomYPixel;
		
		// Clamp the screen coordinates
		if (leftXPixel < 0) {
			leftXPixel = 0;
		}
		
		if (rightXPixel > width) {
			rightXPixel = width;
		}
		
		if (topYPixel < 0) {
			topYPixel = 0;
		}
		
		if (bottomYPixel > height) {
			bottomYPixel = height;
		}
		
		for (int xPixel = leftXPixel; xPixel < rightXPixel; xPixel++) {
			
			// Interpolation value for the x's
			double xInterp = (double) (xPixel - unclampedLeftXPixel) / (unclampedRightXPixel - unclampedLeftXPixel);
			int xTexel = (int) (VOXEL_SIZE * xInterp);
			
			for (int yPixel = topYPixel; yPixel < bottomYPixel; yPixel++) {
				
				double depth = zs;
				if (depthBuffer[xPixel + yPixel * width] < depth) { // If there is something drawn on the screen that is closer than this current pixel, don't draw!
					continue;
				}
				
				// Interpolation value for the y's
				double yInterp = (double) (yPixel - unclampedTopYPixel) / (unclampedBottomYPixel - unclampedTopYPixel);
				int yTexel = (int) (VOXEL_SIZE * yInterp);
				
//				depthBuffer[xPixel + yPixel * width] = depth;
//				pixels[xPixel + yPixel * width] = xTexel << 4 | yTexel << 12;
				
				Bitmap texture = sprite.getBitmap();
				int color = texture.getPixel(xTexel, yTexel);
				if (((color >> 24) & 0xFF) > 0) { // If the alpha component is greater than 0, draw the pixel.
					pixels[xPixel + yPixel * width] = color;
					depthBuffer[xPixel + yPixel * width] = depth;
				}
			}
		}
	}
	
	// Renders a single wall, given the starting tile and the end tile, and the block it is from.
	public void renderWall(Player player, int tileX0, int tileY0, int tileX1, int tileY1, Block block) {
		
		// Check if the wall is completely out of view before rendering (This is only approximate depth)
		Vector2 d = new Vector2(tileX0, tileY0);
		d.add(new Vector2(tileX1, tileY1));
		d.divide(2);
		d.subtract(new Vector2(player.getX(), player.getZ()));
		if (d.length() > (RENDER_DISTANCE * 1.25)) { // Multiply by 1.25 as a tolerance. Without, sometimes a wall may vanish when it is still supposed to be barely visible.
			return;
		}
		
		// Get the player's rotation
		double playerCos = Math.cos(player.getRotation() - Math.PI / 2);
		double playerSin = Math.sin(player.getRotation() - Math.PI / 2);
		
		// Convert from world coordinates to view space coordinates
		double leftX = tileX0 - player.getX();
		double rightX = tileX1 - player.getX();
		
		double topY = -0.5 + player.getY();
		double bottomY = 0.5 + player.getY();
		
		double leftZ = tileY0 - player.getZ();
		double rightZ = tileY1 - player.getZ();
		
		double rotLeftX =  leftX * playerCos + leftZ * playerSin;
		double rotLeftZ = -leftX * playerSin + leftZ * playerCos;
		
		leftX = rotLeftX;
		leftZ = rotLeftZ;
		
		double rotRightX =  rightX * playerCos + rightZ * playerSin;
		double rotRightZ = -rightX * playerSin + rightZ * playerCos;
		
		rightX = rotRightX;
		rightZ = rotRightZ;
		
		// Texture coordinate start and end points for the coordinates
		double xTexelStart = VOXEL_SIZE * 0.0;
		double xTexelEnd = VOXEL_SIZE * 1.0;
		
		// If the vertices are behind the player, find the intersection of the edge of the primitive with the player's near clipping plane and use that instead.
		if (leftZ < 0) {
			double zInterp = (NEAR_CLIPPING_DISTANCE - leftZ) / (rightZ - leftZ);
			double xIntersect = leftX + (rightX - leftX) * zInterp;
			double zIntersect = NEAR_CLIPPING_DISTANCE;
			
			xTexelStart = VOXEL_SIZE * zInterp;
			
			leftX = xIntersect;
			leftZ = zIntersect;
		}
		
		if (rightZ < 0) {
			double zInterp = (NEAR_CLIPPING_DISTANCE - leftZ) / (rightZ - leftZ);
			double ix = leftX + (rightX - leftX) * zInterp;
			double iz = NEAR_CLIPPING_DISTANCE;
			
			xTexelEnd = VOXEL_SIZE * zInterp;
			
			rightX = ix;
			rightZ = iz;
		}
		
		// Apply projection on the view space coordinates to get the screen pixel coordinates
		double xProjection = 1.0 / (aspect_ratio * fov) * (width / 2);
		int leftScreenX = (int) (leftX / leftZ * xProjection + xCenter); // Left side of the wall
		int rightScreenX = (int) (rightX / rightZ * xProjection + xCenter); // Right side of the wall
		
		// Store the unclamped values for interpolation
		int unclampedLeftScreenX = leftScreenX;
		int unclampedRightScreenX = rightScreenX;
		
		// Clamp the screen coordinates to the screen's bounds
		if (leftScreenX < 0) {
			leftScreenX = 0;
		}
		
		if (rightScreenX >= width) {
			rightScreenX = width;
		}
		
		// Apply projection on the view space coordinates to get the screen pixel coordinates
		double yProjection = 1.0 / fov * (height / 2);
		int topLeftScreenY = (int) (topY / leftZ * yProjection + yCenter); // Top of the left side
		int topRightScreenY = (int) (topY / rightZ * yProjection + yCenter); // Top of the right side
		int bottomLeftScreenY = (int) (bottomY / leftZ * yProjection + yCenter + 2); // Bottom of the left side // +2 offset to reduce the "floating" effect (wall appears to float above the ground)
		int bottomRightScreenY = (int) (bottomY / rightZ * yProjection + yCenter + 2); // Bottom of the right side // +2 offset to reduce the "floating" effect (wall appears to float above the ground)
		
		// Get the inverses of the depth for interpolation
		// Because projection is proportional to 1/z, the inverse of the depth must be used for linear interpolation.
		double inverseLeftZ = 1.0 / leftZ;
		double inverseRightZ = 1.0 / rightZ;
		
		// Because the x texel value is dependent on depth, it must be multiplied by the inverse of depth for linear interpolation
		xTexelStart *= inverseLeftZ;
		xTexelEnd *= inverseRightZ;
		
		for (int x = leftScreenX; x < rightScreenX; x++) {
			
			// Interpolation value for x
			double xInterp = (double) (x - unclampedLeftScreenX) / (unclampedRightScreenX - unclampedLeftScreenX);
			
			// Linearly interpolate the y values between the corners of the wall
			int topScreenY = (int) (topLeftScreenY + (topRightScreenY - topLeftScreenY) * xInterp);
			int bottomScreenY = (int) (bottomLeftScreenY + (bottomRightScreenY - bottomLeftScreenY) * xInterp);
			
			// Store the unclamped values for interpolation
			int unclampedTopScreenY = topScreenY;
			int unclampedBottomScreenY = bottomScreenY;
			
			// Clamp the values to the screen size
			if (topScreenY < 0) {
				topScreenY = 0;
			}
			
			if (bottomScreenY > height) {
				bottomScreenY = height;
			}
			
			// Linearly interpolate the inverse depth values. Then apply the inverse again to get the actual depth. 1 / (1 / z) == z
			double depth = 1.0 / (inverseLeftZ + (inverseRightZ - inverseLeftZ) * xInterp);
			
			// Linearly interpolate the x texel
			int xTexel = (int) ((xTexelStart + (xTexelEnd - xTexelStart) * xInterp) * depth);
			
			for (int y = topScreenY; y < bottomScreenY; y++) {
				
				// If there is something in front of this pixel, don't draw.
				if (depthBuffer[x + y * width] < depth) {
					continue;
				}
				
				// Interpolation value for y
				double yInterp = (double) (y - unclampedTopScreenY) / (unclampedBottomScreenY - unclampedTopScreenY);
				
				// Linearly interpolate the y texel
				int yTexel = (int) (VOXEL_SIZE * yInterp);
				
//				depthBuffer[x + y * width] = depth;
//				pixels[x + y * width] = xTexel << 4 | yTexel << 12;
				
				Bitmap texture = block.getBitmap();
				int color = texture.getPixel(xTexel, yTexel);
				if (((color >> 24) & 0xFF) > 0) { // If the alpha component is greater than 0, draw the pixel.
					depthBuffer[x + y * width] = depth;
					pixels[x + y * width] = color;
				}
			}
		}
		
	}
	
	// Using the depth buffer, apply fog to the scene.
	public void renderFog() {
		for (int i = 0; i < depthBuffer.length; i++) {
			
			// Get the current pixel color and split it into its components
			int color = pixels[i];
			int r = (color >> 16) & 0xFF;
			int g = (color >> 8) & 0xFF;
			int b =  color & 0xFF;
			
			// Split the fog color into its components
			int rf = (fogColor >> 16) & 0xFF;
			int gf = (fogColor >> 8) & 0xFF;
			int bf =  fogColor & 0xFF;
			
			// Get the fog interpolation value
			double fogInterp = depthBuffer[i] / RENDER_DISTANCE;
			// Smooth out the interpolation value using a Hermite function (Hermite Interpolation)
			fogInterp = (3.0 - 2.0 * fogInterp) * fogInterp * fogInterp;
			
			// Interpolate the new color components
			int rFinal = (int) (r + (rf - r) * fogInterp);
			int gFinal = (int) (g + (gf - g) * fogInterp);
			int bFinal = (int) (b + (bf - b) * fogInterp);
			
			// Output the new color to the pixel array
			pixels[i] = rFinal << 16 | gFinal << 8 | bFinal;
		}
	}
	
	// Return an int representing the fog color.
	public int getFogColor() {
		return fogColor;
	}

	// Set the fog color to an int
	public void setFogColor(int fogColor) {
		this.fogColor = fogColor;
	}

	// Get the floor texture's bitmap
	public Bitmap getFloorTexture() {
		return floorTexture;
	}

	// Set the floor texture's bitmap
	public void setFloorTexture(Bitmap floorTexture) {
		this.floorTexture = floorTexture;
	}
}
