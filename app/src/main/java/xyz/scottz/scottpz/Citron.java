package xyz.scottz.scottpz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created by lei on 2017/8/5.
 */

public class Citron extends Plant {
    private static Bitmap bitmap=null ;
    private static Bitmap selectBitmap=null;

    private Plasma plasma = null;

    // this controls generation of new plasma
    private long lastGenerationTime = 0 ;
    private int TimePerGeneration = 10000 ;  // 10s per shot

    // this controls movement of plasma
    private long lastPlasmaTime ;
    private int TimePerPlasmaMove = 50 ;  // ms
    private int DistancePerPlasmaMove = 40 ;   // TODO: GridLogic


    private static long rechargeTime = 5000 ;

    public static long getRechargeTime()
    {
        return rechargeTime ;
    }

    Citron() {
        super();
        setSunNeeded(350);
        rechargeTime = 5000;
        damagePerShot = 40;  // nds
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(Game.getResources(), R.drawable.citron);
            selectBitmap = BitmapFactory.decodeResource(Game.getResources(), R.drawable.citronselect);
            // TODO: need to recycle bitmap?
        }
    }


    // move to Plant?
    @Override
    void Draw(Canvas canvas , Paint p) {
        super.Draw(canvas,p);

        Rect src = new Rect() ;
        Rect dst = new Rect() ;
        src.set(0,0,bitmap.getWidth()-1,bitmap.getHeight()-1);
        dst.set(getX(), getY(), getX() + GridLogic.getPlantWidth(), getY() + GridLogic.getPlantHeight());

        canvas.drawBitmap(bitmap, src,dst,p);

        // plasma
        if (plasma!=null) {
            plasma.onDraw(canvas, p);
        }

    }


    // move to Plant?
    @Override
    void drawSelect(Canvas canvas , Paint p) {
        super.Draw(canvas,p);

        Rect src = new Rect() ;
        Rect dst = new Rect() ;
        src.set(0,0,selectBitmap.getWidth()-1,selectBitmap.getHeight()-1);
        dst.set(getX(), getY(), getX() + GridLogic.getSelectWidth(), getY() + GridLogic.getSelectHeight());

        canvas.drawBitmap(selectBitmap, src,dst,p);
    }



    void checkZombieHit(Zombie zombie)
    {

        // if out of range
        // TODO: GridLogic
        if (plasma.getX()>1000) {
            plasma = null ;
            return ;
        } else {
            if (zombie!=null) { // zombie still there
                int peaX = plasma.getX() ;
                int zombieX = zombie.getX() ;
                int diff = zombieX-peaX ;
                // TODO: GridLogic
                if (diff<50 && diff>-50) {
                    zombie.takeDamage(damagePerShot);
                    plasma = null ;    // pea shot can only damage one zombie
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
        Zombie zombie = (Zombie)Game.ExistZombieInFront(GridLogic.calcCol(getX()) , GridLogic.calcRow(getY())) ;

        if (plasma==null) {
            if (zombie!=null) {
                if (lastGenerationTime==0 ||
                        (lastGenerationTime>0 && System.currentTimeMillis()-lastGenerationTime>TimePerGeneration))
                {
                    lastGenerationTime = System.currentTimeMillis();
                    String msg ;
                    msg = String.format("lastGenerationTime=%d" , lastGenerationTime) ;
                    Log.d(null , msg) ;
                    // TODO: add some offset?
                    plasma = new Plasma(getX() , getY());
                    lastPlasmaTime = System.currentTimeMillis();
                    checkZombieHit(zombie);
                }
            }
        } else {
            if ((System.currentTimeMillis()-lastPlasmaTime)>TimePerPlasmaMove) {
                plasma.setX(plasma.getX() + DistancePerPlasmaMove) ;
                lastPlasmaTime += TimePerPlasmaMove ;
                checkZombieHit(zombie);
            }

        }
    }

}
