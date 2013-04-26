package org.dotme.sprite;

import java.util.ArrayList;
import java.util.List;

import playn.core.GroupLayer;

public class SpriteContainer {
	private String name;
	private GroupLayer layer;
	private List<Sprite> spriteList;
	private float alpha = 1.0f;

	public SpriteContainer(String name, GroupLayer layer) {
		this.setName(name);
		this.layer = layer;
		this.spriteList = new ArrayList<Sprite>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GroupLayer getLayer() {
		return layer;
	}

	public List<Sprite> getSpriteList() {
		return spriteList;
	}

	public void addSprite(Sprite s) {
		spriteList.add(s);
		layer.add(s.getLayer());
	}

	public void gotoAndPlay(String animationName) {
		gotoAndPlay(animationName, false);
	}

	public void gotoAndPlay(String animationName, boolean isLooping) {
		setLooping(isLooping);
		for (Sprite sprite : spriteList) {
			if (sprite instanceof SpriteAnimation) {
				((SpriteAnimation) sprite)
						.gotoAndPlay(animationName, isLooping);
			}
		}
	}

	public void pause() {
		for (Sprite sprite : spriteList) {
			if (sprite instanceof SpriteAnimation) {
				((SpriteAnimation) sprite).pause();
			}
		}
	}

	public boolean isPaused() {
		for (Sprite sprite : spriteList) {
			if (sprite instanceof SpriteAnimation) {
				return ((SpriteAnimation) sprite).isPaused();
			}
		}
		return false;
	}

	public boolean isAnimationEnd() {
		for (Sprite sprite : spriteList) {
			if (sprite instanceof SpriteAnimation) {
				return ((SpriteAnimation) sprite).isAnimationEnd();
			}
		}
		return false;
	}

	public void pauseAndReset() {
		for (Sprite sprite : spriteList) {
			if (sprite instanceof SpriteAnimation) {
				((SpriteAnimation) sprite).pauseAndReset();
			}
		}
	}

	public void setLooping(boolean isLooping) {
		for (Sprite sprite : spriteList) {
			if (sprite instanceof SpriteAnimation) {
				((SpriteAnimation) sprite).setLooping(isLooping);
			}
		}
	}

	public int getCurrentFrame() {
		for (Sprite sprite : spriteList) {
			if (sprite instanceof SpriteAnimation) {
				return ((SpriteAnimation) sprite).getCurrentFrame();
			}
		}
		return 0;
	}

	public int getAnimationFrame() {
		for (Sprite sprite : spriteList) {
			if (sprite instanceof SpriteAnimation) {
				return ((SpriteAnimation) sprite).getAnimationFrame();
			}
		}
		return 0;
	}

	public String getCurrentAnimationName() {
		for (Sprite sprite : spriteList) {
			if (sprite instanceof SpriteAnimation) {
				return ((SpriteAnimation) sprite).getCurrentAnimationName();
			}
		}
		return null;
	}
	
	public int getFrameCount() {
		for (Sprite sprite : spriteList) {
			if (sprite instanceof SpriteAnimation) {
				return ((SpriteAnimation) sprite).getFrameCount();
			}
		}
		return 0;
	}

	public void paint(float gameTime) {
		int animationFrame = -1;
		for (Sprite sprite : spriteList) {
			if (sprite instanceof SpriteAnimation) {
				if (animationFrame < 0) {
					animationFrame = ((SpriteAnimation) sprite)
							.getAnimationFrame();
				}
			}
			sprite.paint(gameTime);
		}
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}
}