package org.dotme.arpg;

import static playn.core.PlayN.assets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dotme.sprite.Animation;
import org.dotme.sprite.SpriteAnimation;
import org.dotme.sprite.arpg.CharacterSpriteContainer;

import playn.core.Image;
import playn.core.Sound;

public class MasterData {
	public static final String EFFECT_ANIMATION_DAMAGE = "damage";
	public static final String EFFECT_ANIMATION_PARRIED = "parried";
	public static final String EFFECT_ANIMATION_HEAL = "heal";
	public static final String EFFECT_ANIMATION_DEAD = "dead";

	public static final String EFFECT_SOUND_ATTACK = "attack";
	public static final String EFFECT_SOUND_DEFEAT = "defeat";
	public static final String EFFECT_SOUND_DOWNSTAIR = "downstair";
	public static final String EFFECT_SOUND_HEAL = "heal";
	public static final String EFFECT_SOUND_HIT = "hit";
	public static final String EFFECT_SOUND_PARRIED = "parried";
	public static final String EFFECT_SOUND_PICKUP = "pickup";

	public static final Image IMAGE_SHIELDS = assets().getImage(
			"img/shields.png");
	public static final Image IMAGE_SWORDS = assets()
			.getImage("img/swords.png");

	public static final Map<String, BaseItem> itemData = new HashMap<String, BaseItem>() {
		private static final long serialVersionUID = 1L;
		{
			put("shortSword", new BaseItem() {
				{
					setSpriteFrame(0);
					setType(BaseItem.TYPE_SWORD);
					setRange(20);
					setBasePoint(4);
					setSpeed(1);
				}
			});
			put("longSword", new BaseItem() {
				{
					setSpriteFrame(1);
					setType(BaseItem.TYPE_SWORD);
					setRange(28);
					setBasePoint(8);
					setSpeed(0);
				}
			});
			put("fasterShortSword", new BaseItem() {
				{
					setSpriteFrame(2);
					setType(BaseItem.TYPE_SWORD);
					setRange(20);
					setBasePoint(5);
					setSpeed(2);
				}
			});
			put("handAxe", new BaseItem() {
				{
					setSpriteFrame(3);
					setType(BaseItem.TYPE_SWORD);
					setRange(22);
					setBasePoint(16);
					setSpeed(-2);
				}
			});
			put("katana", new BaseItem() {
				{
					setSpriteFrame(4);
					setType(BaseItem.TYPE_SWORD);
					setRange(28);
					setBasePoint(10);
					setSpeed(1);
				}
			});
			put("ryuyotou", new BaseItem() {
				{
					setSpriteFrame(5);
					setType(BaseItem.TYPE_SWORD);
					setRange(24);
					setBasePoint(13);
					setSpeed(-1);
				}
			});
			put("broadSword", new BaseItem() {
				{
					setSpriteFrame(6);
					setType(BaseItem.TYPE_SWORD);
					setRange(32);
					setBasePoint(12);
					setSpeed(0);
				}
			});
			put("woodenShield", new BaseItem() {
				{
					setSpriteFrame(0);
					setType(BaseItem.TYPE_SHIELD);
					setHitPoint(10);
					setBasePoint(4);
				}
			});
			put("bronzeShield", new BaseItem() {
				{
					setSpriteFrame(1);
					setType(BaseItem.TYPE_SHIELD);
					setHitPoint(40);
					setBasePoint(5);
				}
			});
			put("ironShield", new BaseItem() {
				{
					setSpriteFrame(2);
					setType(BaseItem.TYPE_SHIELD);
					setHitPoint(80);
					setBasePoint(6);
				}
			});
			put("blueShield", new BaseItem() {
				{
					setSpriteFrame(3);
					setType(BaseItem.TYPE_SHIELD);
					setHitPoint(60);
					setBasePoint(12);
				}
			});
			put("redShield", new BaseItem() {
				{
					setSpriteFrame(4);
					setType(BaseItem.TYPE_SHIELD);
					setHitPoint(70);
					setBasePoint(16);
				}
			});
			put("aidBox", new BaseItem() {
				{
					setSpriteFrame(0);
					setType(BaseItem.TYPE_POTION);
				}
			});
		}
	};

	public static final List<EnemyCharacter> enemyData = new ArrayList<EnemyCharacter>() {
		private static final long serialVersionUID = -726928001135783570L;
		CharacterSpriteContainer enemy1Con = new CharacterSpriteContainer(
				"enemy1", "img/enemy1.png");
		{
			add(new EnemyCharacter(enemy1Con, itemData.get("shortSword"),
					itemData.get("woodenShield")));
		}
	};

	public static final SpriteAnimation effectAnimation = new SpriteAnimation(
			assets().getImage("img/effect.png"), 128, 128) {
		{
			getLayer().setOrigin(64, 64);
			addAnimation(new Animation(EFFECT_ANIMATION_DAMAGE, 0, 4));
			addAnimation(new Animation(EFFECT_ANIMATION_PARRIED, 5, 9));
			addAnimation(new Animation(EFFECT_ANIMATION_HEAL, 10, 24));
			addAnimation(new Animation(EFFECT_ANIMATION_DEAD, 25, 39));
		}
	};

	public static final int maxConcurrentSound = 10;
	public static final Map<String, List<Sound>> effectSounds = new HashMap<String, List<Sound>>() {
		private static final long serialVersionUID = -4439019932306021680L;
		{
			HashMap<String, String> nameMap = new HashMap<String, String>() {
				private static final long serialVersionUID = 1L;
				{
					put(EFFECT_SOUND_ATTACK, "se/attack");
					put(EFFECT_SOUND_DEFEAT, "se/defeat");
					put(EFFECT_SOUND_DOWNSTAIR, "se/downstair");
					put(EFFECT_SOUND_HEAL, "se/heal");
					put(EFFECT_SOUND_HIT, "se/hit");
					put(EFFECT_SOUND_PARRIED, "se/parried");
					put(EFFECT_SOUND_PICKUP, "se/pickup");
				}
			};

			for (Map.Entry<String, String> e : nameMap.entrySet()) {
				List<Sound> list = new ArrayList<Sound>();
				for (int i = 0; i < maxConcurrentSound; i++) {
					list.add(assets().getSound(e.getValue()));
				}
				put(e.getKey(), list);
			}
		}
	};

}
