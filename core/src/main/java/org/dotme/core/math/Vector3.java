package org.dotme.core.math;

public class Vector3 {

	public float x, y, z;

	public Vector3() {
	}

	public Vector3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void Normalize() {
		float len = (float) Math.sqrt(x * x + y * y + z * z);
		x /= len;
		y /= len;
		z /= len;
	}
}
