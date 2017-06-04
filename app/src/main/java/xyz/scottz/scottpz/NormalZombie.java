package xyz.scottz.scottpz;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by lei on 2017/6/4.
 */

public class NormalZombie extends Zombie {
    private int x , y ;
    private Bitmap bitmap ;

    NormalZombie(Resources res)
    {
        super(res);
        // bitmap of pea
        //  getResources
        bitmap = BitmapFactory.decodeResource(res, R.drawable.normalzombie);
        // TODO: need to recycle bitmap?
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
    void Draw(Canvas canvas , Paint p) {
        super.Draw(canvas,p);

        Rect src = new Rect() ;
        Rect dst = new Rect() ;
        src.set(0,0,bitmap.getWidth()-1,bitmap.getHeight()-1);
        dst.set(x, y, x + 92, y + 88);

        canvas.drawBitmap(bitmap, src,dst,p);
    }

}
