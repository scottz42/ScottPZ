package xyz.scottz.scottpz;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by lei on 2017/6/16.
 */

// TODO; recover sun when zombie dies

public class RaZombie extends Zombie {
    private static Bitmap bitmap ;
    private int sunsStolen = 0 ;    // already stolen
    private int sunsStealing = 0 ;  // in the process of being stolen

    RaZombie()
    {
        super();
        life = 9.25 ;
        if (bitmap==null) {
            bitmap = BitmapFactory.decodeResource(Game.getResources(), R.drawable.razombie);
        }
    }

    @Override
    void Draw(Canvas canvas , Paint p) {
        super.Draw(canvas,p);

        Rect src = new Rect() ;
        Rect dst = new Rect() ;
        src.set(0,0,bitmap.getWidth()-1,bitmap.getHeight()-1);
        dst.set(getX(), getY(), getX() + GridLogic.getZombieWidth(), getY() + GridLogic.getZombieHeight());

        canvas.drawBitmap(bitmap, src,dst,p);
    }

    @Override
    void Move() {
        super.Move();
        // TODO: sun bean effect

        int canSteal = 0 ;

        if (sunsStolen<250) {
            for (MajorObject o : Game.getMajors()) {
                canSteal = o.calcCanStealSun();
                if (canSteal > 0) {
                    if (sunsStolen + canSteal > 320) {
                        canSteal = 320 - sunsStolen;
                    }
                    o.stealSun(this , canSteal);
                    sunsStealing += canSteal;
                }
            }
            // falling suns & shoveled suns
            canSteal = SunLogic.calcCanSteal();
            if (canSteal>0) {
                if (sunsStolen + canSteal > 320) {
                    canSteal = 320 - sunsStolen;
                }
                SunLogic.stealSun(this,canSteal);
                sunsStealing += canSteal;
            }
        }

    }

    void finishSteal(Sun sun)
    {
        sunsStolen += sun.getNoSun();
    }

    @Override
    void cleanup()
    {
        // TODO: separate into a number of suns
        if (sunsStolen>0) {
            Sun sun = new Sun(Game.getResources(), x, y);
            sun.setNoSun(sunsStolen);
            SunLogic.addFallingSun(sun);
        }
    }
}
