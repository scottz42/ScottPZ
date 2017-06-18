package xyz.scottz.scottpz;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by lei on 2017/6/8.
 */

public class MinorObject {
    protected int x;
    protected int y ;

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


    public MinorObject(Resources res) {
    }

    void onDraw(Canvas c, Paint p){}

    void Move(){}
}
