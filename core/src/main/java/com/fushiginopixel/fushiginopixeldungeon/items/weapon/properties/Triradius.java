package com.fushiginopixel.fushiginopixeldungeon.items.weapon.properties;

import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.Weapon;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.MeleeWeapon;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.MissileWeapon;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.watabou.utils.Point;
import com.watabou.utils.PointF;

import java.util.ArrayList;

public class Triradius extends Weapon.Enchantment {

    @Override
    public boolean procBeforeAttack(Weapon weapon, Char attacker, Char defender, boolean process, EffectType type ) {

        if (process && !type.isExistAttachType(EffectType.SPLIT)){
            if((weapon instanceof MeleeWeapon) || type.isExistAttachType(EffectType.MELEE)) {
                int maxReach = weapon.hasProperty(getClass()) ? weapon.reachFactor(attacker) : weapon.RCH;
                Point vector = Dungeon.level.cellToPoint(attacker.pos).vector(Dungeon.level.cellToPoint(defender.pos));
                float rotate = (float) vector.getVectorAngle();
                ArrayList<Char> chars = new ArrayList<>();

                int j = -45;
                for (int i = 0; i < 2; i++) {
                    Ballistica ballistica = new Ballistica(attacker.pos, rotate + j, Ballistica.WONT_STOP, Ballistica.USING_ROTATION);
                    for (int c : ballistica.subPath(1, maxReach)) {

                        Char ch = Actor.findChar(c);
                        if (ch != null && ch != defender && attacker.canAttack(defender)) {
                            chars.add(ch);
                            break;
                        }
                    }

                    j *= -1;
                }

                for (Char target : chars) {
                    attacker.attack(target, new EffectType(type.attachType | EffectType.SPLIT, type.effectType), weapon);
                }
            }/*else if(weapon instanceof MissileWeapon && type.isExistAttachType(EffectType.MISSILE)){
                int maxReach = weapon.hasProperty(getClass()) ? weapon.reachFactor(attacker) : weapon.RCH;
                Point vector = Dungeon.level.cellToPoint(attacker.pos).vector(Dungeon.level.cellToPoint(defender.pos));
                float rotate = (float) vector.getVectorAngle();
                ArrayList<Float> targets = new ArrayList<>();
                targets.add(rotate - 45);
                targets.add(rotate + 45);

                for (float target : targets) {
                    attacker.attack(target, new EffectType(type.attachType | EffectType.SPLIT, type.effectType), weapon);
                }
            }*/

        }

        return true;
    }
}
