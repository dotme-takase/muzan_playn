package org.dotme.arpg;

public class BaseItem implements Cloneable {
	private int speed = 0;
	private int hitPoint = 0;
	private int maxHitPoint = 0;
	private int bonusPoint = 0;
	private int range = 0;
	private int spriteFrame = -1;
	private String type = null;

	public BaseItem clone() {
		return this.copyTo(new BaseItem());
	}

	protected BaseItem copyTo(BaseItem dest) {
		dest.speed = this.speed;
		dest.hitPoint = this.hitPoint;
		dest.maxHitPoint = this.maxHitPoint;
		dest.bonusPoint = this.bonusPoint;
		dest.range = this.range;
		dest.type = this.type;
		dest.spriteFrame = this.spriteFrame;
		return dest;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getHitPoint() {
		return hitPoint;
	}

	public void setHitPoint(int hitPoint) {
		this.hitPoint = hitPoint;
	}

	public int getMaxHitPoint() {
		return maxHitPoint;
	}

	public void setMaxHitPoint(int maxHitPoint) {
		this.maxHitPoint = maxHitPoint;
	}

	public int getBonusPoint() {
		return bonusPoint;
	}

	public void setBonusPoint(int bonusPoint) {
		this.bonusPoint = bonusPoint;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}
}
