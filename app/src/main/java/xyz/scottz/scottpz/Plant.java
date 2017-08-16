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

   protected double damagePerShot ;   // nds

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
    void Draw(Canvas c ,Paint p) {
        super.Draw(c,p);
    }

    // plant image used for selection
    void drawSelect(Canvas c , Paint p) {}

    void onFinal() {}


    // to be overrriden in each plant, return true if no initial cd
    // default is true, currently only false for sun-producing plants
    public boolean initialCD() { return true; }
}
