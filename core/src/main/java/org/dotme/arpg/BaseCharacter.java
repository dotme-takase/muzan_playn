package org.dotme.arpg;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.dotme.core.math.Vector2;
import org.dotme.sprite.SpriteContainer;
import org.dotme.sprite.arpg.CharacterSpriteContainer;

public abstract class BaseCharacter {

	public static final int CHARACTER_ACTION_NONE = 0;
	public static final int CHARACTER_ACTION_DEFENCE_MOTION = 1;
	public static final int CHARACTER_ACTION_DEFENCE = 2;
	public static final int CHARACTER_ACTION_ATTACK = 3;
	public static final int CHARACTER_ACTION_PARRIED = 4;
	public static final int CHARACTER_ACTION_DAMAGE = 5;
	public static final int CHARACTER_ACTION_DEAD = 6;

	private SpriteContainer spriteCon;
	BaseItem rightArm;
	BaseItem leftArm;
	int speed;
	int direction;
	int radius;
	int x;
	int y;
	double vX;
	double vY;
	int px;
	int py;
	boolean isAction;
	int action;
	int parriedFrame;
	int attackFrame;
	int defenceFrame;
	boolean isWalking;
	int HP;
	int MHP;
	int teamNumber;
	int clientTime;
	Map<BaseItem, Integer> dropRateMap;
	boolean isPlayer;
	String stateId;

	public BaseCharacter(SpriteContainer spriteCon, BaseItem rightArm,
			BaseItem leftArm) {
		this.setSpriteCon(spriteCon);
		this.radius = (int) spriteCon.getLayer().originX();
		this.speed = 10;
		this.direction = 90;
		this.vX = 0;
		this.vY = 0;
		this.px = this.x;
		this.py = this.y;
		this.isAction = false;
		this.action = CHARACTER_ACTION_NONE;
		this.parriedFrame = 0;
		this.attackFrame = 0;
		this.defenceFrame = -1;
		this.isWalking = false;
		this.HP = 20;
		this.teamNumber = 0;
		this.clientTime = 0;
		this.dropRateMap = new HashMap<BaseItem, Integer>();
		this.isPlayer = false;
		this.stateId = UUID.randomUUID().toString();

		this.rightArm = null;
		if (rightArm != null) {
			this.equipRight(rightArm);
		}
		this.leftArm = null;
		if (leftArm != null) {
			this.equipLeft(leftArm);
		}
	};

	public void equipLeft(BaseItem item) {
		this.ejectLeft();
		// ToDo
	};

	public void equipRight(BaseItem item) {
		this.ejectRight();
		// ToDo
	};

	public void ejectLeft() {
		if (this.leftArm != null) {
			// ToDo
		}
	};

	public void ejectRight() {
		if (this.rightArm != null) {
			// ToDo
		}
	};

	public void addToDropList(BaseItem item, int rate) {
		this.dropRateMap.put(item, rate);
	}

	public void die() {
		this.HP = 0;
		this.action = CHARACTER_ACTION_DEAD;
		if (Math.floor(Math.random() * 100) < 80) {
			// ToDo
		}
	};

