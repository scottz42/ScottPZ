package xyz.scottz.scottpz;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

/**
 * Created by lei on 2017/5/8.
 * base class for all plants
 */

public class Plant extends MajorObject {

    private int life ;  // life left
    protected int damagePerShot ;   // nds

    public int getSunNeeded() {
        return sunNeeded;
    }

    public void setSunNeeded(int sunNeeded) {
        this.sunNeeded = sunNeeded;
    }

    protected int sunNeeded ;


    public Plant()
    {
        super();
        life = 3 ;      // TODO: level-based, plant-specific
        damagePerShot = 1 ;
        sunNeeded = 50 ;
    }

    @Override
    public boolean isPlant()
    {
        return true ;
    }

    @Override
    public boolean canBite (int x, int y)
    {
        int diff_x = x - this.getX();
        return (this.getY() ==y) && (diff_x<100) && (diff_x>70) ;
    }

    @Override
    void Draw(Canvas c ,Paint p) {
        super.Draw(c,p);
    }

    void onFinal() {}


    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }


}
