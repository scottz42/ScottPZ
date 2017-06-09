package xyz.scottz.scottpz;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by lei on 2017/5/8.
 * base class for all plants
 */

public class Plant extends MajorObject {

    private int life ;  // life left

    public Plant(Resources res)
    {
        super(res);
        life = 3 ;      // TODO: level-based, plant-specific
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

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }
}
