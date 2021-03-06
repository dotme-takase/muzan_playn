package org.dotme.sprite;

import static playn.core.PlayN.graphics;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

import org.dotme.core.math.Vector2;

import playn.core.Image;
import playn.core.Layer;
import playn.core.Surface;
import playn.core.SurfaceLayer;

public class SpriteAnimation extends Vector2 implements Sprite, Cloneable {

	Image texture;
	private int frameWidth;
	private int frameHeight;
	private Map<String, Animation> animations;
	private Animation currentAnimation = null;
	private SurfaceLayer layer = null;

	private boolean isLooping = false;
	private boolean isPaused = false;
	private boolean isAnimationEnd = false;

	// The amount of time in seconds that the current frame has been shown for.
	private float time;

	// Constructors a new animation.
	public SpriteAnimation(Image texture, int frameWidth, int frameHeight) {
		super(0, 0);
		this.texture = texture;
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		this.layer = graphics().createSurfaceLayer(frameWidth, frameHeight);

		animations = new HashMap<String, Animation>();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		SpriteAnimation clone = (SpriteAnimation) super.clone();
		clone.layer = graphics().createSurfaceLayer(frameWidth, frameHeight);
		clone.layer.setOrigin(layer.originX(), layer.originY());
		return clone;
	}

	public void gotoAndPlay(String animationName, boolean isLooping) {
		if (animations.containsKey(animationName)) {
			currentAnimation = animations.get(animationName);
			this.isLooping = isLooping;
			isPaused = false;
			isAnimationEnd = false;
			currentAnimation.setAnimationFrame(0);
		}
	}

	public void pause() {
		isPaused = true;
	}

	public void pauseAndReset() {
		isPaused = true;
		isAnimationEnd = false;
		if (currentAnimation != null) {
			currentAnimation.setAnimationFrame(0);
		}
	}

	public boolean isPaused() {
		return isPaused;
	}

	public boolean isAnimationEnd() {
		return isAnimationEnd;
	}

	public void setLooping(boolean isLooping) {
		this.isLooping = isLooping;
	}

	public void addAnimation(Animation animation) {
		animations.put(animation.getName(), animation);
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

	public int getCurrentFrame() {
		if (currentAnimation != null) {
			return currentAnimation.getCurrentFrame();
		}
		return 0;
	}

	public int getCurrentAnimationFrameStart() {
		if (currentAnimation != null) {
			return currentAnimation.getFrameStart();
		}
		return 0;
	}

	public void setAnimationFrame(int animationFrame) {
		if (currentAnimation != null) {
			currentAnimation.setAnimationFrame(animationFrame);
		}
	}

	public int getAnimationFrame() {
		if (currentAnimation != null) {
			return currentAnimation.getAnimationFrame();
		}
		return 0;
	}

	public String getCurrentAnimationName() {
		if (currentAnimation != null) {
			return currentAnimation.getName();
		}
		return null;
	}

	public void paintInView(float gameTime, Vector2 viewPoint) {
		this.getLayer().setTranslation(this.x - viewPoint.x,
				this.y - viewPoint.y);
		this.paint(gameTime);
	}

	public void paintCurrentFrame() {
		if (currentAnimation == null) {
			return;
		}
		Surface surf = layer.surface();
		surf.clear();

		// Calculate the source rectangle of the current frame.
		Rectangle source = new Rectangle(
				(int) ((currentAnimation.getCurrentFrame() * this.frameWidth) % texture
						.width()),
				(int) (Math.floor((currentAnimation.getCurrentFrame() * this.frameWidth)
						/ texture.width()) * this.frameHeight),
				(int) this.frameWidth, (int) this.frameHeight);

		// Draw the current frame.
		surf.drawImage(this.texture, 0, 0, this.frameWidth, this.frameHeight,
				source.x, source.y, source.width, source.height);
	}

	public void updateFrame(float gameTime) {
		// Process passing time.
		if (!isPaused) {
			time += gameTime;
			if (time >= currentAnimation.getFrameTime()) {
				time = 0;
				// Advance the frame index; looping or clamping as appropriate.
				if (isLooping) {
					currentAnimation.setAnimationFrame((currentAnimation
							.getAnimationFrame() + 1)
							% currentAnimation.getFrameCount());
				} else {
					if (currentAnimation.getAnimationFrame() >= currentAnimation
							.getFrameCount() - 1) {
						isAnimationEnd = true;
						isPaused = true;
					} else {
						currentAnimation.setAnimationFrame(Math.min(
								currentAnimation.getAnimationFrame() + 1,
								currentAnimation.getFrameCount() - 1));
					}
				}
			}
		}
	}

	// Advances the time position and draws the current frame of the animation.
	@Override
	public void paint(float gameTime) {
		updateFrame(gameTime);
		paintCurrentFrame();
	}

	@Override
	public Layer getLayer() {
		return this.layer;
	}
}
