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

public class BucketheadZombie extends Zombie {
    private final static int bucketheadLife = 65 ;


    private Bitmap bitmap;

    public BucketheadZombie(Resources res) {
        super(res);
        bitmap = BitmapFactory.decodeResource(res, R.drawable.bucketheadzombie);
        // TODO: need to recycle bitmap?
        life = bucketheadLife ;
    }

    @Override
    void Draw(Canvas canvas, Paint p) {
        super.Draw(canvas, p);

        Rect src = new Rect();
        Rect dst = new Rect();
        src.set(0, 0, bitmap.getWidth() - 1, bitmap.getHeight() - 1);
        dst.set(getX(), getY(), getX() + GridLogic.getZombieWidth(), getY() + GridLogic.getZombieHeight());

        canvas.drawBitmap(bitmap, src, dst, p);
    }

}
