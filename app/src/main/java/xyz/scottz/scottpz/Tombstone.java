package xyz.scottz.scottpz;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by lei on 2017/6/18.
 */

// tombstones treated as a special type of zombie, main difference is plant can not be placed where tombstone is

public class Tombstone extends Zombie {
    Bitmap bitmap ;

    Tombstone (int row , int column)
    {
        super() ;
        Resources res = Game.getResources() ;
        // TODO: GridLogic
        setX(GridLogic.getXForCol(column)) ;
        setY(GridLogic.getZombieY(row)) ;
        life = 35 ;
        TimePerStep = 200000000 ;   // TODO: use special flag
        bitmap = BitmapFactory.decodeResource(res, R.drawable.tombstone);
    }

    @Override
    public boolean isTombstone()
    {
        return true;
    }

    @Override
    void Draw(Canvas c, Paint p) {
        super.Draw(c, p);

        Rect src = new Rect() ;
        Rect dst = new Rect() ;
        src.set(0,0,bitmap.getWidth()-1,bitmap.getHeight()-1);
        dst.set(getX() , getY() , getX()+100, getY()+ 100);

        c.drawBitmap(bitmap, src,dst,p);
    }


}
