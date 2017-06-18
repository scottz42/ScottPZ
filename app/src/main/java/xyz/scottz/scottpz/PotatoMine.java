package xyz.scottz.scottpz;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by lei on 2017/6/13.
 */

public class PotatoMine extends Plant {

    private Bitmap bitmapUnarmed , bitmapArmed ;
    private boolean armed ;
    private long armingInterval = 15000 ;   // ms
    Resources res ;
    long creationTime ;     // time when mine is planted

    private static long rechargeTime = 20000 ;

    public static long getRechargeTime()
    {
        return rechargeTime ;
    }

    PotatoMine(Resources res)
    {
        super(res);
        this.res = res ;
        setSunNeeded(25);
        damagePerShot = 90 ;    // nds
        bitmapUnarmed = BitmapFactory.decodeResource(res, R.drawable.potatomineunarmed);
        bitmapArmed = BitmapFactory.decodeResource(res, R.drawable.potatominearmed);
        armed = false ;
        creationTime = System.currentTimeMillis() ;
        // TODO: need to recycle bitmap?
    }

    @Override
    void Draw(Canvas canvas , Paint p) {
        super.Draw(canvas,p);

        Bitmap bitmap = armed?bitmapArmed:bitmapUnarmed ;
        Rect src = new Rect() ;
        Rect dst = new Rect() ;
        src.set(0,0,bitmap.getWidth()-1,bitmap.getHeight()-1);
        dst.set(getX(), getY(), getX() + 92, getY() + 88);

        canvas.drawBitmap(bitmap, src,dst,p);
    }

    @Override
    void Move()
    {
        boolean exploded = false ;
        if (!armed && ((System.currentTimeMillis()-creationTime)>armingInterval)) {
            armed = true ;
        }

        if (armed) {
            for (MajorObject o : Game.getMajors()) {
                if (!o.isPlant()) {
                    Zombie zombie = (Zombie) o;
                    int zombieX = (zombie.getX() / 100) * 100;
                    int zombieY = (zombie.getY() / 100) * 100;
                    // kill zombies in this square ;
                    if (zombieX == getX() && zombieY == getY()) {
                        exploded = true;
                        zombie.setLife(zombie.getLife() - damagePerShot);
                        if (zombie.getLife() <= 0) {
                            Game.removeZombie(zombie);
                        }
                    }
                }
            }
            if (exploded) {
                Game.removePlant(this);
            }
        }
    }

}
