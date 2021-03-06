package org.dotme.arpg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.dotme.core.math.Vector2;
import org.dotme.sprite.SpriteContainer;
import org.dotme.sprite.arpg.CharacterSpriteContainer;

public abstract class BaseCharacter extends Vector2 implements Cloneable {

	public static final int CHARACTER_ACTION_NONE = 0;
	public static final int CHARACTER_ACTION_DEFENCE_MOTION = 1;
	public static final int CHARACTER_ACTION_DEFENCE = 2;
	public static final int CHARACTER_ACTION_ATTACK = 3;
	public static final int CHARACTER_ACTION_PARRIED = 4;
	public static final int CHARACTER_ACTION_DAMAGE = 5;
	public static final int CHARACTER_ACTION_DEAD = 6;

	public int HP;
	public int MHP;
	public int teamNumber;

	private CharacterSpriteContainer spriteCon;
	BaseItem rightArm;
	BaseItem leftArm;
	int speed;
	int direction;
	int radius;
	double vX;
	double vY;
	float px;
	float py;
	boolean isAction;
	int action;
	int parriedFrame;
	int attackFrame;
	int defenceFrame;
	boolean isWalking;
	int clientTime;
	Map<BaseItem, Integer> dropRateMap;
	boolean isPlayer;
	public String stateId;

	public BaseCharacter(CharacterSpriteContainer spriteCon, BaseItem rightArm,
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

		this.rightArm = rightArm;
		if (rightArm != null) {
			this.equipRight(rightArm);
		}
		this.leftArm = leftArm;
		if (leftArm != null) {
			this.equipLeft(leftArm);
		}
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		BaseCharacter clone = (BaseCharacter) super.clone();
		clone.setSpriteCon(new CharacterSpriteContainer(spriteCon.getName(),
				spriteCon.getImageSrc()));
		if (rightArm != null) {
			clone.equipRight(rightArm);
		}
		if (leftArm != null) {
			clone.equipLeft(leftArm);
		}
		clone.stateId = UUID.randomUUID().toString();
		return clone;
	}

	public void equipLeft(BaseItem item) {
		this.ejectLeft();
		try {
			this.leftArm = (BaseItem) item.clone();
			this.spriteCon.getLeftArmSprite().setFrame(item.getSpriteFrame());
		} catch (CloneNotSupportedException e) {
		}
	}

	public void equipRight(BaseItem item) {
		this.ejectRight();
		try {
			this.rightArm = (BaseItem) item.clone();
			this.spriteCon.getRightArmSprite().setFrame(item.getSpriteFrame());
		} catch (CloneNotSupportedException e) {
		}
	}

	public void ejectLeft() {
		if (this.leftArm != null) {
			this.leftArm = null;
			this.spriteCon.getLeftArmSprite().setFrame(-1);
		}
	}

	public void ejectRight() {
		if (this.rightArm != null) {
			this.rightArm = null;
			this.spriteCon.getRightArmSprite().setFrame(-1);
		}
	}

	public void addToDropList(BaseItem item, int rate) {
		this.dropRateMap.put(item, rate);
	}

	public void die(ARPGContext context) {
		this.HP = 0;
		this.action = CHARACTER_ACTION_DEAD;
		if (Math.floor(Math.random() * 100) < 80) {
			int rateSum = 0;
			Map<Integer, BaseItem> rateMap = new HashMap<Integer, BaseItem>();
			for (Entry<BaseItem, Integer> e : dropRateMap.entrySet()) {
				rateSum += e.getValue();
				rateMap.put(Integer.valueOf(rateSum), e.getKey());
			}
			int dice = (int) Math.floor(Math.random() * rateSum);
			for (Entry<Integer, BaseItem> e2 : rateMap.entrySet()) {
				if (dice < e2.getKey()) {
					try {
						ARPGUtils.dropItem(context, this, e2.getValue());
					} catch (Exception e1) {
					}
					break;
				}
			}
		}
	}

