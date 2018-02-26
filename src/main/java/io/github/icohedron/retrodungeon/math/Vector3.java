package io.github.icohedron.retrodungeon.math;

// A vector consisting of three double components
public class Vector3 {
	
	public double x;
	public double y;
	public double z;
	
	public Vector3() {
		this.set(0);
	}
	
	public Vector3(Vector3 v) {
		this.set(v);
	}
	
	public Vector3(double d) {
		this.set(d);
	}
	
	public Vector3(double x, double y, double z) {
		this.set(x, y, z);
	}
	
	public double dot(Vector3 v) {
		return this.x * v.x + this.y * v.y + this.z * v.z;
	}
	
	public double length() {
		return Math.sqrt(this.dot(this));
	}
	
	public Vector3 cross(Vector3 v) {
		double x = this.y * v.z - this.z * v.y;
		double y = this.z * v.x - this.x * v.z;
		double z = this.x * v.y - this.y * v.x;
		return new Vector3(x, y, z);
	}
	
	public Vector3 normalize() {
		double length = this.length();
		if (length == 0) {
			return this;
		}
		return this.divide(length);
	}
	
	public Vector3 add(double x, double y, double z) {
		return this.set(this.x + x, this.y + y, this.z + z);
	}
	
	public Vector3 add(Vector3 v) {
		return this.add(v.x, v.y, v.z);
	}
	
	public Vector3 add(double d) {
		return this.add(d, d, d);
	}
	
	public Vector3 subtract(double x, double y, double z) {
		return this.set(this.x - x, this.y - y, this.z - z);
	}
	
	public Vector3 subtract(Vector3 v) {
		return this.subtract(v.x, v.y, v.z);
	}
	
	public Vector3 subtract(double d) {
		return this.subtract(d, d, d);
	}
	
	public Vector3 multiply(double x, double y, double z) {
		return this.set(this.x * x, this.y * y, this.z * z);
	}
	
	public Vector3 multiply(Vector3 v) {
		return this.multiply(v.x, v.y, v.z);
	}
	
	public Vector3 multiply(double d) {
		return this.multiply(d, d, d);
	}
	
	public Vector3 divide(double x, double y, double  z) {
		return this.set(this.x / x, this.y / y, this.z / z);
	}
	
	public Vector3 divide(Vector3 v) {
		return this.divide(v.x, v.y, v.z);
	}
	
	public Vector3 divide(double d) {
		return this.divide(d, d, d);
	}
	
	public Vector3 set(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	
	public Vector3 set(Vector3 v) {
		return this.set(v.x, v.y, v.z);
	}
	
	public Vector3 set(double d) {
		return this.set(d, d, d);
	}
	
	@Override
	public String toString() {
		return "Vector3: ( " + this.x + ", " + this.y + ", " + this.z + " )";
	}
	
	public Vector3 copy() {
		return new Vector3(this);
	}
}
