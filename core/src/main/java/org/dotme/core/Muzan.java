package org.dotme.core;

import static playn.core.PlayN.graphics;
import static playn.core.PlayN.pointer;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import org.dotme.arpg.ARPGContext;
import org.dotme.arpg.ARPGUtils;
import org.dotme.arpg.BaseCharacter;
import org.dotme.arpg.BaseItem;
import org.dotme.arpg.EnemyCharacter;
import org.dotme.arpg.PlayerCharacter;
import org.dotme.sprite.SpriteAnimation;
import org.dotme.sprite.arpg.SpriteConstants;

import playn.core.Canvas;
import playn.core.Color;
import playn.core.Game;
import playn.core.ImageLayer;
import playn.core.Pointer.Event;
import playn.core.SurfaceLayer;

public class Muzan implements Game {
	private PointerListener pointerListener = null;
	private ARPGContext arpgContext;

	ResourceBundle messages = null;

	@Override
	public void init() {
		arpgContext = ARPGContext.getInstance();
		arpgContext.init();
		pointerListener = new PointerListener();
		pointer().setListener(pointerListener);
		messages = ResourceBundle.getBundle("Messages");
	}

	@Override
	public void paint(float alpha) {
		// the background automatically paints itself, so no need to do anything
		// here!
		if (arpgContext.mode == ARPGContext.MODE_MAIN) {
			ARPGUtils.refixCharacters();
			arpgContext.viewPoint.x = arpgContext.player.x - graphics().width()
					/ 2.0f;
			arpgContext.viewPoint.y = arpgContext.player.y
					- graphics().height() / 2.0f;

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

			for (Iterator<BaseItem> it = arpgContext.droppedItems.iterator(); it
					.hasNext();) {
				BaseItem item = it.next();
				item.paintInView(alpha, arpgContext.viewPoint);
			}
			arpgContext.statusSprite.paint(alpha);
		} else if (arpgContext.mode == ARPGContext.MODE_MENU) {
			((SurfaceLayer) (arpgContext.mapChipSprite.getLayer())).surface()
					.clear();
			if (pointerListener.menuMap == null) {
				pointerListener.menuMap = ARPGUtils.createMenuMap(messages);
				for (Entry<String, ImageLayer> e : pointerListener.menuMap
						.entrySet()) {
					arpgContext.menuLayer.add(e.getValue());
				}
			}
			Canvas menuCanvas = arpgContext.getMenuCanvas();
			menuCanvas.clear();
			menuCanvas.setFillColor(Color.rgb(255, 255, 255));
			for (Entry<String, ImageLayer> e : pointerListener.menuMap
					.entrySet()) {
				ImageLayer l = e.getValue();
				if (e.getKey().equals(pointerListener.menuSelected)) {
					menuCanvas.setAlpha(0.3f);
				} else {
					menuCanvas.setAlpha(0.1f);
				}
				menuCanvas.fillRect(l.tx(), l.ty(), l.width(), l.height());
			}
		}
	}

	@Override
	public void update(float delta) {
		if (arpgContext.mode == ARPGContext.MODE_MAIN) {
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
					((PlayerCharacter) character)
							.inputAction(arpgContext.input);
				}
				character.updateFrame(arpgContext);
			}
			ARPGUtils.checkDropItem(arpgContext, arpgContext.player);
			ARPGUtils.updateContext(arpgContext);
		} else if (arpgContext.mode == ARPGContext.MODE_MENU) {
			if (ARPGContext.MAIN_MENU_START_GAME
					.equals(pointerListener.menuClicked)) {
				arpgContext.menuLayer.setVisible(false);
				arpgContext.mode = ARPGContext.MODE_MAIN;
			}
		}
	}

	@Override
	public int updateRate() {
		return 30;
	}

	private class PointerListener implements playn.core.Pointer.Listener {
		public int mouseDownTime;
		public int mouseUpTime;
		public Map<String, ImageLayer> menuMap = null;
		public String menuSelected = null;
		public String menuClicked = null;

		private boolean drugMode = false;

		public PointerListener() {
			mouseDownTime = -1;
			mouseUpTime = -1;
		}

		@Override
		public void onPointerStart(Event event) {
			menuClicked = menuSelected = null;
			if (menuMap != null) {
				for (Entry<String, ImageLayer> e : menuMap.entrySet()) {
					ImageLayer l = e.getValue();
					if ((event.x() > l.tx())
							&& (event.x() < l.tx() + l.width())
							&& (event.y() > l.ty())
							&& (event.y() < l.ty() + l.height())) {
						menuSelected = e.getKey();
						return;
					}
				}
			}

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
			if ((menuMap != null) && (menuSelected != null)) {
				ImageLayer l = menuMap.get(menuSelected);
				if ((event.x() > l.tx()) && (event.x() < l.tx() + l.width())
						&& (event.y() > l.ty())
						&& (event.y() < l.ty() + l.height())) {
					menuClicked = menuSelected;
					return;
				}
			}

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
			if ((menuMap != null) && (menuSelected != null)) {
				ImageLayer l = menuMap.get(menuSelected);
				if ((event.x() > l.tx()) && (event.x() < l.tx() + l.width())
						&& (event.y() > l.ty())
						&& (event.y() < l.ty() + l.height())) {
				} else {
					menuSelected = null;
				}
			}

			arpgContext.input.axisX = event.x() - graphics().width() / 2;
			arpgContext.input.axisY = event.y() - graphics().height() / 2;
		}

		@Override
		public void onPointerCancel(Event event) {

		}
	}

}
