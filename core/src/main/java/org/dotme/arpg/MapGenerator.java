package org.dotme.arpg;

import org.dotme.core.math.Vector2;
import org.dotme.sprite.MapChip;

public class MapGenerator {
	private MapChip[][] result;

	public static final int MAP_NORTH = 0;
	public static final int MAP_SOUTH = 1;
	public static final int MAP_WEST = 2;
	public static final int MAP_EAST = 3;

	private static final MapChip w1_c = new MapChip(6,
			MapChip.MAPCHIP_TYPE_WALL, "w1_c");
	private static final MapChip w1_tl1 = new MapChip(0,
			MapChip.MAPCHIP_TYPE_WALL, "w1_tl1");
	private static final MapChip w1_t1 = new MapChip(1,
			MapChip.MAPCHIP_TYPE_WALL, "w1_t1");
	private static final MapChip w1_tr1 = new MapChip(2,
			MapChip.MAPCHIP_TYPE_WALL, "w1_tr1");
	private static final MapChip w1_l1 = new MapChip(5,
			MapChip.MAPCHIP_TYPE_WALL, "w1_l1");
	private static final MapChip w1_r1 = new MapChip(7,
			MapChip.MAPCHIP_TYPE_WALL, "w1_r1");
	private static final MapChip w1_bl1 = new MapChip(10,
			MapChip.MAPCHIP_TYPE_WALL, "w1_bl1");
	private static final MapChip w1_b1 = new MapChip(11,
			MapChip.MAPCHIP_TYPE_WALL, "w1_b1");
	private static final MapChip w1_br1 = new MapChip(12,
			MapChip.MAPCHIP_TYPE_WALL, "w1_br1");
	private static final MapChip w1_br2 = new MapChip(3,
			MapChip.MAPCHIP_TYPE_WALL, "w1_br2");
	private static final MapChip w1_bl2 = new MapChip(4,
			MapChip.MAPCHIP_TYPE_WALL, "w1_bl2");
	private static final MapChip w1_tr2 = new MapChip(8,
			MapChip.MAPCHIP_TYPE_WALL, "w1_tr2");
	private static final MapChip w1_tl2 = new MapChip(9,
			MapChip.MAPCHIP_TYPE_WALL, "w1_tl2");
	private static final MapChip f1 = new MapChip(16,
			MapChip.MAPCHIP_TYPE_FLOOR, "f1");
	private static final MapChip s1 = new MapChip(21,
			MapChip.MAPCHIP_TYPE_DOWNSTAIR, "s1");

	class MapArea {
		boolean isRoom;
		int cX1;
		int cX2;
		int cY1;
		int cY2;
		int northEnd;
		int southEnd;
		int westEnd;
		int eastEnd;
		boolean[] connected;
		boolean isRoute;
		int areaX;
		int areaY;

		public MapArea(boolean isRoom, int cX1, int cX2, int cY1, int cY2,
				int northEnd, int southEnd, int westEnd, int eastEnd,
				boolean[] connected, boolean isRoute, int areaX, int areaY) {
			this.isRoom = isRoom;
			this.cX1 = cX1;
			this.cX2 = cX2;
			this.cY1 = cY1;
			this.cY2 = cY2;
			this.northEnd = northEnd;
			this.southEnd = southEnd;
			this.westEnd = westEnd;
			this.eastEnd = eastEnd;
			this.connected = connected;
			this.isRoute = isRoute;
			this.areaX = areaX;
			this.areaY = areaY;
		}
	}

	private int areaWidth = 5;
	private int areaHeight = 5;
	private int areaXSize;
	private int areaYSize;
	private int mapWidth;
	private int mapHeight;
	MapArea[][] areaList;

