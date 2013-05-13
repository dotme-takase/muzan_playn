package org.dotme.sprite;

import static playn.core.PlayN.graphics;

import java.awt.Rectangle;

import org.dotme.core.math.Vector2;

import playn.core.Image;
import playn.core.Layer;
import playn.core.Surface;
import playn.core.SurfaceLayer;

public class MapChipSprite implements Sprite {

	Image texture;
	private int frameWidth;
	private int frameHeight;
	private SurfaceLayer layer = null;
	private MapChip[][] map = null;
	private Vector2 offset = null;

	// Constructors a new animation.
	public MapChipSprite(Image texture, int frameWidth, int frameHeight,
			int screenWidth, int screenHeight) {
		this.texture = texture;
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		this.layer = graphics().createSurfaceLayer(screenWidth, screenHeight);
	}

	// All frames in the animation arranged horizontally.
	public void setTexture(Image texture) {
		this.texture = texture;
	}

	// All frames in the animation arranged horizontally.
	public Image getTexture() {
		return texture;
	}

	// Gets the number of frames in the animation.
	public int getFrameCount() {
		return (int) (getTexture().width() / getFrameWidth());
	}

	// Gets the width of a frame in the animation.
	public int getFrameWidth() {
		return this.frameWidth;
	}

	// Gets the height of a frame in the animation.
	public int getFrameHeight() {
		return this.frameHeight;
	}

	public void setFrameWidth(int frameWidth) {
		this.frameWidth = frameWidth;
	}

	public void setFrameHeight(int frameHeight) {
		this.frameHeight = frameHeight;
	}

	// Advances the time position and draws the current frame of the animation.
	@Override
	public void paint(float gameTime) {
		Surface surf = layer.surface();
		surf.clear();
		if ((this.texture != null) && (this.map != null)
				&& (this.offset != null)) {
			for (int i = 0; i < this.map.length; i++) {
				for (int j = 0; j < this.map[0].length; j++) {
					MapChip block = this.map[i][j];
					float tx = j * this.frameWidth - this.offset.x;
					float ty = i * this.frameHeight - this.offset.y;
					if ((tx > -1.0f * this.frameWidth)
							&& (tx < layer.width() + this.frameHeight)
							&& (ty > -1.0f * this.frameWidth)
							&& (ty < layer.height() + this.frameHeight)) {

						if (block.getFrame() >= 0) {
							Rectangle source = new Rectangle(
									(int) ((block.getFrame() * this.frameWidth) % texture
											.width()),
									(int) (Math.floor((block.getFrame() * this.frameWidth)
											/ texture.width()) * this.frameHeight),
									(int) this.frameWidth,
									(int) this.frameHeight);
							surf.drawImage(this.texture, tx, ty,
									this.frameWidth, this.frameHeight,
									source.x, source.y, source.width,
									source.height);
						}
					}
				}
			}
		}
	}

	@Override
	public Layer getLayer() {
		return this.layer;
	}

	public MapChip[][] getMap() {
		return map;
	}

	public void setMap(MapChip[][] map) {
		this.map = map;
	}

	public Vector2 getOffset() {
		return offset;
	}

	public void setOffset(Vector2 offset) {
		this.offset = offset;
	}
}
