package org.dotme.arpg;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.dotme.core.math.Vector2;
import org.dotme.sprite.MapChip;
import org.dotme.sprite.MapChipSprite;
import org.dotme.sprite.SpriteAnimation;
import org.dotme.sprite.TextSprite;
import org.dotme.sprite.arpg.CharacterSpriteContainer;
import org.dotme.sprite.arpg.SpriteConstants;

import playn.core.GroupLayer;
import playn.core.GroupLayer.Clipped;

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
	public List<BaseItem> droppedItems = null;

	public GroupLayer stageLayer;
	public GroupLayer itemLayer;
	public GroupLayer characterLayer;
	public GroupLayer uiLayer;

	public TextSprite statusSprite;

	private ARPGContext() {
	}

	public static ARPGContext getInstance() {
		return instance;
	}

	public void init() {
		this.stageLayer = graphics().createGroupLayer(graphics().width(),
				graphics().height());
		graphics().rootLayer().add(this.stageLayer);
		this.itemLayer = graphics().createGroupLayer(graphics().width(),
				graphics().height());
		graphics().rootLayer().add(this.itemLayer);
		this.characterLayer = graphics().createGroupLayer(graphics().width(),
				graphics().height());
		graphics().rootLayer().add(this.characterLayer);
		this.uiLayer = graphics().createGroupLayer(graphics().width(),
				graphics().height());
		graphics().rootLayer().add(this.uiLayer);

		mapChipSprite = new MapChipSprite(assets().getImage("img/tiles1.png"),
				SpriteConstants.TILE_SIZE_DEFAULT,
				SpriteConstants.TILE_SIZE_DEFAULT, graphics().width(),
				graphics().height());
		mapGenerator = new MapGenerator();
		MapChip[][] mapChips = mapGenerator.generate(4, 3);
		viewPoint = new Vector2(0, 0);

		mapChipSprite.setOffset(viewPoint);
		mapChipSprite.setMap(mapChips);
		this.stageLayer.add(mapChipSprite.getLayer());

		CharacterSpriteContainer playerCon = new CharacterSpriteContainer(
				"player", "img/player.png");

		player = new PlayerCharacter(playerCon,
				MasterData.itemData.get("shortSword"),
				MasterData.itemData.get("woodenShield"));
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
				this.characterLayer.add(character.getSpriteContainer()
						.getLayer());
			}
		} catch (CloneNotSupportedException e) {
		}

		effects = new ArrayList<SpriteAnimation>();
		droppedItems = new ArrayList<BaseItem>();

		statusSprite = new TextSprite(assets().getImage(MasterData.fontSource),
				MasterData.fontHeight, MasterData.fontMap, 0, 0, graphics()
						.width(), MasterData.fontHeight * 4);
		uiLayer.add(statusSprite.getLayer());
	}
}
