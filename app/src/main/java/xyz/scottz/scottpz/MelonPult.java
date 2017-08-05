package xyz.scottz.scottpz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by lei on 2017/8/6.
 */

public class MelonPult extends Plant {
    private static Bitmap bitmap=null ;
    private static Bitmap selectBitmap=null;
    private Melon melon = null;

    // this controls generation of new peashot
    private long lastGenerationTime = 0 ;
    private int TimePerGeneration = 1500 ;  // 1.5s per shot

    // this controls movement of cabbage shot
    private long lastShotTime ;
    private int TimePerShotMove = 50 ;  // ms
    private int DistancePerShotMove = 40 ;   // TODO: GridLogic

    private static long rechargeTime = 5000 ;

    public static long getRechargeTime()
    {
        return rechargeTime ;
    }

    MelonPult() {
        super();
        setSunNeeded(325);
        rechargeTime = 5000;
        damagePerShot = 4;  // nds
        if (bitmap == null) {
            // TODO: change bitmap
            bitmap = BitmapFactory.decodeResource(Game.getResources(), R.drawable.melonpult);
            selectBitmap = BitmapFactory.decodeResource(Game.getResources(), R.drawable.melonpultslect);
        }
    }

    @Override
    void Draw(Canvas canvas , Paint p) {
        super.Draw(canvas,p);

        Rect src = new Rect() ;
        Rect dst = new Rect() ;
        src.set(0,0,bitmap.getWidth()-1,bitmap.getHeight()-1);
        dst.set(getX(), getY(), getX() + GridLogic.getPlantWidth(), getY() + GridLogic.getPlantHeight());

        canvas.drawBitmap(bitmap, src,dst,p);

        // cabbage shot
        if (melon!=null) {
            melon.onDraw(canvas, p);
        }

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



    void checkZombieHit(Zombie zombie)
    {
        // if out of range
        // TODO: GridLogic
        if (melon.getX()>1000) {
            melon = null ;
            return ;
        } else {
            if (zombie!=null) { // zombie still there
                int cabbageX = melon.getX() ;
                int zombieX = zombie.getX() ;
                int diff = zombieX-cabbageX ;
                // TODO: GridLogic
                if (diff<50 && diff>-50) {


                    int row = GridLogic.calcRow(melon.getY());
                    int col = GridLogic.calcCol(melon.getX());

                    for (MajorObject o : Game.getMajors()) {
                        if (!o.isPlant()) {
                            Zombie z = (Zombie) o;
                            // full damage for targeted zombie, half damage for other zombies in the 3x3 square
                            int zRow = GridLogic.calcRow(z.getY());
                            int zCol = GridLogic.calcCol(z.getX());
                            if (((zRow-row)>=-1 && (zRow-row)<=1) &&
                                    ((zCol-col)>=-1 && (zCol-col)<=1)) {
                                z.takeDamage(damagePerShot/2);
                            }
                        }
                    }
                    // already taken half-damage above
                    zombie.takeDamage(damagePerShot/2);
                    melon = null;
                }
            } else { // zombie killed by other plants?
                return ;
            }
        }

        // if hits zombie reduce zombie's life if life<=0 remove zombie & cabbage shot
        // if out of range remove peashot
    }

    // TODO: clean up
    static long total_t =500 ;

    @Override
    void Move()
    {

        // shoot cabbage
        Zombie zombie = (Zombie)Game.ExistZombieInFront(GridLogic.calcCol(getX()) , GridLogic.calcRow(getY())) ;

        if (melon==null) {
            if (zombie!=null) {
                if (lastGenerationTime==0 ||
                        (lastGenerationTime>0 && System.currentTimeMillis()-lastGenerationTime>TimePerGeneration))
                {
                    lastGenerationTime = System.currentTimeMillis();
                    // TODO: add some offset?
                    melon = new Melon(getX() , getY());
                    lastShotTime = System.currentTimeMillis();
                    total_t = (zombie.getX()-getX())*TimePerShotMove/DistancePerShotMove;
                    checkZombieHit(zombie);
                }
            }
        } else {
            if ((System.currentTimeMillis()-lastShotTime)>TimePerShotMove) {
                // TODO: update position
                melon.setX(melon.getX() + DistancePerShotMove) ;
                double v = 4.9;
                double t = ((double)(System.currentTimeMillis() - lastGenerationTime))/total_t;  // 0-1
                int  diff =  (int) ((v*t - 0.5*9.8*t*t)*200);     // parabola: t=0->0, t=0.5->1.225, t=1->0
                melon.setY(getY() - diff);
                lastShotTime += TimePerShotMove ;
                checkZombieHit(zombie);
            }

        }
    }

}