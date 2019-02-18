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

package com.fushiginopixel.fushiginopixeldungeon.actors.buffs;

import com.fushiginopixel.fushiginopixeldungeon.Assets;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.levels.CavesBossLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.CityBossLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.HallsBossLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.PrisonBossLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.SewerBossLevel;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Bundle;

public class LockedFloor extends Buff {

	//the amount of turns remaining before beneficial passive effects turn off
	private float left = 50; //starts at 50 turns

	@Override
	public boolean act() {
		if( Dungeon.level instanceof SewerBossLevel || Dungeon.level instanceof PrisonBossLevel || Dungeon.level instanceof CityBossLevel || Dungeon.level instanceof CavesBossLevel){
			Music.INSTANCE.play( Assets.BOSS_MID, true );
		}
		else if( Dungeon.level instanceof HallsBossLevel){
			Music.INSTANCE.play( Assets.BOSS_FINAL, true );
		}
		spend(TICK);

		if (!Dungeon.level.locked)
			detach();

		if (left >= 1)
			left --;



		return true;
	}

	@Override
	public void detach() {
		super.detach();
		GameScene.playlevelmusic();
	}

	public void addTime(float time){
		left += time;
	}

	public boolean regenOn(){
		return left >= 1;
	}

	private final String LEFT = "left";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put( LEFT, left );
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		left = bundle.getFloat( LEFT );
	}

	@Override
	public int icon() {
		return BuffIndicator.LOCKED_FLOOR;
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}
}
