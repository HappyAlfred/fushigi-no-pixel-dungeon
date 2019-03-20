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

package com.fushiginopixel.fushiginopixeldungeon.actors;

import com.fushiginopixel.fushiginopixeldungeon.Assets;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.Blob;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.Electricity;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.TearGas;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.ToxicGas;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Bleeding;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Bless;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Blindness;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Burning;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Charm;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Chill;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Corrosion;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Corruption;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Cripple;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Doom;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.EarthImbue;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.FireImbue;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Frost;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Hunger;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.MagicalSleep;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Ooze;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Paralysis;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Poison;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Preparation;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Slow;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Speed;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Vertigo;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.HeroSubClass;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.DeathEye;
import com.fushiginopixel.fushiginopixeldungeon.effects.CellEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.items.bombs.Bombs;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.RingOfElements;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.RingOfTenacity;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfSelfDestruct;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.WandOfFireblast;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.WandOfLightning;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.WandOfPrismaticLight;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.enchantments.Blazing;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.enchantments.Grim;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.enchantments.Shocking;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.MissileWeapon;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.darts.ShockingDart;
import com.fushiginopixel.fushiginopixeldungeon.levels.Terrain;
import com.fushiginopixel.fushiginopixeldungeon.levels.features.Chasm;
import com.fushiginopixel.fushiginopixeldungeon.levels.features.Door;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.GrimTrap;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.sprites.CharSprite;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.Arrays;
import java.util.HashSet;

public abstract class Char extends Actor {
	
	public int pos = 0;
	
	public CharSprite sprite;
	
	public String name = "mob";
	
	public int HT;
	public int HP;
	public int SHLD;
	
	protected float baseSpeed	= 1;
	protected PathFinder.Path path;

	public int paralysed	    = 0;
	public boolean rooted		= false;
	public boolean flying		= false;
	public boolean passWall		= false;
	public int invisible		= 0;
	
	//these are relative to the hero
	public enum Alignment{
		ENEMY,
		NEUTRAL,
		ALLY
	}
	public Alignment alignment;
	
	public int viewDistance	= 8;
	
	protected boolean[] fieldOfView = null;
	
	private HashSet<Buff> buffs = new HashSet<>();
	
	@Override
	protected boolean act() {
		if (fieldOfView == null || fieldOfView.length != Dungeon.level.length()){
			fieldOfView = new boolean[Dungeon.level.length()];
		}
		Dungeon.level.updateFieldOfView( this, fieldOfView );
		return false;
	}

	public boolean act(Char c) {
		if (fieldOfView == null || fieldOfView.length != Dungeon.level.length()){
			fieldOfView = new boolean[Dungeon.level.length()];
		}
		Dungeon.level.updateFieldOfView( this, fieldOfView );
		return false;
	}
	
	protected static final String POS       = "pos";
	protected static final String TAG_HP    = "HP";
	protected static final String TAG_HT    = "HT";
	protected static final String TAG_SHLD  = "SHLD";
	protected static final String BUFFS	    = "buffs";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		
		super.storeInBundle( bundle );
		
		bundle.put( POS, pos );
		bundle.put( TAG_HP, HP );
		bundle.put( TAG_HT, HT );
		bundle.put( TAG_SHLD, SHLD );
		bundle.put( BUFFS, buffs );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		
		super.restoreFromBundle( bundle );
		
		pos = bundle.getInt( POS );
		HP = bundle.getInt( TAG_HP );
		HT = bundle.getInt( TAG_HT );
		SHLD = bundle.getInt( TAG_SHLD );
		
