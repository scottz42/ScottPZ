package xyz.scottz.scottpz;

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
    protected double life ;
    protected long LastMoveTime ;    // time for last move, in ms
    protected int DistancePerStep = 20 ;

    public long getLastMoveTime() {
        return LastMoveTime;
    }

    public void setLastMoveTime(long lastMoveTime) {
        LastMoveTime = lastMoveTime;
    }

    public double getLife() {
        return life;
    }

    public void setLife(double life) {
        this.life = life;
    }

    protected int prevDistance = 20 ;
    protected int TimePerStep = 1000; // in ms
    protected int damagePerAttack = 1 ;
    protected int TimePerAttack = 1000 ; // in ms
    protected long LastAttackTime = 0 ;

    protected long freezeDuration=0;
    protected long startFreezeTime=0;

    public  Zombie() {
        super();
        LastMoveTime = System.currentTimeMillis() ;
    }

    @Override
    // TODO: use thread instead of timer model?
    void Move()
    {
        if (startFreezeTime>0 && (System.currentTimeMillis()<startFreezeTime+freezeDuration)){
            return;
        }
        if (startFreezeTime>0 && (System.currentTimeMillis()>startFreezeTime+freezeDuration)){
            LastMoveTime += System.currentTimeMillis() - startFreezeTime ;  // continue moving
            startFreezeTime=0;
        }


        // zombie eat plant ;
        Plant plant = Game.findPlant(getX(), getY());
        if (plant != null) {    // there is plant to eat
            if (LastAttackTime > 0) { // eating started
                if (System.currentTimeMillis() - LastAttackTime>TimePerAttack){  // finished one attack
                    plant.setLife(plant.getLife() - damagePerAttack);
                    if (plant.getLife() <= 0) {   // plant is eaten by zombie
                        Game.removePlant(plant);
                        DistancePerStep = prevDistance;
                        LastAttackTime = 0;
                    } else {
                        LastAttackTime += TimePerAttack;
                    }
                } else {  // wait for this attack to finish
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
            x -= DistancePerStep ;
            // TODO: GridLogic
            if (x<0) {
                x = 1000 ;
                y = 100*((int)(Math.random()*4)+1);
            }
            if (x>1200) {
                x = 0;
            }
            LastMoveTime += TimePerStep ;
        }
    }

    // true if dead
    public boolean takeDamage(int damage)
    {
        setLife(getLife() - damage);
        if (getLife() <= 0) {
            cleanup();
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


    @Override
    void Draw(Canvas c , Paint p) {
        super.Draw(c,p);
    }

    // to be overriden for each zombie
    void cleanup() {}

}
