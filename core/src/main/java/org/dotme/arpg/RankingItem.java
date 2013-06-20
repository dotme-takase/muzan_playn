package org.dotme.arpg;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import playn.core.Json;
import playn.core.PlayN;

public class RankingItem implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -7955107801494919369L;
	public String enemyName;
	public int floor;
	public int score;

	public static final String JSON_KEY_ENEMY_NAME = "enemyName";
	public static final String JSON_KEY_FLOOR = "floor";
	public static final String JSON_KEY_SCORE = "score";

	public String getString(ResourceBundle messages, int index) {
		return MessageFormat.format(messages.getString("textRecordDefeated"),
				floor, messages.getString("npc" + enemyName), index);
	}

	public String toJson() {
		Json.Writer writer = PlayN.json().newWriter();
		writer.object();
		writer.value(JSON_KEY_ENEMY_NAME, enemyName);
		writer.value(JSON_KEY_FLOOR, floor);
		writer.value(JSON_KEY_SCORE, score);
		writer.end();
		return writer.write();
	}

	public static RankingItem fromJson(String json) {
		Json.Object object = PlayN.json().parse(json);
		RankingItem item = new RankingItem();
		item.enemyName = object.getString(JSON_KEY_ENEMY_NAME);
		item.floor = object.getInt(JSON_KEY_FLOOR);
		item.score = object.getInt(JSON_KEY_SCORE);
		return item;
	}
}
