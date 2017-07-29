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
    private static Bitmap bitmap=null ;
    private static Bitmap fireBitmap=null;
    private boolean onFire = false;

    PeaShot (int x, int y)
    {
        super(Game.getResources());
        onFire = false;
        setX(x) ;
        setY(y) ;
        if (bitmap==null) {
            bitmap = BitmapFactory.decodeResource(Game.getResources(), R.drawable.peashot);
            fireBitmap =BitmapFactory.decodeResource(Game.getResources(), R.drawable.firepea2);
        }
    }

    @Override
    public void onDraw(Canvas canvas , Paint p)
    {
        Bitmap bm = onFire?fireBitmap:bitmap;
        super.onDraw(canvas,p);

        Rect src = new Rect() ;
        Rect dst = new Rect() ;
        src.set(0,0,bm.getWidth()-1,bm.getHeight()-1);
        dst.set(getX() , getY() , getX()+GridLogic.getPeaWidth(), getY()+GridLogic.getPeaHeight());

        canvas.drawBitmap(bm, src,dst,p);
    }

    public boolean isOnFire() {
        return onFire;
    }

    public void setOnFire(boolean onFire) {
        this.onFire = onFire;
    }
}
