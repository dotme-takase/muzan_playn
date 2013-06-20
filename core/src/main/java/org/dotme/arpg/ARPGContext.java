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

import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.Color;
import playn.core.GroupLayer;
import playn.core.ImageLayer;

public class ARPGContext {
	public static final String MAIN_MENU_START_GAME = "menuStartGame";
	public static final String MAIN_MENU_RANKING = "menuRanking";
	public static final String RANKING_MENU_BACK = "backToMainMenu";
	public static final int MODE_MAIN = 0;
	public static final int MODE_MENU = 1;
	public static final int MODE_RANKING = 2;
	public static final int MODE_FLOOR_FADE = 3;

	public static final int FLOOR_FADE_COUNT = 16;

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
	public ImageLayer uiImageLayer;
	public ImageLayer floorLabelLayer;

	public GroupLayer autoMapLayer;
	public ImageLayer autoMapImageLayer;
	public ImageLayer autoMapPlayerLayer;
	public boolean[][] autoMap;

	public GroupLayer menuLayer;
	public ImageLayer menuImageLayer;

	public GroupLayer rankingLayer;
	public ImageLayer rankingImageLayer;

	public TextSprite statusSprite;

	public int mode = MODE_MENU;
	public int floor = 0;
	public int mapType = -1;
	public int floorFadeCount = 0;

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

		this.autoMapLayer = graphics().createGroupLayer(graphics().width() / 2,
				graphics().height());
		this.autoMapLayer.setTx(graphics().width() / 2);
		this.autoMapImageLayer = graphics().createImageLayer(
				graphics().createImage(graphics().width() / 2,
						graphics().height()));
		this.autoMapPlayerLayer = graphics().createImageLayer(
				graphics().createImage(MasterData.AUTOMAP_SIZE,
						MasterData.AUTOMAP_SIZE));
		Canvas autoMapPlayerCanvas = ((CanvasImage) this.autoMapPlayerLayer
				.image()).canvas();
		autoMapPlayerCanvas.setFillColor(Color.rgb(0, 255, 0));
		autoMapPlayerCanvas.fillRect(0, 0, MasterData.AUTOMAP_SIZE,
				MasterData.AUTOMAP_SIZE);
		this.autoMapLayer.add(autoMapImageLayer);
		this.autoMapLayer.add(autoMapPlayerLayer);
		graphics().rootLayer().add(this.autoMapLayer);
		this.autoMapLayer.setAlpha(0.5f);
		this.autoMapLayer.setVisible(false);

		this.uiLayer = graphics().createGroupLayer(graphics().width(),
				graphics().height());
		this.uiImageLayer = graphics()
				.createImageLayer(
						graphics().createImage(graphics().width(),
								graphics().height()));
		graphics().rootLayer().add(this.uiLayer);

		this.menuLayer = graphics().createGroupLayer(graphics().width(),
				graphics().height());
		this.menuImageLayer = graphics()
				.createImageLayer(
						graphics().createImage(graphics().width(),
								graphics().height()));
		this.menuLayer.add(menuImageLayer);
		graphics().rootLayer().add(this.menuLayer);

		this.rankingLayer = graphics().createGroupLayer(graphics().width(),
				graphics().height());
		this.rankingImageLayer = graphics()
				.createImageLayer(
						graphics().createImage(graphics().width(),
								graphics().height()));
		this.rankingLayer.add(rankingImageLayer);
		graphics().rootLayer().add(this.rankingLayer);

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
		uiLayer.add(this.uiImageLayer);

		initFloor(3, 3, false);
	}

	public Canvas getMenuCanvas() {
		return ((CanvasImage) this.menuImageLayer.image()).canvas();
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

		int _y = mapChips.length;
		int _x = mapChips[0].length;

		autoMap = new boolean[_y][_x];
		for (int aY = 0; aY < _y; aY++) {
			for (int aX = 0; aX < _x; aX++) {
				autoMap[aY][aX] = false;
			}
		}
		((CanvasImage) this.autoMapImageLayer.image()).canvas().clear();

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
