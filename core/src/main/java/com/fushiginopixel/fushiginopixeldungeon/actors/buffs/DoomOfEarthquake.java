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
import com.fushiginopixel.fushiginopixeldungeon.Statistics;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Mob;
import com.fushiginopixel.fushiginopixeldungeon.effects.CellEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.levels.CavesBossLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.CityBossLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.HallsBossLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.PrisonBossLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.SewerBossLevel;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.ui.BuffIndicator;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class DoomOfEarthquake extends Buff {

	//the amount of turns remaining before beneficial passive effects turn off
	private float timer = 0; //starts at 50 turns

	@Override
	public boolean act() {
		/*if(!target.isAlive()){
			return true;
		}

		if (timer <= 7500)
			timer ++;*/

		if(!Statistics.amuletObtained){
			if(timer == 900 || timer == 1100 || timer == 1300){
				Camera.main.shake(2, 0.7f);
				Sample.INSTANCE.play(Assets.SND_ROCKS);
				((Hero)target).resting = false;
				if(timer == 900)
					GLog.w(Messages.get(this, "warning"));
				else if(timer == 1100)
					GLog.w(Messages.get(this, "warning_1"));
				else if(timer == 1300)
					GLog.w(Messages.get(this, "warning_2"));
				for (Mob mob : Dungeon.level.mobs) {
					mob.beckon( -1 );
				}
			}else if(timer >= 1500){
				for (int cell = 0 ; cell < Dungeon.level.length() ; cell++) {

					if (Dungeon.level.heroFOV[cell] && !Dungeon.level.solid[cell]) {
						CellEmitter.get(cell - Dungeon.level.width()).start(Speck.factory(Speck.ROCK), 0.07f, 10);
					}

				}

				for (Mob mob : Dungeon.level.mobs.toArray(new Mob[Dungeon.level.mobs.size()])) {
					mob.destroy();
					mob.sprite.killAndErase();
				}
				Camera.main.shake(3, 0.7f);
				Sample.INSTANCE.play(Assets.SND_ROCKS);
				Dungeon.hero.dieByDoom(this);
				GLog.n( Messages.get(this, "ondeath") );
			}
		}else{
			if(timer == 4500 || timer == 5500 || timer == 6500) {
				Camera.main.shake(2, 0.7f);
				Sample.INSTANCE.play(Assets.SND_ROCKS);
				((Hero)target).resting = false;
				if(timer == 4500)
					GLog.w(Messages.get(this, "warning"));
				else if(timer == 5500)
					GLog.w(Messages.get(this, "warning_1"));
				else if(timer == 6500)
					GLog.w(Messages.get(this, "warning_2"));
				for (Mob mob : Dungeon.level.mobs) {
					mob.beckon( -1 );
				}

			}else if(timer >= 7500){
				for (int cell = 0 ; cell < Dungeon.level.length() ; cell++) {

					if (Dungeon.level.heroFOV[cell] && !Dungeon.level.solid[cell]) {
						CellEmitter.get(cell - Dungeon.level.width()).start(Speck.factory(Speck.ROCK), 0.07f, 10);
					}

				}

				for (Mob mob : Dungeon.level.mobs) {
					mob.destroy();
					mob.sprite.killAndErase();
				}
				Camera.main.shake(3, 0.7f);
				Sample.INSTANCE.play(Assets.SND_ROCKS);
				Dungeon.hero.dieByDoom(this);
				GLog.n( Messages.get(this, "ondeath") );
			}
		}

		spend(TICK);
		if(target.isAlive()){
			if(timer <= 7500){
				timer++;
			}
		}else{
			detach();
			return true;
		}


		return true;
	}

	public void setTime(float time){
		timer = time;
	}

	@Override
	public void detach() {
		super.detach();
		timer = 0;
	}

	private final String TIMER = "timer";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put( TIMER, timer );
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		timer = bundle.getFloat( TIMER );
	}
}
