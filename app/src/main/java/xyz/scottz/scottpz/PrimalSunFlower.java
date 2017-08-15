package xyz.scottz.scottpz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.ArrayList;

import xyz.scottz.scottpz.Plant;

/**
 * Created by lei on 2017/8/4.
 */

public class PrimalSunFlower extends Plant {
    private static Bitmap bitmap = null;
    private static Bitmap selectBitmap = null;

    private ArrayList<Sun> suns;
    private long LastGenerateTime;
    private long TimePerGenerate = 24000;   // ms

    private static long rechargeTime = 5000;

    public static long getRechargeTime() {
        return rechargeTime;
    }


    public PrimalSunFlower() {
        super();
        sunNeeded=75;
        suns = new ArrayList<Sun>();
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(Game.getResources(), R.drawable.primalsunflower);
            selectBitmap = BitmapFactory.decodeResource(Game.getResources(), R.drawable.primalsunflowerselect);
        }

        LastGenerateTime = System.currentTimeMillis();
    }

    @Override
    void Move() {
        if ((System.currentTimeMillis() - LastGenerateTime) > TimePerGenerate) {
            Sun sun = new Sun(Game.getResources(), getX(), getY());
            sun.setNoSun(75);
            suns.add(sun);
            LastGenerateTime += TimePerGenerate;
        }
        for (Sun sun : suns) {
            sun.onTimer();
        }
    }

    // TODO: should only pick one sun at a time
    @Override
    public void checkSun(MotionEvent event) {
        ArrayList removeSuns = new ArrayList();
        for (Sun sun : suns) {
            int diffX = (int) event.getX() - sun.getX();
            int diffY = (int) event.getY() - sun.getY();
            if (diffX < 60 && diffX > 0 && diffY < 60 && diffY > 0) {
                Game.setNoSun(Game.getNoSun() + sun.getNoSun());  // TODO: adjust for different size of suns
                removeSuns.add(sun);
            }
        }
        suns.removeAll(removeSuns);
    }

    // TODO: same logic for all sun-producing plants
    @Override
    int calcCanStealSun() {
        int result = 0;
        for (Sun sun : suns) {
            result += sun.calcCanSteal();
        }
        return result;
    }

    @Override
    void stealSun(Zombie zombie, int noSun) {
        int total = 0;
        for (Sun sun : suns) {
            if (total+sun.getNoSun() <= noSun) {
                sun.steal(zombie);
                total += 50;
            }
        }

    }

    @Override
    void Draw(Canvas canvas, Paint p) {
        super.Draw(canvas, p);

        Rect src = new Rect();
        Rect dst = new Rect();
        src.set(0, 0, bitmap.getWidth() - 1, bitmap.getHeight() - 1);
        dst.set(getX(), getY(), getX() + 92, getY() + 88);

        canvas.drawBitmap(bitmap, src, dst, p);

        // TODO: move to onTimer?
        for (Sun sun : suns) {
            if (sun.isDead()) {
                suns.remove(sun);
            } else {
                sun.onDraw(canvas, p);
            }
        }

    }

    @Override
    void drawSelect(Canvas canvas, Paint p) {
        super.Draw(canvas, p);

        Rect src = new Rect();
        Rect dst = new Rect();
        src.set(0, 0, selectBitmap.getWidth() - 1, selectBitmap.getHeight() - 1);
        dst.set(getX(), getY(), getX() + GridLogic.getSelectWidth(), getY() + GridLogic.getSelectHeight());

        canvas.drawBitmap(selectBitmap, src, dst, p);
    }
    @Override
    public boolean initialCD()
    {
        return false;
    }

}
