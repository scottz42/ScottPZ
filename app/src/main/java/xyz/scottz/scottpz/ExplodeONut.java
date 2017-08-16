package xyz.scottz.scottpz;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by lei on 2017/7/2.
 */

public class ExplodeONut extends Plant {
    private static Bitmap bitmap=null ;
    private static Bitmap selectBitmap=null;

    private static long rechargeTime = 10000 ;

    public static long getRechargeTime()
    {
        return rechargeTime ;
    }

    ExplodeONut()
    {
        super();
        setLife(30);
        damagePerShot = 90;
        if (bitmap==null) {
            bitmap = BitmapFactory.decodeResource(Game.getResources(), R.drawable.explodeonutcostume);
            selectBitmap = BitmapFactory.decodeResource(Game.getResources(), R.drawable.explodeonutcostume);
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
    void onFinal()
    {
        int row = GridLogic.calcRow(getY());
        int col = GridLogic.calcCol(getX());

        for (MajorObject o : Game.getMajors()) {
            if (!o.isPlant()) {
                Zombie zombie = (Zombie) o;
                int zombieRow = GridLogic.calcRow(zombie.getY());
                int zombieCol = GridLogic.calcCol(zombie.getX());

                // kill zombies in 3*3 square ;
                if ((zombieRow>=row-1 && zombieRow<=row+1) && (zombieCol>=col-1 && zombieCol<=col+1)) {
                    zombie.takeDamage(damagePerShot);
                }
            }
        }
    }

    @Override
    public boolean blocksFlying()
    {
        return true;
    }
}
