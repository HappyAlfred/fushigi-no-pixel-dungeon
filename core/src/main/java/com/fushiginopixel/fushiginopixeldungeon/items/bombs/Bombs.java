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

package com.fushiginopixel.fushiginopixeldungeon.items.bombs;

import com.fushiginopixel.fushiginopixeldungeon.Assets;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Vertigo;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.effects.CellEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.BlastParticle;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.SmokeParticle;
import com.fushiginopixel.fushiginopixeldungeon.items.Heap;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.levels.RegularLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.rooms.Room;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.CharSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSpriteSheet;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Bombs extends Item {

	{
		usesTargeting = true;
		defaultAction = AC_LIGHTTHROW;

		stackable = true;
	}

	public Fuse fuse;

	public Char enemyThrow;

	//FIXME using a static variable for this is kinda gross, should be a better way
	protected static boolean lightingFuse = false;

	protected static final String AC_LIGHTTHROW = "LIGHTTHROW";

	protected boolean canLightingFuse = true;

	protected float delay = 2f;

	protected int tier = 0;

	private static final String FUSE = "fuse";
	private static final String ENEMYTHROW = "enemythrow";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put( FUSE, fuse );
		bundle.put( ENEMYTHROW, enemyThrow == null ? -1 :enemyThrow.id() );
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		if (bundle.contains( FUSE ))
			Actor.add( fuse = ((Fuse)bundle.get(FUSE)).ignite(this) );

		if (bundle.contains( ENEMYTHROW ))
			enemyThrow = Actor.findChar(bundle.getInt(ENEMYTHROW));
	}

	@Override
	public boolean isSimilar(Item item) {
		return super.isSimilar(item) && this.fuse == ((Bombs) item).fuse;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions( hero );
		if(canLightingFuse)
		actions.add ( AC_LIGHTTHROW );
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {

		if (action.equals(AC_LIGHTTHROW)) {
			lightingFuse = true;
			action = AC_THROW;
		} else
			lightingFuse = false;

		super.execute(hero, action);
	}

	@Override
	protected void onThrow( int cell ) {
		if(delay > 0 && lightingFuse) {
			if (!Dungeon.level.pit[cell] && lightingFuse) {
				Actor.addDelayed(fuse = new Fuse().ignite(this), delay);
			}
			if (Actor.findChar(cell) != null && !(Actor.findChar(cell) instanceof Hero)) {
				ArrayList<Integer> candidates = new ArrayList<>();
				for (int i : PathFinder.NEIGHBOURS8)
					if (Dungeon.level.passable[cell + i])
						candidates.add(cell + i);
				int newCell = candidates.isEmpty() ? cell : Random.element(candidates);
				Dungeon.level.drop(this, newCell).sprite.drop(cell);
			} else
				super.onThrow(cell);
		}else if(delay <=0 && lightingFuse){
			explode(cell);
		}else{
			super.onThrow(cell);
		}
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public boolean doPickUp(Hero hero) {
		if(Dungeon.level.mobs.contains(enemyThrow) && enemyThrow!= null && Dungeon.level.heroFOV[enemyThrow.pos]) {
			return false;

		}else{
			if (fuse != null) {
				GLog.w(Messages.get(Bombs.class, "snuff_fuse"));
				fuse = null;
			}
			return super.doPickUp(hero);
		}
	}

	public void lightThrow(int cell ,float delay){
		lightingFuse = true;
		this.delay = delay;
		onThrow(cell);
	}

	public void lightThrow(int cell){
		lightThrow(cell,delay);
	}

	public void explode(int cell){
		//We're blowing up, so no need for a fuse anymore.
		this.fuse = null;

		Sample.INSTANCE.play( Assets.SND_BLAST );

		if (Dungeon.level.heroFOV[cell]) {
			CellEmitter.center( cell ).burst( BlastParticle.FACTORY, 30 );
		}

		boolean terrainAffected = false;

		for (int n : PathFinder.NEIGHBOURS9) {
			int c = cell + n;
			if (c >= 0 && c < Dungeon.level.length()) {
				if (Dungeon.level.heroFOV[c]) {
					CellEmitter.get( c ).burst( SmokeParticle.FACTORY, 4 );
				}

				terrainAffected = blowUp(c , tier);

				Char ch = Actor.findChar( c );
				if (ch != null) {
					//those not at the center of the blast take damage less consistently.

					int dmg = damageRoll(min(),max(),c==cell);
					dmg -= ch.drRoll();
					int effectiveDamage = proc(ch , dmg);
					if (effectiveDamage > 0) {
						ch.damage( effectiveDamage, this ,new EffectType(EffectType.BURST,0));
					}

					if (ch == Dungeon.hero && !ch.isAlive())
						Dungeon.fail( getClass() );
				}
			}
		}

		if (terrainAffected) {
			Dungeon.observe();
		}
	}

	public static boolean blowUp(int c, int tier){
		boolean terrainAffected = false;
		//destroys items / triggers bombs caught in the blast.
		if(tier >= 1){
			Heap heap = Dungeon.level.heaps.get( c );
			if(heap != null)
				heap.explode();

			if(Dungeon.level.flamable[c]){
				Dungeon.level.destroy( c );
				terrainAffected = true;
			}
		}
		if(tier >= 2 && Dungeon.level.destructable[c] && !Dungeon.canNotBlowUpLevel() && !terrainAffected) {
			Dungeon.level.destroy( c );
			terrainAffected = true;
		}
		if(terrainAffected) {
			Dungeon.level.terrainAdjustComplete();
			GameScene.updateMap(c);
		}
		return terrainAffected;
	}

	public int proc(Char enemy , int damage){
		return damage;
	}

	public int min(){
		return 0;
	}

	public int max(){
		return 0;
	}

	public int damageRoll(int minDamage , int maxDamage, Boolean center){
		return 0;
	}

	@Override
	public String desc() {
		String info = Messages.get(this, "desc");
		if(delay > 0) {
			if (fuse == null)
				info += "\n\n" + Messages.get(Bombs.class, "normal");
			else
				info += "\n\n" + Messages.get(Bombs.class, "burning");
		}

		switch (tier){
			case (0) : info += "\n\n" + Messages.get(Bombs.class, "none");break;
			case (1) : info += "\n\n" + Messages.get(Bombs.class, "weak_explode");break;
			case (2) : info += "\n\n" + Messages.get(Bombs.class, "strong_explode");break;
		}

		return info;
	}

	public static class Fuse extends Actor{

		{
			actPriority = BUFF_PRIO; //as if it were a buff
		}

		private Bombs bomb;

		public Fuse ignite(Bombs bomb){
			this.bomb = bomb;
			return this;
		}

		@Override
		protected boolean act() {

			//something caused our bomb to explode early, or be defused. Do nothing.
			if (bomb.fuse != this){
				Actor.remove( this );
				return true;
			}

			//look for our bomb, remove it from its heap, and blow it up.
			for (Heap heap : Dungeon.level.heaps.values()) {
				if (heap.items.contains(bomb)) {
					heap.items.remove(bomb);

					bomb.explode(heap.pos);

					Actor.remove(this);
					return true;
				}
			}

			//can't find our bomb, something must have removed it, do nothing.
			bomb.fuse = null;
			Actor.remove( this );
			return true;
		}
	}
}