		for (Bundlable b : bundle.getCollection( BUFFS )) {
			if (b != null) {
				((Buff)b).attachTo( this );
			}
		}
	}

	public boolean attack( Char enemy ) {
		return attack( enemy ,new EffectType(EffectType.MELEE ,0));
	}
	
	public boolean attack( Char enemy ,EffectType type) {

		if (enemy == null || !enemy.isAlive()) return false;
		
		boolean visibleFight = Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[enemy.pos];
		
		if (hit( this, enemy, false )) {
			
			int dr = enemy.drRoll();
			
			if (this instanceof Hero){
				Hero h = (Hero)this;
				if (h.belongings.weapon instanceof MissileWeapon
						&& h.subClass == HeroSubClass.SNIPER){
					dr = 0;
				}
			}
			
			int dmg;
			Preparation prep = buff(Preparation.class);
			if (prep != null){
				dmg = prep.damageRoll(this, enemy);
			} else {
				dmg = damageRoll();
			}
			
			int effectiveDamage = enemy.defenseProc( this, dmg, type );
			effectiveDamage = Math.max( effectiveDamage - dr, 0 );
			if(canCriticalAttack( enemy, effectiveDamage ,type)){
				if(enemy != null)
					enemy.sprite.emitter().burst(Speck.factory(Speck.CRIT), 12);
				effectiveDamage *= criticalAttack();
			}
			effectiveDamage = attackProc( enemy, effectiveDamage ,type);
			
			if (visibleFight) {
				Sample.INSTANCE.play( Assets.SND_HIT, 1, 1, Random.Float( 0.8f, 1.25f ) );
			}

			// If the enemy is already dead, interrupt the attack.
			// This matters as defence procs can sometimes inflict self-damage, such as armor glyphs.
			if (!enemy.isAlive()){
				return true;
			}

			//TODO: consider revisiting this and shaking in more cases.
			float shake = 0f;
			if (enemy == Dungeon.hero)
				shake = effectiveDamage / (enemy.HT / 4);

			if (shake > 1f)
				Camera.main.shake( GameMath.gate( 1, shake, 5), 0.3f );

			enemy.damage( effectiveDamage, this ,type);

			if (buff(FireImbue.class) != null)
				buff(FireImbue.class).proc(enemy);
			if (buff(EarthImbue.class) != null)
				buff(EarthImbue.class).proc(enemy);

			enemy.sprite.bloodBurstA( sprite.center(), effectiveDamage );
			enemy.sprite.flash();

			if (!enemy.isAlive() && visibleFight) {
				if (enemy == Dungeon.hero) {

					Dungeon.fail( getClass() );
					GLog.n( Messages.capitalize(Messages.get(Char.class, "kill", name)) );
					
				} else if (this == Dungeon.hero) {
					GLog.i( Messages.capitalize(Messages.get(Char.class, "defeat", enemy.name)) );
				}
			}
			
			return true;
			
		} else {
			
			if (visibleFight) {
				String defense = enemy.defenseVerb();
				enemy.sprite.showStatus( CharSprite.NEUTRAL, defense );
				
				Sample.INSTANCE.play(Assets.SND_MISS);
				onMissed(enemy);
				enemy.onDodgeed(this);
			}
			
			return false;
			
		}
	}

	public boolean canCriticalAttack( Char enemy, int damage, EffectType type){
		return false;
	}

	public float criticalAttack(){
		return 2;
	}
	
	public static boolean hit( Char attacker, Char defender, boolean magic ) {
		float acuRoll = Random.Float( attacker.attackSkill( defender ) );
		float defRoll = Random.Float( defender.defenseSkill( attacker ) );
		if (attacker.buff(Bless.class) != null) acuRoll *= 1.20f;
		if (defender.buff(Bless.class) != null) defRoll *= 1.20f;
		return (magic ? acuRoll * 2 : acuRoll) >= defRoll;
	}
	
	public int attackSkill( Char target ) {
		return 0;
	}
	
	public int defenseSkill( Char enemy ) {
		return 0;
	}
	
	public String defenseVerb() {
		return Messages.get(this, "def_verb");
	}
	
	public int drRoll() {
		return 0;
	}
	
	public int damageRoll() {
		return 1;
	}
	
	public int attackProc( Char enemy, int damage, EffectType type ) {
		return damage;
	}
	
	public int defenseProc( Char enemy, int damage, EffectType type  ) {
		return damage;
	}
	
	public float speed() {
		return buff( Cripple.class ) == null ? baseSpeed : baseSpeed * 0.5f;
	}

	public void damage( int dmg, Object src ) {
		damage(dmg, src, new EffectType(0,0));
	}
	
	public void damage( int dmg, Object src ,EffectType type) {
		
		if (!isAlive() || dmg < 0) {
			return;
		}
		if (this.buff(Frost.class) != null){
			Buff.detach( this, Frost.class );
		}
		if (this.buff(MagicalSleep.class) != null){
			Buff.detach(this, MagicalSleep.class);
		}
		if (this.buff(Doom.class) != null){
			dmg *= 2;
		}
		
		Class<?> srcClass = src.getClass();
		if (isImmune( srcClass,type )) {
			dmg = 0;
		} else {
			dmg = Math.round( dmg * resist( srcClass,type ));
		}
		
		if (buff( Paralysis.class ) != null) {
			buff( Paralysis.class ).processDamage(dmg);
		}

		//FIXME: when I add proper damage properties, should add an IGNORES_SHIELDS property to use here.
		if (src instanceof Hunger || SHLD == 0){
			HP -= dmg;
		} else if (SHLD >= dmg){
			SHLD -= dmg;
		} else if (SHLD > 0) {
			HP -= (dmg - SHLD);
			SHLD = 0;
		}
		
		sprite.showStatus( HP > HT / 2 ?
			CharSprite.WARNING :
			CharSprite.NEGATIVE,
			Integer.toString( dmg ) );

		if (HP < 0) HP = 0;

		if (!isAlive()) {
			die( src ,type );
		}
	}

	public void onMissed(Char enemy) {
		return;
	}

	public void onDodgeed(Char enemy) {
		return;
	}
	
	public void destroy() {
		destroy(null, new EffectType());
	}

	public void destroy(Object src, EffectType type ) {
		HP = 0;
		Actor.remove( this );
	}
	
	public void die( Object src ) {
		die(src , new EffectType(0,0));
	}

	public void die( Object src,EffectType type ) {
		destroy(src, new EffectType(type.attachType,type.effectType));
		if (src != Chasm.class) sprite.die();
	}
	
	public boolean isAlive() {
		return HP > 0;
	}
	
	@Override
	protected void spend( float time ) {
		
		float timeScale = 1f;
		if (buff( Slow.class ) != null) {
			timeScale *= 0.5f;
			//slowed and chilled do not stack
		} else if (buff( Chill.class ) != null) {
			timeScale *= buff( Chill.class ).speedFactor();
		}
		if (buff( Speed.class ) != null) {
			timeScale *= 2.0f;
		}
		
		super.spend( time / timeScale );
	}
	
	public synchronized HashSet<Buff> buffs() {
		return new HashSet<>(buffs);
	}
	
	@SuppressWarnings("unchecked")
	public synchronized <T extends Buff> HashSet<T> buffs( Class<T> c ) {
		HashSet<T> filtered = new HashSet<>();
		for (Buff b : buffs) {
			if (c.isInstance( b )) {
				filtered.add( (T)b );
			}
		}
		return filtered;
	}

	@SuppressWarnings("unchecked")
	public synchronized  <T extends Buff> T buff( Class<T> c ) {
		for (Buff b : buffs) {
			if (c.isInstance( b )) {
				return (T)b;
			}
		}
		return null;
	}

	public synchronized boolean isCharmedBy( Char ch ) {
		int chID = ch.id();
		for (Buff b : buffs) {
			if (b instanceof Charm && ((Charm)b).object == chID) {
				return true;
			}
		}
		return false;
	}

	public synchronized void add( Buff buff ) {
		
		buffs.add( buff );
		Actor.add( buff );

		if (sprite != null)
			switch(buff.type){
				case POSITIVE:
					sprite.showStatus(CharSprite.POSITIVE, buff.toString()); break;
				case NEGATIVE:
					sprite.showStatus(CharSprite.NEGATIVE, buff.toString());break;
				case NEUTRAL:
					sprite.showStatus(CharSprite.NEUTRAL, buff.toString()); break;
				case SILENT: default:
					break; //show nothing
			}

	}
	
	public synchronized void remove( Buff buff ) {
		
		buffs.remove( buff );
		Actor.remove( buff );

	}
	
	public synchronized void remove( Class<? extends Buff> buffClass ) {
		for (Buff buff : buffs( buffClass )) {
			remove( buff );
		}
	}
	
	@Override
	protected synchronized void onRemove() {
		for (Buff buff : buffs.toArray(new Buff[buffs.size()])) {
			buff.detach();
		}
	}
	
	public synchronized void updateSpriteState() {
		for (Buff buff:buffs) {
			buff.fx( true );
		}
	}
	
	public int stealth() {
		return 0;
	}
	
	public void move( int step ) {

		if (Dungeon.level.adjacent( step, pos ) && buff( Vertigo.class ) != null && this instanceof Hero) {
			sprite.interruptMotion();
			int newPos = pos + PathFinder.NEIGHBOURS8[Random.Int( 8 )];
			if (!(Dungeon.level.passable[newPos] || Dungeon.level.avoid[newPos] || (Dungeon.level.solid[newPos] && passWall)) || Actor.findChar( newPos ) != null)
				return;
			else {
				sprite.move(pos, newPos);
				step = newPos;
			}
		}
		//if (!(Dungeon.level.passable[step] || Dungeon.level.avoid[step] || (Dungeon.level.solid[step] && passWall))/* || Actor.findChar( step ) != null*/)
		//	return;

		if (Dungeon.level.map[pos] == Terrain.OPEN_DOOR) {
			Door.leave( pos );
		}

		pos = step;
		
		if (flying && !passWall && Dungeon.level.map[pos] == Terrain.DOOR) {
			Door.enter( pos );
		}
		
		if (this != Dungeon.hero) {
			sprite.visible = Dungeon.level.heroFOV[pos];
		}
		
		if (!flying) {
			Dungeon.level.press( pos, this );
		}
	}
	
	public int distance( Char other ) {
		return Dungeon.level.distance( pos, other.pos );
	}
	
	public void onMotionComplete() {
		//Does nothing by default
		//The main actor thread already accounts for motion,
		// so calling next() here isn't necessary (see Actor.process)
	}
	
	public void onAttackComplete() {
		next();
	}
	
	public void onOperateComplete() {
		next();
	}
	
	protected final HashSet<EffectType> resistances = new HashSet<>();
	
	//returns percent effectiveness after resistances
	//TODO currently resistances reduce effectiveness by a static 50%, and do not stack.
	public float resist( Class srcClass,EffectType effect ){
		HashSet<EffectType> resists = new HashSet<>(resistances);

		effect.attachClass = srcClass;
		for (Property p : properties()){
			resists.addAll(p.resistances());
		}
		for (Buff b : buffs()){
			resists.addAll(b.resistances());
		}
		
		float result = 1f;

		if(EffectType.isExistType(effect,resists)){
			result *= 0.5f;
		}
		/*
		for (Class c : resists){
			if (c.isAssignableFrom(effect)){
				result *= 0.5f;
			}
		}*/

		result *= RingOfElements.resist(this, srcClass) * RingOfTenacity.resist(this,srcClass);

		return result;
	}
	
	protected final HashSet<EffectType> immunities = new HashSet<>();
	
	public boolean isImmune(Class srcClass,EffectType effect ){

		effect.attachClass = srcClass;
		HashSet<EffectType> immunes = new HashSet<>(immunities);
		for (Property p : properties()){
			immunes.addAll(p.immunities());
		}
		for (Buff b : buffs()){
			immunes.addAll(b.immunities());
		}
		if(EffectType.isExistType(effect,immunes)){
			return true;
		}
		/*for (Class c : immunes){
			if (c.isAssignableFrom(effect)){
				return true;
			}
		}*/
		return false;
	}

	protected HashSet<Property> properties = new HashSet<>();

	public HashSet<Property> properties() {
		return new HashSet<>(properties);
	}

	public enum Property{
		BOSS ( new HashSet<EffectType>( Arrays.asList(new EffectType(Grim.class),new EffectType(ScrollOfSelfDestruct.class))),
				new HashSet<EffectType>( Arrays.asList(new EffectType(Corruption.class) ))),
		MINIBOSS ( new HashSet<EffectType>(),
				new HashSet<EffectType>( Arrays.asList(new EffectType(Corruption.class) ))),
		UNDEAD (new HashSet<EffectType>(), new HashSet<EffectType>(Arrays.asList(new EffectType(Grim.class),new EffectType(EffectType.BEAM,0,DeathEye.class),new EffectType(GrimTrap.class)))),
		DEMONIC,
		INORGANIC ( new HashSet<EffectType>(),
				new HashSet<EffectType>( Arrays.asList(new EffectType(Bleeding.class), new EffectType(EffectType.GAS,0), new EffectType(0,EffectType.POISON)) )),
		BLOB_IMMUNE ( new HashSet<EffectType>(),
				new HashSet<EffectType>( Arrays.asList(new EffectType(EffectType.BLOB,0) ))),
		FIERY ( new HashSet<EffectType>( Arrays.asList(new EffectType(EffectType.FIRE,0))),
				new HashSet<EffectType>( Arrays.asList(new EffectType(Burning.class)))),
		ACIDIC ( new HashSet<EffectType>(),
				new HashSet<EffectType>( Arrays.asList(new EffectType(0,EffectType.CORRROSION)))),
		ELECTRIC ( new HashSet<EffectType>( Arrays.asList(new EffectType(0,EffectType.ELETRIC))),
				new HashSet<EffectType>()),
		MACHANIC ( new HashSet<EffectType>(Arrays.asList(new EffectType(EffectType.BURST,0))),
				new HashSet<EffectType>(Arrays.asList(new EffectType(0,EffectType.SPIRIT)))),
		ANGEL ( new HashSet<EffectType>( Arrays.asList(new EffectType(0,EffectType.LIGHT))),
				new HashSet<EffectType>(Arrays.asList(new EffectType(EffectType.BEAM,0)))),
		IMMOVABLE;

		private HashSet<EffectType> resistances;
		private HashSet<EffectType> immunities;
		
		Property(){
			this(new HashSet<EffectType>(), new HashSet<EffectType>());
		}
		
		Property( HashSet<EffectType> resistances, HashSet<EffectType> immunities){
			this.resistances = resistances;
			this.immunities = immunities;
		}
		
		public HashSet<EffectType> resistances(){
			return new HashSet<>(resistances);
		}
		
		public HashSet<EffectType> immunities(){
			return new HashSet<>(immunities);
		}
	}
}
