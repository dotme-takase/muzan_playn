package org.dotme.sprite.arpg;

import java.util.ArrayList;
import java.util.List;

import org.dotme.core.math.Vector2;
import org.dotme.core.math.Vector2withDegree;

public class SpriteConstants {
	public static final int TILE_SIZE_DEFAULT = 128;
	public static final int CHARACTER_RADIUS_DEFAULT = 32;
	public static final int RIGHT_ARM_WIDTH_DEFAULT = 32;
	public static final int RIGHT_ARM_HEIGHT_DEFAULT = 64;
	public static final Vector2 RIGHT_ARM_ORIGIN_DEFAULT = new Vector2(15, 55);
	public static final List<Vector2withDegree> RIGHT_ARM_MAP_DEFAULT = new ArrayList<Vector2withDegree>() {
		private static final long serialVersionUID = -2103366155437987739L;
		{
			add(new Vector2withDegree(0, 26, 90, false));
			add(new Vector2withDegree(-13, 26, 105, false));
			add(new Vector2withDegree(-21, 19, 130, false));
			add(new Vector2withDegree(-13, 26, 105, false));
			add(new Vector2withDegree(0, 26, 90, false));
			add(new Vector2withDegree(17, 19, 75, false));
			add(new Vector2withDegree(23, 10, 50, false));
			add(new Vector2withDegree(17, 19, 75, false));
			add(new Vector2withDegree(0, 26, 260, false));
			add(new Vector2withDegree(-23, 16, 290, false));
			add(new Vector2withDegree(-24, -3, 310, false));
			add(new Vector2withDegree(-13, 26, 180, false));
			add(new Vector2withDegree(0, 26, 160, false));
			add(new Vector2withDegree(23, 16, 115, false));
			add(new Vector2withDegree(28, -7, 60, false));
			add(new Vector2withDegree(17, 19, 75, false));
		}
	};

	public static final int LEFT_ARM_WIDTH_DEFAULT = 32;
	public static final int LEFT_ARM_HEIGHT_DEFAULT = 32;
	public static final Vector2 LEFT_ARM_ORIGIN_DEFAULT = new Vector2(16, 20);
	public static final List<Vector2withDegree> LEFT_ARM_MAP_DEFAULT = new ArrayList<Vector2withDegree>() {
		private static final long serialVersionUID = -440068081280366693L;
		{
			add(new Vector2withDegree(3, -27, 0, false));
			add(new Vector2withDegree(17, -19, 15, false));
			add(new Vector2withDegree(23, -10, 30, false));
			add(new Vector2withDegree(17, -19, 15, false));
			add(new Vector2withDegree(3, -27, 0, false));
			add(new Vector2withDegree(-13, -26, -25, false));
			add(new Vector2withDegree(-21, -19, -40, false));
			add(new Vector2withDegree(-13, -26, -25, false));
			add(new Vector2withDegree(3, -27, 0, false));
			add(new Vector2withDegree(23, -19, 50, false));
			add(new Vector2withDegree(28, 7, 90, false));
			add(new Vector2withDegree(17, -19, 0, false));
			add(new Vector2withDegree(3, -27, -15, false));
			add(new Vector2withDegree(-23, -19, -30, false));
			add(new Vector2withDegree(-24, 3, -65, false));
			add(new Vector2withDegree(-13, -26, -75, false));
		}
	};

}
