package org.dotme.arpg;

import java.util.ArrayList;
import java.util.List;

import org.dotme.core.math.Vector2;
import org.dotme.sprite.MapChip;
import org.dotme.sprite.MapChipSprite;
import org.dotme.sprite.arpg.CharacterSpriteContainer;
import org.dotme.sprite.arpg.SpriteConstants;

public class EnemyCharacter extends BaseCharacter {

	private BaseCharacter target = null;
	private int mode = RANDOM_WALK;
	private static final int ATTACK_TO_TARGET = 10;
	private static final int RANDOM_WALK = 20;
	private List<Vector2> path = null;
	private Vector2 nextPath;
	private int defenceFrame = 0;

	public EnemyCharacter(CharacterSpriteContainer spriteCon,
			BaseItem rightArm, BaseItem leftArm) {
		super(spriteCon, rightArm, leftArm);
		this.teamNumber = 1;
	}

	class PathNode {
		Vector2 pos, goal;
		float hs, fs;
		List<PathNode> ownerList;
		PathNode parentNode;

		public PathNode(float x, float y) {
			this.pos = new Vector2(x, y);
			this.goal = ARPGUtils.getMapPoint(target);

			this.hs = (float) (Math.pow((this.pos.x - this.goal.x), 2) + Math
					.pow((this.pos.y - this.goal.y), 2));
			this.fs = 0;
			this.ownerList = null;
			this.parentNode = null;
		}

		public boolean isGoal() {
			return ((this.goal.x == this.pos.x) && (this.goal.y == this.pos.y));
		};
	}

	class PathNodeList {
		List<PathNode> list;

		public PathNodeList(List<PathNode> list) {
			this.list = list;
		}

		public PathNode find(int x, int y) {
			for (PathNode t : this.list) {
				if (((int) t.pos.x == x) && ((int) t.pos.y == y)) {
					return t;
				}
			}
			return null;
		}

		public void remove(PathNode node) {
			list.remove(node);
		}

		public void append(PathNode node) {
			this.list.add(node);
		};

		public PathNode minFs() {
			float min = -1;
			PathNode node = null;
			for (PathNode _node : this.list) {
				node = _node;
				if ((min < 0) || (min > node.fs)) {
					min = node.fs;
				}
			}
			return node;
		}
	}

	private List<Vector2> pathToTargetByAStar(MapChipSprite mapChipSprite,
			List<BaseCharacter> characters, int maxDepth) {
		int depth = 0;
		if (target == null) {
			for (BaseCharacter other : characters) {
				if (other.teamNumber != this.teamNumber) {
					target = other;
					break;
				}
			}
		}

		// スタート位置とゴール位置を設定
		Vector2 mapPoint = ARPGUtils.getMapPoint(this);
		PathNode startNode = new PathNode(mapPoint.x, mapPoint.y);
		PathNode endNode = null;
		MapChip[][] map = mapChipSprite.getMap();
		int mapHeight = map.length;
		int mapWidth = map[0].length;

		// OpenリストとCloseリストを設定
		PathNodeList openList = new PathNodeList(new ArrayList<PathNode>());
		PathNodeList closeList = new PathNodeList(new ArrayList<PathNode>());
		startNode.fs = startNode.hs;
		openList.append(startNode);

		List<Vector2> vectors = new ArrayList<Vector2>() {
			private static final long serialVersionUID = 1L;
			{
				this.add(new Vector2(1, 0));
				this.add(new Vector2(-1, 0));
				this.add(new Vector2(0, 1));
				this.add(new Vector2(0, -1));
			}
		};
		while (true) {
			depth++;
			if (depth > maxDepth) {
				break;
			}
			// Openリストが空になったら解なし
			if (openList.list.size() == 0) {
				break;
			}
			// Openリストからf*が最少のノードnを取得
			PathNode n = openList.minFs();
			if (n == null) {
				break;
			}
			openList.remove(n);
			closeList.append(n);

			// 最小ノードがゴールだったら終了
			if (n.isGoal()) {
				endNode = n;
				break;
			}

			// f*() = g*() + h*() -> g*() = f*() - h*()
			float n_gs = n.fs - n.hs;

			// ノードnの移動可能方向のノードを調べる
			for (Vector2 v : vectors) {
				int x = (int) (n.pos.x + v.x);
				int y = (int) (n.pos.y + v.y);
				// マップが範囲外または壁(O)の場合はcontinue
				if ((y < 0) || (y >= mapHeight) || (x < 0) || (x >= mapWidth)
						|| (!map[y][x].isWalkable())) {
					continue;
				}

				// 移動先のノードがOpen,Closeのどちらのリストに
				// 格納されているか、または新規ノードなのかを調べる
				PathNode m = openList.find(x, y);
				float dist = (float) (Math.pow((n.pos.x - x), 2) + Math.pow(
						(n.pos.y - y), 2));
				if (m != null) {
					// 移動先のノードがOpenリストに格納されていた場合、
					// より小さいf*ならばノードmのf*を更新し、親を書き換え
					if (m.fs > n_gs + m.hs + dist) {
						m.fs = n_gs + m.hs + dist;
						m.parentNode = n;
					}
				} else {
					m = closeList.find(x, y);
					if (m != null) {
						// 移動先のノードがCloseリストに格納されていた場合、
						// より小さいf*ならばノードmのf*を更新し、親を書き換え
						// かつ、Openリストに移動する
						if (m.fs > n_gs + m.hs + dist) {
							m.fs = n_gs + m.hs + dist;
							m.parentNode = n;
							openList.append(m);
							closeList.remove(m);
						}

					} else {
						// 新規ノードならばOpenリストにノードに追加
						m = new PathNode(x, y);
						m.fs = n_gs + m.hs + dist;
						m.parentNode = n;
						openList.append(m);
					}
				}
			}
		}
		// endノードから親を辿っていくと、最短ルートを示す
		List<Vector2> list = null;
		if (endNode != null) {
			list = new ArrayList<Vector2>();
			PathNode n = endNode;
			while ((n != null) && (n.parentNode != null)) {
				list.add(0, new Vector2(n.pos.x, n.pos.y));
				n = n.parentNode;
			}
		}
		return list;
	}

