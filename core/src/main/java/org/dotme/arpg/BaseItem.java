package org.dotme.arpg;

public class BaseItem implements Cloneable {
	public static final int TYPE_SWORD = 100;
	public static final int TYPE_SHIELD = 200;
	public static final int TYPE_POTION = 300;

	private int speed = 0;
	private int hitPoint = 0;
	private int maxHitPoint = 0;
	private int basePoint = 0;
	private int bonusPoint = 0;
	private int range = 0;
	private int spriteFrame = -1;
	private int type = 0;

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public int getBasePoint() {
		return basePoint;
	}

	public void setBasePoint(int basePoint) {
		this.basePoint = basePoint;
	}

	public void onUse(BaseCharacter character) {
		character.HP += Math.min(character.MHP - character.HP, this.basePoint);
	}

	public int getSpriteFrame() {
		return spriteFrame;
	}

	public void setSpriteFrame(int spriteFrame) {
		this.spriteFrame = spriteFrame;
	}
}
