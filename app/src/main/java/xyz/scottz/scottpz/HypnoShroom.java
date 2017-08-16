package xyz.scottz.scottpz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by scott on 8/14/2017.
 */

public class HypnoShroom extends Plant {
    private static Bitmap bitmap=null ;
    private static Bitmap selectBitmap=null;

    private static long rechargeTime = 10000 ;

    public static long getRechargeTime()
    {
        return rechargeTime ;
    }

   HypnoShroom()
    {
        super();
        setSunNeeded(125);
        setLife(30);
        damagePerShot = 0 ;
        if (bitmap==null) {
            bitmap = BitmapFactory.decodeResource(Game.getResources(), R.drawable.hypnoshroom);
            selectBitmap = BitmapFactory.decodeResource(Game.getResources(), R.drawable.hypnoshroomselect);
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
        for (MajorObject o : Game.getMajors()) {
            if (!o.isPlant() && !o.isTombstone()) {
                Zombie zombie = (Zombie) o;
                // hypnotize one zombie in this square ;
                if (GridLogic.isZombieInPlantSquare(zombie , this)) {
                    Game.removePlant(this);
                    zombie.setHypnotized();
                    return;
                }
            }
        }
    }


}
