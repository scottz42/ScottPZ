package xyz.scottz.scottpz;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

/**
 * Created by lei on 2017/5/8.
 * base class for plants and zombies and other major objects that show on screen
 */

// TODO: reduce bitmap memory usage: at least share bitmap for each type, also reduce original size of image

public class MajorObject {
    protected int x;
    protected int y ;

    public MajorObject(Resources res) {
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isPlant()
    {
        return false ;
    }

    public boolean canBite(int x , int y)
    {
        return false ;
    }

    void Draw(Canvas c, Paint p){}

    void Move(){}


    public void checkSun(MotionEvent event)
    {
    }

    // used for stealing sun from plant
    int calcCanStealSun() { return 0 ; }
    void stealSun(Zombie zombie , int noSun) {}
}
