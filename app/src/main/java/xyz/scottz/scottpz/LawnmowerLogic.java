package xyz.scottz.scottpz;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

/**
 * Created by lei on 2017/6/18.
 */

public class LawnmowerLogic extends Logic {
    static boolean hasLawnmower = true ;
    static LawnMower mowers[] = {null , null , null , null , null} ;
    static LawnMower movingMower = null ;

    @Override
    public void init() {
        super.init();

        // initialize lawn mowers
        if (hasLawnmower) {
            for (int i=0 ; i<mowers.length ; i++) {
                mowers[i] = new LawnMower(Game.getResources() , GridLogic.getMowerX(i) , GridLogic.getMowerY(i)) ;
            }
            movingMower = null;
        }
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public boolean onTimer()
    {
        if (!Game.isNormalPlay()) return false;
        if (movingMower!=null) {
            movingMower.move() ;
            // TODO: GridLogic
            if (movingMower.getX()>1100) movingMower = null ;
        }
        return movingMower!=null;
    }

    @Override
    public void onDraw(Canvas canvas, Paint paint)
    {
        super.onDraw(canvas, paint);

        // draw mowers
        for (int i=0 ; i<mowers.length ; i++) {
            if (mowers[i]!=null) mowers[i].onDraw(canvas , paint);
        }

        // moving mower
        if (movingMower!=null) {
            movingMower.onDraw(canvas, paint);
        }
    }

    public boolean hasMovingMower()
    {
        return movingMower!=null ;
    }

    // check to see if there is lawnmower in that lane to destroy zombies
    // return true if there is a lawnmower, false if lost
    static boolean checkLawnmower(MajorObject o)
    {
        Zombie zombie = (Zombie) o ;
        int row = GridLogic.calcRow(zombie.getY())  ;
        if (hasLawnmower && mowers[row]!=null) {
            movingMower = mowers[row] ;
            movingMower.setLastMowerMoveTime(System.currentTimeMillis());
            mowers[row] = null ;
            return true ;
        } else {
            return false ;
        }
    }

}
