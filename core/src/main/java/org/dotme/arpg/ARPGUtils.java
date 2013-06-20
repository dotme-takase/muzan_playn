package org.dotme.arpg;

import static playn.core.PlayN.graphics;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.dotme.core.math.Vector2;
import org.dotme.sprite.MapChip;
import org.dotme.sprite.MapChipSprite;
import org.dotme.sprite.SpriteAnimation;
import org.dotme.sprite.arpg.SpriteConstants;

import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.Color;
import playn.core.Font;
import playn.core.ImageLayer;
import playn.core.Json;
import playn.core.PlayN;
import playn.core.Sound;
import playn.core.Storage;
import playn.core.SurfaceLayer;
import playn.core.TextFormat;
import playn.core.TextLayout;

public class ARPGUtils {
	public static Vector2 getRandomPoint(MapChip[][] map) {
		List<Vector2> arr = new ArrayList<Vector2>();
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map.length; j++) {
				MapChip block = map[i][j];
				if ((block != null)
						&& (block.getType() == MapChip.MAPCHIP_TYPE_FLOOR)) {
					arr.add(new Vector2(j, i));
				}
			}
		}
		return arr.get((int) Math.max(0,
				(Math.floor(Math.random() * arr.size()) - 1)));
	}

	public static double fixAngle(double angle) {
		angle = angle % 360;
		if (angle > 180) {
			angle -= 360;
		} else if (angle < -180) {
			angle += 360;
		}
		return angle;
	}

	public static void refixCharacters() {
		ARPGContext context = ARPGContext.getInstance();
		refixCharacters(context.characters, context.mapChipSprite,
				context.characterPreviousPoints);
	}

	public static void refixCharacters(List<BaseCharacter> characters,
			MapChipSprite mapSprite,
			Map<String, Vector2> characterPreviousPoints) {

		for (BaseCharacter character : characters) {
			collideCharacters(character, characters, mapSprite);
		}
		for (BaseCharacter character : characters) {
			collideBlocks(character, mapSprite);
		}
		if (characterPreviousPoints == null) {
			characterPreviousPoints = new HashMap<String, Vector2>();
		}
		for (BaseCharacter character : characters) {
			Vector2 mapPoint = getMapPoint(character);
			if (characterPreviousPoints.containsKey(character.stateId)) {
				Vector2 prev = characterPreviousPoints.get(character.stateId);
				Vector2 prevMapPoint = getMapPoint(prev);
				float dX = Math.abs(mapPoint.x - prevMapPoint.x);
				float dY = Math.abs(mapPoint.y - prevMapPoint.y);
				if ((dX > 1) || (dY > 1)) {
					character.x = (int) prev.x;
					character.y = (int) prev.y;
				}
			}
			characterPreviousPoints.put(character.stateId, new Vector2(
					character.x, character.y));
		}
	}

	private static void collideCharacters(BaseCharacter obj,
			List<BaseCharacter> otherCharacters, MapChipSprite mapSprite) {
		float oldX = obj.x;
		float oldY = obj.y;
		// boolean isNPC = (obj.teamNumber == 0);
		for (BaseCharacter other : otherCharacters) {
			if (other != obj) {
				float deltaX = other.x - oldX;
				float deltaY = other.y - oldY;
				double range = (other.radius + obj.radius);
				double collisionRange = range * 0.6;
				double distance = Math.sqrt(Math.pow(deltaX, 2)
						+ Math.pow(deltaY, 2));
				double theta = Math.atan2(deltaY, deltaX);
				double angleForOther = (theta * 180 / Math.PI) - obj.direction;
				angleForOther = fixAngle(angleForOther);
				double angleForObj = (theta * 180 / Math.PI) - 180
						- other.direction;
				angleForObj = fixAngle(angleForObj);

				if (obj.teamNumber != other.teamNumber
						&& obj.isAction
						&& !obj.isWalking
						&& (obj.action == BaseCharacter.CHARACTER_ACTION_ATTACK)
						&& (obj.attackFrame > 2)) {

					int weaponRange = 0;
					int weaponPoint = 0;
					if (obj.rightArm != null
							&& obj.rightArm.getType() == BaseItem.TYPE_SWORD) {
						weaponRange = obj.rightArm.getRange();
						weaponPoint = obj.rightArm.getBasePoint();
					}

					if ((distance < range + weaponRange)
							&& ((angleForOther > -20) && (angleForOther < 80))) {
						// right
						if ((other.isAction && (other.action == BaseCharacter.CHARACTER_ACTION_DEFENCE))
								&& ((other.leftArm != null && other.leftArm
										.getType() == BaseItem.TYPE_SHIELD))
								&& ((angleForObj > -30) && (angleForObj < 30))) {
							double kickBackRange = -1 * Math.random()
									* obj.radius / 4;
							obj.x -= Math.cos(theta) * kickBackRange;
							obj.y -= Math.sin(theta) * kickBackRange;
							collideBlocks(obj, mapSprite);
							obj.isAction = true;
							obj.action = BaseCharacter.CHARACTER_ACTION_PARRIED;
							obj.parriedFrame = 1;
							other.leftArm.setHitPoint(other.leftArm
									.getHitPoint()
									- (int) Math.ceil(weaponPoint
											* (Math.random() * 0.20 + 1)));
							if (other.leftArm.getHitPoint() <= 0) {
								other.ejectLeft();
							}
						} else if (!other.isAction
								|| (other.action != BaseCharacter.CHARACTER_ACTION_DAMAGE)) {
							double kickBackRange = -1 * Math.random()
									* obj.radius / 4;
							other.vX -= Math.cos(theta) * kickBackRange;
							other.vY -= Math.sin(theta) * kickBackRange;
							other.isAction = true;
							other.action = BaseCharacter.CHARACTER_ACTION_DAMAGE;
							other.HP -= Math.ceil(weaponPoint
									* (Math.random() * 0.20 + 1));

							if (other instanceof PlayerCharacter) {
								((PlayerCharacter) other).setLastEnemy(obj);
							}
						}
					}
				}

				if (distance < collisionRange) {
					obj.x -= Math.cos(theta) * (collisionRange - distance);
					obj.y -= Math.sin(theta) * (collisionRange - distance);
					collideBlocks(obj, mapSprite);
				}
			}
		}
	}

	public static Vector2 getMapPoint(Vector2 obj) {
		return new Vector2((int) Math.floor(obj.x
				/ SpriteConstants.TILE_SIZE_DEFAULT), (int) Math.floor(obj.y
				/ SpriteConstants.TILE_SIZE_DEFAULT));
	}

	private static void collideBlocks(BaseCharacter obj, MapChipSprite mapSprite) {
		float oldX = obj.x;
		float oldY = obj.y;

		int deltaW = 4;
		int deltaH = 4;
		int delta = 0;

		int tileSize = mapSprite.getFrameWidth();

		MapChip[][] map = mapSprite.getMap();
		Vector2 mapPoint = getMapPoint(obj);
		List<Vector2> nearBlocks = new ArrayList<Vector2>();
		for (int _y = (int) mapPoint.y - 2; _y < mapPoint.y + 3; _y++) {
			for (int _x = (int) mapPoint.x - 2; _x < mapPoint.x + 3; _x++) {
				if ((_y >= 0 && _y < map.length)
						&& (_x >= 0 && _x < map[_y].length)
						&& (map[_y][_x] != null)) {
					MapChip mapChip = map[_y][_x];
					if (mapChip.getType() == MapChip.MAPCHIP_TYPE_WALL) {
						nearBlocks
								.add(new Vector2(_x * tileSize, _y * tileSize));
					}
				}
			}
		}
		for (Vector2 block : nearBlocks) {
			boolean isFixed = false;
			if (oldY > block.y - deltaH && oldY < block.y + tileSize + deltaH
					&& obj.px >= block.x + tileSize + obj.radius
					&& oldX < block.x + tileSize + obj.radius) {
				obj.x = (int) (block.x + tileSize + obj.radius + delta);
				isFixed = true;
			}

			if (oldY > block.y - deltaH && oldY < block.y + tileSize + deltaH
					&& oldX > block.x - obj.radius
					&& obj.px <= block.x - obj.radius) {
				obj.x = (int) (block.x - obj.radius - delta);
				isFixed = true;
			}

			if (obj.py >= block.y + tileSize + obj.radius
					&& oldY < block.y + tileSize + obj.radius
					&& oldX > block.x - deltaW
					&& oldX < block.x + tileSize + deltaW) {
				obj.y = (int) (block.y + tileSize + obj.radius + delta);
				isFixed = true;
			}

			if (oldY > block.y - obj.radius && obj.py <= block.y - obj.radius
					&& oldX > block.x - deltaW
					&& oldX < block.x + tileSize + deltaW) {
				obj.y = (int) (block.y - obj.radius - delta);
				isFixed = true;
			}

			if (!isFixed) {
				if (oldY > block.y - obj.radius && oldY <= block.y
						&& oldX > block.x - deltaW
						&& oldX < block.x + tileSize + deltaW) {
					obj.y = (int) (block.y - obj.radius - delta);
				}
				if (oldY > block.y - deltaH
						&& oldY < block.y + tileSize + deltaH
						&& oldX > block.x - obj.radius && oldX <= block.x) {
					obj.x = (int) (block.x - obj.radius - delta);
				}
				if (oldY >= block.y && oldY < block.y + tileSize + obj.radius
						&& oldX > block.x - deltaW
						&& oldX < block.x + tileSize + deltaW) {
					obj.y = (int) (block.y + tileSize + obj.radius + delta);
				}
				if (oldY > block.y - deltaH
						&& oldY < block.y + tileSize + deltaH
						&& oldX >= block.x
						&& oldX < block.x + tileSize + obj.radius) {
					obj.x = (int) (block.x + tileSize + obj.radius + delta);
				}
			}
		}
		obj.px = obj.x;
		obj.py = obj.y;
	}

	public static void checkDropItem(ARPGContext context,
			BaseCharacter character) {
		for (Iterator<BaseItem> it = context.droppedItems.iterator(); it
				.hasNext();) {
			BaseItem item = it.next();
			float deltaX = item.x - character.x;
			float deltaY = item.y - character.y;
			float collisionRange = character.radius;
			float distance = (float) Math.sqrt(Math.pow(deltaX, 2)
					+ Math.pow(deltaY, 2));
			if (distance < collisionRange) {
				item.onPickup(character);
				it.remove();
				item.getLayer().destroy();
			}
		}
	}

	public static void dropItem(ARPGContext context, BaseCharacter character,
			BaseItem item) throws Exception {
		BaseItem clone = (BaseItem) (item.clone());
		clone.x = character.x;
		clone.y = character.y;
		context.itemLayer.add(clone.getLayer());
		context.droppedItems.add(clone);
	}

	public static void warpToRandomPoint(BaseCharacter character,
			MapChipSprite mapSprite,
			Map<String, Vector2> characterPreviousPoints) {
		MapChip[][] map = mapSprite.getMap();
		List<Vector2> arr = new ArrayList<Vector2>();
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map.length; j++) {
				MapChip block = map[i][j];
				if (block.getType() == MapChip.MAPCHIP_TYPE_FLOOR) {
					arr.add(new Vector2((j + 0.5f)
							* SpriteConstants.TILE_SIZE_DEFAULT, (i + 0.5f)
							* SpriteConstants.TILE_SIZE_DEFAULT));
				}
			}
		}
		Vector2 point = arr.get((int) Math.max(0,
				(Math.floor(Math.random() * arr.size()) - 1)));
		character.px = character.x = (int) point.x;
		character.py = character.y = (int) point.y;
		if (characterPreviousPoints != null
				&& characterPreviousPoints.containsKey(character.stateId)) {
			characterPreviousPoints.remove(character.stateId);
		}
	}

	public static void addEffect(float x, float y, String animationName) {
		try {
			SpriteAnimation animation = (SpriteAnimation) MasterData.effectAnimation
					.clone();
			animation.x = x;
			animation.y = y;
			animation.gotoAndPlay(animationName, false);
			ARPGContext context = ARPGContext.getInstance();
			context.effects.add(animation);
			context.characterLayer.add(animation.getLayer());
		} catch (Exception e) {
		}
	}

	public static void playSound(String name) {
		if (MasterData.effectSounds.containsKey(name)) {
			List<Sound> list = MasterData.effectSounds.get(name);
			for (Sound sound : list) {
				if (!sound.isPlaying()) {
					sound.play();
					break;
				}
			}
		}
	}

	public static void updateContext(ARPGContext context,
			ResourceBundle messages) {
		try {
			Vector2 mapPoint = getMapPoint(context.player);
			MapChip mapChip = context.mapChipSprite.getMap()[(int) mapPoint.y][(int) mapPoint.x];
			if (context.player.HP <= 0) {
				int ranking = saveRanking(context);
				context.initFloor(3, 3, true);
				createRankingScreen(context, messages, ranking);
				context.mode = ARPGContext.MODE_RANKING;
				context.autoMapLayer.setVisible(false);
				context.menuLayer.setVisible(true);
				context.rankingLayer.setVisible(true);
				((SurfaceLayer) context.statusSprite.getLayer()).surface()
						.clear();
			} else if (mapChip.getType() == MapChip.MAPCHIP_TYPE_DOWNSTAIR) {
				context.initFloor(3, 3, false);
				context.mode = ARPGContext.MODE_FLOOR_FADE;
				context.autoMapLayer.setVisible(false);
				context.floorLabelLayer = createMessageText(
						MessageFormat.format(
								messages.getString("textFloorLabel"),
								context.floor), 64, Color.rgb(255, 255, 255));
				context.floorLabelLayer
						.setTx((graphics().width() - context.floorLabelLayer
								.width()) / 2);
				context.floorLabelLayer
						.setTy((graphics().height() - context.floorLabelLayer
								.height()) / 2);
				context.uiLayer.add(context.floorLabelLayer);
			}
		} finally {
			context.statusSprite.setText("B" + context.floor + " "
					+ context.player.HP + "/" + context.player.MHP);
		}
	}

	public static void drawAutoMap(ARPGContext context) {
		try {
			Vector2 mapPoint = getMapPoint(context.player);
			Canvas canvas = ((CanvasImage) context.autoMapImageLayer.image())
					.canvas();
			int size = MasterData.AUTOMAP_SIZE;
			context.autoMapPlayerLayer.setTranslation(mapPoint.x * size,
					mapPoint.y * size);

			for (int y = (int) (mapPoint.y - 1); y <= mapPoint.y + 1; y++) {
				if ((y > 0) && (y < context.autoMap.length)) {
					for (int x = (int) (mapPoint.x - 1); x <= mapPoint.x + 1; x++) {
						if ((x > 0) && (x < context.autoMap[y].length)) {
							if (context.autoMap[y][x] == false) {
								context.autoMap[y][x] = true;
								MapChip chip = context.mapChipSprite.getMap()[y][x];
								if (chip.getType() == MapChip.MAPCHIP_TYPE_DOWNSTAIR) {
									canvas.setFillColor(Color
											.rgb(192, 192, 256));
									canvas.fillRect(x * size, y * size, size,
											size);
								} else if (chip.getType() == MapChip.MAPCHIP_TYPE_FLOOR) {
									canvas.setFillColor(Color
											.rgb(256, 256, 192));
									canvas.fillRect(x * size, y * size, size,
											size);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createRankingScreen(ARPGContext context,
			ResourceBundle messages, Integer highlight) {
		Canvas canvas = ((CanvasImage) context.rankingImageLayer.image())
				.canvas();
		canvas.clear();
		canvas.setFillColor(Color.rgb(0, 0, 0));
		canvas.fillRect(0, 0, graphics().width(), graphics().height());
		List<RankingItem> list = loadRanking();
		if (list != null && list.size() > 0) {
			int itemHeight = graphics().height()
					/ (MasterData.RANKING_SIZE + 1);
			int offY = itemHeight / 2;
			int index = 1;
			for (RankingItem item : list) {
				ImageLayer layer = createMessageText(
						item.getString(messages, index), itemHeight / 2,
						Color.rgb(255, 255, 255));
				offY += itemHeight;
				int offX = (int) ((graphics().width() - layer.width()) / 2);
				if ((highlight != null) && (highlight.intValue() == index)) {
					canvas.setFillColor(Color.rgb(96, 96, 96));
				} else {
					canvas.setFillColor(Color.rgb(16, 16, 16));
				}
				canvas.fillRect(offX, offY + 2, layer.width(),
						layer.height() - 4);
				canvas.drawImage(layer.image(), offX, offY);
				index++;
			}
		}
	}

	private static ImageLayer createMessageText(String text, int fontSize,
			Integer fontColor) {
		Font font = graphics().createFont("Helvetica", Font.Style.PLAIN,
				fontSize);
		TextLayout layout = graphics().layoutText(text,
				new TextFormat().withFont(font));
		ImageLayer textLayer = createTextLayer(layout, fontColor);
		return textLayer;
	}

	private static ImageLayer createTextLayer(TextLayout layout,
			Integer fontColor) {
		float width = layout.width() + 64;
		float offX = (width - layout.width()) / 2.0f;
		float height = layout.height() + 8;
		float offY = (height - layout.height()) / 2.0f;
		CanvasImage image = graphics().createImage((int) Math.ceil(width),
				(int) Math.ceil(height));
		image.canvas().clear();
		if (fontColor != null)
			image.canvas().setFillColor(fontColor);
		image.canvas().fillText(layout, offX, offY);

		return graphics().createImageLayer(image);
	}

	public static Map<String, ImageLayer> createMenuMap(ResourceBundle messages) {
		Map<String, ImageLayer> result = new HashMap<String, ImageLayer>();
		List<String> list = new ArrayList<String>();
		list.add(ARPGContext.MAIN_MENU_START_GAME);
		list.add(ARPGContext.MAIN_MENU_RANKING);
		Collections.reverse(list);
		int offY = (graphics().height());
		for (String text : list) {
			ImageLayer layer = createMessageText(messages.getString(text), 24,
					Color.rgb(255, 255, 255));
			offY -= layer.height() + 12;
			layer.setTx((graphics().width() - layer.width()) / 2);
			layer.setTy(offY);
			result.put(text, layer);
		}
		Collections.reverse(list);
		return result;
	}

	public static List<RankingItem> loadRanking() {
		Storage storage = PlayN.storage();
		String json = storage.getItem(MasterData.STRAGE_KEY_SAVE_DATA);
		List<RankingItem> list = new ArrayList<RankingItem>();
		if (json != null) {
			Json.Array array = PlayN.json().parseArray(json);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				try {
					RankingItem item = RankingItem.fromJson(array.getString(i));
					list.add(item);
				} finally {
				}
			}
		}
		return list;
	}

	public static int saveRanking(ARPGContext context) {
		int rank = 0;
		BaseCharacter lastEnemy = ((PlayerCharacter) context.player)
				.getLastEnemy();
		if (lastEnemy != null) {
			Storage storage = PlayN.storage();
			RankingItem item = new RankingItem();
			item.floor = context.floor;
			item.score = context.floor;
			item.enemyName = ((EnemyCharacter) lastEnemy).name;
			List<RankingItem> list = loadRanking();
			boolean inserted = false;
			int length = list.size();
			for (int i = 0; i < length; i++) {
				RankingItem historyItem = list.get(i);
				if (historyItem.score <= item.score) {
					rank = i + 1;
					list.add(i, item);
					inserted = true;
					break;
				}
			}
			if (!inserted) {
				list.add(item);
				rank = list.size();
			}
			length = Math.min(list.size(), MasterData.RANKING_SIZE);
			Json.Writer writer = PlayN.json().newWriter();
			writer.array();
			for (int i = 0; i < length; i++) {
				RankingItem historyItem = list.get(i);
				writer.value(historyItem.toJson());
			}
			writer.end();
			storage.setItem(MasterData.STRAGE_KEY_SAVE_DATA, writer.write());
		}
		return rank;
	}
}