	public MapChip[][] generate(int _areaXSize, int _areaYSize) {

		this.areaXSize = _areaXSize;
		this.areaYSize = _areaYSize;

		mapWidth = areaWidth * areaXSize;
		mapHeight = areaHeight * areaYSize;

		result = new MapChip[mapHeight][];
		for (int y1 = 0; y1 < mapHeight; y1++) {
			MapChip[] line = new MapChip[mapWidth];
			for (int x1 = 0; x1 < mapWidth; x1++) {
				line[x1] = null;
			}
			result[y1] = line;
		}

		int areaCount = 0;
		int roomCount = 0;
		this.areaList = new MapArea[areaYSize][];
		for (int y2 = 0; y2 < areaYSize; y2++) {
			MapArea[] areaLine = new MapArea[areaXSize];
			for (int x2 = 0; x2 < areaXSize; x2++) {
				int offX = x2 * areaWidth;
				int offY = y2 * areaHeight;
				int northEnd = 0;
				int southEnd = 0;
				int westEnd = 0;
				int eastEnd = 0;

				boolean isRoom = true;//Math.ceil(Math.random() * 5) > 1;
				if ((areaCount >= areaXSize) && (roomCount == 0)) {
					isRoom = true;
				}
				if (isRoom) {
					int mN = (int) Math.ceil(Math.random() * 2);
					for (int m1 = 0; m1 < mN; m1++) {
						int my1 = offY + m1;
						for (int _mx1 = 0; _mx1 < areaWidth; _mx1++) {
							int mx1 = offX + _mx1;
							if (m1 == mN - 1) {
								result[my1][mx1] = w1_t1;
								northEnd = my1;
							} else {
								result[my1][mx1] = w1_c;
							}
						}
					}
					// SouthWall
					int mS = (int) Math.ceil(Math.random() * 2);
					for (int m2 = 0; m2 < mS; m2++) {
						int my2 = offY + areaHeight - m2 - 1;
						for (int _mx2 = 0; _mx2 < areaWidth; _mx2++) {
							int mx2 = offX + _mx2;
							if (m2 == mS - 1) {
								result[my2][mx2] = w1_b1;
								southEnd = my2;
							} else {
								result[my2][mx2] = w1_c;
							}
						}
					}

					// WestWall
					int mW = (int) Math.ceil(Math.random() * 2);
					for (int m3 = 0; m3 < mW; m3++) {
						int mx3 = offX + m3;
						for (int _my3 = 0; _my3 < areaHeight; _my3++) {
							int my3 = offY + _my3;
							if (m3 == mW - 1) {
								if (my3 == northEnd) {
									result[my3][mx3] = w1_tl1;
								} else if (my3 == southEnd) {
									result[my3][mx3] = w1_bl1;
								} else if (result[my3][mx3] == null) {
									result[my3][mx3] = w1_l1;
								}
								westEnd = mx3;
							} else {
								result[my3][mx3] = w1_c;
							}
						}
					}
					// EastWall
					int mE = (int) Math.ceil(Math.random() * 2);
					for (int m4 = 0; m4 < mE; m4++) {
						int mx4 = offX + areaWidth - m4 - 1;
						for (int _my4 = 0; _my4 < areaHeight; _my4++) {
							int my4 = offY + _my4;
							if (m4 == mE - 1) {
								if (my4 == northEnd) {
									result[my4][mx4] = w1_tr1;
								} else if (my4 == southEnd) {
									result[my4][mx4] = w1_br1;
								} else if (result[my4][mx4] == null) {
									result[my4][mx4] = w1_r1;
								}
								eastEnd = mx4;
							} else {
								result[my4][mx4] = w1_c;
							}
						}
					}
					int cX1 = westEnd + 1;
					int cX2 = westEnd + 1;
					if (eastEnd - westEnd > 2) {
						cX1 += Math.round(Math.random()
								* (eastEnd - westEnd - 2));
						cX2 += Math.round(Math.random()
								* (eastEnd - westEnd - 2));
						// cX1 += (eastEnd - westEnd - 2);
					}
					int cY1 = northEnd + 1;
					int cY2 = northEnd + 1;
					if (southEnd - northEnd > 2) {
						cY1 += Math.round(Math.random()
								* (southEnd - northEnd - 2));
						cY2 += Math.round(Math.random()
								* (southEnd - northEnd - 2));
						// cY1 += (southEnd - northEnd - 2);
					}

					boolean[] allFalse = { false, false, false, false };
					areaLine[x2] = new MapArea(true, cX1, cX2, cY1, cY2,
							northEnd, southEnd, westEnd, eastEnd, allFalse,
							false, x2, y2);
					roomCount++;
				} else {
					for (int _mx = 0; _mx < areaWidth; _mx++) {
						int mx = offX + _mx;
						for (int _my = 0; _my < areaWidth; _my++) {
							int my = offY + _my;
							result[my][mx] = w1_c;
						}
					}
					boolean[] allFalse = { false, false, false, false };
					areaLine[x2] = new MapArea(false, 0, 0, 0, 0, northEnd,
							southEnd, westEnd, eastEnd, allFalse, false, x2, y2);
				}
				areaCount++;
			}
			this.areaList[y2] = (areaLine);
		}

		for (int y3 = 0; y3 < areaYSize; y3++) {
			for (int x3 = 0; x3 < areaXSize; x3++) {
				MapArea area = this.areaList[y3][x3];
				if (area.isRoom) {
					if ((y3 == 0) && (x3 == 0)) {
						area.isRoute = true;
					}
					int rootDice = (int) Math.ceil(Math.random() * 3);
					int conS = (rootDice & 1);
					if ((conS != 0) && (y3 + 1 < areaYSize)) {
						connectNorthSouth(area, this.areaList[y3 + 1][x3]);
					}
					int conE = (rootDice & 2);
					if ((conE != 0) && (x3 + 1 < areaXSize)) {
						connectEastWest(area, this.areaList[y3][x3 + 1]);
					}
				}
			}
		}

		for (int y2 = 0; y2 < areaYSize; y2++) {
			for (int x2 = 0; x2 < areaXSize; x2++) {
				MapArea area = this.areaList[y2][x2];
				if (!area.isRoute) {
					forceRoute(area);
				}
			}
		}

		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[0].length; j++) {
				MapChip block = result[i][j];
				if (block == null) {
					result[i][j] = f1;
				}
			}
		}

		Vector2 randomPoint = ARPGUtils.getRandomPoint(result);
		result[(int) randomPoint.y][(int) randomPoint.x] = s1;

		return result;
	}

	private void dig(int tnX, int tnY) {
		if (result[tnY][tnX] != null) {
			result[tnY][tnX] = null;
			if ((tnY - 1) >= 0 && (result.length > tnY - 1)) {
				if (result[tnY - 1][tnX] == w1_c) {
					result[tnY - 1][tnX] = w1_t1;
				}
			}
			if ((tnY + 1) >= 0 && (result.length > tnY + 1)) {
				if (result[tnY + 1][tnX] == w1_c) {
					result[tnY + 1][tnX] = w1_b1;
				}
			}
			if ((tnX - 1) >= 0 && (result[tnY].length > tnX - 1)) {
				if (result[tnY][tnX - 1] == w1_c) {
					result[tnY][tnX - 1] = w1_l1;
				}
			}
			if ((tnX + 1) >= 0 && (result[tnY].length > tnX + 1)) {
				if (result[tnY][tnX + 1] == w1_c) {
					result[tnY][tnX + 1] = w1_r1;
				}
			}
		}
	}

	private void connectNorthSouth(MapArea area1, MapArea area2) {
		if (area1.isRoom) {
			if (area2.isRoom) {
				area1.connected[MAP_SOUTH] = true;
				area2.connected[MAP_NORTH] = true;
				if (area1.isRoute) {
					area2.isRoute = true;
				}
				if (area2.northEnd - area1.southEnd <= 3) {
					if (area1.cX2 > area2.eastEnd - 1) {
						area1.cX2 = area2.eastEnd - 1;
					} else if (area1.cX2 < area2.westEnd + 1) {
						area1.cX2 = area2.westEnd + 1;
					}
					area2.cX1 = area1.cX2;
					int tnX = area1.cX2;
					for (int tnY = area1.southEnd; tnY < area2.northEnd; tnY++) {
						dig(tnX, tnY);
					}
				} else {
					int tnX = area1.cX2;
					for (int tnY = area1.southEnd; tnY < area2.northEnd; tnY++) {
						dig(tnX, tnY);
						if (tnY == area1.southEnd + 2) {
							if (tnX < area2.cX1) {
								if (result[tnY + 1][tnX - 1] != null) {
									result[tnY + 1][tnX - 1] = w1_bl1;
								}
								if (result[tnY - 1][tnX + 1] != null) {
									result[tnY - 1][tnX + 1] = w1_tr2;
								}
								for (int _tnX = tnX; _tnX <= area2.cX1; _tnX++) {
									dig(_tnX, tnY);
									if (_tnX == area2.cX1) {
										if (result[tnY - 1][_tnX + 1] != null) {
											result[tnY - 1][_tnX + 1] = w1_tr1;
										}
										if (result[tnY + 1][_tnX - 1] != null) {
											result[tnY + 1][_tnX - 1] = w1_bl2;
										}
									}
								}
							} else if (tnX > area2.cX1) {
								if (result[tnY + 1][tnX + 1] != null) {
									result[tnY + 1][tnX + 1] = w1_br1;
								}
								if (result[tnY - 1][tnX - 1] != null) {
									result[tnY - 1][tnX - 1] = w1_tl2;
								}
								for (int _tnX = tnX; _tnX >= area2.cX1; _tnX--) {
									dig(_tnX, tnY);
									if (_tnX == area2.cX1) {
										if (result[tnY - 1][_tnX - 1] != null) {
											result[tnY - 1][_tnX - 1] = w1_tl1;
										}
										if (result[tnY + 1][_tnX + 1] != null) {
											result[tnY + 1][_tnX + 1] = w1_br2;
										}
									}
								}
							}
							tnX = area2.cX1;
						}
					}
				}
			}
			result[area1.southEnd][area1.cX2 - 1] = w1_bl2;
			result[area1.southEnd][area1.cX2 + 1] = w1_br2;
			if (area1.cX2 == area1.westEnd + 1) {
				if (result[area1.southEnd - 1][area1.cX2 - 1] != null) {
					result[area1.southEnd][area1.cX2 - 1] = w1_l1;
				}
			}
			if (area1.cX2 == area1.eastEnd - 1) {
				if (result[area1.southEnd - 1][area1.cX2 + 1] != null) {
					result[area1.southEnd][area1.cX2 + 1] = w1_r1;
				}
			}
			result[area1.southEnd][area1.cX2] = null;
		}

		if (area2.isRoom) {
			result[area2.northEnd][area2.cX1 - 1] = w1_tl2;
			result[area2.northEnd][area2.cX1 + 1] = w1_tr2;
			if (area2.cX1 == area2.westEnd + 1) {
				if (result[area2.northEnd + 1][area2.cX1 - 1] != null) {
					result[area2.northEnd][area2.cX1 - 1] = w1_l1;
				}
			}
			if (area2.cX1 == area2.eastEnd - 1) {
				if (result[area2.northEnd + 1][area2.cX1 + 1] != null) {
					result[area2.northEnd][area2.cX1 + 1] = w1_r1;
				}
			}
			result[area2.northEnd][area2.cX1] = null;
		}
	}

	private void connectEastWest(MapArea area1, MapArea area2) {
		if (area1.isRoom) {
			if (area2.isRoom) {
				area1.connected[MAP_WEST] = true;
				area2.connected[MAP_EAST] = true;
				if (area1.isRoute) {
					area2.isRoute = true;
				}
				if (area2.westEnd - area1.eastEnd <= 3) {
					if (area1.cY2 > area2.southEnd - 1) {
						area1.cY2 = area2.southEnd - 1;
					} else if (area1.cY2 < area2.northEnd + 1) {
						area1.cY2 = area2.northEnd + 1;
					}
					area2.cY1 = area1.cY2;
					int tnY = area1.cY2;
					for (int tnX = area1.eastEnd; tnX < area2.westEnd; tnX++) {
						dig(tnX, tnY);
					}
				} else {
					int tnY = area1.cY2;
					for (int tnX = area1.eastEnd; tnX < area2.westEnd; tnX++) {
						dig(tnX, tnY);
						if (tnX == area1.eastEnd + 2) {
							if (tnY < area2.cY1) {
								if (result[tnY + 1][tnX - 1] != null) {
									result[tnY + 1][tnX - 1] = w1_bl2;
								}
								if (result[tnY - 1][tnX + 1] != null) {
									result[tnY - 1][tnX + 1] = w1_tr1;
								}
								for (int _tnY = tnY; _tnY <= area2.cY1; _tnY++) {
									dig(tnX, _tnY);
									if (_tnY == area2.cY1) {
										if (result[_tnY + 1][tnX - 1] != null) {
											result[_tnY + 1][tnX - 1] = w1_bl1;
										}
										if (result[_tnY - 1][tnX + 1] != null) {
											result[_tnY - 1][tnX + 1] = w1_tr2;
										}
									}
								}
							} else if (tnY > area2.cY1) {
								if (result[tnY - 1][tnX - 1] != null) {
									result[tnY - 1][tnX - 1] = w1_tl2;
								}
								if (result[tnY + 1][tnX + 1] != null) {
									result[tnY + 1][tnX + 1] = w1_br1;
								}
								for (int _tnY = tnY; _tnY >= area2.cY1; _tnY--) {
									dig(tnX, _tnY);
									if (_tnY == area2.cY1) {
										if (result[_tnY - 1][tnX - 1] != null) {
											result[_tnY - 1][tnX - 1] = w1_tl1;
										}
										if (result[_tnY + 1][tnX + 1] != null) {
											result[_tnY + 1][tnX + 1] = w1_br2;
										}
									}
								}
							}
							tnY = area2.cY1;
						}
					}
				}
			}
			result[area1.cY2 - 1][area1.eastEnd] = w1_tr2;
			result[area1.cY2 + 1][area1.eastEnd] = w1_br2;
			if (area1.cY2 == area1.northEnd + 1) {
				if (result[area1.cY2 - 1][area1.eastEnd - 1] != null) {
					result[area1.cY2 - 1][area1.eastEnd] = w1_t1;
				}
			}
			if (area1.cY2 == area1.southEnd - 1) {
				if (result[area1.cY2 + 1][area1.eastEnd - 1] != null) {
					result[area1.cY2 + 1][area1.eastEnd] = w1_b1;
				}
			}
			result[area1.cY2][area1.eastEnd] = null;
		}

		if (area2.isRoom) {
			result[area2.cY1 - 1][area2.westEnd] = w1_tl2;
			result[area2.cY1 + 1][area2.westEnd] = w1_bl2;
			if (area2.cY1 == area2.northEnd + 1) {
				if (result[area2.cY1 - 1][area2.westEnd + 1] != null) {
					result[area2.cY1 - 1][area2.westEnd] = w1_t1;
				}
			}
			if (area2.cY1 == area2.southEnd - 1) {
				if (result[area2.cY1 + 1][area2.westEnd + 1] != null) {
					result[area2.cY1 + 1][area2.westEnd] = w1_b1;
				}
			}
			result[area2.cY1][area2.westEnd] = null;
		}
	}

	private void forceRoute(MapArea area) {
		if (area.isRoom) {
			MapArea nextArea = null;
			if (area.areaY > 0 && !area.connected[MAP_NORTH]) {
				nextArea = this.areaList[area.areaY - 1][area.areaX];
				if (nextArea.isRoom) {
					connectNorthSouth(nextArea, area);
				}
			} else if (area.areaY < areaYSize - 1 && !area.connected[MAP_SOUTH]) {
				nextArea = this.areaList[area.areaY + 1][area.areaX];
				if (nextArea.isRoom) {
					connectNorthSouth(area, nextArea);
				}
			} else if (area.areaX > 0 && !area.connected[MAP_EAST]) {
				nextArea = this.areaList[area.areaY][area.areaX - 1];
				if (nextArea.isRoom) {
					connectEastWest(nextArea, area);
				}
			} else if (area.areaX < areaXSize - 1 && !area.connected[MAP_WEST]) {
				nextArea = this.areaList[area.areaY][area.areaX + 1];
				if (nextArea.isRoom) {
					connectEastWest(area, nextArea);
				}
			}

			if ((nextArea != null) && (!nextArea.isRoute)) {
				forceRoute(nextArea);
			}
		}
	}
}
