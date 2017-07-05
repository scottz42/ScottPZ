package xyz.scottz.scottpz;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import static android.R.attr.x;

/**
 * Created by lei on 2017/6/10.
 */

public class PeaShot extends MinorObject {
    private Bitmap bitmap ;

    PeaShot (Resources res , int column , int row)
    {
        super(res);
        // TODO: GridLogic
        setX(column*100) ;
        setY(row*100) ;
        bitmap = BitmapFactory.decodeResource(res, R.drawable.peashot) ;
    }

    @Override
    public void onDraw(Canvas canvas , Paint p)
    {
        super.onDraw(canvas,p);

        Rect src = new Rect() ;
        Rect dst = new Rect() ;
        src.set(0,0,bitmap.getWidth()-1,bitmap.getHeight()-1);
        dst.set(getX() , getY() , getX()+40, getY()+40);

        canvas.drawBitmap(bitmap, src,dst,p);
    }
}
