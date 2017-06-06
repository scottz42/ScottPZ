package xyz.scottz.scottpz;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by lei on 2017/5/8.
 * class for normal pea
 * TODO: one class for each plant or a generic plant class to handle all plants?
 * generic class will need to include choices for different types of behavior like lobbing etc.
 * one for each plant would make necessitate new class each time a new plant is added, can not add just through data resources
 */

public class NormalPea extends Plant {
    private Bitmap bitmap ;

    NormalPea(Resources res)
    {
        super(res);
        // bitmap of pea
      //  getResources
       bitmap = BitmapFactory.decodeResource(res, R.drawable.pea1);
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