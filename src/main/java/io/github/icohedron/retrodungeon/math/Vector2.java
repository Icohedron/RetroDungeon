package io.github.icohedron.retrodungeon.math;

// A vector consisting of two double components
public class Vector2 {
	
	public double x;
	public double y;
	
	public Vector2() {
		this.set(0);
	}
	
	public Vector2(Vector2 v) {
		this.set(v);
	}
	
	public Vector2(double d) {
		this.set(d);
	}
	
	public Vector2(double x, double y) {
		this.set(x, y);
	}
	
	public double dot(Vector2 v) {
		return this.x * v.x + this.y * v.y;
	}
	
	public double length() {
		return Math.sqrt(this.dot(this));
	}
	
	public Vector2 normalize() {
		double length = this.length();
		if (length == 0) {
			return this;
		}
		return this.divide(length);
	}
	
	public Vector2 rotate(double radians) {
		double cos = Math.cos(radians);
		double sin = Math.sin(radians);
		return this.set(this.x * cos - this.y * sin, this.x * sin + this.y * cos);
	}
	
	public Vector2 add(double x, double y) {
		return this.set(this.x + x, this.y + y);
	}
	
	public Vector2 add(Vector2 v) {
		return this.add(v.x, v.y);
	}
	
	public Vector2 add(double d) {
		return this.add(d, d);
	}
	
	public Vector2 subtract(double x, double y) {
		return this.set(this.x - x, this.y - y);
	}
	
	public Vector2 subtract(Vector2 v) {
		return this.subtract(v.x, v.y);
	}
	
	public Vector2 subtract(double d) {
		return this.subtract(d, d);
	}
	
	public Vector2 multiply(double x, double y) {
		return this.set(this.x * x, this.y * y);
	}
	
	public Vector2 multiply(Vector2 v) {
		return this.multiply(v.x, v.y);
	}
	
	public Vector2 multiply(double d) {
		return this.multiply(d, d);
	}
	
	public Vector2 divide(double x, double y) {
		return this.set(this.x / x, this.y / y);
	}
	
	public Vector2 divide(Vector2 v) {
		return this.divide(v.x, v.y);
	}
	
	public Vector2 divide(double d) {
		return this.divide(d, d);
	}
	
	public Vector2 set(double x, double y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	public Vector2 set(Vector2 v) {
		return this.set(v.x, v.y);
	}
	
	public Vector2 set(double d) {
		return this.set(d, d);
	}
	
	@Override
	public String toString() {
		return "Vector2: ( " + this.x + ", " + this.y + " )";
	}
	
	public Vector2 copy() {
		return new Vector2(this);
	}
}
