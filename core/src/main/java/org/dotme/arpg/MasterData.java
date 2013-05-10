package org.dotme.arpg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dotme.sprite.arpg.CharacterSpriteContainer;

public class MasterData {
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

}
