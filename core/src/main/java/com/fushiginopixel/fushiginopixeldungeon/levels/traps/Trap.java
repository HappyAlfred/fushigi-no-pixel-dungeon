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

package com.fushiginopixel.fushiginopixeldungeon.levels.traps;

import com.fushiginopixel.fushiginopixeldungeon.Assets;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.CorrosiveGas;
import com.fushiginopixel.fushiginopixeldungeon.levels.Level;
import com.fushiginopixel.fushiginopixeldungeon.levels.Terrain;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public abstract class Trap implements Bundlable {

	//trap colors
	public static final int RED     = 0;
	public static final int ORANGE  = 1;//flame
	public static final int YELLOW  = 2;//thunder
	public static final int GREEN   = 3;
	public static final int TEAL    = 4;
	public static final int VIOLET  = 5;
	public static final int WHITE   = 6;
	public static final int GREY    = 7;
	public static final int BLACK   = 8;

	//trap shapes
	public static final int DOTS        = 0;//self target
	public static final int WAVES       = 1;//magical
	public static final int GRILL       = 2;//gas
	public static final int STARS       = 3;//self target && powerful
	public static final int DIAMOND     = 4;//mechanic
	public static final int CROSSHAIR   = 5;//auto aiming
	public static final int LARGE_DOT   = 6;//dangerous

	public static final int SINGLE   		= 0;  //trigger once
	public static final int   MULTIPLE     	= 1;  //can trigger multiple
	public static final int   PERPETUAL     	= 2;  //can't broken

	public int durability = SINGLE;
	public boolean alwaysVisible = false;

	public String name = Messages.get(this, "name");

	public int color;
	public int shape;

	public int pos;

	public boolean visible;
	public boolean active = true;

	public Trap set(int pos){
		this.pos = pos;
		return this;
	}

	public Trap reveal() {
		visible = true;
		GameScene.updateMap(pos);
		return this;
	}

	public Trap hide() {
		if(alwaysVisible){
			return reveal();
		}else {
			visible = false;
			GameScene.updateMap(pos);
			return this;
		}
	}

	public void trigger() {
		if (active) {
			if (Dungeon.level.heroFOV[pos]) {
				Sample.INSTANCE.play(Assets.SND_TRAP);
			}
			if(durability == SINGLE) {
				disarm();
				reveal();
				activate();
			}
			else if(durability == MULTIPLE){
				//this trap is not disarmed by being triggered
				if(visible && Random.Int(3) == 0){
					disarm();
				}
				reveal();
				if(active) {
					Level.set(pos, Terrain.TRAP);
				}
				activate();
			}
			else if(durability == PERPETUAL){
				reveal();
				Level.set(pos, Terrain.TRAP);
				activate();
			}
		}

	}

	//not be set in boss level
	public boolean dangerous() {
		return false;
	}

	public abstract void activate();

	protected void disarm(){
		Dungeon.level.disarmTrap(pos);
		active = false;
	}

	private static final String POS	= "pos";
	private static final String VISIBLE	= "visible";
	private static final String ACTIVE = "active";

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		pos = bundle.getInt( POS );
		visible = bundle.getBoolean( VISIBLE );
		if (bundle.contains(ACTIVE)){
			active = bundle.getBoolean(ACTIVE);
		}
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		bundle.put( POS, pos );
		bundle.put( VISIBLE, visible );
		bundle.put( ACTIVE, active );
	}

	public String desc() {
		String info = Messages.get(this, "desc");
		switch (durability){
			case (SINGLE) : info += "\n\n" + Messages.get(Trap.class, "single");break;
			case (MULTIPLE) : info += "\n\n" + Messages.get(Trap.class, "multiple");break;
			case (PERPETUAL) : info += "\n\n" + Messages.get(Trap.class, "perpetual");break;
		}
		if(alwaysVisible){
			info += "\n\n" + Messages.get(Trap.class, "always_visible");
		}
		return info;
	}

	public static final Class<?>[] traps(){
		return new Class[]{
				AlarmTrap.class, BurningTrap.class, ShockingTrap.class, OozeTrap.class, TeleportationTrap.class, ChillingTrap.class, GrippingTrap.class,
				WeakeningTrap.class, SummoningTrap.class, CursingTrap.class, FlockTrap.class,
				ToxicTrap.class, ConfusionTrap.class, CorrosionTrap.class,
				GuardianTrap.class, BlazingTrap.class, StormTrap.class, MucusTrap.class, WarpingTrap.class, FrostTrap.class, FlashingTrap.class,
				PitfallTrap.class, ExplosiveTrap.class, MultiplicationTrap.class, RockfallTrap.class,
				LogTrap.class, PoisonDartTrap.class, DisintegrationTrap.class, WornDartTrap.class,
				DisarmingTrap.class, DistortionTrap.class, GrimTrap.class
		};
	}
}
