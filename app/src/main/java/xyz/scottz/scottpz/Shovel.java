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
    private static Bitmap bitmap=null ;
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
        if (bitmap==null) {
            bitmap = BitmapFactory.decodeResource(res, R.drawable.shovel);
        }
    }

    @Override
    void onDraw(Canvas c, Paint p) {
        super.onDraw(c, p);

        Rect src = new Rect() ;
        Rect dst = new Rect() ;
        src.set(0,0,bitmap.getWidth()-1,bitmap.getHeight()-1);
        dst.set(getX() , getY() , getX()+GridLogic.getShovelWidth(), getY()+ GridLogic.getShovelHeight());

        c.drawBitmap(bitmap, src,dst,p);
    }

    // true if handled
    boolean onTouch(MotionEvent event)
    {
        int x = (int) event.getX() ;
        int y = (int) event.getY() ;

        int shovelX = GridLogic.getShovelX();
        int shovelY = GridLogic.getShovelY();

        if (x>shovelX & x<shovelX+GridLogic.getShovelWidth() && y>shovelY && y<shovelY+GridLogic.getShovelHeight()) {
            setShovelMode(true) ;
            return true ;
        } else {
            return false ;
        }
    }
}
