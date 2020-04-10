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
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Barkskin;
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
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Invisibility;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.MagicalSleep;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Ooze;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Paralysis;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Poison;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Preparation;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Slow;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Speed;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Vertigo;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Weakness;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Belongings;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.HeroSubClass;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.DeathEye;
import com.fushiginopixel.fushiginopixeldungeon.effects.CellEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.KindOfWeapon;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.Armor;
import com.fushiginopixel.fushiginopixeldungeon.items.artifacts.CapeOfThorns;
import com.fushiginopixel.fushiginopixeldungeon.items.bombs.Bombs;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.RingOfElements;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.RingOfEvasion;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.RingOfFuror;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.RingOfHaste;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.RingOfMight;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.RingOfTenacity;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfSelfDestruct;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.WandOfFireblast;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.WandOfLightning;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.WandOfPrismaticLight;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.Weapon;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.enchantments.Blazing;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.enchantments.Grim;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.enchantments.Shocking;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.Flail;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.MissileWeapon;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.darts.ShockingDart;
import com.fushiginopixel.fushiginopixeldungeon.levels.Terrain;
import com.fushiginopixel.fushiginopixeldungeon.levels.features.Chasm;
import com.fushiginopixel.fushiginopixeldungeon.levels.features.Door;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.GrimTrap;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.plants.Earthroot;
import com.fushiginopixel.fushiginopixeldungeon.sprites.CharSprite;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
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

	public Belongings belongings;

	protected Char enemy;
	
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

	public Char() {
		super();
		belongings = new Belongings( this );
	}

	public void updateHT( boolean boostHP ){
		int curHT = HT;

		float multiplier = RingOfMight.HTMultiplier(this);
		HT = Math.round(multiplier * HT);

		if (boostHP){
			HP += Math.max(HT - curHT, 0);
		}
		HP = Math.min(HP, HT);
	}
	
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

		belongings.storeInBundle( bundle );
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
	//this variable is only needed because of the boomerang, remove if/when it is no longer equippable
	//fack you Evan!!!
	//protected boolean rangedAttack = false;

	public boolean shoot( Char enemy, MissileWeapon wep ) {

		//temporarily set the hero's weapon to the missile weapon being used
		//KindOfWeapon equipped = belongings.weapon;
		//belongings.weapon = wep;
		//rangedAttack = true;
		this.enemy = enemy;
		boolean result = attack( enemy ,new EffectType(EffectType.MISSILE ,0) ,wep , true);
		Invisibility.dispel();
		//belongings.weapon = equipped;
		//rangedAttack = false;

		return result;
	}

	//direct attack
	public boolean attack( Char enemy ) {
		return attack( enemy ,new EffectType(EffectType.MELEE ,0), belongings.weapon);
	}

	//attack with something, confirm final attach type. Using for ranged attack.
    public boolean attack( Char enemy ,EffectType type, KindOfWeapon item) {
	    return attack(enemy ,type , item, true);
    }

    //attack with something but cannot affect anything
	public boolean attack( Char enemy ,EffectType type ,Item item, boolean attackWithItem) {

		if (enemy == null || !enemy.isAlive()) return false;
		
		boolean visibleFight = Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[enemy.pos];
		KindOfWeapon weapon = null;
		if(attackWithItem && item != null && item instanceof KindOfWeapon){
            weapon = (KindOfWeapon) item;
        }
		if (!beforeAttack(weapon, enemy, type)) return false;
		
		if (hit( this, weapon, enemy, false )) {
			
			int dr = enemy.totalDR();

			/*
			if (this instanceof Hero){
				Hero h = (Hero)this;
				if (h.belongings.weapon instanceof MissileWeapon
						&& h.subClass == HeroSubClass.SNIPER){
					dr = 0;
				}
			}
			*/
			
			int dmg;
			Preparation prep = buff(Preparation.class);
			if (prep != null){
				dmg = prep.damageRoll(weapon, this, enemy);
			} else {
				dmg = totalDamageRoll(weapon);
			}
			
			int effectiveDamage = enemy.defenseProc( this, dmg, type );
			effectiveDamage = Math.max( effectiveDamage - dr, 0 );
			if(canCriticalAttack( weapon, enemy, effectiveDamage ,type)){
				if(enemy != null)
					enemy.sprite.centerEmitter().burst(Speck.factory(Speck.CRIT), 12);
				effectiveDamage *= criticalAttack();
			}
			effectiveDamage = attackProc( weapon, enemy, effectiveDamage ,type);
			
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
				onMissed(weapon, enemy);
				enemy.onDodgeed(this);
			}
			
			return false;
			
		}
	}

	public boolean beforeAttack(KindOfWeapon weapon, Char enemy, EffectType type){
		if(weapon != null) {
			return weapon.procBeforeAttack(this, enemy, true, type);
		}else return true;
	}

	public boolean canCriticalAttack(KindOfWeapon weapon, Char enemy, int damage, EffectType type){
	    /*
		if(belongings.weapon != null) {
			return belongings.weapon.canCriticalAttack(this, enemy, damage, type);
		}else return false;
		*/
        if(weapon != null) {
            return weapon.canCriticalAttack(this, enemy, damage, type);
        }else return false;
	}

	public float criticalAttack(){
		return 2;
	}

	/*
	Attacker acc = A
	Defender def = E
	Percent of hit C is
	if A > E, C = (A - E) / A + E / (2 * A)
	else C = A / (2 * E)
	*/
	public static boolean hit( Char attacker, KindOfWeapon weapon, Char defender, boolean magic ) {
		float acuRoll = Random.Float( attacker.totalAttackSkill( weapon, defender ) );
		float defRoll = Random.Float( defender.totalDefenseSkill( attacker ) );
		if (attacker.buff(Bless.class) != null) acuRoll *= 1.20f;
		if (defender.buff(Bless.class) != null) defRoll *= 1.20f;
		return (magic ? acuRoll * 2 : acuRoll) >= defRoll;
	}
	
	public int attackSkill( Char target ) {
		return 0;
	}

	public int totalAttackSkill( KindOfWeapon wep, Char target ) {
		int at = attackSkill(target);
		//KindOfWeapon wep = belongings.weapon;

		float accuracy = 1;
		/*
		if (wep instanceof MissileWeapon && rangedAttack
				&& Dungeon.level.distance( pos, target.pos ) == 1) {
			accuracy *= 0.5f;
		}
		*/

		if (wep != null) {
			return (int)(at * accuracy * wep.accuracyFactor( this ,target));
		} else {
			return (int)(at * accuracy);
		}
	}
	
	public int defenseSkill( Char enemy ) {
		return 0;
	}

    public int totalDefenseSkill( Char target ) {

        float evasion = defenseSkill(target);

        evasion *= RingOfEvasion.evasionMultiplier( this );

        if (belongings.armor != null) {
            evasion = belongings.armor.evasionFactor(this, target, evasion);
        }

        return Math.round(evasion);
    }

    public boolean canSurpriseAttack(){
        if (belongings.weapon instanceof Flail)                                     return false;

        return true;
    }

    public boolean canSurpriseAttack(Char target){
        boolean enemyInFOV = target != null && target.isAlive() && fieldOfView[target.pos];

        return !enemyInFOV && paralysed <= 0 && canSurpriseAttack();
    }

    public Char enemy(){
        return enemy;
    }
	
	public String defenseVerb() {
		return Messages.get(this, "def_verb");
	}
	
	public int drRoll() {
		return 0;
	}

	public int totalDR() {
		int dr = 0;
		Barkskin bark = buff(Barkskin.class);

		if (belongings.armor != null) {
			int armDr = Random.NormalIntRange( belongings.armor.min(), belongings.armor.max());
			if (armDr > 0) dr += armDr;
		}
		if (belongings.weapon != null)  {
			int wepDr = Random.NormalIntRange( 0 , belongings.weapon.defenseFactor( this ) );
			if (wepDr > 0) dr += wepDr;
		}
		dr += drRoll();
		if (bark != null)               dr += Random.NormalIntRange( 0 , bark.level() );

		return dr;
	}
	
	public int damageRoll() {
		return 0;
	}

	public int totalDamageRoll(KindOfWeapon weapon){
		//KindOfWeapon wep = belongings.weapon;
		int dmg = 0;
		if (weapon != null) {
			dmg += weapon.damageRoll( this );
		}
		dmg += damageRoll();
		if (dmg < 0) dmg = 0;

		return dmg;
	}
	
	public int attackProc( KindOfWeapon weapon, Char enemy, int damage, EffectType type ) {
		//KindOfWeapon wep = belongings.weapon;

		if (weapon != null) damage = weapon.procInAttack( this, enemy, damage ,type );
		return damage;
	}
	
	public int defenseProc( Char enemy, int damage, EffectType type  ) {
		if (belongings.armor != null) {
			damage = belongings.armor.procSufferAttack( enemy, this, damage, type );
		}

		Earthroot.Armor armor = buff( Earthroot.Armor.class );
		if (armor != null) {
			damage = armor.absorb( damage );
		}

		return damage;
	}
	
	public float speed() {

        float speed = buff( Cripple.class ) == null ? baseSpeed : baseSpeed * 0.5f;

        speed *= RingOfHaste.speedMultiplier(this);

        if (belongings.armor != null) {
            speed = belongings.armor.speedFactor(this, speed);
        }

        return speed;
	}

	public int damage( int dmg, Object src ) {
		return damage(dmg, src, new EffectType(0,0));
	}
	
	public int damage( int dmg, Object src ,EffectType type) {
		CapeOfThorns.Thorns thorns = buff( CapeOfThorns.Thorns.class );
		if (thorns != null) {
			dmg = thorns.proc(dmg, (src instanceof Char ? (Char)src : null),  this, type);
		}

		//dmg = (int)Math.ceil(dmg * RingOfTenacity.damageMultiplier( this ));

		if (belongings.armor != null && isAlive()) {
			dmg = belongings.armor.procBeforeDamage( src, this, dmg, type);
		}

		if (!isAlive() || dmg < 0) {
			return 0;
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
		float res = 1f;
		if (isImmune( srcClass,type )) {
			dmg = 0;
		} else {
            res = resist( srcClass,type );
			dmg = Math.round( dmg * res);
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
		
		sprite.showStatus( HP > HT / 2 || res > 1 ?
			CharSprite.WARNING :
			CharSprite.NEGATIVE,
			Integer.toString( dmg ) );

		if (HP < 0) HP = 0;

		if (belongings.armor != null && isAlive()) {
			belongings.armor.procAfterDamage( src, this, dmg, type );
		}

		if (!isAlive()) {
			die( src ,type );
		}

		return dmg;
	}

	public void onMissed(KindOfWeapon weapon, Char enemy) {
	    /*
		if (belongings.weapon != null)
			belongings.weapon.onMissed( this, enemy);
		return;
		*/
        if (weapon != null)
            weapon.onMissed( this, enemy);
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

	public void busy(){}
	public void spendAndNext( float time ) {
		busy();
		spend( time );
		next();
	}

	@Override
	public void spend( float time ) {
		
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

	public boolean catchItem(Item item, Char from) {
		return false;
	}

	public boolean canAttack(Char enemy){
		if (enemy == null || pos == enemy.pos)
			return false;

		//can always attack adjacent enemies
		if (Dungeon.level.adjacent(pos, enemy.pos) && (passWall || !Dungeon.level.solid[enemy.pos]))
			return true;

		KindOfWeapon wep = this.belongings.weapon;

		if (wep != null){
			return wep.canAttack(this, enemy);
		} else {
			return false;
		}
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

	public float attackDelay() {
		return 1f;
	}

	public float totalAttackDelay() {
		float dly = attackDelay();
		if (belongings.weapon != null) {

			return belongings.weapon.speedFactor( this );

		} else {
			//Normally putting furor speed on unarmed attacks would be unnecessary
			//But there's going to be that one guy who gets a furor+force ring combo
			//This is for that one guy, you shall get your fists of fury!
			return RingOfFuror.modifyAttackDelay(dly, this);
		}
	}
	
	public void onOperateComplete() {
		next();
	}
	
	protected final ArrayList<EffectResistance> resistances = new ArrayList<>();
	
	//returns percent effectiveness after resistances
	//TODO currently resistances reduce effectiveness by a static 50%, and do not stack.
	public float resist( Class srcClass,EffectType effect ){
		/*
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

		result *= RingOfElements.resist(this, srcClass) * RingOfTenacity.resist(this,srcClass);
		*/
		ArrayList<EffectResistance> resists = new ArrayList<>(resistances);
		effect.attachClass = srcClass;
		float result = 1f;

		for (Property p : properties()){
			resists.addAll(p.resistances());
		}

		for (Buff b : buffs()){
			resists.addAll(b.resistances());
		}

		for(EffectResistance ef : resists){
			if(EffectType.isExistType(effect, ef.effect))
				result *= ef.multiplier;
		}

		if(effect.isExistEffectType(EffectType.ELETRIC) && !flying && Dungeon.level.water[pos]){
		    result *= 2f;
        }
		result *= RingOfElements.resist(this, effect) * RingOfTenacity.resist(this,effect);

		return result;
	}
	
	//protected final HashSet<EffectType> immunities = new HashSet<>();
	
	public boolean isImmune(Class srcClass,EffectType effect ){

		/*
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
		return false;
		*/
		return resist(srcClass, effect) <= 0;
	}

	protected HashSet<Property> properties = new HashSet<>();

	public HashSet<Property> properties() {
		return new HashSet<>(properties);
	}

	public enum Property{
		BOSS ( new HashSet<>( Arrays.asList(
				new EffectResistance(new EffectType(Grim.class), 0.25f),
				new EffectResistance(new EffectType(ScrollOfSelfDestruct.class), 0.25f),
                new EffectResistance(new EffectType(Weakness.class), 0.5f),
				new EffectResistance(new EffectType(0,EffectType.ASPHYXIA), 0.25f),
				new EffectResistance(new EffectType(Corruption.class), 0)))),
		MINIBOSS ( new HashSet<>( Arrays.asList(
				new EffectResistance(new EffectType(Corruption.class), 0)))),
		UNDEAD (new HashSet<>(Arrays.asList(
				new EffectResistance(new EffectType(Grim.class), 0),
				new EffectResistance(new EffectType(EffectType.BEAM,0,DeathEye.class), 0),
				new EffectResistance(new EffectType(GrimTrap.class), 0),
				new EffectResistance(new EffectType(0,EffectType.ASPHYXIA), 0),
				new EffectResistance(new EffectType(0, EffectType.LIGHT), 1.333f)))),
		DEMONIC(new HashSet<>(Arrays.asList(
				new EffectResistance(new EffectType(0, EffectType.DARK), 0.5f),
				new EffectResistance(new EffectType(0, EffectType.LIGHT), 1.333f)))),
		INORGANIC ( new HashSet<>( Arrays.asList(
				new EffectResistance(new EffectType(Bleeding.class), 0),
				new EffectResistance(new EffectType(EffectType.GAS,0), 0),
				new EffectResistance(new EffectType(0,EffectType.POISON), 0)))),
		BLOB_IMMUNE ( new HashSet<>( Arrays.asList(
				new EffectResistance(new EffectType(EffectType.BLOB,0), 0)))),
		FIERY ( new HashSet<>( Arrays.asList(
				new EffectResistance(new EffectType(EffectType.FIRE,0), 0.5f),
				new EffectResistance(new EffectType(Burning.class), 0)))),
		ACIDIC ( new HashSet<>( Arrays.asList(
				new EffectResistance(new EffectType(0,EffectType.CORRROSION), 0)))),
		ELECTRIC ( new HashSet<>( Arrays.asList(
				new EffectResistance(new EffectType(0,EffectType.ELETRIC), 0)))),
		MACHANIC ( new HashSet<>(Arrays.asList(
				new EffectResistance(new EffectType(0,EffectType.SPIRIT), 0)))),
		ANGEL ( new HashSet<>( Arrays.asList(
				new EffectResistance(new EffectType(0,EffectType.LIGHT), 0.5f)))),
		IMMOVABLE;

		private HashSet<EffectResistance> resistances;
		//private HashSet<EffectType> immunities;
		
		Property(){
			this(new HashSet<EffectResistance>());
		}
		
		Property( HashSet<EffectResistance> resistances){
			this.resistances = resistances;
			//this.immunities = immunities;
		}
		
		public HashSet<EffectResistance> resistances(){
			return new HashSet<>(resistances);
		}
		/*
		public HashSet<EffectType> immunities(){
			return new HashSet<>(immunities);
		}
		*/
	}
}
