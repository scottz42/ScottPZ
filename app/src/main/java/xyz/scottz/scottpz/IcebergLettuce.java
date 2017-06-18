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
    Bitmap bitmap;
    Resources res ;

    private static long rechargeTime = 20000 ;

    public static long getRechargeTime()
    {
        return rechargeTime ;
    }

  IcebergLettuce(Resources res)
    {
        super(res);
        this.res = res ;
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
        dst.set(getX(), getY(), getX() + 92, getY() + 88);

        canvas.drawBitmap(bitmap, src,dst,p);
    }

    @Override
    void Move()
    {
           for (MajorObject o : Game.getMajors()) {
                if (!o.isPlant() && !o.isTombstone()) {
                    Zombie zombie = (Zombie) o;
                    int zombieX = (zombie.getX() / 100) * 100;
                    int zombieY = (zombie.getY() / 100) * 100;
                    // kill zombies in this square ;
                    if (zombieX == getX() && zombieY == getY()) {
                        Game.removePlant(this);
                        zombie.freeze(10000);
                    }
                }
            }
    }

}
