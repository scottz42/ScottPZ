package xyz.scottz.scottpz;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by lei on 2017/6/11.
 */

public class Wallnut extends Plant {
    private Bitmap bitmap ;
    Resources res ;

    private static long rechargeTime = 20000 ;

    public static long getRechargeTime()
    {
        return rechargeTime ;
    }

    Wallnut(Resources res)
    {
        super(res);
        this.res = res ;
        setLife(40);
        bitmap = BitmapFactory.decodeResource(res, R.drawable.wallnut);
        // TODO: need to recycle bitmap?
    }

    @Override
    void Draw(Canvas canvas , Paint p) {
        super.Draw(canvas,p);

        Rect src = new Rect() ;
        Rect dst = new Rect() ;
        src.set(0,0,bitmap.getWidth()-1,bitmap.getHeight()-1);
        dst.set(getX(), getY(), getX() + 92, getY() + 88);

        canvas.drawBitmap(bitmap, src,dst,p);


    }

}
