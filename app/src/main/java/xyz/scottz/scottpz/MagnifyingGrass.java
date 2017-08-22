package xyz.scottz.scottpz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created by scott on 8/21/2017.
 */

public class MagnifyingGrass extends Plant {
    private static Bitmap bitmap=null ;
    private static Bitmap selectBitmap=null;
    
    private Light light=null;

    // this controls movement of light
    private long lastLightTime ;
    private int TimePerLightMove = 50 ;  // ms
    private int DistancePerLightMove = 60 ;   // TODO: GridLogic

    private static long rechargeTime = 5000 ;

    public static long getRechargeTime()
    {
        return rechargeTime ;
    }
    MagnifyingGrass()
    {
        setLife(3);
        damagePerShot = 27.75;
        if (bitmap==null) {
            bitmap = BitmapFactory.decodeResource(Game.getResources(), R.drawable.magnifyinggrass);
            selectBitmap = BitmapFactory.decodeResource(Game.getResources(), R.drawable.magnifyinggrassselect);
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

        if (light!=null) {
            light.onDraw(canvas , p);
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
        if (light.getX()>1000) {
            light = null ;
            return ;
        } else {
            if (zombie!=null) { // zombie still there
                int peaX = light.getX() ;
                int zombieX = zombie.getX() ;
                int diff = zombieX-peaX ;
                // TODO: GridLogic
                if (diff<50 && diff>-50) {
                    zombie.takeDamage(damagePerShot);
                    light = null ;    // light can only damage one zombie
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
        // shoot light
        Zombie zombie = (Zombie)Game.ExistZombieInFront(GridLogic.calcCol(getX()) , GridLogic.calcRow(getY())) ;

        // TODO: if no zombie still shoot?
        if (light==null) {
            if (zombie!=null) {
                if (isSelected() && SunLogic.getNoSun()>=50)
                {
                    // TODO: add some offset?
                    SunLogic.setNoSun(SunLogic.getNoSun()-50);
                    light = new Light(getX() , getY());
                    lastLightTime = System.currentTimeMillis();
                    checkZombieHit(zombie);
                }
            }
        } else {
            if ((System.currentTimeMillis()-lastLightTime)>TimePerLightMove) {
                light.setX(light.getX() + DistancePerLightMove) ;
                lastLightTime += TimePerLightMove ;
                checkZombieHit(zombie);
            }

        }

        if (isSelected()) {
            setSelected(false);
        }
    }
}
