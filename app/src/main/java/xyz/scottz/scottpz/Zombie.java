package xyz.scottz.scottpz;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;

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
    protected int life ;
    protected long LastMoveTime ;    // time for last move, in ms
    protected int DistancePerStep = 20 ;

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    protected int prevDistance = 20 ;
    protected int TimePerStep = 1000; // in ms
    protected int damagePerAttack = 1 ;
    protected int TimePerAttack = 1000 ; // in ms
    protected long LastAttackTime = 0 ;

    public  Zombie(Resources res) {
        super(res);
        LastMoveTime = System.currentTimeMillis() ;
    }

    @Override
    void Move()
    {


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
            if (x<0) {
                x = 1000 ;
                y = 100*((int)(Math.random()*4)+1);
            }
            if (x>1000) {
                x = 0;
            }
            LastMoveTime += TimePerStep ;
        }
    }

    @Override
    void Draw(Canvas c , Paint p) {
        super.Draw(c,p);
    }

}
