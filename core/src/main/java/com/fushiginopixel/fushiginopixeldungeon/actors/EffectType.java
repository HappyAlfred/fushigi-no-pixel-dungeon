package com.fushiginopixel.fushiginopixeldungeon.actors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class EffectType {

    //attachtyoe
    public static final int MELEE = 0b1;
    public static final int MISSILE = 0b10;
    public static final int BURST = 0b100;
    public static final int BLOB = 0b1000;
    public static final int GAS = 0b10000;
    public static final int INSIDE = 0b100000;
    public static final int MAGICAL_BOLT = 0b1000000;
    public static final int BEAM = 0b10000000;
    public static final int BUFF = 0b100000000;

    //effecttype
    public static final int ICE = 0b1;
    public static final int FIRE = 0b10;
    public static final int ELETRIC = 0b100;
    public static final int POISON = 0b1000;
    public static final int CORRROSION = 0b10000;
    public static final int LIGHT = 0b100000;
    public static final int DARK = 0b1000000;
    public static final int SPIRIT = 0b10000000;

    //public static final int MAGICAL_AFFECT = MAGICAL_BOLT + BEAM;
    public static final ArrayList<EffectType> MAGICAL_AFFECTS = new ArrayList<>();
    static {
        MAGICAL_AFFECTS.add(new EffectType(MAGICAL_BOLT, 0));
        MAGICAL_AFFECTS.add(new EffectType(BEAM, 0));
    }

    public int attachType;
    public int effectType;
    public Class attachClass;

    public EffectType(int attachType, int effectType){
        this(attachType,effectType,null);
    }

    public EffectType(Class attachClass){
        this(0,0,attachClass);
    }

    public EffectType(){
        this(0,0,null);
    }

    public EffectType(int attachType, int effectType, Class attachClass){
        this.attachType = attachType;
        this.effectType = effectType;
        this.attachClass = attachClass;
    }

    public boolean isExistAttachType(int type){

        return (attachType & type) == type;

    }

    public boolean isExistEffectType(int type){

        return (effectType & type) == type;

    }

    public static boolean isExistType(int typeToCheck , int type){

        if((typeToCheck & type) == type)
            return true;
        else return false;

    }

    public static boolean isExistType(EffectType typeToCheck , EffectType type){

        if(isExistType(typeToCheck.attachType,type.attachType) && isExistType(typeToCheck.effectType , type.effectType)){
            if(type.attachClass == null){
                return true;
            }else if(typeToCheck.attachClass != null && type.attachClass.isAssignableFrom(typeToCheck.attachClass)){
                return true;
            }
        }
        return false;

    }

    public static boolean isExistType(EffectType typeToCheck , Collection<EffectType> collection){

        for(EffectType type : collection){
            if(isExistType(typeToCheck,type)){
                return true;
            }
        }
        return false;

    }
}
