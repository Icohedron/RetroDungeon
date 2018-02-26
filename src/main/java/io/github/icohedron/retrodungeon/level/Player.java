package io.github.icohedron.retrodungeon.level;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import io.github.icohedron.retrodungeon.Input;
import io.github.icohedron.retrodungeon.math.Vector2;
import io.github.icohedron.retrodungeon.math.Vector3;

// The view-point / camera and the thing the user controls
public class Player {
	
	public static final double SPEED = 0.05;
	public static final double MOUSE_SENSITIVITY = 0.005;
	public static final double COLLISION_PADDING = 0.15;
	
	public static final double HEIGHT = 0.15;
	public static final double PITCH = 0.4;
	
	private Vector3 position;
	private double rotation;
	
	private Vector3 forward;
	private Vector3 right;
	private final Vector3 up = new Vector3(0.0, 1.0, 0.0);
	
	private List<Block> collisions; // List of blocks collided with [North, East, South, West]
	
	public Player(Vector2 worldPos, double rotation) {
		this(worldPos.x, worldPos.y, rotation);
	}
	
	public Player(double worldPosX, double worldPosY, double rotation) {
		position = new Vector3(worldPosX, HEIGHT, worldPosY);
		this.rotation = rotation;
		forward = new Vector3();
		right = new Vector3();
		updateVectors();
		collisions = new ArrayList<Block>();
	}
	
	// Update the player's values
	public void update(Level level) {
		updateMovement(level);
	}
	
	// Update the player's movement
	private void updateMovement(Level level) {
		Vector2 input = new Vector2();
		if (Input.isKeyDown(KeyEvent.VK_W) || Input.isKeyDown(KeyEvent.VK_UP)) {
			input.y += 1;
		}
		if (Input.isKeyDown(KeyEvent.VK_S) || Input.isKeyDown(KeyEvent.VK_DOWN)) {
			input.y -= 1;
		}
		if (Input.isKeyDown(KeyEvent.VK_A) || Input.isKeyDown(KeyEvent.VK_LEFT)) {
			input.x -= 1;
		}
		if (Input.isKeyDown(KeyEvent.VK_D) || Input.isKeyDown(KeyEvent.VK_RIGHT)) {
			input.x += 1;
		}
		
		Vector3 direction = forward.multiply(input.y).add(right.multiply(input.x));
		direction.normalize().multiply(SPEED);
		
		collisions.clear();
		
		Vector3 normal = new Vector3();
		if (direction.x < 0) {
			Block block = level.getBlock(position.x + direction.x - COLLISION_PADDING, position.z);
			if (block != null && block.isSolid()) { // Collision in the west
				normal.add(-direction.x, 0.0, 0.0);
				collisions.add(block);
			}
		} else if (direction.x > 0) {
			Block block = level.getBlock(position.x + direction.x + COLLISION_PADDING, position.z);
			if (block != null && block.isSolid()) { // Collision in the east
				normal.add(-direction.x, 0.0, 0.0);
				collisions.add(block);
			}
		}

		if (direction.z < 0) {
			Block block = level.getBlock(position.x, position.z + direction.z - COLLISION_PADDING);
			if (block != null && block.isSolid()) {  // Collision in the south
				normal.add(0.0, 0.0, -direction.z);
				collisions.add(block);
			}
		} else if (direction.z > 0) {
			Block block = level.getBlock(position.x, position.z + direction.z + COLLISION_PADDING);
			if (block != null && block.isSolid()) {  // Collision in the north
				normal.add(0.0, 0.0, -direction.z);
				collisions.add(block);
			}
		}
		
		normal.normalize();
		
		Vector3 projected = normal.multiply(direction.dot(normal));
		direction.subtract(projected);
		
		position.add(direction);
		
		addRotation(-Input.getMouseDiffX() * MOUSE_SENSITIVITY);
	}
	
	// Update the player's forward and right vectors
	private void updateVectors() {
		double cos = Math.cos(rotation);
		double sin = Math.sin(rotation);
		forward.set(cos, 0.0, sin);
		
		right = up.cross(forward);
	}
	
	// Check if the player has collided with an object since its last update
	public boolean hasCollided() {
		for (Block b : collisions) {
			if (b != null) {
				return true;
			}
		}
		return false;
	}
	
	// Get the array of blocks which the player has collided with since its last update
	public List<Block> getCollidedBlocks() {
		return collisions;
	}
	
	// Get the current block that the player is standing on
	public Block getCurrentBlock(Level level) { // Get the block the player is standing on/in
		return level.getBlock(getWorldPosition());
	}
	
//	// Get this player's world x coordinate
//	public double getWorldX() {
//		return position.x;
//	}
//	
//	// Get this player's world y coordinate
//	public double getWorldY() {
//		return position.y;
//	}
//	
//	// Set this player's world x coordinate
//	public void setWorldX(double x) {
//		position.x = x;
//	}
//	
//	// Set this player's world y coordinate
//	public void setWorldY(double y) {
//		position.z = y;
//	}
	
	// Get this player's x coordinate
	public double getX() {
		return position.x;
	}
	
	// Get this player's y coordinate
	public double getY() {
		return position.y;
	}
	
	// Get this player's z coordinate
	public double getZ() {
		return position.z;
	}
	
//	// Set this player's x coordinate
//	public void setX(double x) {
//		position.x = x;
//	}
//	
//	// Set this player's y coordinate
//	public void setY(double y) {
//		position.y = y;
//	}
//	
//	// Set this player's z coordinate
//	public void setZ(double z) {
//		position.z = z;
//	}
	
//	// Get this player's position vector (copy)
//	public Vector3 getPosition() {
//		return new Vector3(position);
//	}
	
	// Get this player's world position vector (copy)
	public Vector2 getWorldPosition() {
		return new Vector2(position.x, position.z);
	}
	
	// Get this player's rotation
	public double getRotation() {
		return rotation;
	}
	
	// Add to the player's rotation
	public void addRotation(double radians) {
		rotation += radians;
		updateVectors();
	}
	
//	// Set this player's rotation
//	public void setRotation(double radians) {
//		rotation = radians;
//		updateVectors();
//	}

}
