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

// three types so far: generated by a plant, or falling from sky, shovel

    // falling sun: 6s per sun, falling time 3s, disappearing time same as plant-generated sun, 3s for initial sun

public class Sun extends MinorObject {
    private static Bitmap bitmap=null ;
    private long createTime ;
    private long duration = 10000 ;  // TODO: 10s or so

    private long fallingDuration = 3000 ;   // ms
    private static int timePerSunMove = 100 ;   // ms
    private static int distancePerSunMove = 10 ;
    private long lastSunMoveTime ;
    // initial position, current position could change due to stealing
    int origX , origY ;

    // sun-stealing
    private long startStealTime ;
    private long startStealDelay = 1000 ;
    private long stealFrameTime = 500 ;
    private int stealSteps = 4 ;
    private RaZombie stealingZombie ;

    private int noSun = 50;

    public int getNoSun() {
        return noSun;
    }

    public void setNoSun(int noSun) {
        this.noSun = noSun;
    }

    Sun (Resources res , int x , int y)
    {
        super(res) ;
        setX(x) ;
        setY(y) ;
        origX = x ;
        origY = y ;
        if (bitmap==null) {
            bitmap = BitmapFactory.decodeResource(res, R.drawable.sun);
        }
        createTime = System.currentTimeMillis();
        lastSunMoveTime = createTime ;
        startStealTime = 0 ;
        stealingZombie = null;
        fallingDuration = 2000 + ((int)(Math.random()*2000)) ;
    }



    // after an initial delay, sun can be stolen
    public int calcCanSteal()
    {
        if (stealingZombie==null && (System.currentTimeMillis()-createTime)>startStealDelay) {
            return noSun ;
        } else {
            return 0 ;
        }
    }

    public void steal(Zombie zombie)
    {
        stealingZombie = (RaZombie)zombie ;
        startStealTime = System.currentTimeMillis() ;
    }

    void onDraw(Canvas canvas , Paint p)
    {
        super.onDraw(canvas,p);

        Rect src = new Rect() ;
        Rect dst = new Rect() ;
        src.set(0,0,bitmap.getWidth()-1,bitmap.getHeight()-1);
        if (startStealTime>0) {
            dst.set(getX(), getY(), getX() + 100, getY() + 100);
        } else {
            dst.set(getX(), getY(), getX() + 63, getY() + 60);
        }
        canvas.drawBitmap(bitmap, src,dst,p);
    }

    // movement of falling sun
    // logic:
    void move() {
        if (startStealTime==0) {
            if ((System.currentTimeMillis() - createTime) < fallingDuration) {
                if ((System.currentTimeMillis() - lastSunMoveTime) > timePerSunMove) {
                    lastSunMoveTime += timePerSunMove;
                    y += distancePerSunMove;
                    origY = y;
                }
            }
        }
    }

    void onTimer()
    {

        // if stealing, recalculate sun position & finish stealing if time is up
        if (startStealTime>0) {
            int step = (int) ((System.currentTimeMillis()-startStealTime)/stealFrameTime) ;
            if (step>=stealSteps) {
                step = stealSteps ;
                // TODO: finish stealing, current code is not quite right, need to update zombie properly
                stealingZombie.finishSteal(this);
                createTime = System.currentTimeMillis()-duration ;        // make current sun disappear
            }
            int zombieX = stealingZombie.getX() ;
            int zombieY = stealingZombie.getY() ;
            x = origX + (zombieX-origX)*step/stealSteps ;
            y = origY + (zombieY-origY)*step/stealSteps ;
        }

    }


    boolean isDead()
    {
        return ((System.currentTimeMillis()-createTime)>duration) ;
    }
}
