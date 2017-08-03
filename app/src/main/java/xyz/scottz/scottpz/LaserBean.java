package xyz.scottz.scottpz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created by lei on 2017/8/4.
 */

public class LaserBean extends Plant {

        private static Bitmap bitmap=null ;
        private static Bitmap selectBitmap=null;

        private PeaShot peaShot = null;

        // this controls generation of new peashot
        private long lastGenerationTime = 0 ;
        private int TimePerGeneration = 3000 ;  // 1.5s per shot

        // this controls movement of peashot
        private long lastPeashotTime ;
        private int TimePerPeashotMove = 50;  // ms
        private int DistancePerPeashotMove = 40 ;   // TODO: GridLogic


        private static long rechargeTime = 5000 ;

    public static long getRechargeTime()
    {
        return rechargeTime ;
    }

    LaserBean() {
        super();
        setSunNeeded(200);
        rechargeTime = 5000;
        damagePerShot = 2;  // nds
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(Game.getResources(), R.drawable.laserbean);
            selectBitmap = BitmapFactory.decodeResource(Game.getResources(), R.drawable.laserbeanselect);
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


