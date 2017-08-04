package xyz.scottz.scottpz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created by lei on 2017/8/4.
 */

public class LaserBean extends Plant {

        private static Bitmap bitmap=null ;
        private static Bitmap selectBitmap=null;


        // this controls generation of new laser
        private long lastGenerationTime = 0 ;
        private int TimePerGeneration = 3000 ;  // 3s per shot
        private int laserDuration = 500; // show 0.5s
        private boolean bShooting = false ;    // true during the 0.5s

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

        if (bShooting) {
            DrawLaser(canvas , p);
        }
    }

    // draw laser beam from laser bean
    void DrawLaser(Canvas canvas , Paint paint)
    {
        int left = x + GridLogic.getPlantWidth()/2;
        int top = y + GridLogic.getPlantHeight()/2;
        int right = GridLogic.getGridRight();
        paint.setColor(Color.WHITE);
        canvas.drawRect(left , top-2, right, top+2, paint);
        paint.setColor(Color.BLUE);
        canvas.drawRect(left , top-3,right,top-2,paint);
        canvas.drawRect(left,top+2,right,top+3,paint);
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


    @Override
    void Move()
    {
        Zombie zombie = (Zombie)Game.ExistZombieInFront(GridLogic.calcCol(getX()) , GridLogic.calcRow(getY())) ;

        if (bShooting==false) {
            if (zombie!=null) {
                if (lastGenerationTime==0 ||
                        (lastGenerationTime>0 && System.currentTimeMillis()-lastGenerationTime>TimePerGeneration))
                {
                    lastGenerationTime = System.currentTimeMillis();
                    bShooting = true;
                }
            }
        } else {
            if ((System.currentTimeMillis()-lastGenerationTime)>laserDuration) {
                // deal damage to every plant in front
                GridLogic.doDamageInFront( GridLogic.calcRow(getY()),GridLogic.calcCol(getX()) , damagePerShot);
                bShooting = false;
            }
        }
    }
}

