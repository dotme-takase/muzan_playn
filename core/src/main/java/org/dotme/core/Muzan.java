package org.dotme.core;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import static playn.core.PlayN.pointer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dotme.arpg.ARPGUtils;
import org.dotme.arpg.BaseCharacter;
import org.dotme.arpg.InputStatus;
import org.dotme.arpg.MapGenerator;
import org.dotme.arpg.PlayerCharacter;
import org.dotme.core.math.Vector2;
import org.dotme.sprite.MapChip;
import org.dotme.sprite.MapChipSprite;
import org.dotme.sprite.arpg.CharacterSpriteContainer;
import org.dotme.sprite.arpg.SpriteConstants;

import playn.core.Game;
import playn.core.Pointer.Event;

public class Muzan implements Game {
	private PlayerCharacter player;
	private MapChipSprite mapChipSprite;
	private MapGenerator mapGenerator;
	private Vector2 viewPoint;
	private List<BaseCharacter> characters;
	private Map<String, Vector2> characterPreviousPoints = null;
	private InputStatus input = null;
	private PointerListener pointerListener = null;

	@Override
	public void init() {
		mapChipSprite = new MapChipSprite(assets().getImage("img/tiles1.png"),
				SpriteConstants.TILE_SIZE_DEFAULT,
				SpriteConstants.TILE_SIZE_DEFAULT, graphics().width(),
				graphics().height());
		mapGenerator = new MapGenerator();
		MapChip[][] mapChips = mapGenerator.generate(5, 5);
		viewPoint = new Vector2(0, 0);

		mapChipSprite.setOffset(viewPoint);
		mapChipSprite.setMap(mapChips);
		graphics().rootLayer().add(mapChipSprite.getLayer());

		CharacterSpriteContainer playerCon = new CharacterSpriteContainer(
				"player", "img/player.png");
		playerCon.getLeftArmSprite().setTexture(
				assets().getImage("img/shields.png"));
		playerCon.getRightArmSprite().setTexture(
				assets().getImage("img/swords.png"));
		graphics().rootLayer().add(playerCon.getLayer());
		player = new PlayerCharacter(playerCon, null, null);
		input = new InputStatus();
		pointerListener = new PointerListener();
		pointer().setListener(pointerListener);
		characters = new ArrayList<BaseCharacter>();
		characters.add(player);
		ARPGUtils.warpToRandomPoint(player, mapChipSprite,
				characterPreviousPoints);
		playerCon.gotoAndPlay(CharacterSpriteContainer.ANIMATION_WALK, true);
		playerCon.pauseAndReset();
	}

	@Override
	public void paint(float alpha) {
		// the background automatically paints itself, so no need to do anything
		// here!
		ARPGUtils.refixCharacters(characters, mapChipSprite,
				characterPreviousPoints);
		this.viewPoint.x = player.getX() - graphics().width() / 2.0f;
		this.viewPoint.y = player.getY() - graphics().height() / 2.0f;

		mapChipSprite.setOffset(viewPoint);
		mapChipSprite.paint(alpha);
		player.paintInView(alpha, viewPoint);
	}

	@Override
	public void update(float delta) {
		if (pointerListener.mouseDownTime >= 0) {
			pointerListener.mouseDownTime++;
		}

		if (pointerListener.mouseUpTime >= 0) {
			pointerListener.mouseUpTime++;
		}
		player.inputAction(input);
		player.updateFrame();
	}

	@Override
	public int updateRate() {
		return 25;
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
			input.isMouseDown = true;
			onPointerDrag(event);
			if (drugMode) {
				if (Math.pow(input.axisX, 2) + Math.pow(input.axisY, 2) < Math
						.pow(SpriteConstants.CHARACTER_RADIUS_DEFAULT, 2)) {
					input.isCursor = true;
				}
			} else {
				if (mouseUpTime < 0 || mouseUpTime >= 8) {
					input.isCursor = true;
				}
			}
			mouseDownTime = 0;
			mouseUpTime = -1;
			input.isMouseClick = false;
		}

		@Override
		public void onPointerEnd(Event event) {
			input.isCursor = input.isMouseDown = false;
			if (mouseDownTime < 8) {
				input.isMouseClick = true;
				mouseUpTime = 0;
			} else {
				mouseUpTime = -1;
				input.axisX = input.axisY = 0;
			}
			mouseDownTime = -1;
		}

		@Override
		public void onPointerDrag(Event event) {
			input.axisX = event.x() - graphics().width() / 2;
			input.axisY = event.y() - graphics().height() / 2;
		}

		@Override
		public void onPointerCancel(Event event) {

		}
	}

}
