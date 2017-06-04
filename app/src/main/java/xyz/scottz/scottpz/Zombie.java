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
2. uneven timer for the next step, some steps will have the same time for multiple objects

 */

public class Zombie extends MajorObject {

    public  Zombie(Resources res) {
        super(res);}


    @Override
    void Draw(Canvas c , Paint p) {
        super.Draw(c,p);
    }

}