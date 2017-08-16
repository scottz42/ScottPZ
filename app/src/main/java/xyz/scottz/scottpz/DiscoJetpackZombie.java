package xyz.scottz.scottpz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by scott on 8/15/2017.
 */

public class DiscoJetpackZombie extends Zombie {
    private static Bitmap bitmap=null ;

    private boolean boosted=false;

    DiscoJetpackZombie()
    {
        super();
        life = 15 ;
        flying=true;
        if (bitmap==null) {
            bitmap = BitmapFactory.decodeResource(Game.getResources(), R.drawable.discojetpackzombie);
        }
    }

    @Override
    void Draw(Canvas canvas , Paint p) {
        super.Draw(canvas,p);

        Rect src = new Rect() ;
        Rect dst = new Rect() ;
        src.set(0,0,bitmap.getWidth()-1,bitmap.getHeight()-1);
        // TODO: GridLogic
        dst.set(getX(), getY(), getX() + (shrunk?GridLogic.getZombieWidth()/2:GridLogic.getZombieWidth()),
                getY() + (shrunk?GridLogic.getZombieHeight()/2:GridLogic.getZombieHeight()));

        if (shrunk) {
            dst.set(getX() + GridLogic.getZombieWidth() / 2,
                    getY() + GridLogic.getZombieHeight() / 2, getX() + GridLogic.getZombieWidth(), getY() + GridLogic.getZombieHeight());
        } else {
            dst.set(getX(), getY(), getX() + GridLogic.getZombieWidth(),
                    getY() + GridLogic.getZombieHeight());
        }

        // fly a little bit higher or much higher if boosted; lower if attacking plant
        // TODO: GridLogic
        if (LastAttackTime==0) {
            dst.offset(0, -(boosted ? GridLogic.getZombieHeight() : 20));
        }

        canvas.drawBitmap(bitmap, src,dst,p);
    }

    @Override
    void Move() {
        super.Move();

        boosted = (Game.findPlant(getX(),getY(),false))!=null;
    }
}
