package xyz.scottz.scottpz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by lei on 2017/8/7.
 */

public class GoldBloom extends Plant {
    private static long rechargeTime = 75000 ;      // ms
    private static long generateTime = 200 ;    // ms

    private long plantTime;
    private static Bitmap bitmap=null;
    private static Bitmap selectBitmap=null;

    public static long getRechargeTime()
    {
        return rechargeTime ;
    }

    GoldBloom()
    {
        super();
        setSunNeeded(0);
        if (bitmap==null) {
            bitmap = BitmapFactory.decodeResource(Game.getResources(), R.drawable.goldbloom);
            selectBitmap = BitmapFactory.decodeResource(Game.getResources(), R.drawable.goldbloomselect);
        }
        plantTime = System.currentTimeMillis();
    }

    @Override
    void Draw(Canvas canvas , Paint p) {
        super.Draw(canvas,p);

        Rect src = new Rect() ;
        Rect dst = new Rect() ;
        src.set(0,0,bitmap.getWidth()-1,bitmap.getHeight()-1);
        dst.set(getX(), getY(), getX() + GridLogic.getPlantWidth(), getY() + GridLogic.getPlantHeight());

        canvas.drawBitmap(bitmap, src,dst,p);
    }


    @Override
    void drawSelect(Canvas canvas , Paint p) {
        super.Draw(canvas,p);

        Rect src = new Rect() ;
        Rect dst = new Rect() ;
        src.set(0,0,selectBitmap.getWidth()-1,selectBitmap.getHeight()-1);
        dst.set(getX(), getY(), getX() + GridLogic.getSelectWidth(), getY() + GridLogic.getSelectHeight());

        canvas.drawBitmap(selectBitmap, src,dst,p);
    }



    @Override
    void Move()
    {
        if ((System.currentTimeMillis()-plantTime)>=generateTime) {
            for (int i=0 ; i<5 ; i++) {
                Sun sun = new Sun(Game.getResources(), getX() + (i - 2) * 40, getY());
                sun.setNoSun(75);
                SunLogic.addFallingSun(sun);
            }

            Game.removePlant(this);

        }
    }



}
