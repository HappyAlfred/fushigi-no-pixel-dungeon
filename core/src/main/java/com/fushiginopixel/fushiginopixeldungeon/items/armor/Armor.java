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

package com.fushiginopixel.fushiginopixeldungeon.items.armor;

import com.fushiginopixel.fushiginopixeldungeon.Badges;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.Fushiginopixeldungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Momentum;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.items.BrokenSeal;
import com.fushiginopixel.fushiginopixeldungeon.items.EquipableItem;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.curses.AntiEntropy;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.curses.Bulk;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.curses.Corrosion;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.curses.Displacement;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.curses.Metabolism;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.curses.Multiplicity;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.curses.Overgrowth;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.curses.Stench;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.glyphs.Affection;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.glyphs.AntiMagic;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.glyphs.Balance;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.glyphs.Brimstone;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.glyphs.Camouflage;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.glyphs.Entanglement;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.glyphs.Obfuscation;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.glyphs.Potential;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.glyphs.Repulsion;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.glyphs.Stone;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.glyphs.Swiftness;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.glyphs.Thorns;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.glyphs.Viscosity;
import com.fushiginopixel.fushiginopixeldungeon.levels.Terrain;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.HeroSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSpriteSheet;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndEnchantmentsTab;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.watabou.utils.SparseArray;

import java.util.ArrayList;
import java.util.Collection;

public class Armor extends EquipableItem {

	private static final int HITS_TO_KNOW    = 5;

	protected static final int UPGRADE_DEFENSE    = 4;

	protected static final String AC_DETACH       = "DETACH";
	protected static final String AC_GLYPHLIST       = "GLYPHLIST";
	protected static final String AC_PROPERTYLIST       = "PROPERTYLIST";
	public int tier;
	public int 	LIMIT = 2;//tier1:3,tier2/3:2,tier3/4 :1
	//FUCK THIS!!!!
	/*public enum Augment {
		EVASION (1.5f , -1f),
		DEFENSE (-1.5f, 1f),
		NONE	(0f   ,  0f);
		
		private float evasionFactor;
		private float defenceFactor;
		
		Augment(float eva, float df){
			evasionFactor = eva;
			defenceFactor = df;
		}

		//TODO balance on this seems good, but needs testing.
		public int evasionFactor(int level){
			return Math.round((2 + level) * evasionFactor);
		}
		
		public int defenseFactor(int level){
			return Math.round((2 + level) * defenceFactor);
		}
	}
	
	public Augment augment = Augment.NONE;*/
	public ArrayList<Glyph> glyph = new ArrayList<>();
	public ArrayList<Glyph>  properties = new ArrayList<>();

	private BrokenSeal seal;

	
	private int hitsToKnow = HITS_TO_KNOW;
	
	/*public Armor( int tier ) {
		this.tier = tier;
	}*/

