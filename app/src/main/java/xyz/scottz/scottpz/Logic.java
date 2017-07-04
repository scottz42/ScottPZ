package xyz.scottz.scottpz;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

/**
 * Created by lei on 2017/6/18.
 */

// parent class for different pieces of game logic
// init, destroy , onTimer , onTouch, onDraw

public class Logic {

    public void initOnce()
    {
    }

    // init() really is the start of a new level, change name?
    public void init()
    {
    }

    public void destroy()
    {
    }

    public boolean onTimer()
    {
        return false ;
    }

    public boolean onTouch(MotionEvent event)
    {
        return false ;
    }

    public void onDraw(Canvas canvas , Paint paint)
    {
    }

}
