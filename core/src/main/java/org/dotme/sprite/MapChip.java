package org.dotme.sprite;


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

	public boolean isWalkable() {
		return (type == MAPCHIP_TYPE_FLOOR || type == MAPCHIP_TYPE_DOWNSTAIR);
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
