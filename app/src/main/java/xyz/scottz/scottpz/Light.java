package xyz.scottz.scottpz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by scott on 8/21/2017.
 */

public class Light extends MinorObject {
    private static Bitmap bitmap=null ;

    Light (int x, int y)
    {
        super(Game.getResources());
        setX(x) ;
        setY(y) ;
        if (bitmap==null) {
            bitmap = BitmapFactory.decodeResource(Game.getResources(), R.drawable.plasma);
        }
    }

    @Override
    public void onDraw(Canvas canvas , Paint p)
    {
        super.onDraw(canvas,p);

        Rect src = new Rect() ;
        Rect dst = new Rect() ;
        src.set(0,0,bitmap.getWidth()-1,bitmap.getHeight()-1);
        dst.set(getX() , getY() , getX()+GridLogic.getPlasmaWidth(), getY()+GridLogic.getPlasmaHeight());

        canvas.drawBitmap(bitmap, src,dst,p);
    }
}
