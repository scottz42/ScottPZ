package xyz.scottz.scottpz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by scott on 8/16/2017.
 */

public class BreakdancerZombie extends Zombie {
    private static Bitmap bitmap=null ;
    private static Bitmap spinningBitmap=null;
    private long LastKickTime =0;
    private long spinTime = 1000;   // ms; spinning time
    private long kickingDuration=1500;  // ms; total time between kicks


    BreakdancerZombie()
    {
        super();
        life = 12 ;
        LastKickTime = 0;
        if (bitmap==null) {
            bitmap = BitmapFactory.decodeResource(Game.getResources(), R.drawable.breakdancerzombie);
            spinningBitmap = BitmapFactory.decodeResource(Game.getResources(), R.drawable.spinningbreakdancer);
        }
    }

    @Override
    void Draw(Canvas canvas , Paint p) {
        super.Draw(canvas,p);

        Bitmap bm = (LastKickTime>0 && (System.currentTimeMillis()-LastKickTime)<spinTime)?spinningBitmap:bitmap;

        Rect src = new Rect() ;
        Rect dst = new Rect() ;
        src.set(0,0,bm.getWidth()-1,bm.getHeight()-1);
        dst.set(getX(), getY(), getX() + (shrunk?GridLogic.getZombieWidth()/2:GridLogic.getZombieWidth()),
                getY() + (shrunk?GridLogic.getZombieHeight()/2:GridLogic.getZombieHeight()));

        canvas.drawBitmap(bm, src,dst,p);
    }

    @Override
    void Move()
    {
        super.Move();

        // kick zombie

        if ((LastKickTime==0) || ((System.currentTimeMillis() - LastKickTime) > kickingDuration)) {

            int zRow = GridLogic.calcRow(getY());
            int zCol = GridLogic.calcCol(getX());

            for (MajorObject o: Game.getMajors()) {
                if (!o.isPlant() && (o.getY()==getY() &&
                        ((getX()-o.getX())<80 && (getX()-o.getX())>50))) {
                    // TODO: make it flying a short while
                    // TODO: can not kick frozen zombie
                    o.setX(o.getX()-200);
                    LastKickTime = System.currentTimeMillis();
                }
            }
        }
    }
}
