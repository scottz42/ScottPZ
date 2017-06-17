package xyz.scottz.scottpz;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by lei on 2017/6/17.
 */


// lawn mower logic:
// state: global lawn mower flag
//  state: lawn mower presence for each lane
// if (zombie at leftmost && lawnmower[i])
//    lawnmower move from left to right and kill zombie in its lane
//    change presence to false



public class LawnMower extends MinorObject {

    // this controls movement of lawnmower
    private long lastMowerMoveTime ;
    private int TimePerMowerMove = 50 ;  // ms
    private int DistancePerMowerMove = 40 ;

    private Bitmap bitmap ;

    public long getLastMowerMoveTime() {
        return lastMowerMoveTime;
    }

    public void setLastMowerMoveTime(long lastMowerMoveTime) {
        this.lastMowerMoveTime = lastMowerMoveTime;
    }

    LawnMower (Resources res , int x , int y)
    {
        super(res);
        setX(x) ;
        setY(y) ;
        lastMowerMoveTime = -1 ;
        bitmap = BitmapFactory.decodeResource(res, R.drawable.lawnmower) ;
    }

    @Override
    public void Draw(Canvas canvas , Paint p)
    {
        super.Draw(canvas,p);

        Rect src = new Rect() ;
        Rect dst = new Rect() ;
        src.set(0,0,bitmap.getWidth()-1,bitmap.getHeight()-1);
        dst.set(getX() , getY() , getX()+185, getY()+131);

        canvas.drawBitmap(bitmap, src,dst,p);
    }

    void destroyZombies()
    {
        int mowerX = getX() ;
        int mowerY = getY() ;
        ArrayList toRemove = new ArrayList() ;
        for (MajorObject o: Game.getMajors()) {
            if (!o.isPlant() && mowerY==o.getY() && o.getX()<mowerX) {
                toRemove.add(o) ;
            }
        }
        Game.getMajors().removeAll(toRemove) ;
    }

    public void move() {
        if (lastMowerMoveTime != -1) {
            if ((System.currentTimeMillis() - lastMowerMoveTime) > TimePerMowerMove) {
                setX(getX() + DistancePerMowerMove);
                lastMowerMoveTime += TimePerMowerMove;
                destroyZombies();
            }
        }
    }
}
