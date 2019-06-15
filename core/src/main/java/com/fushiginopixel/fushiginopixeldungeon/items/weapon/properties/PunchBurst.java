package com.fushiginopixel.fushiginopixeldungeon.items.weapon.properties;

import com.fushiginopixel.fushiginopixeldungeon.Assets;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.FlavourBuff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.MagicalSleep;
import com.fushiginopixel.fushiginopixeldungeon.effects.CellEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.BlastParticle;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.SmokeParticle;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.Weapon;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class PunchBurst extends Weapon.Enchantment {

    private ItemSprite.Glowing WHITE = new ItemSprite.Glowing( 0xCCCCCC );
    @Override
    public float proc(Weapon weapon, Char attacker, Char defender, int damage, EffectType type ) {

        Sample.INSTANCE.play( Assets.SND_BLAST );

        int cell = defender.pos;
        if (Dungeon.level.heroFOV[cell]) {
            CellEmitter.center( cell ).burst( BlastParticle.FACTORY, 20 );
        }

        for (int n : PathFinder.NEIGHBOURS8) {
            int c = cell + n;
            if (c >= 0 && c < Dungeon.level.length()) {
                if (Dungeon.level.heroFOV[c]) {
                    CellEmitter.get( c ).burst( SmokeParticle.FACTORY, 3 );
                }

                Char ch = Actor.findChar( c );
                if (ch != null && ch != attacker && ch != defender) {
                    int dmg = Random.NormalIntRange( damage/4, damage/2);
                    if(weapon.hasProperty(getClass()))
                        dmg *= 2;
                    dmg -= ch.drRoll();
                    if (dmg > 0) {
                        ch.damage( dmg, this ,new EffectType(EffectType.BURST,0));
                    }
                }
            }
        }

        return 1;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return WHITE;
    }
}
