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

    private static Bitmap bitmapUnarmed=null , bitmapArmed=null;
    private static Bitmap selectBitmap=null;
    private boolean armed ;
    private long armingInterval = 15000 ;   // ms
    long creationTime ;     // time when mine is planted

    private static long rechargeTime = 20000 ;

    public static long getRechargeTime()
    {
        return rechargeTime ;
    }

    PotatoMine()
    {
        super();
        setSunNeeded(25);
        damagePerShot = 90 ;    // nds
        if (bitmapArmed==null) {
            bitmapUnarmed = BitmapFactory.decodeResource(Game.getResources(), R.drawable.potatomineunarmed);
            bitmapArmed = BitmapFactory.decodeResource(Game.getResources(), R.drawable.potatominearmed);
            selectBitmap = BitmapFactory.decodeResource(Game.getResources(), R.drawable.potatominearmed);
        }
        armed = false ;
        creationTime = System.currentTimeMillis() ;
    }

    @Override
    void Draw(Canvas canvas , Paint p) {
        super.Draw(canvas,p);

        Bitmap bitmap = armed?bitmapArmed:bitmapUnarmed ;
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
        boolean exploded = false ;

        if (!armed && ((System.currentTimeMillis()-creationTime)>armingInterval)) {
            armed = true ;
        }

        if (armed) {
            for (MajorObject o : Game.getMajors()) {
                if (!o.isPlant()) {
                    Zombie zombie = (Zombie) o;
                    // kill zombies in this square ;
                    if (GridLogic.isZombieInPlantSquare(zombie , this)) {
                        exploded = true;
                        zombie.takeDamage(damagePerShot);
                    }
                }
            }
            if (exploded) {
                Game.removePlant(this);
            }
        }
    }

}
