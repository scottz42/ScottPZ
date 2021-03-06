package xyz.scottz.scottpz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by lei on 2017/8/7.
 */

public class SnapDragon extends Plant {
    private static long rechargeTime = 5000 ;      // ms
    private static long generateTime = 1500 ;    // ms

    private long lastGenerateTime;
    private static Bitmap bitmap=null;
    private static Bitmap selectBitmap=null;

    public static long getRechargeTime()
    {
        return rechargeTime ;
    }

    SnapDragon()
    {
        super();
        setSunNeeded(150);
        damagePerShot = 1.5 ;    // nds
        if (bitmap==null) {
            bitmap = BitmapFactory.decodeResource(Game.getResources(), R.drawable.snapdragon);
            selectBitmap = BitmapFactory.decodeResource(Game.getResources(), R.drawable.snapdragonselect);
        }
        lastGenerateTime = 0;
    }

    @Override
    void Draw(Canvas canvas , Paint p) {
        super.Draw(canvas,p);

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
        if (lastGenerateTime==0 || (System.currentTimeMillis()-lastGenerateTime>generateTime)) {
            int row = GridLogic.calcRow(getY());
            int col = GridLogic.calcCol(getX());
            if (GridLogic.ExistZombieInFront2x3(col,row)!=null) {
                for (MajorObject o : Game.getMajors()) {
                    if (!o.isPlant()) {
                        Zombie zombie = (Zombie) o;
                        // hurt zombies in this 2*3 square
                        int zRow = GridLogic.calcRow(o.getY());
                        int zCol = GridLogic.calcCol(o.getX());
                        if (((zRow-row)>=-1 && (zRow-row)<=1) &&
                                ((zCol-col)>=1 && (zCol-col)<=2)) {
                            zombie.takeDamage(damagePerShot);
                        }
                    }
                }
                lastGenerateTime = System.currentTimeMillis();
            }
        }
    }


}
