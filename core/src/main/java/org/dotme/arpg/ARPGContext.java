package org.dotme.arpg;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dotme.core.math.Vector2;
import org.dotme.sprite.MapChip;
import org.dotme.sprite.MapChipSprite;
import org.dotme.sprite.SpriteAnimation;
import org.dotme.sprite.arpg.CharacterSpriteContainer;
import org.dotme.sprite.arpg.SpriteConstants;

public class ARPGContext {
	private static final ARPGContext instance = new ARPGContext();

	public MapGenerator mapGenerator;
	public BaseCharacter player;
	public MapChipSprite mapChipSprite;
	public Vector2 viewPoint;
	public List<BaseCharacter> characters;
	public Map<String, Vector2> characterPreviousPoints = null;
	public InputStatus input = null;
	public List<SpriteAnimation> effects = null;

	private ARPGContext() {
	}

	public static ARPGContext getInstance() {
		return instance;
	}

	public void init() {
		mapChipSprite = new MapChipSprite(assets().getImage("img/tiles1.png"),
				SpriteConstants.TILE_SIZE_DEFAULT,
				SpriteConstants.TILE_SIZE_DEFAULT, graphics().width(),
				graphics().height());
		mapGenerator = new MapGenerator();
		MapChip[][] mapChips = mapGenerator.generate(2, 2);
		viewPoint = new Vector2(0, 0);

		mapChipSprite.setOffset(viewPoint);
		mapChipSprite.setMap(mapChips);
		graphics().rootLayer().add(mapChipSprite.getLayer());

		CharacterSpriteContainer playerCon = new CharacterSpriteContainer(
				"player", "img/player.png");

		player = new PlayerCharacter(playerCon, null, null);
		input = new InputStatus();

		characters = new ArrayList<BaseCharacter>();
		try {
			characters.add(player);
			ARPGUtils.warpToRandomPoint(player, mapChipSprite,
					characterPreviousPoints);
			for (int i = 0; i < 5; i++) {
				EnemyCharacter enemy = (EnemyCharacter) MasterData.enemyData
						.get(0).clone();
				characters.add(enemy);
				ARPGUtils.warpToRandomPoint(enemy, mapChipSprite,
						characterPreviousPoints);
			}
			for (BaseCharacter character : this.characters) {
				graphics().rootLayer().add(
						character.getSpriteContainer().getLayer());
			}
		} catch (CloneNotSupportedException e) {
		}

		effects = new ArrayList<SpriteAnimation>();
	}

}
