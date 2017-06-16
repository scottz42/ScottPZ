package xyz.scottz.scottpz;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by lei on 2017/6/16.
 */

public class RaZombie extends Zombie {
    private Bitmap bitmap ;
    private int sunsStolen = 0 ;

    RaZombie(Resources res)
    {
        super(res);
        life = 9.25 ;
        bitmap = BitmapFactory.decodeResource(res, R.drawable.razombie);
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

    @Override
    void Move() {
        super() ;
        // TODO: falling sun, shoveled sun, sun bean effect

        int canSteal ;
        if (sunsStolen<250) {
            for (MajorObject o: Game.getMajors()) {
                canSteal = o.calcCanStealSun() ;
                if (canSteal>0) {
                    if (sunsStolen+canSteal>320) {
                        canSteal = 320 - sunsStolenlen ;
                    }
                    o.stealSun(canSteal) ;
                }
            }
            sunsStolen += canSteal ;
            if (sunsStolen>320) {
                sunsStolen = 320 ;
            }
    }

}
