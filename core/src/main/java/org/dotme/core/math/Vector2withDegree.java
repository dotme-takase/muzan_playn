package org.dotme.core.math;

public class Vector2withDegree extends Vector2 {

	public float degree;
	public boolean flip;

	public Vector2withDegree() {
	}

	public Vector2withDegree(float x, float y, float degree) {
		super(x, y);
		this.degree = degree;
		this.flip = false;
	}

	public Vector2withDegree(float x, float y, float degree, boolean flip) {
		super(x, y);
		this.degree = degree;
		this.flip = flip;
	}
}
