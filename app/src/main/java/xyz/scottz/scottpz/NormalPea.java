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
    private static Bitmap bitmap=null ;
    private static Bitmap selectBitmap=null;

    private PeaShot peaShot = null;

    // this controls generation of new peashot
    private long lastGenerationTime = 0 ;
    private int TimePerGeneration = 1500 ;  // 1.5s per shot

    // this controls movement of peashot
    private long lastPeashotTime ;
    private int TimePerPeashotMove = 50 ;  // ms
    private int DistancePerPeashotMove = 40 ;   // TODO: GridLogic


    private static long rechargeTime = 5000 ;

    public static long getRechargeTime()
    {
        return rechargeTime ;
    }

    NormalPea() {
        super();
        setSunNeeded(100);
        rechargeTime = 5000;
        damagePerShot = 1;  // nds
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(Game.getResources(), R.drawable.pea1);
            selectBitmap = BitmapFactory.decodeResource(Game.getResources(), R.drawable.pea1);
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

        // pea shot ;
        if (peaShot!=null) {
            peaShot.onDraw(canvas, p);
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

        // TODO: check for torchwood,
        // if torchwood is in square that pea is in, set pea on fire
        Plant plant = Game.existPlant(GridLogic.calcCol(peaShot.getX()),GridLogic.calcRow(peaShot.getY()));
        if (plant!=null && plant.getClass().getName().equals("xyz.scottz.scottpz.Torchwood")) {
            peaShot.setOnFire(true);
        }

        // if out of range
        // TODO: GridLogic
        if (peaShot.getX()>1000) {
            peaShot = null ;
            return ;
        } else {
            if (zombie!=null) { // zombie still there
                int peaX = peaShot.getX() ;
                int zombieX = zombie.getX() ;
                int diff = zombieX-peaX ;
                // TODO: GridLogic
                if (diff<50 && diff>-50) {
                    zombie.takeDamage(peaShot.isOnFire()?damagePerShot*2:damagePerShot);
                    peaShot = null ;    // pea shot can only damage one zombie
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
        /*
            if (Game.ExistZombieInFront()) {
            }

            // pea move itself & checkZombieHit()
            pea.Move();
         */

        // shoot pea
        Zombie zombie = (Zombie)Game.ExistZombieInFront(GridLogic.calcCol(getX()) , GridLogic.calcRow(getY())) ;

        if (peaShot==null) {
            if (zombie!=null) {
                if (lastGenerationTime==0 ||
                        (lastGenerationTime>0 && System.currentTimeMillis()-lastGenerationTime>TimePerGeneration))
                {
                    lastGenerationTime = System.currentTimeMillis();
                    String msg ;
                    msg = String.format("lastGenerationTime=%d" , lastGenerationTime) ;
                    Log.d(null , msg) ;
                    // TODO: add some offset?
                    peaShot = new PeaShot(getX() , getY());
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
