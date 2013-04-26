package org.dotme.arpg;


public class BaseItem {
	private int speed = 0;
	public BaseItem clone() {
		return this;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
}
