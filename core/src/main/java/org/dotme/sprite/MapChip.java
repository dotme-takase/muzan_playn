package org.dotme.sprite;

import static playn.core.PlayN.graphics;

import java.awt.Rectangle;
import java.util.List;

import org.dotme.core.math.Vector2;
import org.dotme.core.math.Vector2withDegree;

import playn.core.Image;
import playn.core.Layer;
import playn.core.Surface;
import playn.core.SurfaceLayer;

public class MapChip {
	public static final int MAPCHIP_TYPE_EMPTY = 0;
	public static final int MAPCHIP_TYPE_FLOOR = 1;
	public static final int MAPCHIP_TYPE_DOWNSTAIR = 2;

	public static final int MAPCHIP_TYPE_WALL = 10;
	private int frame;
	private int type;
	private String name;

	// Constructors a new animation.
	public MapChip(int frame, int type, String name) {
		this.setFrame(frame);
		this.setType(type);
		this.setName(name);
	}

	public int getFrame() {
		return frame;
	}

	public void setFrame(int frame) {
		this.frame = frame;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
