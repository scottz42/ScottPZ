package xyz.scottz.scottpz;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by lei on 2017/7/6.
 */

public class Torchwood extends Plant {
    private static Bitmap bitmap = null;
    private static Bitmap selectBitmap = null;

    private static long rechargeTime = 20000 ;

    public static long getRechargeTime()
    {
        return rechargeTime ;
    }

    Torchwood()
    {
        setLife(5);
        setSunNeeded(175);
        if (bitmap==null) {
            bitmap = BitmapFactory.decodeResource(Game.getResources(), R.drawable.torchwood);
            selectBitmap = BitmapFactory.decodeResource(Game.getResources(), R.drawable.torchwoodselect);
        }
    }

    @Override
    void Draw(Canvas canvas , Paint p) {
        super.Draw(canvas,p);

        Rect src = new Rect() ;
        Rect dst = new Rect() ;
        src.set(0,0,bitmap.getWidth()-1,bitmap.getHeight()-1);
        dst.set(getX(), getY(), getX() + GridLogic.getPlantWidth(), getY() + GridLogic.getPlantHeight());

        canvas.drawBitmap(bitmap, src,dst,p);
    }

    @Override
    void drawSelect(Canvas canvas , Paint p) {
        super.Draw(canvas,p);

        Rect src = new Rect() ;
        Rect dst = new Rect() ;
        src.set(0,0,selectBitmap.getWidth()-1,selectBitmap.getHeight()-1);
        dst.set(getX(), getY(), getX() + GridLogic.getSelectWidth(), getY() + GridLogic.getSelectHeight());

        canvas.drawBitmap(selectBitmap, src,dst,p);
    }


}
