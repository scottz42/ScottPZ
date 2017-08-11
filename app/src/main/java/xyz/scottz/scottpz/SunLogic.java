package xyz.scottz.scottpz;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import java.util.ArrayList;

/**
 * Created by lei on 2017/6/18.
 */

// handle sun-related logic
// currently includes sun accounting, plant-generated sun(partially in plant classes), falling sun, shovel-generated sun, stealing sun
public class SunLogic extends Logic {

    // sun accounting
    static int noSun = 50 ;
    public static int getNoSun() {
        return noSun;
    }
    public static void setNoSun(int inNoSun) {
        noSun = inNoSun;
    }

    // falling suns
    static boolean fallingSunMode = true ;
    static long lastFallingSunTime = 0 ;
    static long fallingSunInterval = 6000 ;
    static private ArrayList<Sun> fallingSuns ;

    @Override
    public void init()
    {
        super.init();

        noSun = 50 ;

        lastFallingSunTime = System.currentTimeMillis() ;
        fallingSuns = new ArrayList() ;

    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        super.onTouch(event) ;

        if (!Game.isNormalPlay()) return false;

        for (MajorObject o : Game.getMajors()) {
            o.checkSun(event);
        }

        for (Object o: fallingSuns) {
            Sun sun = (Sun) o ;
            int diffX = (int)event.getX()-sun.getX() ;
            int diffY = (int)event.getY() - sun.getY() ;
            if (diffX<60 && diffX>0 && diffY<60 && diffY>0) {
                setNoSun(getNoSun()+sun.getNoSun());
                fallingSuns.remove(o) ;
                return true ;
            }
        }
        return false ;

    }


    // generate new falling suns & move existing falling suns & destroy suns that haven't been picked up
    // generate: if falling sun mode & enough time elapsed from last sun generated
    // move: has not reached final position & enough time to move to next position
    // destroy：enough time since final position
    @Override
    public boolean onTimer()
    {
        super.onTimer();

        if (!Game.isNormalPlay()) return false;
        if (fallingSunMode) {
            // generate
            // TODO: initial vs. subsequent ones
            if ((System.currentTimeMillis() - lastFallingSunTime) > fallingSunInterval) {
                lastFallingSunTime = System.currentTimeMillis();
                // TODO: figure out rule for initial position
                int x = ((int) (Math.random() * 500)) + 300;
                int y = 50;
                Sun sun = new Sun(Game.getResources(), x, y);
                sun.setFalling(true);
                sun.setNoSun(50);
                fallingSuns.add(sun);
            }

            // sun falling
            for (Sun sun : fallingSuns) {
                sun.move();
            }

            // destroy
            ArrayList sunsToRemove = new ArrayList();
            for (Sun sun : fallingSuns) {
                if (sun.isDead()) {
                    sunsToRemove.add(sun);
                }
            }
            fallingSuns.removeAll(sunsToRemove);
        }
        return true ;
    }

    @Override
    public void onDraw(Canvas canvas, Paint paint)
    {
        super.onDraw(canvas, paint);

        // TODO: better display of sun left
        paint.setColor(Color.BLUE);
        paint.setTextSize(50);
        String s = String.format("Suns: %d" , getNoSun()) ;
        canvas.drawText(s , 10 , 70 , paint) ;

        for (Object o: fallingSuns) {
            Sun sun = (Sun)o ;
            sun.onDraw(canvas , paint);
        }
    }

    static int calcCanSteal() {
        int result = 0;
        for (Sun sun : fallingSuns) {
            result += sun.calcCanSteal();
        }
        return result;
    }

    static void stealSun(Zombie zombie, int noSun) {
        int total = 0;
        for (Sun sun : fallingSuns) {
            if (total+sun.getNoSun() <= noSun) {
                sun.steal(zombie);
                total += sun.getNoSun();
            }
        }
    }




    public static void addFallingSun(Sun sun)
    {
        fallingSuns.add(sun);
    }
}
