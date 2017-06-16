package xyz.scottz.scottpz;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by lei on 2017/6/8.
 */

public class Sun extends MinorObject {
    private Bitmap bitmap ;
    private long createTime ;
    private long duration = 8000 ;  // TODO: 10s or so

    // initial position, current position could change due to stealing
    int origX , origY ;

    // sun-stealing
    private long startStealTime ;
    private long startStealDelay = 1000 ;
    private long stealFrameTime = 500 ;
    private int stealSteps = 4 ;
    private Zombie stealingZombie ;

    Sun (Resources res , int x , int y)
    {
        super(res) ;
        setX(x) ;
        setY(y) ;
        origX = x ;
        origY = y ;
        bitmap = BitmapFactory.decodeResource(res, R.drawable.sun);
        createTime = System.currentTimeMillis();
        startStealTime = 0 ;
    }

    // after an initial delay, sun can be stolen
    public int calcCanSteal()
    {
        if ((System.currentTimeMillis()-createTime)>startStealDelay) {
            return 50 ;
        } else {
            return 0 ;
        }
    }

    public void steal(Zombie zombie)
    {
        stealingZombie = zombie ;
        startStealTime = System.currentTimeMillis() ;
    }

    void Draw(Canvas canvas , Paint p)
    {
        super.Draw(canvas,p);

        // if stealing, recalculate sun position & finish stealing if time is up
        if (startStealTime>0) {
            int step = (int) ((System.currentTimeMillis()-startStealTime)*stealSteps/stealFrameTime) ;
            if (step>=stealSteps) {
                step = stealSteps ;
                // TODO: finish stealing, current code is not quite right, need to update zombie properly
                createTime = 0 ;        // make current sun disappear
            }
            int zombieX = stealingZombie.getX() ;
            int zombieY = stealingZombie.getY() ;
            setX(origX + (zombieX-origX)*step/stealSteps) ;
            setY(origY + (zombieY-origY)*step/stealSteps) ;

        }


        Rect src = new Rect() ;
        Rect dst = new Rect() ;
        src.set(0,0,bitmap.getWidth()-1,bitmap.getHeight()-1);
        dst.set(getX() , getY() , getX()+63, getY()+ 60);

        canvas.drawBitmap(bitmap, src,dst,p);

    }

    boolean isDead()
    {
        return ((System.currentTimeMillis()-createTime)>duration) ;
    }
}
