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
    private Bitmap bitmap;

    public ConeheadZombie(Resources res) {
        super(res);
        bitmap = BitmapFactory.decodeResource(res, R.drawable.coneheadzombie);
        // TODO: need to recycle bitmap?
        // TODO: test only
        TimePerStep = 500;
        life = 20 ;
    }

    @Override
    void Draw(Canvas canvas, Paint p) {
        super.Draw(canvas, p);

        Rect src = new Rect();
        Rect dst = new Rect();
        src.set(0, 0, bitmap.getWidth() - 1, bitmap.getHeight() - 1);
        dst.set(getX(), getY(), getX() + 92, getY() + 88);

        canvas.drawBitmap(bitmap, src, dst, p);
    }
}

