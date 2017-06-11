package xyz.scottz.scottpz;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

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

    // this controls generation of new peashot
    private long lastGenerationTime = 0 ;
    private int TimePerGeneration = 1500 ;  // 1.5s per shot

    // this controls movement of peashot
    private long lastPeashotTime ;
    private int TimePerPeashotMove = 50 ;  // ms
    private int DistancePerPeashotMove = 40 ;

    Resources res ;

    NormalPea(Resources res)
    {
        super(res);
        this.res = res ;
        setSunNeeded(100);
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
        // if out of range
        if (peaShot.getX()>1000) {
            peaShot = null ;
            return ;
        } else {
            if (zombie!=null) { // zombie still there
                int peaX = peaShot.getX() ;
                int zombieX = zombie.getX() ;
                int diff = zombieX-peaX ;
                if (diff<50 && diff>-50) {
                    peaShot = null ;    // pea shot can only damage one zombie
                    // TODO: damage per shot
                    zombie.setLife(zombie.getLife()-damagePerShot) ;
                    if (zombie.getLife()<=0) {
                        Game.removeZombie(zombie) ;
                    }
                }
            } else { // zombie killed by other plants?
                return ;
            }
        }

       // if hits zombie reduce zombie's life if life<=0 remove zombie & peashot
        // if out of range remove peashot
    }


    @Override
    void Move()
    {
        // shoot pea
        Zombie zombie = (Zombie)Game.ExistZombieInFront(getX()/100 , getY()/100) ;

        if (peaShot==null) {
            if (zombie!=null) {
                if (lastGenerationTime==0 ||
                        (lastGenerationTime>0 && System.currentTimeMillis()-lastGenerationTime>TimePerGeneration))
                {
                    lastGenerationTime = System.currentTimeMillis();
                    String msg ;
                    msg = String.format("lastGenerationTime=%d" , lastGenerationTime) ;
                    Log.d(null , msg) ;
                    peaShot = new PeaShot(res, getX() / 100, getY() / 100);
                    lastPeashotTime = System.currentTimeMillis();
                    checkZombieHit(zombie);
               }
            }
        } else {
            if ((System.currentTimeMillis()-lastPeashotTime)>TimePerPeashotMove) {
                peaShot.setX(peaShot.getX() + DistancePerPeashotMove) ;
                lastPeashotTime += TimePerPeashotMove ;
                checkZombieHit(zombie);
            }

        }
    }

}
