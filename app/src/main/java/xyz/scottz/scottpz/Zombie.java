package xyz.scottz.scottpz;

import android.bluetooth.BluetoothClass;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.provider.Settings;

import xyz.scottz.scottpz.MajorObject;

/**
 * Created by lei on 2017/6/4.
 */


/* zombie movement model:
normal case: zombie moves at a constant speed: one step per time period
special cases: eg. move to a different lane, speeds up, moves backward etc.

main states:
    (x,y) current logical position
    seconds per step:
    speed: distance per step;  x = initial_x + distance_per_step*[delta_t/seconds_per_step]

possible implementations:
1. uniform small timer(say 10ms): calculate position based on time
pro: simple time event logic
con: most timer events could be unnecessary
2. uneven timer for the next step, some steps will have the same time for multiple objects
con: data structure for keeping track of time events
pro: no wasted timer-induced calculation
 */

public class Zombie extends MajorObject {
    protected long LastMoveTime ;    // time for last move, in ms
    protected int DistancePerStep = 20 ;
    protected int PrevDistancePerStep;
    public long getLastMoveTime() {
        return LastMoveTime;
    }

    public void setLastMoveTime(long lastMoveTime) {
        LastMoveTime = lastMoveTime;
    }

    protected int prevDistance = 20 ;
    protected int TimePerStep = 1000; // in ms
    protected int damagePerAttack = 1 ;
    protected int TimePerAttack = 1000 ; // in ms
    protected long LastAttackTime = 0 ;
    protected int PrevTimePerAttack;

    protected long freezeDuration=0;
    protected long startFreezeTime=0;

    protected long startSlowdownTime=0;
    protected long slowdownDuration=6000;   // in ms

    protected boolean shrunk=false;

    protected boolean hypnotized=false;

    protected boolean flying=false;

    public  Zombie() {
        super();
        LastMoveTime = System.currentTimeMillis() ;
    }

    @Override
    // TODO: use thread instead of timer model?
    // TODO: needs cleanup
    void Move()
    {
        // freeze for a certain duration
        if (startFreezeTime>0 && (System.currentTimeMillis()<startFreezeTime+freezeDuration)){
            return;
        }
        if (startFreezeTime>0 && (System.currentTimeMillis()>startFreezeTime+freezeDuration)){
            LastMoveTime += System.currentTimeMillis() - startFreezeTime ;  // continue moving
            startFreezeTime=0;
        }

        // slowdown timer
        if (startSlowdownTime>0 && (System.currentTimeMillis()>startSlowdownTime+slowdownDuration)) {
            startSlowdownTime = 0;
            // restore
            DistancePerStep=PrevDistancePerStep;
            TimePerAttack=PrevTimePerAttack;
        }


        // zombie eat plant and hypnotized zombie
        MajorObject target = Game.findPlant(getX(), getY(),hypnotized);    // return plant or zombie depends on hypnotization
        // need to resume zombie movement when the plant it was attaking is gone for some reason
        if (target==null && LastAttackTime>0) {
            DistancePerStep = prevDistance;
            LastAttackTime = 0;
        }
        if (target != null && (!flying || target.blocksFlying())) {    // there is plant to eat
            if (LastAttackTime > 0) { // eating started
                if (System.currentTimeMillis() - LastAttackTime>TimePerAttack){  // finished one attack
                    target.setLife(target.getLife() - (shrunk?damagePerAttack/2:damagePerAttack));
                    if (target.getLife() <= 0) {   // plant is eaten by zombie
                        if (target.isPlant()) {
                            Game.removePlant((Plant) target);
                        } else {
                            Game.removeZombie((Zombie)target);
                        }
                        DistancePerStep = prevDistance;
                        LastAttackTime = 0;
                    } else {
                        LastAttackTime += TimePerAttack;
                    }
                } else {  // wait for this attack to finish
                    // TODO: need to take care of case where plant is eaten by another zombie

                }
            } else {  // start to eat
                LastAttackTime = System.currentTimeMillis();
                if (DistancePerStep != 0) {  // stop zombie movement
                    prevDistance = DistancePerStep;
                    DistancePerStep = 0;
                }
            }
        }


        // zombie movement
        if ((System.currentTimeMillis()-LastMoveTime)>TimePerStep) {
            if (hypnotized) {
                x += DistancePerStep ;
            } else {
                x -= DistancePerStep;
            }
            LastMoveTime += TimePerStep ;
        }
    }

    // true if dead
    public boolean takeDamage(double damage)
    {
        // if protected by rainbow environment, no damage
        // TODO: cleanup
        if (GridLogic.getEnvs()!=null) {
            for (Environment env : GridLogic.getEnvs()) {
                // TODO: make cleaner
                if (env.getClass().getName().endsWith("RainbowEnvironment") && !this.getClass().getName().endsWith("GlitterZombie")) {
                    RainbowEnvironment rainbow = (RainbowEnvironment) env;
                    if (rainbow.getArea().contains(getX(), getY())) {
                        return false;
                    }
                }
            }
        }


        // if shrunk, take double damage
        setLife(getLife() - (shrunk?damage*2:damage));
        if (getLife() <= 0) {
            Game.removeZombie(this);
            return true;
        } else {
            return false;
        }

    }

    public void freeze(long duration)
    {
        freezeDuration=duration;
        startFreezeTime= System.currentTimeMillis();
    }

    // factor: percentage, eg. 0.7 for 70% speed
    public void slowdown(double factor)
    {
        PrevDistancePerStep=DistancePerStep;
        PrevTimePerAttack=TimePerAttack;
        startSlowdownTime = System.currentTimeMillis();
        DistancePerStep *= factor;
        TimePerAttack /= factor;
    }

    // shrunk by shrinking violet
    // done: half size, take double damage, inflict half damage
    // TODO: other special effects for each type of zombie
    public void shrink()
    {
        shrunk = true;
    }

    public boolean isFlying() {
        return flying;
    }

    // hypnotize
    // effects: moves backwards, hurts zombies, can be hurt by zombies too
    public void setHypnotized()
    {
        hypnotized = true;
    }

    public boolean isHypnotized()
    {
        return hypnotized;
    }



    @Override
    // TODO: move most drawing functionality here?
    void Draw(Canvas c , Paint p) {
        super.Draw(c,p);
    }


    // called after zombie is killed
    // use same name onFinal as plant?
    void cleanup() {}

}
