package xyz.scottz.scottpz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by lei on 2017/8/7.
 */

public class ImpPirate extends Zombie {
    private final static double impLife= 9.25 ;

    private static Bitmap bitmap;

    public ImpPirate() {
        super();
        DistancePerStep=27;     // hungry speed
        if (bitmap==null) {
            bitmap = BitmapFactory.decodeResource(Game.getResources(), R.drawable.imppirate);
        }
        life = impLife ;
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