	public void updateFrame(ARPGContext context) {
		List<BaseCharacter> characters = context.characters;
		this.spriteCon.setAlpha(1.0f);
		if (this.HP <= 0) {
			this.die(context);
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
			if (this.action == CHARACTER_ACTION_DAMAGE) {
				if (!CharacterSpriteContainer.ANIMATION_DAMAGE
						.equals(this.spriteCon.getCurrentAnimationName())) {
					this.spriteCon
							.gotoAndPlay(CharacterSpriteContainer.ANIMATION_DAMAGE);
					ARPGUtils.addEffect(this.x, this.y,
							MasterData.EFFECT_ANIMATION_DAMAGE);
					ARPGUtils.playSound("hit");
				} else if (this.spriteCon.isAnimationEnd()) {
					this.vX = this.vY = 0;
					this.action = CHARACTER_ACTION_NONE;
				}
				this.spriteCon.setAlpha(0.5f);
			} else if (this.action == CHARACTER_ACTION_DEAD) {
				float size = this.radius / 2;
				float half = size / 2;
				for (int i = 0; i < 4; i++) {
					ARPGUtils.addEffect(
							(float) (this.x + Math.random() * size - half),
							(float) (this.y + Math.random() * size - half),
							"dead");
				}
				ARPGUtils.playSound("defeat");
				if (characters.contains(this)) {
					this.getSpriteContainer().getLayer().destroy();
				}
			} else if (this.action == CHARACTER_ACTION_DEFENCE_MOTION) {
				if (!CharacterSpriteContainer.ANIMATION_DEFENCE
						.equals(this.spriteCon.getCurrentAnimationName())) {
					this.spriteCon
							.gotoAndPlay(CharacterSpriteContainer.ANIMATION_DEFENCE);
				} else if (this.spriteCon.isAnimationEnd()) {
					this.vX = this.vY = 0;
					this.action = CHARACTER_ACTION_DEFENCE;
				}
				this.vX = Math.cos(this.direction * Math.PI / 180) * -2;
				this.vY = Math.sin(this.direction * Math.PI / 180) * -2;
			} else if (this.action == CHARACTER_ACTION_DEFENCE) {
				if (!CharacterSpriteContainer.ANIMATION_DEFENCE
						.equals(this.spriteCon.getCurrentAnimationName())) {
					this.spriteCon
							.gotoAndPlay(CharacterSpriteContainer.ANIMATION_DEFENCE);
				}
			} else if (this.action == CHARACTER_ACTION_PARRIED) {
				if (!CharacterSpriteContainer.ANIMATION_PARRIED
						.equals(getSpriteContainer().getCurrentAnimationName())) {
					this.spriteCon
							.gotoAndPlay(CharacterSpriteContainer.ANIMATION_PARRIED);
					if (this.parriedFrame > 0) {
						ARPGUtils.addEffect(this.x, this.y,
								MasterData.EFFECT_ANIMATION_PARRIED);
					}
					ARPGUtils.playSound(MasterData.EFFECT_ANIMATION_PARRIED);
				} else if (this.spriteCon.isAnimationEnd()) {
					if (this.parriedFrame <= 0) {
						this.vX = this.vY = 0;
						this.action = CHARACTER_ACTION_NONE;
					} else {
						this.parriedFrame--;
						this.spriteCon
								.gotoAndPlay(CharacterSpriteContainer.ANIMATION_PARRIED);
					}
				}
				this.vX = Math.cos(this.direction * Math.PI / 180) * -1;
				this.vY = Math.sin(this.direction * Math.PI / 180) * -1;
			} else if (this.action == CHARACTER_ACTION_ATTACK) {
				this.attackFrame = this.spriteCon.getCurrentFrame()
						- this.spriteCon.getCurrentAnimationFrameStart();
				if (!getSpriteContainer().getCurrentAnimationName().startsWith(
						CharacterSpriteContainer.ANIMATION_ATTACK)) {
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
					ARPGUtils.playSound(MasterData.EFFECT_SOUND_ATTACK);
				} else if (this.spriteCon.isAnimationEnd()) {
					this.attackFrame = 0;
					this.vX = this.vY = 0;
					this.action = CHARACTER_ACTION_NONE;
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
	}

	public SpriteContainer getSpriteContainer() {
		return spriteCon;
	}

	public void setSpriteCon(CharacterSpriteContainer spriteCon) {
		this.spriteCon = spriteCon;
	}

	public void paintInView(float gameTime, Vector2 viewPoint) {
		if (spriteCon != null) {
			spriteCon.getLayer().setTranslation(this.x - viewPoint.x,
					this.y - viewPoint.y);
			spriteCon.paint(gameTime);
		}
	}

}
