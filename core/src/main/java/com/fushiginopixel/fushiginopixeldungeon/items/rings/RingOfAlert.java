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

package com.fushiginopixel.fushiginopixeldungeon.items.rings;

import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.MindVision;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Random;

public class RingOfAlert extends Ring {

	public int charge;

	@Override
	protected RingBuff buff() {
		charge = this.level();
		return new Alert();
	}

	@Override
	public boolean doEquip(final Char hero){
		Dungeon.viewUpdate();
		return super.doEquip(hero);
	}

	@Override
	public boolean doUnequip(Char hero, boolean collect, boolean single){
		Dungeon.viewUpdate();
		return super.doUnequip(hero, collect, single);
	}

	@Override
	public String desc() {
		if(this.isIdentified() && this.level()>= 5) {
			return Messages.get(this, "desc");
		}
		else {
			return Messages.get(this, "desc_1");
		}
	}

	public class Alert extends RingBuff {

		public int level = charge;
		public int distance = 2;@Override
		public int icon() {
			return BuffIndicator.MIND_VISION;
		}

		@Override
		public String toString() {
			if(MindVision.mindvrBonus(target) >= 6){
				return Messages.get(MindVision.class, "name");
			}
			else{
				return Messages.get(MindVision.class, "name_1");
			}
		}

		@Override
		public void detach() {
			super.detach();
			Dungeon.observe();
			GameScene.updateFog();
		}


		@Override
		public String desc() {
			if(MindVision.mindvrBonus(target) > 5){
				return Messages.get(this, "desc");
			}
			else if(MindVision.mindvrBonus(target) > 0){
				return Messages.get(this, "desc_1",MindVision.mindvrBonus(target) * 2,0);
			}
			else return Messages.get(MindVision.class, "desc_2");
		}
		/*
		@Override
		public boolean act() {
			super.act();

			if(target.buff(MindVision.class) == null){
				MindVision mindvr = Buff.affect(target, MindVision.class, 3);
				mindvr.level = charge;
			}
			return true;
		}
		*/
	}

	/*
	//ring of force
	@Override
	protected RingBuff buff( ) {
		return new Force();
	}
	
	public static int armedDamageBonus( Char ch ){
		return getBonus( ch, Force.class);
	}
	
	
	// *** Weapon-like properties ***

	private static float tier(int str){
		float tier = Math.max(1, (str - 8)/2f);
		//each str point after 18 is half as effective
		if (tier > 5){
			tier = 5 + (tier - 5) / 2f;
		}
		return tier;
	}

	public static int damageRoll( Hero hero ){
		if (hero.buff(Force.class) != null) {
			int level = getBonus(hero, Force.class);
			float tier = tier(hero.STR());
			return Random.NormalIntRange(min(level, tier), max(level, tier));
		} else {
			//attack without any ring of force influence
			return Random.NormalIntRange(1, Math.max(hero.STR()-8, 1));
		}
	}

	//same as equivalent tier weapon
	private static int min(int lvl, float tier){
		return Math.round(
				tier +  //base
				lvl     //level scaling
		);
	}

	//same as equivalent tier weapon
	private static int max(int lvl, float tier){
		return Math.round(
				5*(tier+1) +    //base
				lvl*(tier+1)    //level scaling
		);
	}

	@Override
	public String desc() {
		String desc = super.desc();
		float tier = tier(Dungeon.hero.STR());
		if (levelKnown) {
			desc += "\n\n" + Messages.get(this, "avg_dmg", min(level(), tier), max(level(), tier));
		} else {
			desc += "\n\n" + Messages.get(this, "typical_avg_dmg", min(1, tier), max(1, tier));
		}

		return desc;
	}

	public class Force extends RingBuff {
	}
	*/
}

