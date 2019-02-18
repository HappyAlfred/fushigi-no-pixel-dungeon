/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2018 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.fushiginopixel.fushiginopixeldungeon.windows;

import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.npcs.NPC;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;

import java.util.ArrayList;

public class WndQuest extends WndTitledMessage {

	protected ArrayList<String> chat = new ArrayList<>();
	protected int index = 0;
	public NPC questgiver;

	public WndQuest( NPC questgiver, String text ) {
		super( questgiver.sprite(), Messages.titleCase( questgiver.name ), text );
	}

	public WndQuest( NPC questgiver, ArrayList<String> text, int index){
		super( questgiver.sprite(), Messages.titleCase( questgiver.name ), text.get(index) );
		this.index = index;
		this.chat = text;
		this.questgiver = questgiver;
	}

	@Override
	public void hide() {
		super.hide();
		if(chat != null && !chat.isEmpty()){
			if(index < chat.size() - 1){
				index++;
				GameScene.show(new WndQuest(questgiver, chat, index));
			}
		}
	}

	public static void chating( NPC questgiver ,ArrayList<String> text){
		if(text != null && !text.isEmpty()){
			GameScene.show(new WndQuest(questgiver, text, 0));
		}
	}
}
