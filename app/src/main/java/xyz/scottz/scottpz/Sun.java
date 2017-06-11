package xyz.scottz.scottpz;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by lei on 2017/6/8.
 */

public class Sun extends MinorObject {
    private Bitmap bitmap ;
    private long createTime ;
    private long duration = 5000 ;  // TODO: 10s or so

    Sun (Resources res , int x , int y)
    {
        super(res) ;
        setX(x) ;
        setY(y) ;
        bitmap = BitmapFactory.decodeResource(res, R.drawable.sun);
        createTime = System.currentTimeMillis();
    }

    void Draw(Canvas canvas , Paint p)
    {
        super.Draw(canvas,p);

        Rect src = new Rect() ;
        Rect dst = new Rect() ;
        src.set(0,0,bitmap.getWidth()-1,bitmap.getHeight()-1);
        dst.set(getX() , getY() , getX()+42, getY()+ 40);

        canvas.drawBitmap(bitmap, src,dst,p);

    }

    boolean isDead()
    {
        return ((System.currentTimeMillis()-createTime)>duration) ;
    }
}
