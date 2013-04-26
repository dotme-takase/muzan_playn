package org.dotme.arpg;

import org.dotme.sprite.SpriteContainer;

public class PlayerCharacter extends BaseCharacter {
	public PlayerCharacter(SpriteContainer spriteCon, BaseItem rightArm,
			BaseItem leftArm) {
		super(spriteCon, rightArm, leftArm);
	}

	public void inputAction(InputStatus input) {
		if (this.isAction
				&& (this.action == BaseCharacter.CHARACTER_ACTION_DAMAGE
						|| this.action == BaseCharacter.CHARACTER_ACTION_DEAD || this.action == BaseCharacter.CHARACTER_ACTION_PARRIED)) {
			this.isWalking = false;
		} else {
			if (input.isMouseDown || input.isMouseClick) {
				this.direction = (int) (Math.atan2(input.axisY, input.axisX) * 180 / Math.PI);
			}

			if (input.isCursor) {
				this.isWalking = true;
				this.isAction = false;
			} else if (input.isMouseDown) {
				this.isWalking = false;
				if (this.isAction) {
					if (this.action == BaseCharacter.CHARACTER_ACTION_DEFENCE) {
						if (this.defenceFrame > 0) {
							this.defenceFrame--;
						}
					}
				} else {
					this.isAction = true;
					this.action = BaseCharacter.CHARACTER_ACTION_DEFENCE_MOTION;
					this.defenceFrame = 8;
				}
			} else {
				this.isWalking = false;
				if (!this.isAction && input.isMouseClick) {
					this.isAction = true;
					this.action = BaseCharacter.CHARACTER_ACTION_DEFENCE_MOTION;
				}

				if (this.isAction) {
					if (this.action == BaseCharacter.CHARACTER_ACTION_ATTACK) {
					} else if ((this.action == BaseCharacter.CHARACTER_ACTION_DEFENCE)
							&& (this.defenceFrame > 0)) {
						this.action = BaseCharacter.CHARACTER_ACTION_ATTACK;
					} else if (this.action == BaseCharacter.CHARACTER_ACTION_DEFENCE_MOTION) {
						this.action = BaseCharacter.CHARACTER_ACTION_ATTACK;
					} else {
						this.isAction = false;
						this.action = BaseCharacter.CHARACTER_ACTION_NONE;
					}
				} else {

				}
			}
		}
		input.isMouseClick = false;
	}
}
