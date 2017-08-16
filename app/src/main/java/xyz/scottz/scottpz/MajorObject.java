package xyz.scottz.scottpz;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

/**
 * Created by lei on 2017/5/8.
 * base class for plants and zombies and other major objects that show on screen
 */

public class MajorObject {
    protected int x;
    protected int y ;

    protected double life ;  // life left

    public MajorObject() {
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


    public double getLife() {
        return life;
    }

    public void setLife(double life) {
        this.life = life;
    }


    public boolean isPlant()
    {
        return false ;
    }

    public boolean isTombstone() { return false ; }

    // can block flying zombies
    // override in appropriate plants
    public boolean blocksFlying()
    {
        return false;
    }


    public boolean canBite (int x, int y , boolean hyptonized)
    {
        // TODO: GridLogic
        int diff_x = x - this.getX();
        return hyptonized?((this.getY() ==y) && (diff_x<-50) && (diff_x>-80)):((this.getY() ==y) && (diff_x<80) && (diff_x>50)) ;
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
