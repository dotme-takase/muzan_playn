package org.dotme.sprite.arpg;

import static playn.core.PlayN.graphics;

import java.awt.Rectangle;
import java.util.List;

import org.dotme.core.math.Vector2;
import org.dotme.core.math.Vector2withDegree;
import org.dotme.sprite.Sprite;
import org.dotme.sprite.SpriteAnimation;

import playn.core.Image;
import playn.core.Layer;
import playn.core.Surface;
import playn.core.SurfaceLayer;

public class LinkedSprite implements Sprite {

	Image texture;
	private int frameWidth;
	private int frameHeight;
	private int frame;
	private SpriteAnimation parent;
	private SurfaceLayer layer = null;
	private List<Vector2withDegree> pointMap = null;

	// Constructors a new animation.
	public LinkedSprite(Image texture, int frameWidth, int frameHeight,
			int frame, SpriteAnimation parent, Vector2 origin,
			List<Vector2withDegree> pointMap) {
		this.texture = texture;
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		this.frame = frame;
		this.layer = graphics().createSurfaceLayer(frameWidth, frameHeight);
		this.layer.setOrigin(origin.x, origin.y);
		this.pointMap = pointMap;
		this.parent = parent;
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
		Vector2withDegree dest = this.pointMap.get(parent.getCurrentFrame()
				% this.pointMap.size());
		layer.setRotation((float) (Math.PI * dest.degree / 180.0f));
		layer.setTranslation(dest.x + this.parent.getFrameWidth() / 2, dest.y
				+ this.parent.getFrameHeight() / 2);
		surf.clear();
		if (this.texture != null) {
			Rectangle source = new Rectangle(
					(int) ((this.frame * this.frameWidth) % texture.width()),
					(int) (Math.floor((this.frame * this.frameWidth)
							/ texture.width()) * this.frameHeight),
					(int) this.frameWidth, (int) this.frameHeight);
			surf.drawImage(this.texture, 0, 0, this.frameWidth,
					this.frameHeight, source.x, source.y, source.width,
					source.height);
		}
	}

	@Override
	public Layer getLayer() {
		return this.layer;
	}
}