	public void updateFrame() {
		this.spriteCon.setAlpha(1.0f);
		if (this.HP <= 0) {
			this.die();
		}
		if (this.isWalking) {
			if (this.spriteCon.isPaused()) {
				this.spriteCon.gotoAndPlay(
						CharacterSpriteContainer.ANIMATION_WALK, true);
			}
			this.vX = Math.cos(this.direction * Math.PI / 180) * this.speed;
			this.vY = Math.sin(this.direction * Math.PI / 180) * this.speed;
		} else if (this.isAction) {
			this.vX = this.vY = 0;
			if (this.action == CHARACTER_ACTION_DEFENCE_MOTION) {
				if (this.spriteCon.isAnimationEnd()) {
					this.vX = this.vY = 0;
					this.action = CHARACTER_ACTION_DEFENCE;
				} else {
					if (!CharacterSpriteContainer.ANIMATION_DEFENCE
							.equals(this.spriteCon.getCurrentAnimationName())) {
						this.spriteCon
								.gotoAndPlay(CharacterSpriteContainer.ANIMATION_DEFENCE);
					}

					this.vX = Math.cos(this.direction * Math.PI / 180) * -2;
					this.vY = Math.sin(this.direction * Math.PI / 180) * -2;
				}
			} else if (this.action == CHARACTER_ACTION_DEFENCE) {

			} else if (this.action == CHARACTER_ACTION_PARRIED) {
				if (this.spriteCon.isAnimationEnd()) {
					if (this.parriedFrame <= 0) {
						this.vX = this.vY = 0;
						this.action = CHARACTER_ACTION_NONE;
					} else {
						this.parriedFrame--;
						this.spriteCon
								.gotoAndPlay(CharacterSpriteContainer.ANIMATION_PARRIED);
					}
				} else {
					if (!CharacterSpriteContainer.ANIMATION_PARRIED
							.equals(getSpriteContainer()
									.getCurrentAnimationName())) {
						this.spriteCon
								.gotoAndPlay(CharacterSpriteContainer.ANIMATION_PARRIED);
						if (this.parriedFrame > 0) {
							// this.context.addEffect(this.x, this.y,
							// CharacterSpriteContainer.ANIMATION_PARRIED);
						}
						// this.context.playSound(CharacterSpriteContainer.ANIMATION_PARRIED);
					}
					this.vX = Math.cos(this.direction * Math.PI / 180) * -1;
					this.vY = Math.sin(this.direction * Math.PI / 180) * -1;
				}
			} else if (this.action == CHARACTER_ACTION_DAMAGE) {
				if (this.spriteCon.isAnimationEnd()) {
					this.vX = this.vY = 0;
					this.action = CHARACTER_ACTION_NONE;
				} else {
					if (!CharacterSpriteContainer.ANIMATION_DAMAGE
							.equals(this.spriteCon.getCurrentAnimationName())) {
						this.spriteCon
								.gotoAndPlay(CharacterSpriteContainer.ANIMATION_DAMAGE);
						// this.context.addEffect(this.x, this.y,
						// CharacterSpriteContainer.ANIMATION_DAMAGE);
						// this.context.playSound("hit");
					}
					this.spriteCon.setAlpha(0.5f);
				}
			} else if (this.action == CHARACTER_ACTION_DEAD) {
				// ToDo
			} else if (this.action == CHARACTER_ACTION_ATTACK) {
				if (this.spriteCon.isAnimationEnd()
						&& getSpriteContainer()
								.getCurrentAnimationName()
								.startsWith(
										CharacterSpriteContainer.ANIMATION_ATTACK)) {
					if (this.action != CHARACTER_ACTION_ATTACK) {
						return;
					}
					this.attackFrame = 0;
					this.vX = this.vY = 0;
					this.action = CHARACTER_ACTION_NONE;
				} else {
					this.attackFrame = this.spriteCon.getAnimationFrame();
					if ((this.spriteCon.isPaused())
							|| (!getSpriteContainer()
									.getCurrentAnimationName()
									.startsWith(
											CharacterSpriteContainer.ANIMATION_ATTACK))) {
						int weaponSpeed = 2;
						if (this.rightArm != null) {
							weaponSpeed = this.rightArm.getSpeed();
						}
						if (weaponSpeed >= 2) {
							this.spriteCon
									.gotoAndPlay(CharacterSpriteContainer.ANIMATION_ATTACK_2);
						} else if (weaponSpeed == 1) {
							this.spriteCon
									.gotoAndPlay(CharacterSpriteContainer.ANIMATION_ATTACK_1);
						} else {
							this.spriteCon
									.gotoAndPlay(CharacterSpriteContainer.ANIMATION_ATTACK);
						}

						// this.context.playSound(CharacterSpriteContainer.ANIMATION_ATTACK);
					}

					int attackEnd = this.spriteCon.getFrameCount()
							- this.spriteCon.getAnimationFrame();
					if (attackEnd > 6) {
						this.vX = Math.cos(this.direction * Math.PI / 180) * -2;
						this.vY = Math.sin(this.direction * Math.PI / 180) * -2;
					} else if (attackEnd == 1) {
						this.vX = Math.cos(this.direction * Math.PI / 180) * -3;
						this.vY = Math.sin(this.direction * Math.PI / 180) * -3;
					} else {
						this.vX = Math.cos(this.direction * Math.PI / 180) * 3;
						this.vY = Math.sin(this.direction * Math.PI / 180) * 3;
					}
				}
			} else if (this.action == CHARACTER_ACTION_NONE) {
				this.isAction = false;
				this.vX = this.vY = 0;
				this.spriteCon.gotoAndPlay(
						CharacterSpriteContainer.ANIMATION_WALK, true); // animate
				this.spriteCon.pauseAndReset();
			}
		} else {
			this.isAction = false;
			this.vX = this.vY = 0;
			this.spriteCon.gotoAndPlay(CharacterSpriteContainer.ANIMATION_WALK,
					true); // animate
			this.spriteCon.pauseAndReset();
		}

		this.x += this.vX;
		this.y += this.vY;
		this.spriteCon.getLayer().setRotation(
				(float) (Math.PI * (this.direction) / 180.0f));

		this.clientTime++;
	};

	public void checkDropItem() {
		// ToDo
	}

	public SpriteContainer getSpriteContainer() {
		return spriteCon;
	}

	public void setSpriteCon(SpriteContainer spriteCon) {
		this.spriteCon = spriteCon;
	}

	public void paintInView(float gameTime, Vector2 viewPoint) {
		if (spriteCon != null) {
			spriteCon.getLayer().setTranslation(this.x - viewPoint.x,
					this.y - viewPoint.y);
			spriteCon.paint(gameTime);
		}
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}
}
