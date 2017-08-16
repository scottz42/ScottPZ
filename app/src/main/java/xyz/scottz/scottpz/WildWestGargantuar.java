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
        DistancePerStep = 27; // hungry speed
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
        dst.set(getX(), getY()-110, getX() + (shrunk?75:150), getY() + (shrunk?45:90));
        canvas.drawBitmap(bitmap, src,dst,p);
    }


    @Override
        // TODO: use thread instead of timer model?
        // TODO: needs cleanup
    void Move()
    {
        super.Move();

        // throw imp
        if (!(startFreezeTime>0 && (System.currentTimeMillis()<startFreezeTime+freezeDuration)) && getLife()<=90 && !thrownImp) {
            thrownImp = true ;
            ImpPirate zombie = new ImpPirate();
            zombie.setY(getY());
            int zCol = GridLogic.calcCol(getX());
            int col = (int) (Math.random()*(zCol-1));
            zombie.setX(GridLogic.getXForCol(col));
            Game.addZombie(zombie);
        }
    }

}
