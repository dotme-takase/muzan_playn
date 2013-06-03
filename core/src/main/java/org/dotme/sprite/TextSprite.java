package org.dotme.sprite;

import static playn.core.PlayN.graphics;

import java.awt.Rectangle;
import java.util.Map;

import org.dotme.core.math.Vector2;

import playn.core.Image;
import playn.core.Layer;
import playn.core.Surface;
import playn.core.SurfaceLayer;

public class TextSprite implements Sprite {

	Image texture;
	private int height;
	private Map<Integer, Rectangle> fontMap = null;
	private SurfaceLayer layer = null;
	private Vector2 offset = null;
	private String text = null;

	// Constructors a new animation.
	public TextSprite(Image texture, int height,
			Map<Integer, Rectangle> fontMap, int x, int y, int screenWidth,
			int screenHeight) {
		this.texture = texture;
		this.height = height;
		this.fontMap = fontMap;
		this.offset = new Vector2(x, y);
		this.layer = graphics().createSurfaceLayer(screenWidth, screenHeight);
		this.layer.setTranslation(x, y);
	}

	// All frames in the animation arranged horizontally.
	public void setTexture(Image texture) {
		this.texture = texture;
	}

	// All frames in the animation arranged horizontally.
	public Image getTexture() {
		return texture;
	}

	// Advances the time position and draws the current frame of the animation.
	@Override
	public void paint(float gameTime) {
		Surface surf = layer.surface();
		surf.clear();
		if ((this.texture != null) && (this.text != null)
				&& (this.offset != null)) {
			char[] charArray = this.text.toCharArray();
			float offX = 0;
			float offY = 0;
			for (char ch : charArray) {
				Integer chCode = Integer.valueOf(ch);
				if (ch == '\n') {
					offX = 0;
					offY += this.height;
				} else if (ch == ' ') {
					offX += this.height / 3;
				} else if (fontMap.containsKey(chCode)) {
					Rectangle source = fontMap.get(chCode);
					surf.drawImage(this.texture, offX, offY, source.width,
							source.height, source.x, source.y, source.width,
							source.height);
					offX += source.width * 0.90;
				}
			}
		}
	}

	@Override
	public Layer getLayer() {
		return this.layer;
	}

	public Vector2 getOffset() {
		return offset;
	}

	public void setOffset(Vector2 offset) {
		this.offset = offset;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
