package org.dotme.core;

import static playn.core.PlayN.graphics;
import static playn.core.PlayN.pointer;

import java.util.Iterator;

import org.dotme.arpg.ARPGContext;
import org.dotme.arpg.ARPGUtils;
import org.dotme.arpg.BaseCharacter;
import org.dotme.arpg.EnemyCharacter;
import org.dotme.arpg.PlayerCharacter;
import org.dotme.sprite.SpriteAnimation;
import org.dotme.sprite.arpg.SpriteConstants;

import playn.core.Game;
import playn.core.Pointer.Event;

public class Muzan implements Game {
	private PointerListener pointerListener = null;
	private ARPGContext arpgContext;

	@Override
	public void init() {
		arpgContext = ARPGContext.getInstance();
		arpgContext.init();
		pointerListener = new PointerListener();
		pointer().setListener(pointerListener);
	}

	@Override
	public void paint(float alpha) {
		// the background automatically paints itself, so no need to do anything
		// here!
		ARPGUtils.refixCharacters();
		arpgContext.viewPoint.x = arpgContext.player.x - graphics().width()
				/ 2.0f;
		arpgContext.viewPoint.y = arpgContext.player.y - graphics().height()
				/ 2.0f;

		arpgContext.mapChipSprite.setOffset(arpgContext.viewPoint);
		arpgContext.mapChipSprite.paint(alpha);

		for (Iterator<BaseCharacter> it = arpgContext.characters.iterator(); it
				.hasNext();) {
			BaseCharacter character = it.next();
			if (character.getSpriteContainer().getLayer().destroyed()) {
				it.remove();
			} else {
				character.paintInView(alpha, arpgContext.viewPoint);
			}
		}

		for (Iterator<SpriteAnimation> it = arpgContext.effects.iterator(); it
				.hasNext();) {
			SpriteAnimation effect = it.next();
			effect.paintInView(alpha, arpgContext.viewPoint);
			if (effect.isAnimationEnd()) {
				effect.getLayer().destroy();
				it.remove();
			}
		}
	}

	@Override
	public void update(float delta) {
		if (pointerListener.mouseDownTime >= 0) {
			pointerListener.mouseDownTime++;
		}

		if (pointerListener.mouseUpTime >= 0) {
			pointerListener.mouseUpTime++;
		}
		for (BaseCharacter character : arpgContext.characters) {
			if (character instanceof EnemyCharacter) {
				((EnemyCharacter) character).simpleAction();
			} else if (character instanceof PlayerCharacter) {
				((PlayerCharacter) character).inputAction(arpgContext.input);
			}
			character.updateFrame(arpgContext.characters);
		}
	}

	@Override
	public int updateRate() {
		return 30;
	}

	private class PointerListener implements playn.core.Pointer.Listener {

		public int mouseDownTime;
		public int mouseUpTime;
		private boolean drugMode = false;

		public PointerListener() {
			mouseDownTime = -1;
			mouseUpTime = -1;
		}

		@Override
		public void onPointerStart(Event event) {
			arpgContext.input.isMouseDown = true;
			onPointerDrag(event);
			if (drugMode) {
				if (Math.pow(arpgContext.input.axisX, 2)
						+ Math.pow(arpgContext.input.axisY, 2) < Math.pow(
						SpriteConstants.CHARACTER_RADIUS_DEFAULT, 2)) {
					arpgContext.input.isCursor = true;
				}
			} else {
				if (mouseUpTime >= 0 && mouseUpTime < 8) {
					if (!arpgContext.input.isDoubleClick) {
						arpgContext.input.isDoubleDown = true;
					}
				} else {
					arpgContext.input.isCursor = true;
				}
			}
			mouseDownTime = 0;
			mouseUpTime = -1;
			arpgContext.input.isMouseClick = false;
			arpgContext.input.isDoubleClick = false;
		}

		@Override
		public void onPointerEnd(Event event) {
			if (mouseDownTime < 8) {
				arpgContext.input.isMouseClick = true;
				if (arpgContext.input.isDoubleDown) {
					arpgContext.input.isDoubleClick = true;
				}
				mouseUpTime = 0;
			} else {
				mouseUpTime = -1;
				arpgContext.input.axisX = arpgContext.input.axisY = 0;
			}
			mouseDownTime = -1;

			arpgContext.input.isCursor = false;
			arpgContext.input.isMouseDown = false;
			arpgContext.input.isDoubleDown = false;
		}

		@Override
		public void onPointerDrag(Event event) {
			arpgContext.input.axisX = event.x() - graphics().width() / 2;
			arpgContext.input.axisY = event.y() - graphics().height() / 2;
		}

		@Override
		public void onPointerCancel(Event event) {

		}
	}

}
