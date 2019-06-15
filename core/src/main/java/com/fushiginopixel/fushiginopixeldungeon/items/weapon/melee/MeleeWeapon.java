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

package com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee;

import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.Weapon;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.watabou.utils.Random;

public class MeleeWeapon extends Weapon {

	public int tier;
	@Override
	public int min(int lvl) {
		return  tier +  //base
				lvl;    //level scaling
	}

	@Override
	public int max(int lvl) {
		return  5*(tier+1) +    //base
				lvl*3;   //level scaling
	}

	/*public int STRReq(int lvl){
		lvl = Math.max(0, lvl);
		//strength req decreases at +1,+3,+6,+10,etc.
		return (8 + tier * 2) - (int)(Math.sqrt(8 * lvl + 1) - 1)/2;
	}*/
	
	@Override
	public int damageRoll(Char owner) {
		int damage = augment.damageFactor(super.damageRoll( owner ));

		/*
		if (owner instanceof Hero) {
			damage *= ((Hero)owner).strengthMultiplier();
		}
		*/
		
		return damage;
	}
	
	@Override
	public String info() {

		String info = desc();

		if (levelKnown) {
			info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_known", tier, augment.damageFactor(min()), augment.damageFactor(max()));
		} else {
			info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_unknown", tier, minUnIdentified(), maxUnIdentified());
			/*if (STRReq(0) > Dungeon.hero.STR()) {
				info += " " + Messages.get(MeleeWeapon.class, "probably_too_heavy");
			}*/
		}

		float strengthAttack = Dungeon.hero.strengthMultiplier();
		if (strengthAttack < 1) {
			info += " " + Messages.get(Weapon.class, "too_heavy", Math.round((1 - strengthAttack) * 100));
		} else if (strengthAttack > 1){
			info += " " + Messages.get(Weapon.class, "excess_str", Math.round((strengthAttack - 1) * 100));
		}

		String stats_desc = Messages.get(this, "stats_desc");
		if (!stats_desc.equals("")) info+= "\n\n" + stats_desc;

		switch (augment) {
			case SPEED:
				info += "\n\n" + Messages.get(Weapon.class, "faster");
				break;
			case DAMAGE:
				info += "\n\n" + Messages.get(Weapon.class, "stronger");
				break;
			case NONE:
		}

		if(isIdentified())
			info += "\n"  + Messages.get(Weapon.class, "limit", this.enchantmentCount() , this.LIMIT);
		else
			info += "\n"  + Messages.get(Weapon.class, "limit_1", this.LIMIT);

		if(enchantmentCount() > 0 || properties.size() > 0) {
			info += "\n";
		}
		for(Enchantment e :properties){
			info += "\n" + Messages.get(Weapon.class, "enchanted", e.nameProperties());
		}
		if (enchantmentCount() != 0 && (cursedKnown || !hasCurseEnchant())){
			for(Enchantment e :enchantment){
				info += "\n" + Messages.get(Weapon.class, "enchanted", e.name());
			}
		}

		if (cursed && isEquipped( Dungeon.hero )) {
			info += "\n\n" + Messages.get(Weapon.class, "cursed_worn");
		} else if (cursedKnown && cursed) {
			info += "\n\n" + Messages.get(Weapon.class, "cursed");
		}
		
		return info;
	}
	
	@Override
	public int price() {
		int price = 20 * tier;
		if (hasGoodEnchant()) {
			price *= 1.5;
		}
		if (cursedKnown && (cursed || hasCurseEnchant())) {
			price /= 2;
		}
		if (levelKnown && level() > 0) {
			price *= (level() + 1);
		}
		if (price < 1) {
			price = 1;
		}
		return price;
	}

}
