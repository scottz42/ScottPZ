package xyz.scottz.scottpz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by lei on 2017/8/6.
 */

public class WildWestGargantuar extends Zombie {
    private static Bitmap bitmap=null ;
    private boolean thrownImp = false ;

    WildWestGargantuar()
    {
        super();
        life = 180 ;
        thrownImp = false;
        if (bitmap==null) {
            bitmap = BitmapFactory.decodeResource(Game.getResources(), R.drawable.wildwestgargantuar);
        }
    }

    @Override
    void Draw(Canvas canvas , Paint p) {
        super.Draw(canvas,p);

        Rect src = new Rect() ;
        Rect dst = new Rect() ;
        src.set(0,0,bitmap.getWidth()-1,bitmap.getHeight()-1);
        // TODO: GridLogic
        dst.set(getX(), getY()-110, getX() + 150, getY() + 90);
        canvas.drawBitmap(bitmap, src,dst,p);
    }


    @Override
        // TODO: use thread instead of timer model?
        // TODO: needs cleanup
    void Move()
    {
        // freeze for a certain duration
        if (startFreezeTime>0 && (System.currentTimeMillis()<startFreezeTime+freezeDuration)){
            return;
        }
        if (startFreezeTime>0 && (System.currentTimeMillis()>startFreezeTime+freezeDuration)){
            LastMoveTime += System.currentTimeMillis() - startFreezeTime ;  // continue moving
            startFreezeTime=0;
        }

        // throw imp
        if (getLife()<=90 && !thrownImp) {
            thrownImp = true ;
            // TODO: change to imp
            NormalZombie zombie = new NormalZombie();
            zombie.setY(getY());
            int zCol = GridLogic.calcCol(getX());
            int col = (int) (Math.random()*(zCol-1));
            zombie.setX(GridLogic.getXForCol(col));
            Game.addZombie(zombie);
        }

        // zombie eat plant ;
        Plant plant = Game.findPlant(getX(), getY());
        if (plant != null) {    // there is plant to eat
            if (LastAttackTime > 0) { // eating started
                if (System.currentTimeMillis() - LastAttackTime>TimePerAttack){  // finished one attack
                    plant.setLife(plant.getLife() - damagePerAttack);
                    // TODO: special plants that aren't killed instantly
                    Game.removePlant(plant);
                    DistancePerStep = prevDistance;
                    LastAttackTime = 0;
                } else {  // wait for this attack to finish
                }
            } else {  // start to eat
                LastAttackTime = System.currentTimeMillis();
                if (DistancePerStep != 0) {  // stop zombie movement
                    prevDistance = DistancePerStep;
                    DistancePerStep = 0;
                }
            }
        }

        // zombie movement
        if ((System.currentTimeMillis()-LastMoveTime)>TimePerStep) {
            x -= DistancePerStep ;
            // TODO: GridLogic
            if (x<0) {
                x = 1000 ;
                y = 100*((int)(Math.random()*4)+1);
            }
            if (x>1200) {
                x = 0;
            }
            LastMoveTime += TimePerStep ;
        }
    }

}
