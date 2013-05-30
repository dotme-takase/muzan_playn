package org.dotme.arpg;

import static playn.core.PlayN.assets;

import java.awt.Rectangle;
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
	public static final Image IMAGE_ITEMS = assets().getImage("img/items.png");

	public static final int IMAGE_SHIELDS_HEIGHT = 32;
	public static final int IMAGE_SHIELDS_WIDTH = 32;
	public static final int IMAGE_SWORDS_HEIGHT = 64;
	public static final int IMAGE_SWORDS_WIDTH = 32;
	public static final int IMAGE_ITEMS_HEIGHT = 32;
	public static final int IMAGE_ITEMS_WIDTH = 32;

	public static final Map<String, BaseItem> itemData = new HashMap<String, BaseItem>() {
		private static final long serialVersionUID = 1L;
		{
			put("shortSword", new BaseItem(IMAGE_SWORDS, IMAGE_SWORDS_WIDTH,
					IMAGE_SWORDS_HEIGHT) {
				{
					setSpriteFrame(0);
					setType(BaseItem.TYPE_SWORD);
					setRange(20);
					setBasePoint(4);
					setSpeed(1);
				}
			});
			put("longSword", new BaseItem(IMAGE_SWORDS, IMAGE_SWORDS_WIDTH,
					IMAGE_SWORDS_HEIGHT) {
				{
					setSpriteFrame(1);
					setType(BaseItem.TYPE_SWORD);
					setRange(28);
					setBasePoint(8);
					setSpeed(0);
				}
			});
			put("fasterShortSword", new BaseItem(IMAGE_SWORDS,
					IMAGE_SWORDS_WIDTH, IMAGE_SWORDS_HEIGHT) {
				{
					setSpriteFrame(2);
					setType(BaseItem.TYPE_SWORD);
					setRange(20);
					setBasePoint(5);
					setSpeed(2);
				}
			});
			put("handAxe", new BaseItem(IMAGE_SWORDS, IMAGE_SWORDS_WIDTH,
					IMAGE_SWORDS_HEIGHT) {
				{
					setSpriteFrame(3);
					setType(BaseItem.TYPE_SWORD);
					setRange(22);
					setBasePoint(16);
					setSpeed(-2);
				}
			});
			put("katana", new BaseItem(IMAGE_SWORDS, IMAGE_SWORDS_WIDTH,
					IMAGE_SWORDS_HEIGHT) {
				{
					setSpriteFrame(4);
					setType(BaseItem.TYPE_SWORD);
					setRange(28);
					setBasePoint(10);
					setSpeed(1);
				}
			});
			put("ryuyotou", new BaseItem(IMAGE_SWORDS, IMAGE_SWORDS_WIDTH,
					IMAGE_SWORDS_HEIGHT) {
				{
					setSpriteFrame(5);
					setType(BaseItem.TYPE_SWORD);
					setRange(24);
					setBasePoint(13);
					setSpeed(-1);
				}
			});
			put("broadSword", new BaseItem(IMAGE_SWORDS, IMAGE_SWORDS_WIDTH,
					IMAGE_SWORDS_HEIGHT) {
				{
					setSpriteFrame(6);
					setType(BaseItem.TYPE_SWORD);
					setRange(32);
					setBasePoint(12);
					setSpeed(0);
				}
			});
			put("woodenShield", new BaseItem(IMAGE_SHIELDS,
					IMAGE_SHIELDS_WIDTH, IMAGE_SHIELDS_HEIGHT) {
				{
					setSpriteFrame(0);
					setType(BaseItem.TYPE_SHIELD);
					setHitPoint(10);
					setBasePoint(4);
				}
			});
			put("bronzeShield", new BaseItem(IMAGE_SHIELDS,
					IMAGE_SHIELDS_WIDTH, IMAGE_SHIELDS_HEIGHT) {
				{
					setSpriteFrame(1);
					setType(BaseItem.TYPE_SHIELD);
					setHitPoint(40);
					setBasePoint(5);
				}
			});
			put("ironShield", new BaseItem(IMAGE_SHIELDS, IMAGE_SHIELDS_WIDTH,
					IMAGE_SHIELDS_HEIGHT) {
				{
					setSpriteFrame(2);
					setType(BaseItem.TYPE_SHIELD);
					setHitPoint(80);
					setBasePoint(6);
				}
			});
			put("blueShield", new BaseItem(IMAGE_SHIELDS, IMAGE_SHIELDS_WIDTH,
					IMAGE_SHIELDS_HEIGHT) {
				{
					setSpriteFrame(3);
					setType(BaseItem.TYPE_SHIELD);
					setHitPoint(60);
					setBasePoint(12);
				}
			});
			put("redShield", new BaseItem(IMAGE_SHIELDS, IMAGE_SHIELDS_WIDTH,
					IMAGE_SHIELDS_HEIGHT) {
				{
					setSpriteFrame(4);
					setType(BaseItem.TYPE_SHIELD);
					setHitPoint(70);
					setBasePoint(16);
				}
			});
			put("aidBox", new BaseItem(IMAGE_ITEMS, IMAGE_ITEMS_WIDTH,
					IMAGE_ITEMS_HEIGHT) {
				{
					setSpriteFrame(0);
					setType(BaseItem.TYPE_POTION);
					setBasePoint(20);
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
					itemData.get("woodenShield")) {
				{
					this.speed = 5;
					this.dropRateMap = new HashMap<BaseItem, Integer>() {
						private static final long serialVersionUID = -3178259687079263841L;
						{
							put(itemData.get("aidBox"), 1);
						}
					};
				}
			});
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

	public static final Map<Integer, Rectangle> fontMap = new HashMap<Integer, Rectangle>() {
		private static final long serialVersionUID = 8921677876862235913L;
		{
			put(33, new Rectangle(4, 6, 15, 36));
			put(34, new Rectangle(19, 6, 19, 36));
			put(35, new Rectangle(38, 6, 27, 36));
			put(36, new Rectangle(66, 6, 22, 36));
			put(37, new Rectangle(88, 6, 37, 36));
			put(38, new Rectangle(125, 6, 28, 36));
			put(39, new Rectangle(152, 6, 11, 36));
			put(40, new Rectangle(164, 6, 17, 36));
			put(41, new Rectangle(182, 6, 17, 36));
			put(42, new Rectangle(201, 6, 20, 36));
			put(43, new Rectangle(223, 6, 25, 36));
			put(44, new Rectangle(247, 6, 16, 36));
			put(45, new Rectangle(263, 6, 18, 36));
			put(46, new Rectangle(282, 6, 12, 36));
			put(47, new Rectangle(294, 6, 20, 36));
			put(48, new Rectangle(4, 48, 24, 36));
			put(49, new Rectangle(29, 48, 21, 36));
			put(50, new Rectangle(50, 48, 24, 36));
			put(51, new Rectangle(74, 48, 23, 36));
			put(52, new Rectangle(96, 48, 24, 36));
			put(53, new Rectangle(120, 48, 23, 36));
			put(54, new Rectangle(143, 48, 23, 36));
			put(55, new Rectangle(166, 48, 22, 36));
			put(56, new Rectangle(188, 48, 24, 36));
			put(57, new Rectangle(211, 48, 24, 36));
			put(58, new Rectangle(6, 90, 12, 36));
			put(59, new Rectangle(19, 90, 15, 36));
			put(60, new Rectangle(36, 90, 25, 36));
			put(61, new Rectangle(63, 90, 24, 36));
			put(62, new Rectangle(90, 90, 24, 36));
			put(63, new Rectangle(116, 90, 20, 36));
			put(64, new Rectangle(136, 90, 31, 36));
			put(65, new Rectangle(4, 132, 26, 36));
			put(66, new Rectangle(30, 132, 24, 36));
			put(67, new Rectangle(53, 132, 24, 36));
			put(68, new Rectangle(77, 132, 26, 36));
			put(69, new Rectangle(103, 132, 22, 36));
			put(70, new Rectangle(125, 132, 21, 36));
			put(71, new Rectangle(146, 132, 25, 36));
			put(72, new Rectangle(172, 132, 26, 36));
			put(73, new Rectangle(199, 132, 18, 36));
			put(74, new Rectangle(216, 132, 19, 36));
			put(75, new Rectangle(235, 132, 26, 36));
			put(76, new Rectangle(260, 132, 23, 36));
			put(77, new Rectangle(282, 132, 27, 36));
			put(78, new Rectangle(311, 132, 25, 36));
			put(79, new Rectangle(337, 132, 27, 36));
			put(80, new Rectangle(364, 132, 23, 36));
			put(81, new Rectangle(386, 132, 27, 36));
			put(82, new Rectangle(413, 132, 24, 36));
			put(83, new Rectangle(438, 132, 21, 36));
			put(84, new Rectangle(459, 132, 25, 36));
			put(85, new Rectangle(483, 132, 25, 36));
			put(86, new Rectangle(507, 132, 25, 36));
			put(87, new Rectangle(531, 132, 34, 36));
			put(88, new Rectangle(564, 132, 26, 36));
			put(89, new Rectangle(590, 132, 24, 36));
			put(90, new Rectangle(614, 132, 23, 36));
			put(91, new Rectangle(6, 174, 16, 36));
			put(92, new Rectangle(21, 174, 25, 36));
			put(93, new Rectangle(46, 174, 16, 36));
			put(94, new Rectangle(64, 174, 26, 36));
			put(95, new Rectangle(90, 174, 25, 36));
			put(96, new Rectangle(117, 174, 16, 36));
			put(97, new Rectangle(4, 216, 23, 36));
			put(98, new Rectangle(27, 216, 22, 36));
			put(99, new Rectangle(49, 216, 20, 36));
			put(100, new Rectangle(68, 216, 23, 36));
			put(101, new Rectangle(91, 216, 22, 36));
			put(102, new Rectangle(112, 216, 16, 36));
			put(103, new Rectangle(128, 216, 23, 36));
			put(104, new Rectangle(151, 216, 21, 36));
			put(105, new Rectangle(173, 216, 12, 36));
			put(106, new Rectangle(184, 216, 17, 36));
			put(107, new Rectangle(201, 216, 23, 36));
			put(108, new Rectangle(223, 216, 10, 36));
			put(109, new Rectangle(236, 216, 32, 36));
			put(110, new Rectangle(269, 216, 22, 36));
			put(111, new Rectangle(291, 216, 24, 36));
			put(112, new Rectangle(314, 216, 22, 36));
			put(113, new Rectangle(336, 216, 24, 36));
			put(114, new Rectangle(360, 216, 17, 36));
			put(115, new Rectangle(376, 216, 19, 36));
			put(116, new Rectangle(394, 216, 19, 36));
			put(117, new Rectangle(412, 216, 22, 36));
			put(118, new Rectangle(433, 216, 22, 36));
			put(119, new Rectangle(455, 216, 29, 36));
			put(120, new Rectangle(484, 216, 22, 36));
			put(121, new Rectangle(506, 216, 22, 36));
			put(122, new Rectangle(528, 216, 20, 36));
			put(123, new Rectangle(4, 258, 21, 36));
			put(124, new Rectangle(30, 258, 12, 36));
			put(125, new Rectangle(45, 258, 21, 36));
			put(126, new Rectangle(67, 258, 26, 36));
		}
	};

	public static final int fontHeight = 36;
	public static final String fontSource = "img/font.png";
}
