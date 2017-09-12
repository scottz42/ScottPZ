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
    private static Bitmap bitmap[]=null ;

    NormalZombie()
    {
        super();
        life = 10 ;
        if (bitmap==null) {
            bitmap = new Bitmap[3];
            bitmap[0] = BitmapFactory.decodeResource(Game.getResources(), R.drawable.normalzombief1);
            bitmap[1] = BitmapFactory.decodeResource(Game.getResources(), R.drawable.normalzombief2);
            bitmap[2] = BitmapFactory.decodeResource(Game.getResources(), R.drawable.normalzombief3);
        }
    }

    @Override
    void Draw(Canvas canvas , Paint p) {
        super.Draw(canvas,p);

        // pick right animation
        // TODO: always start with frame 0
        Bitmap bm = bitmap[((int)(System.currentTimeMillis()/TimePerStep))%3];

        Rect src = new Rect() ;
        Rect dst = new Rect() ;
        src.set(0,0,bm.getWidth()-1,bm.getHeight()-1);
        dst.set(getX(), getY(), getX() + (shrunk?GridLogic.getZombieWidth()/2:GridLogic.getZombieWidth()),
                getY() + (shrunk?GridLogic.getZombieHeight()/2:GridLogic.getZombieHeight()));

        canvas.drawBitmap(bm, src,dst,p);
    }

}
