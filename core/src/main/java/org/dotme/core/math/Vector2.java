package org.dotme.core.math;

public class Vector2 {

	public static final Vector2 Zero = new Vector2(0, 0);

	public float x;
	public float y;

	public Vector2(float X, float Y) {
		this.x = X;
		this.y = Y;
	}

	public Vector2() {
	}

	public Vector2 sub(Vector2 v) {
		return new Vector2(x - v.x, y - v.y);
	}

	public float LengthSquared() {
		return x * x + y * y;
	}

	public Vector2 mul(float a) {
		return new Vector2(x * a, y * a);
	}

	public Vector2 add(Vector2 v) {
		return new Vector2(x + v.x, y + v.y);
	}

	public Vector2 mul(Vector2 v) {
		return new Vector2(x * v.x, y * v.y);
	}
}
