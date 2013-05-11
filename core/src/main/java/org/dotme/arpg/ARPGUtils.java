package org.dotme.arpg;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dotme.core.math.Vector2;
import org.dotme.sprite.MapChip;
import org.dotme.sprite.MapChipSprite;
import org.dotme.sprite.SpriteAnimation;
import org.dotme.sprite.arpg.SpriteConstants;

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
					// ToDo
					// if (obj.rightArm.type == BitmapItem.TYPE_SWORD) {
					// weaponRange = obj.rightArm.range;
					// weaponPoint = obj.rightArm.bonusPoint;
					// }

					if ((distance < range + weaponRange)
							&& ((angleForOther > -20) && (angleForOther < 80))) {
						// right
						if ((other.isAction && (other.action == BaseCharacter.CHARACTER_ACTION_DEFENCE))
								// ToDo && (other.leftArm != null &&
								// other.leftArm.type ==
								// BitmapItem.TYPE_SHIELD))
								&& ((angleForObj > -30) && (angleForObj < 30))) {
							double kickBackRange = -1 * Math.random()
									* obj.radius / 4;
							obj.x -= Math.cos(theta) * kickBackRange;
							obj.y -= Math.sin(theta) * kickBackRange;
							collideBlocks(obj, mapSprite);
							obj.isAction = true;
							obj.action = BaseCharacter.CHARACTER_ACTION_PARRIED;
							obj.parriedFrame = 1;
							// ToDo
							// other.leftArm.onUse(other, obj);
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
							// ToDo
							// if ((playData != null) && (other ==
							// player)) {
							// playData.enemy = obj;
							// }
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
			graphics().rootLayer().add(animation.getLayer());
		} catch (Exception e) {
		}
	}

	public static void playSound(String name) {
		if (MasterData.effectSounds.containsKey(name)) {
			MasterData.effectSounds.get(name).play();
		}
	}
}