	private static final String UNFAMILIRIARITY	= "unfamiliarity";
	private static final String GLYPH			= "glyph";
	private static final String SEAL            = "seal";
	//private static final String AUGMENT			= "augment";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( UNFAMILIRIARITY, hitsToKnow );
        SparseArray list = new SparseArray();
        for(Glyph gl :glyph){
			list.put(glyph.indexOf(gl),gl);

		}
		bundle.put( GLYPH, list.values() );
		bundle.put( SEAL, seal);
		//bundle.put( AUGMENT, augment);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		hitsToKnow = bundle.getInt( UNFAMILIRIARITY );
		Collection<Bundlable> collection = bundle.getCollection( GLYPH );
		for (Bundlable g : collection) {
			Glyph gl = (Glyph)g;
			glyph.add(gl);
		}
		seal = (BrokenSeal)bundle.get(SEAL);
		//pre-0.6.5 saves
		//if (bundle.contains(AUGMENT)) augment = bundle.getEnum(AUGMENT, Augment.class);
	}

	@Override
	public void reset() {
		super.reset();
		//armor can be kept in bones between runs, the seal cannot.
		seal = null;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		if (seal != null) actions.add(AC_DETACH);
		if (cursedKnown && glyphCount() != 0) actions.add(AC_GLYPHLIST);
		if (properties.size() > 0) actions.add(AC_PROPERTYLIST);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {

		super.execute(hero, action);

		if (action.equals(AC_DETACH) && seal != null){
			BrokenSeal.WarriorShield sealBuff = hero.buff(BrokenSeal.WarriorShield.class);
			if (sealBuff != null) sealBuff.setArmor(null);

			if (seal.level() > 0){
				degrade();
			}
			GLog.i( Messages.get(Armor.class, "detach_seal") );
			hero.sprite.operate(hero.pos);
			if (!seal.collect()){
				Dungeon.level.drop(seal, hero.pos);
			}
			seal = null;
		}
		else if (action.equals(AC_GLYPHLIST) && glyphCount() != 0){
			GameScene.show(new WndEnchantmentsTab(this , null , WndEnchantmentsTab.Mode.ENCHANTMENT , Messages.get(WndEnchantmentsTab.class, "glyph")));
		}else if(properties.size() > 0 && action.equals(AC_PROPERTYLIST)){
			GameScene.show(new WndEnchantmentsTab(this , null, WndEnchantmentsTab.Mode.PROPERTY, Messages.get(Item.class, "properties")));
		}
	}

	@Override
	public boolean doEquip( Hero hero ) {
		
		detach(hero.belongings.backpack);

		if (hero.belongings.armor == null || hero.belongings.armor.doUnequip( hero, true, false )) {
			
			hero.belongings.armor = this;
			
			cursedKnown = true;
			if (cursed) {
				equipCursed( hero );
				GLog.n( Messages.get(Armor.class, "equip_cursed") );
			}
			
			((HeroSprite)hero.sprite).updateArmor();
			activate(hero);

			hero.spendAndNext( time2equip( hero ) );
			return true;
			
		} else {
			
			collect( hero.belongings.backpack );
			return false;
			
		}
	}

	@Override
	public void activate(Char ch) {
		if (seal != null) Buff.affect(ch, BrokenSeal.WarriorShield.class).setArmor(this);
	}

	public void affixSeal(BrokenSeal seal){
		this.seal = seal;
		if (seal.level() > 0){
			//doesn't trigger upgrading logic such as affecting curses/glyphs
			level(level()+1);
			Badges.validateItemLevelAquired(this);
		}
		if (isEquipped(Dungeon.hero)){
			Buff.affect(Dungeon.hero, BrokenSeal.WarriorShield.class).setArmor(this);
		}
	}

	public BrokenSeal checkSeal(){
		return seal;
	}

	@Override
	public boolean isUnique(){
		if(checkSeal() != null){
			return true;
		}
		return super.isUnique();
	}

	@Override
	protected float time2equip( Hero hero ) {
		return 2 / hero.speed();
	}

	@Override
	public boolean doUnequip( Hero hero, boolean collect, boolean single ) {
		if (super.doUnequip( hero, collect, single )) {

			hero.belongings.armor = null;
			((HeroSprite)hero.sprite).updateArmor();

			BrokenSeal.WarriorShield sealBuff = hero.buff(BrokenSeal.WarriorShield.class);
			if (sealBuff != null) sealBuff.setArmor(null);

			return true;

		} else {

			return false;

		}
	}
	
	@Override
	public boolean isEquipped( Hero hero ) {
		return hero.belongings.armor == this;
	}

	public int max(){
		return max(level());
	}

	public int max(int lvl){
		int max = DRMax(lvl);
		if (hasGlyph(Balance.class))
			max = (max + DRMin(lvl))/2;
		return max;
	}

	public int min(){
		return min(level());
	}

	public int min(int lvl){
		int max = max();
		int min = DRMin(lvl);
		if (hasGlyph(Balance.class))
			min = max;
		return min;
	}

	public int DRMax(int lvl){
		int max = tier * (2 + lvl);// + augment.defenseFactor(lvl);
		//if (hasGlyph(Balance.class)) max = (max + lvl)/2;
		/*if (lvl > max){
			return ((lvl - max)+1)/2;
		} else {
			return max;
		}*/
		return max;
	}

	public int DRMin(int lvl){
		//int max = DRMax(lvl);
		int min = lvl;
		//if (hasGlyph(Balance.class)) min = max;
		/*if (min >= max && !hasGlyph(Balance.class)){
			return (min - max);
		} else {
			return min;
		}*/
		return min;
	}
	
	public float evasionFactor( Char owner, Char enemy, float evasion ){
		/*
		if (hasGlyph(Stone.class) && !((Stone)getGlyph(Stone.class)).testingEvasion()){
			return 0;
		}
		*/
		if (owner instanceof Hero){
			int aEnc = - ((Hero) owner).STR();//STRReq() - ((Hero) owner).STR();
			//if (aEnc > 0) evasion /= Math.pow(1.5, aEnc);
			
			Momentum momentum = owner.buff(Momentum.class);
			if (momentum != null){
				evasion += momentum.evasionBonus(Math.max(0, -aEnc));
			}
		}

        float adapt = 1f;

        for(Glyph e : properties){
            adapt *= e.evasionFactor(this ,owner ,enemy, evasion);
            if(adapt > 1000) adapt = 1000;
        }
        for(Glyph e : glyph){
            adapt *= e.evasionFactor(this ,owner , enemy, evasion);
            if(adapt > 1000) adapt = 1000;
        }
        evasion *= adapt;
		
		return evasion ;//+ augment.evasionFactor(level());
	}
	
	public float speedFactor( Char owner, float speed ){
		
		/*if (owner instanceof Hero) {
			int aEnc = STRReq() - ((Hero) owner).STR();
			if (aEnc > 0) speed /= Math.pow(1.2, aEnc);
		}*/
		
		if (hasGlyph(Swiftness.class)) {
			boolean enemyNear = false;
			for (Char ch : Actor.chars()){
				if (Dungeon.level.adjacent(ch.pos, owner.pos) && owner.alignment != ch.alignment){
					enemyNear = true;
					break;
				}
			}
			if (!enemyNear) speed *= (1.2f + 0.04f * level());
		}/* else if (hasGlyph(Balance.class) && Dungeon.level.water[owner.pos]){
			speed *= (1.5f + 0.1f * level());
		}*/
		
		if (hasGlyph(Bulk.class) &&
				(Dungeon.level.map[owner.pos] == Terrain.DOOR
						|| Dungeon.level.map[owner.pos] == Terrain.OPEN_DOOR )) {
			speed /= 3f;
		}
		
		return speed;
		
	}
	
	public float stealthFactor( Char owner, float stealth ){
		
		if (hasGlyph(Obfuscation.class)){
			stealth += 1 + level()/3f;
		}
		
		return stealth;
	}

	@Override
	public Item upgrade() {
		return upgrade( false );
	}
	public Item upgrade(boolean enchant ) {

		if (enchant && (!hasAllGlyph() || glyph.size() < LIMIT)){
			inscribe();
		}

		cursed = false;

		if (seal != null && seal.level() == 0)
			seal.upgrade();

		return super.upgrade();
	}
	
	public int proc(Char attacker, Char defender, int damage, EffectType type) {

		for(Glyph e : properties){
			damage *= e.proc(this, attacker, defender, damage, type);
		}
		for(Glyph e : glyph){
			damage *= e.proc(this, attacker, defender, damage, type);
		}
		
		if (!levelKnown) {
			if (--hitsToKnow <= 0) {
				identify();
				GLog.w( Messages.get(Armor.class, "identify") );
				Badges.validateItemLevelAquired( this );
			}
		}
		
		return damage;
	}


	
	@Override
	public String info() {
		String info = desc();
		
		if (levelKnown) {
			info += "\n\n" + Messages.get(Armor.class, "curr_absorb",tier, min(), max());
			
			/*if (STRReq() > Dungeon.hero.STR()) {
				info += " " + Messages.get(Armor.class, "too_heavy");
			}*/
		} else {
			info += "\n\n" + Messages.get(Armor.class, "avg_absorb",tier, min(0), max(0));

			/*if (STRReq(0) > Dungeon.hero.STR()) {
				info += " " + Messages.get(Armor.class, "probably_too_heavy");
			}*/
		}

		String stats_desc = Messages.get(this, "stats_desc");
		if (!stats_desc.equals("")) info+= "\n\n" + stats_desc;

		/*switch (augment) {
			case EVASION:
				info += "\n\n" + Messages.get(Armor.class, "evasion");
				break;
			case DEFENSE:
				info += "\n\n" + Messages.get(Armor.class, "defense");
				break;
			case NONE:
		}*/
		if(isIdentified())
			info += "\n"  + Messages.get(Armor.class, "limit", this.glyphCount() , this.LIMIT);
		else
			info += "\n"  + Messages.get(Armor.class, "limit_1", this.LIMIT);

		if(glyphCount() > 0 || properties.size() > 0) {
			info += "\n";
		}
		for(Glyph e :properties){
			info += "\n" + Messages.get(Armor.class, "inscribed", e.nameProperties());
		}
		if (glyphCount() != 0 && (cursedKnown || !hasCurseGlyph())){
			info += "\n";
			for(Glyph e :glyph){
				info += "\n" + Messages.get(Armor.class, "inscribed", e.name());
			}
		}
		
		if (cursed && isEquipped( Dungeon.hero )) {
			info += "\n\n" + Messages.get(Armor.class, "cursed_worn");
		} else if (cursedKnown && cursed) {
			info += "\n\n" + Messages.get(Armor.class, "cursed");
		} else if (seal != null) {
			info += "\n\n" + Messages.get(Armor.class, "seal_attached");
		}
		
		return info;
	}

	@Override
	public Emitter emitter() {
		if (seal == null) return super.emitter();
		Emitter emitter = new Emitter();
		emitter.pos(ItemSpriteSheet.film.width(image)/2f + 2f, ItemSpriteSheet.film.height(image)/3f);
		emitter.fillTarget = false;
		emitter.pour(Speck.factory( Speck.RED_LIGHT ), 0.6f);
		return emitter;
	}

	@Override
	public Item random() {
		int n = 0;
		//+0: 75% (3/4)
		//+1: 20% (4/20)
		//+2: 5%  (1/20)
		/*if (Random.Int(4) == 0) {
			n++;
			if (Random.Int(5) == 0) {
				n++;
			}
		}*/

		//+0: 42.2% (19/45)
		//+1: 40% (2/5)
		//+2: 13% (2/15)
		//+3: 4%  (2/45)
		if(Random.Int(5) < 2){
			n++;
			if(Random.Int(3) == 0){
				n++;
				if(Random.Int(3) == 0){
					n++;
				}
			}
		}
		level(n);
		
		//30% chance to be cursed
		//15% chance to be inscribed
		float effectRoll = Random.Float();
		if (effectRoll < 0.1f) {
			inscribe(Glyph.randomCurse());
			cursed = true;
		} else if (effectRoll >= 0.85f){
			inscribe();
		}

		return this;
	}

	/*public int STRReq(){
		return STRReq(level());
	}

	public int STRReq(int lvl){
		lvl = Math.max(0, lvl);

		//strength req decreases at +1,+3,+6,+10,etc.
		return (8 + Math.round(tier * 2)) - (int)(Math.sqrt(8 * lvl + 1) - 1)/2;
	}*/
	
	@Override
	public int price() {
		if (seal != null) return 0;

		int price = 20 * tier;
		if (hasGoodGlyph()) {
			price *= 1.5;
		}
		if (cursedKnown && (cursed || hasCurseGlyph())) {
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

	public Armor inscribe( Glyph gl ) {
		if (!canInscribe(null)) {
			GLog.i(Messages.get(this, "full_glyph"));
		}
		else if(!canInscribe(gl.getClass())){
			GLog.i(Messages.get(this, "has_glyph"));
		}
		else{
			glyph.add(gl);
		}
		return this;
	}

	public Armor inscribe() {

		Glyph gl = Glyph.random();
		if (!canInscribe(null)) {
			//GLog.i(Messages.get(this, "full_glyph"));
			return this;
		}
		while (!canInscribe(gl.getClass())) {
			gl = Glyph.random();
		}
		return inscribe( gl );
	}

	public boolean hasGlyph(Class<?extends Glyph> type) {
		for(Glyph e : properties){
			if (e.getClass() == type)return true;
		}
		for(Glyph e : glyph){
			if (e.getClass() == type)return true;
		}
		return false;
	}

	public boolean hasProperty(Class<?extends Glyph> type) {
		for(Glyph e : properties){
			if (e.getClass() == type)return true;
		}
		return false;
	}

	public boolean canInscribe(Class<?extends Glyph> type) {
		if(glyphCount() < LIMIT) {
			if (type == null) {
				if(!hasAllGlyph()) {
					return true;
				}
			}else if(!hasGlyph(type)) {
				return true;
			}
		}
		return false;
	}

	public Item fusion(Item item) {
		int level = item.level();
		if(item instanceof Armor)
			level = (((Armor)item).seal != null && ((Armor)item).seal.level() > 0) ? item.level()-1 :item.level();
		if (level > 0) {
			upgrade( level );
		} else if (level < 0) {
			degrade( -level );
		}
		if(item instanceof Armor) {
			Armor armor = (Armor)item;
			if (armor.glyphCount() != 0 || armor.properties.size() != 0) {
				for (Armor.Glyph e : armor.properties) {
					if (canInscribe(e.getClass()))
						glyph.add(e);
					else if(glyphCount() >= LIMIT) break;
				}
				for (Armor.Glyph e : armor.glyph) {
					//LIMIT-count = enchantAble
					if (canInscribe(e.getClass()))
						glyph.add(e);
					else if(glyphCount() >= LIMIT) break;
				}
			}
			BrokenSeal seal = armor.checkSeal();
			if (seal != null){
				armor.seal = null;
				affixSeal(seal);
			}
		}
		return this;
	}

	public boolean hasAllGlyph() {
		if (glyphCount() != 0 || properties.size() != 0){
			Class<?>[] elist = Glyph.getAllglyph();
			for(int i=0 ; i < elist.length ; i++) {
				boolean isexist = false;
				for (Glyph e : properties) {
					if (e.getClass() == elist[i])
						isexist = true;
				}
				for (Glyph e : glyph) {
					if (e.getClass() == elist[i])
						isexist = true;
				}
				if(!isexist){
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public int glyphCount(){
		int i = 0;
		for(Glyph en : glyph){
			if(en != null){
				i++;
			}
		}
		return i;
	}

	public Glyph getGlyph(Class<?extends Glyph> type){
		for(Glyph e : properties){
			if (e.getClass() == type)return e;
		}
		for(Glyph e : glyph){
			if (e.getClass() == type)return e;
		}
		return null;
	}

	public boolean hasGoodGlyph(){

		if (glyphCount() != 0){
			for(Glyph e : glyph){
				if (!e.curse())return true;
			}
		}
		return false;
	}

	public boolean hasCurseGlyph(){

		if (glyphCount() != 0){
			for(Glyph e : glyph){
				if (e.curse())return true;
			}
		}
		return false;
	}
	
	//FIXME need to adjust glyphs given armor augmentation changes
	public static abstract class Glyph implements Bundlable {
		
		private static final Class<?>[] glyphs = new Class<?>[]{
				Obfuscation.class, Swiftness.class, Viscosity.class, Potential.class,
				Brimstone.class, Entanglement.class, Repulsion.class, Camouflage.class, Balance.class,
				Affection.class, AntiMagic.class, Thorns.class };
		private static final float[] chances= new float[]{
				10, 10, 10, 10,
				5, 5, 5, 5, 5,
				5, 5, 5 };

		private static final Class<?>[] curses = new Class<?>[]{
				AntiEntropy.class, Corrosion.class, Displacement.class, Metabolism.class,
				Multiplicity.class, Stench.class, Overgrowth.class, Bulk.class
		};
			
		public abstract float proc( Armor armor, Char attacker, Char defender, int damage , EffectType type );

        public float evasionFactor( Armor armor, Char attacker, Char defender, float evasion){
            return 1;
        }

		public static Class<?>[] getAllglyph(){
			return glyphs;
		}

		public static Class<?>[] getAllcurses(){
			return curses;
		}
		
		public String name() {
			if (!curse())
				return name( Messages.get(this, "glyph") );
			else
				return name( Messages.get(Item.class, "curse"));
		}

		public String nameProperties() {
			return name( Messages.get(Item.class, "properties"));
		}
		
		public String name( String armorName ) {
			return Messages.get(this, "name", armorName);
		}

		public String desc(Armor armor) {
			return Messages.get(this, "desc");
		}

		public boolean curse() {
			return false;
		}
		
		@Override
		public void restoreFromBundle( Bundle bundle ) {
		}

		@Override
		public void storeInBundle( Bundle bundle ) {
		}
		
		public abstract ItemSprite.Glowing glowing();

		@SuppressWarnings("unchecked")
		public static Glyph random() {
			try {
				return ((Class<Glyph>)glyphs[ Random.chances( chances ) ]).newInstance();
			} catch (Exception e) {
				Fushiginopixeldungeon.reportException(e);
				return null;
			}
		}

		@SuppressWarnings("unchecked")
		public static Glyph randomCurse(){
			try {
				return ((Class<Glyph>)Random.oneOf(curses)).newInstance();
			} catch (Exception e) {
				Fushiginopixeldungeon.reportException(e);
				return null;
			}
		}
		
	}
}
