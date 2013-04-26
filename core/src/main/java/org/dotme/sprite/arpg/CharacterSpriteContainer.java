package org.dotme.sprite.arpg;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

import org.dotme.sprite.Animation;
import org.dotme.sprite.SpriteAnimation;
import org.dotme.sprite.SpriteContainer;

import playn.core.Image;

public class CharacterSpriteContainer extends SpriteContainer {
	public static final String ANIMATION_WALK = "walk";
	public static final String ANIMATION_ATTACK = "attack";
	public static final String ANIMATION_ATTACK_1 = "attack_1";
	public static final String ANIMATION_ATTACK_2 = "attack_2";
	public static final String ANIMATION_DEFENCE = "defence";
	public static final String ANIMATION_DAMAGE = "damage";
	public static final String ANIMATION_PARRIED = "parried";

	private LinkedSprite leftArmSprite;
	private LinkedSprite rightArmSprite;

	public CharacterSpriteContainer(String name, String src) {
		super(name, graphics().createGroupLayer());
		Image bodyImage = assets().getImage(src);
		getLayer().setOrigin(SpriteConstants.CHARACTER_RADIUS_DEFAULT,
				SpriteConstants.CHARACTER_RADIUS_DEFAULT);
		int bodySize = SpriteConstants.CHARACTER_RADIUS_DEFAULT * 2;
		SpriteAnimation bodyAnim1 = new SpriteAnimation(bodyImage, bodySize,
				bodySize);
		bodyAnim1.addAnimation(new Animation(ANIMATION_WALK, 0, 7));
		bodyAnim1.addAnimation(new Animation(ANIMATION_ATTACK, 10, 15));
		bodyAnim1.addAnimation(new Animation(ANIMATION_ATTACK_1, 11, 15));
		bodyAnim1.addAnimation(new Animation(ANIMATION_ATTACK_2, 12, 15));
		bodyAnim1.addAnimation(new Animation(ANIMATION_DEFENCE, 8, 10));
		bodyAnim1.addAnimation(new Animation(ANIMATION_DAMAGE, 0, 1));
		bodyAnim1.addAnimation(new Animation(ANIMATION_PARRIED, 0, 7));

		this.leftArmSprite = new LinkedSprite(null,
				SpriteConstants.LEFT_ARM_WIDTH_DEFAULT,
				SpriteConstants.LEFT_ARM_HEIGHT_DEFAULT, 0, bodyAnim1,
				SpriteConstants.LEFT_ARM_ORIGIN_DEFAULT,
				SpriteConstants.LEFT_ARM_MAP_DEFAULT);

		this.rightArmSprite = new LinkedSprite(null,
				SpriteConstants.RIGHT_ARM_WIDTH_DEFAULT,
				SpriteConstants.RIGHT_ARM_HEIGHT_DEFAULT, 0, bodyAnim1,
				SpriteConstants.RIGHT_ARM_ORIGIN_DEFAULT,
				SpriteConstants.RIGHT_ARM_MAP_DEFAULT);

		SpriteAnimation bodyAnim2 = new SpriteAnimation(bodyImage, bodySize,
				bodySize);
		bodyAnim2.addAnimation(new Animation(ANIMATION_WALK, 16, 23));
		bodyAnim2.addAnimation(new Animation(ANIMATION_ATTACK, 36, 41));
		bodyAnim2.addAnimation(new Animation(ANIMATION_ATTACK_1, 37, 41));
		bodyAnim2.addAnimation(new Animation(ANIMATION_ATTACK_2, 38, 41));
		bodyAnim2.addAnimation(new Animation(ANIMATION_DEFENCE, 24, 26));
		bodyAnim2.addAnimation(new Animation(ANIMATION_DAMAGE, 16, 17));
		bodyAnim2.addAnimation(new Animation(ANIMATION_PARRIED, 16, 23));

		this.addSprite(bodyAnim2);
		this.addSprite(this.leftArmSprite);
		this.addSprite(this.rightArmSprite);
		this.addSprite(bodyAnim1);
	}

	public LinkedSprite getLeftArmSprite() {
		return leftArmSprite;
	}

	public LinkedSprite getRightArmSprite() {
		return rightArmSprite;
	}
}