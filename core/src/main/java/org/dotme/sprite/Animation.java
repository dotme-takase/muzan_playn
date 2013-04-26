package org.dotme.sprite;

public class Animation {
	private String name;
	private int frameStart;
	private int frameEnd;
	private int animationFrame = 0;
	private int frameTime = 1;

	public Animation(String name, int frameStart, int frameEnd) {
		this.setName(name);
		this.setFrameStart(frameStart);
		this.setFrameEnd(frameEnd);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getFrameStart() {
		return frameStart;
	}

	public void setFrameStart(int frameStart) {
		this.frameStart = frameStart;
	}

	public int getFrameEnd() {
		return frameEnd;
	}

	public void setFrameEnd(int frameEnd) {
		this.frameEnd = frameEnd;
	}

	public int getAnimationFrame() {
		return animationFrame;
	}

	public void setAnimationFrame(int animationFrame) {
		this.animationFrame = animationFrame;
	}

	public int getCurrentFrame() {
		return animationFrame + frameStart;
	}

	public int getFrameCount() {
		return frameEnd - frameStart + 1;
	}

	public int getFrameTime() {
		return frameTime;
	}

	public void setFrameTime(int frameTime) {
		this.frameTime = frameTime;
	}
}