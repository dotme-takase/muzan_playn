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
import org.dotme.sprite.TextSprite;
import org.dotme.sprite.arpg.CharacterSpriteContainer;
import org.dotme.sprite.arpg.SpriteConstants;

import playn.core.GroupLayer;

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

	public int floor = 0;
	public int mapType = -1;

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
		viewPoint = new Vector2(0, 0);
		mapChipSprite = new MapChipSprite(assets().getImage("img/tiles1.png"),
				SpriteConstants.TILE_SIZE_DEFAULT,
				SpriteConstants.TILE_SIZE_DEFAULT, graphics().width(),
				graphics().height());
		mapChipSprite.setOffset(viewPoint);
		this.stageLayer.add(mapChipSprite.getLayer());
		mapGenerator = new MapGenerator();

		player = new PlayerCharacter(new CharacterSpriteContainer("player",
				"img/player.png"), MasterData.itemData.get("shortSword"),
				MasterData.itemData.get("woodenShield"));
		this.characterLayer.add(player.getSpriteContainer().getLayer());
		input = new InputStatus();

		statusSprite = new TextSprite(assets().getImage(MasterData.fontSource),
				MasterData.fontHeight, MasterData.fontMap, 4, 4, graphics()
						.width(), MasterData.fontHeight);
		uiLayer.add(statusSprite.getLayer());

		initFloor(3, 3, false);
	}

	private void destroyLayers() {
		try {
			if (characters != null) {
				for (BaseCharacter o : characters) {
					if (!o.stateId.equals(player.stateId)) {
						o.getSpriteContainer().getLayer().destroy();
					}
				}
			}
			if (effects != null) {
				for (SpriteAnimation o : effects) {
					o.getLayer().destroy();
				}
			}
			if (droppedItems != null) {
				for (BaseItem o : droppedItems) {
					o.getLayer().destroy();
				}
			}
		} finally {
		}
	}

	public void initFloor(int x, int y, boolean reset) {
		destroyLayers();
		if (!reset) {
			this.floor++;
		} else {
			if (!player.getSpriteContainer().getLayer().destroyed()) {
				player.getSpriteContainer().getLayer().destroy();
			}
			player.setSpriteCon(new CharacterSpriteContainer("player",
					"img/player.png"));
			player.ejectLeft();
			player.ejectRight();
			player.equipLeft(MasterData.itemData.get("woodenShield"));
			player.equipRight(MasterData.itemData.get("shortSword"));
			player.HP = player.MHP;
			player.isAction = player.isWalking = false;
			player.action = BaseCharacter.CHARACTER_ACTION_NONE;
			this.floor = 1;
		}
		MapChip[][] mapChips = mapGenerator.generate(x, y);
		mapChipSprite.setMap(mapChips);
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
				if (reset || !character.stateId.equals(player.stateId)) {
					this.characterLayer.add(character.getSpriteContainer()
							.getLayer());
				}
			}
		} catch (CloneNotSupportedException e) {
		}
		effects = new ArrayList<SpriteAnimation>();
		droppedItems = new ArrayList<BaseItem>();
	}
}
