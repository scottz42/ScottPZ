package xyz.scottz.scottpz;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by lei on 2017/5/8.
 * class for normal pea
 * TODO: one class for each plant or a generic plant class to handle all plants?
 * generic class will need to include choices for different types of behavior like lobbing etc.
 * one for each plant would make necessitate new class each time a new plant is added, can not add just through data resources
 */

public class NormalPea extends Plant {
    private Bitmap bitmap ;
    private PeaShot peaShot ;
    private long lastPeashotTime ;
    private int TimePerPeashotMove = 100 ;  // ms
    private int DistancePerPeashotMove = 40 ;
    Resources res ;

    NormalPea(Resources res)
    {
        super(res);
        this.res = res ;
       bitmap = BitmapFactory.decodeResource(res, R.drawable.pea1);
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

        // pea shot ;
        if (peaShot!=null) {
            peaShot.Draw(canvas, p);
        }

    }


    void checkZombieHit(Zombie zombie)
    {
       // if hits zombie reduce zombie's life if life<=0 remove zombie
        // if ()
    }


    @Override
    void Move()
    {
        // shoot pea
        Zombie zombie = (Zombie)Game.ExistZombieInFront(getX()/100 , getY()/100) ;
        if (zombie!=null) {
            if (peaShot==null) {
                peaShot = new PeaShot(res , getX()/100 , getY()/100) ;
                lastPeashotTime = System.currentTimeMillis() ;
                checkZombieHit(zombie);
            } else {
                if ((System.currentTimeMillis()-lastPeashotTime)>TimePerPeashotMove) {
                    peaShot.setX(peaShot.getX() + DistancePerPeashotMove) ;
                    lastPeashotTime += TimePerPeashotMove ;
                    checkZombieHit(zombie);
                }
            }
        }
    }

}
