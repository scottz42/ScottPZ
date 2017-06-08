package xyz.scottz.scottpz;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by lei on 2017/5/8.
 * base class for all plants
 */

public class Plant extends MajorObject {

    protected int x;
    protected int y ;
    private int life ;  // life left

    public Plant(Resources res)
    {
        super(res);
        life = 3 ;      // TODO: level-based, plant-specific
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
