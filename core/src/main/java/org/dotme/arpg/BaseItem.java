package org.dotme.arpg;

import static playn.core.PlayN.graphics;

import java.awt.Rectangle;

import org.dotme.core.math.Vector2;
import org.dotme.sprite.Sprite;

import playn.core.Image;
import playn.core.Layer;
import playn.core.Surface;
import playn.core.SurfaceLayer;

public class BaseItem extends Vector2 implements Cloneable, Sprite {
	public static final int TYPE_SWORD = 100;
	public static final int TYPE_SHIELD = 200;
	public static final int TYPE_POTION = 300;

	private Image texture;
	private int speed = 0;
	private int hitPoint = 0;
	private int maxHitPoint = 0;
	private int basePoint = 0;
	private int bonusPoint = 0;
	private int range = 0;
	private int spriteFrame = -1;
	private int type = 0;
	private SurfaceLayer layer = null;
	private int frameWidth;
	private int frameHeight;

	public BaseItem(Image texture, int frameWidth, int frameHeight) {
		super(0, 0);
		this.texture = texture;
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		this.layer = graphics().createSurfaceLayer(frameWidth, frameHeight);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		BaseItem clone = (BaseItem) super.clone();
		clone.layer = graphics().createSurfaceLayer(frameWidth, frameHeight);
		clone.layer.setOrigin(layer.originX(), layer.originY());
		return clone;
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
		if (TYPE_POTION == this.type) {
			character.HP += Math.min(character.MHP - character.HP,
					this.basePoint);
			ARPGUtils.addEffect(character.x, character.y,
					MasterData.EFFECT_ANIMATION_HEAL);
			ARPGUtils.playSound(MasterData.EFFECT_SOUND_HEAL);
		} else if (TYPE_SHIELD == this.type) {
			character.equipLeft(this);
			ARPGUtils.playSound(MasterData.EFFECT_SOUND_PICKUP);
		} else if (TYPE_SWORD == this.type) {
			character.equipRight(this);
			ARPGUtils.playSound(MasterData.EFFECT_SOUND_PICKUP);
		}

	}

	public void onPickup(BaseCharacter character) {
		onUse(character);
	}

	public int getSpriteFrame() {
		return spriteFrame;
	}

	public void setSpriteFrame(int spriteFrame) {
		this.spriteFrame = spriteFrame;
	}

	public void paintInView(float gameTime, Vector2 viewPoint) {
		this.getLayer().setTranslation(this.x - viewPoint.x,
				this.y - viewPoint.y);
		this.paint(gameTime);
	}

	@Override
	public void paint(float gameTime) {
		Surface surf = layer.surface();
		int frame = getSpriteFrame();
		if (TYPE_SHIELD == this.type) {
			frame += texture.width() / this.frameWidth;
		}
		surf.clear();
		if ((this.texture != null) && (frame >= 0)) {
			Rectangle source = new Rectangle(
					(int) ((frame * this.frameWidth) % texture.width()),
					(int) (Math.floor((frame * this.frameWidth)
							/ texture.width()) * this.frameHeight),
					(int) this.frameWidth, (int) this.frameHeight);
			surf.drawImage(this.texture, 0, 0, this.frameWidth,
					this.frameHeight, source.x, source.y, source.width,
					source.height);
		}
	}

	@Override
	public Layer getLayer() {
		return this.layer;
	}
}
