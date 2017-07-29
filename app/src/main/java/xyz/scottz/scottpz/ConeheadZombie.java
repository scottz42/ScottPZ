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

public class ConeheadZombie extends Zombie {
    private static final double coneheadLife = 27.75 ;

    private static Bitmap bitmap=null;

    public ConeheadZombie() {
        super();
        if (bitmap==null) {
            bitmap = BitmapFactory.decodeResource(Game.getResources(), R.drawable.coneheadzombie2);
        }
        life = coneheadLife ;
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
