package xyz.scottz.scottpz;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by lei on 2017/7/2.
 */

public class Jalapeno extends Plant {
    private static long rechargeTime = 35000 ;      // ms
    private static long explodeTime = 200 ;    // ms

    private long plantTime;
    Bitmap bitmap;
    Resources res ;

    public static long getRechargeTime()
    {
        return rechargeTime ;
    }

    Jalapeno(Resources res)
    {
        super(res);
        this.res = res ;
        setSunNeeded(125);
        damagePerShot = 90 ;    // nds
        bitmap = BitmapFactory.decodeResource(res, R.drawable.jalapeno);
        plantTime = System.currentTimeMillis();
        // TODO: need to recycle bitmap?
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
    void Move()
    {
        if ((System.currentTimeMillis()-plantTime)>explodeTime) {

            int row = GridLogic.calcRow(getY());

            for (MajorObject o : Game.getMajors()) {
                if (!o.isPlant() && !o.isTombstone()) {
                    Zombie zombie = (Zombie) o;
                    // kill zombies in this row
                    if (GridLogic.calcRow(o.getY())==row) {
                        zombie.setLife(zombie.getLife() - damagePerShot);
                        if (zombie.getLife() <= 0) {
                            Game.removeZombie(zombie);
                        }
                    }
                }
            }
            Game.removePlant(this);

        }
    }

}


