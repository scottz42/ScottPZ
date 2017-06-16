package xyz.scottz.scottpz;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

/**
 * Created by lei on 2017/6/16.
 */

public class Shovel extends MinorObject {
    Bitmap bitmap ;
    boolean shovelMode = false ;

    public boolean isShovelMode() {
        return shovelMode;
    }

    public void setShovelMode(boolean shovelMode) {
        this.shovelMode = shovelMode;
    }

    Shovel (Resources res , int x , int y)
    {
        super(res) ;
        setX(x) ;
        setY(y) ;
        bitmap = BitmapFactory.decodeResource(res, R.drawable.shovel);
    }

    @Override
    void Draw(Canvas c, Paint p) {
        super.Draw(c, p);

        Rect src = new Rect() ;
        Rect dst = new Rect() ;
        src.set(0,0,bitmap.getWidth()-1,bitmap.getHeight()-1);
        dst.set(getX() , getY() , getX()+83, getY()+ 83);

        c.drawBitmap(bitmap, src,dst,p);
    }

    // true if handled
    boolean onTouch(MotionEvent event)
    {
        int x = (int) event.getX() ;
        int y = (int) event.getY() ;

        if (x>1000 & x<1083 && y>600 && y<683) {
            setShovelMode(true) ;
            return true ;
        } else {
            return false ;
        }
    }
}