	private void searchTarget(List<BaseCharacter> characters) {
		BaseCharacter tempTarget = null;
		float tempDistance = 0;
		for (BaseCharacter other : characters) {
			if (other.teamNumber != this.teamNumber) {
				float otherDistance = (float) Math.sqrt(Math.pow(
						(other.x - this.x), 2)
						+ Math.pow((other.y - this.y), 2));
				if (tempTarget != null) {
					if (otherDistance < tempDistance) {
						tempTarget = other;
						tempDistance = otherDistance;
					}
				} else {
					tempTarget = other;
					tempDistance = otherDistance;
				}
			}
		}
		if (tempTarget != null) {
			this.target = tempTarget;
		}
	}

	public void simpleAction() {
		ARPGContext context = ARPGContext.getInstance();
		simpleAction(context.mapChipSprite, context.characters);
	}

	public void simpleAction(MapChipSprite mapChipSprite,
			List<BaseCharacter> characters) {
		float deltaX, deltaY, range, distance, theta, angleForTarget;
		this.defenceFrame = Math.max(this.defenceFrame - 1, 0);
		distance = -1;
		range = -1;
		if (this.target != null) {
			deltaX = this.target.x - this.x;
			deltaY = this.target.y - this.y;
			range = this.target.radius;
			distance = (float) Math.sqrt(Math.pow(deltaX, 2)
					+ Math.pow(deltaY, 2));
			theta = (float) Math.atan2(deltaY, deltaX);
			angleForTarget = (float) ((theta * 180 / Math.PI) - this.direction);
		} else {
			theta = 0;
			angleForTarget = (float) ((theta * 180 / Math.PI) - this.direction);
			searchTarget(characters);
		}
		if (this.isAction
				&& (this.action == BaseCharacter.CHARACTER_ACTION_DAMAGE
						|| this.action == BaseCharacter.CHARACTER_ACTION_DEAD || this.action == BaseCharacter.CHARACTER_ACTION_PARRIED)) {
			this.isWalking = false;
		} else {
			if (this.mode == RANDOM_WALK) {
				if (this.path == null || this.path.size() == 0) {
					this.path = this.pathToTargetByAStar(mapChipSprite,
							characters, 100);
				}
				if (this.path != null && this.path.size() > 0) {
					this.nextPath = this.path.get(0);
					this.path.remove(0);
				}
				if (this.target != null) {
					if ((distance < range * 5) && (angleForTarget > -60)
							&& (angleForTarget < 60)) {
						this.mode = ATTACK_TO_TARGET;
					} else if (this.action == BaseCharacter.CHARACTER_ACTION_DAMAGE) {
						this.mode = ATTACK_TO_TARGET;
						this.direction = (int) (theta * 180 / Math.PI);
					}
				}

				if (this.mode != ATTACK_TO_TARGET) {
					this.isWalking = true;
					if ((this.target == null) || (Math.random() * 100 > 80)) {
						// change to nearest target
						searchTarget(characters);
					} else if (this.nextPath != null) {
						Vector2 mapPt = ARPGUtils.getMapPoint(this);
						if ((this.nextPath.x == mapPt.x)
								&& (this.nextPath.y == mapPt.y)) {
							if (this.path != null && this.path.size() > 0) {
								this.nextPath = this.path.get(0);
								this.path.remove(0);
							} else {
								this.nextPath = null;
							}
						}
						if (this.nextPath != null) {
							Vector2 nextPoint = new Vector2(
									(float) (this.nextPath.x + 0.5)
											* SpriteConstants.TILE_SIZE_DEFAULT,
									(float) (this.nextPath.y + 0.5)
											* SpriteConstants.TILE_SIZE_DEFAULT);
							float _deltaX = nextPoint.x - this.x;
							float _deltaY = nextPoint.y - this.y;
							float _theta = (float) Math.atan2(_deltaY, _deltaX);
							this.direction = (int) (_theta * 180 / Math.PI);
						}
					} else {
						this.path = null;
					}
				}
			} else if (this.mode == ATTACK_TO_TARGET) {
				if (this.target.HP <= 0) {
					this.mode = RANDOM_WALK;
				} else if (distance < range + this.rightArm.getRange()) {
					double dice = Math.random() * 4;
					if (!this.isAction) {
						this.isWalking = false;
						this.isAction = true;
						this.action = BaseCharacter.CHARACTER_ACTION_DEFENCE_MOTION;
						this.defenceFrame = (int) Math.max((32 - this.speed)
								+ Math.round(dice), 0);
					} else {
						if (this.isWalking) {
							this.action = BaseCharacter.CHARACTER_ACTION_NONE;
							this.isAction = false;
						}
						if ((this.action == BaseCharacter.CHARACTER_ACTION_DEFENCE)
								|| (this.action == BaseCharacter.CHARACTER_ACTION_DEFENCE_MOTION)) {
							if (this.defenceFrame <= 0) {
								this.action = BaseCharacter.CHARACTER_ACTION_ATTACK;
							}
						}
					}

					if (this.action == BaseCharacter.CHARACTER_ACTION_ATTACK) {
					}
				} else {
					this.isWalking = true;
				}
				this.direction = (int) (theta * 180 / Math.PI);
				if (distance > range * 5) {
					this.mode = RANDOM_WALK;
				}
			}
		}
	}
}
