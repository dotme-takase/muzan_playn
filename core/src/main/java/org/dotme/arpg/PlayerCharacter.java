package org.dotme.arpg;

import org.dotme.sprite.arpg.CharacterSpriteContainer;

public class PlayerCharacter extends BaseCharacter {
	private BaseCharacter lastEnemy = null;
	public PlayerCharacter(CharacterSpriteContainer spriteCon,
			BaseItem rightArm, BaseItem leftArm) {
		super(spriteCon, rightArm, leftArm);
		this.MHP = this.HP = 100;
		this.teamNumber = 0;
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
					} else if (this.action == BaseCharacter.CHARACTER_ACTION_ATTACK) {
						if (input.isDoubleDown) {
							this.action = BaseCharacter.CHARACTER_ACTION_DEFENCE_MOTION;
							this.defenceFrame = 16;
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
					this.action = BaseCharacter.CHARACTER_ACTION_ATTACK;
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

	public BaseCharacter getLastEnemy() {
		return lastEnemy;
	}

	public void setLastEnemy(BaseCharacter lastEnemy) {
		this.lastEnemy = lastEnemy;
	}
}
