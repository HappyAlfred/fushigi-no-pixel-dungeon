package com.fushiginopixel.fushiginopixeldungeon.effects;

import com.watabou.noosa.Game;
import com.watabou.noosa.Gizmo;
import com.watabou.utils.Callback;

public class Delay extends Gizmo {

    Callback callback;
    float duration;
    //delay(ms)
    public Delay(float duration, Callback callback){
        super();

        this.callback = callback;
        this.duration = duration;
    }


    @Override
    public void update() {
        duration -= Game.elapsed;
        if (duration < 0) {

            killAndErase();
            callback.call();
        }
    }
}
