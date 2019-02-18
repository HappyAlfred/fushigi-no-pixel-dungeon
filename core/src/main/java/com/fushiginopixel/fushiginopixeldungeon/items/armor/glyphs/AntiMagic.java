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

package com.fushiginopixel.fushiginopixeldungeon.items.armor.glyphs;

import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Charm;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Weakness;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Dragon;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.DeathEye;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.FallenAngel;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Lich;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Shaman;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Warlock;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Yog;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.Armor;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.DisintegrationTrap;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.GrimTrap;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite;

import java.util.HashSet;

public class AntiMagic extends Armor.Glyph {

	private static ItemSprite.Glowing TEAL = new ItemSprite.Glowing( 0x88EEFF );
	
	/*public static final HashSet<Class> RESISTS = new HashSet<>();
	static {
		RESISTS.add( Charm.class );
		RESISTS.add( Weakness.class );
		
		RESISTS.add( DisintegrationTrap.class );
		RESISTS.add( GrimTrap.class );

		RESISTS.add( FallenAngel.class );
		RESISTS.add( Shaman.class );
		RESISTS.add( Dragon.class );
		RESISTS.add( Warlock.class );
		RESISTS.add( Lich.class );
		RESISTS.add( Eye.class );
		RESISTS.add( Yog.BurningFist.class );
	}*/

	public static final HashSet<EffectType> RESISTSTYPE = new HashSet<>();
	static {
		RESISTSTYPE.addAll(EffectType.MAGICAL_AFFECTS);
	}
	
	@Override
	public float proc(Armor armor, Char attacker, Char defender, int damage, EffectType type ) {
		//no proc effect, see Hero.damage
		return 1;
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return TEAL;
	}

}