package org.dotme.sprite;

import playn.core.Layer;


public interface Sprite {
	public void paint(float gameTime);
	public Layer getLayer();
}
