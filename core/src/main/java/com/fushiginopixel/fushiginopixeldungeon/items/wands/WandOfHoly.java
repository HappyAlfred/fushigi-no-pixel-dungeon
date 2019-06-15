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

package com.fushiginopixel.fushiginopixeldungeon.items.wands;

import com.fushiginopixel.fushiginopixeldungeon.Assets;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Charm;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Mob;
import com.fushiginopixel.fushiginopixeldungeon.effects.Beam;
import com.fushiginopixel.fushiginopixeldungeon.effects.CellEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.BloodParticle;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.LeafParticle;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.ShadowParticle;
import com.fushiginopixel.fushiginopixeldungeon.items.Generator;
import com.fushiginopixel.fushiginopixeldungeon.items.Heap;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.Armor;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.Ring;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.Weapon;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.MagesStaff;
import com.fushiginopixel.fushiginopixeldungeon.levels.Terrain;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.plants.Plant;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.CharSprite;
import com.fushiginopixel.fushiginopixeldungeon.tiles.DungeonTilemap;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.Iterator;

public class WandOfHoly extends Wand {

	{
		initials = 12;

		collisionProperties = Ballistica.PROJECTILE;
	}

	@Override
	protected void onZap(Ballistica beam) {

		for (int c : beam.subPath(0, beam.dist))
			CellEmitter.center(c).burst( BloodParticle.BURST, 1 );

		int cell = beam.collisionPos;

		Char ch = Actor.findChar(cell);
		Heap heap = Dungeon.level.heaps.get(cell);

		//this wand does a bunch of different things depending on what it targets.

		//if we find a character..
		if (ch != null && ch instanceof Mob){

			processSoulMark(ch, chargesPerCast());

			if( ch.properties().contains(Char.Property.ANGEL)){
				float duration = 5+level()/3;
				Buff.affect(ch, Charm.class, duration, new EffectType(EffectType.BEAM,EffectType.SPIRIT)).object = curUser.id();

				ch.sprite.centerEmitter().start( Speck.factory( Speck.HEART ), 0.2f, 5 );
			}

			if (ch.properties().contains(Char.Property.UNDEAD) || ch.properties().contains(Char.Property.DEMONIC)){

				int damage = 30 + level() * 3;
				ch.damage(damage, this, new EffectType(EffectType.BEAM,0));
				ch.sprite.emitter().start(ShadowParticle.UP, 0.05f, 20);
				Sample.INSTANCE.play(Assets.SND_BURNING);

			}else if (!ch.properties().contains(Char.Property.MACHANIC)){

				int missingHP = ch.HT - ch.HP;
				int healing = Math.min(missingHP ,30 + level() * 3);
				ch.HP += healing;
				ch.sprite.emitter().burst(Speck.factory(Speck.HEALING), 3);
				ch.sprite.showStatus(CharSprite.POSITIVE, "+%dHP", healing);

			//harms the undead
			}


		//if we find an item...
		} else if (heap != null && heap.type == Heap.Type.HEAP){
			Item item = heap.peek();

			//50% chance to uncurse the item and 20% chance to clear curse enchantments
			if (item != null && Random.Float() <= 0.5f){
				if (item.cursed){
					item.cursed = false;
					CellEmitter.get(cell).start( ShadowParticle.UP, 0.05f, 10 );
					Sample.INSTANCE.play(Assets.SND_BURNING);
				}
			}

			boolean procced = false;
			if(item != null && Random.Float() <= 0.2f && !item.cursed){

				if (item instanceof Weapon){
					Weapon w = (Weapon) item;
					if(w.hasCurseEnchant()){
						Iterator<Weapon.Enchantment> iterator = ((Weapon)item).enchantment.iterator();
						while (iterator.hasNext()){
							Weapon.Enchantment next = iterator.next();
							if(next.curse()){
								iterator.remove();
							}
						}
						procced = true;
					}
				}else if (item instanceof Armor){
					Armor a = (Armor) item;
					if (a.hasCurseGlyph()){
						Iterator<Armor.Glyph> iterator = ((Armor)item).glyph.iterator();
						while (iterator.hasNext()){
							Armor.Glyph next = iterator.next();
							if(next.curse()){
								iterator.remove();
							}
						}
						procced = true;
					}
				}
			}

			if(procced){
				CellEmitter.get(cell).burst(Speck.factory(Speck.CRIT), 12);
			}

		//if we find some trampled grass...
		} else if (Dungeon.level.map[cell] == Terrain.GRASS) {

			//regrow one grass tile, suuuuuper useful...
			Dungeon.level.set(cell, Terrain.HIGH_GRASS);
			GameScene.updateMap(cell);
			CellEmitter.get( cell ).burst(LeafParticle.LEVEL_SPECIFIC, 4);

		//If we find embers...
		} else if (Dungeon.level.map[cell] == Terrain.EMBERS) {

			//30% + 3%*lvl chance to grow a random plant, or just regrow grass.
			if (Random.Float() <= 0.3f+level()*0.03f) {
				Dungeon.level.plant((Plant.Seed) Generator.random(Generator.Category.SEED), cell);
				CellEmitter.get( cell ).burst(LeafParticle.LEVEL_SPECIFIC, 8);
				GameScene.updateMap(cell);
			} else{
				Dungeon.level.set(cell, Terrain.HIGH_GRASS);
				GameScene.updateMap(cell);
				CellEmitter.get( cell ).burst(LeafParticle.LEVEL_SPECIFIC, 4);
			}

		} else
			return; //don't damage the hero if we can't find a target;
	}

	@Override
	public int initialCharges() {
		return Random.Int(4,7);
	}

	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage, EffectType type) {
		if (attacker.isAlive()){
			if (attacker.properties().contains(Char.Property.UNDEAD) || attacker.properties().contains(Char.Property.DEMONIC)){

				int holyDamage = 3 + level() / 5;
				attacker.damage(holyDamage, this, new EffectType(type.attachType,0));
				attacker.sprite.emitter().start(ShadowParticle.UP, 0.05f, 20);
				Sample.INSTANCE.play(Assets.SND_BURNING);

			}else{

				int missingHP = attacker.HT - attacker.HP;
				int healing = Math.min(missingHP ,3 + level() / 5);
				attacker.HP += healing;
				attacker.sprite.emitter().burst(Speck.factory(Speck.HEALING), 3);
				attacker.sprite.showStatus(CharSprite.POSITIVE, "+%dHP", healing);
			}

		}
	}

	@Override
	protected void fx(Ballistica beam, Callback callback) {
		curUser.sprite.parent.add(
				new Beam.HealthRay(curUser.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(beam.collisionPos)));
		callback.call();
	}

	@Override
	public void staffFx(MagesStaff.StaffParticle particle) {
		particle.color( 0xCC3333 );
		particle.am = 0.6f;
		particle.setLifespan(1f);
		particle.speed.polar( Random.Float(PointF.PI2), 2f );
		particle.setSize( 1f, 2f);
		particle.radiateXY(0.5f);
	}

}
