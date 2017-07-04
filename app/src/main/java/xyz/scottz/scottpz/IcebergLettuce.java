package xyz.scottz.scottpz;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by lei on 2017/6/18.
 */

public class IcebergLettuce extends Plant {
    private static long rechargeTime = 20000 ;      // ms
    private static long freezeTime = 10000 ;    // ms

    Bitmap bitmap;
    Resources res ;


    public static long getRechargeTime()
    {
        return rechargeTime ;
    }

  IcebergLettuce()
    {
        super();
        this.res = Game.getResources() ;
        setSunNeeded(0);
        damagePerShot = 0 ;    // nds
        bitmap = BitmapFactory.decodeResource(res, R.drawable.iceberglettuce);
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
           for (MajorObject o : Game.getMajors()) {
                if (!o.isPlant() && !o.isTombstone()) {
                    Zombie zombie = (Zombie) o;
                    // freeze zombies in this square ;
                    if (GridLogic.isZombieInPlantSquare(zombie , this)) {
                        Game.removePlant(this);
                        zombie.freeze(freezeTime);
                    }
                }
            }
    }

}
