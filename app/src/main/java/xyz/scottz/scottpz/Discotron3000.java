package xyz.scottz.scottpz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by scott on 8/15/2017.
 */

public class Discotron3000 extends Zombie {
    private static Bitmap bitmap=null ;
    private long LastGenerateTime =0;
    private long TimePerGenerate = 15000;   // ms


    Discotron3000()
    {
        super();
        life = 120 ;
        LastGenerateTime = 0;
        if (bitmap==null) {
            bitmap = BitmapFactory.decodeResource(Game.getResources(), R.drawable.discotron3000);
        }
    }

    @Override
    void Draw(Canvas canvas , Paint p) {
        super.Draw(canvas,p);

        Rect src = new Rect() ;
        Rect dst = new Rect() ;
        src.set(0,0,bitmap.getWidth()-1,bitmap.getHeight()-1);
        dst.set(getX(), getY(), getX() + (shrunk?GridLogic.getZombieWidth()/2:GridLogic.getZombieWidth()),
                getY() + (shrunk?GridLogic.getZombieHeight()/2:GridLogic.getZombieHeight()));

        canvas.drawBitmap(bitmap, src,dst,p);
    }

    @Override
    void Move()
    {
        super.Move();

        // summons 4 disco jetpack zombies

        if (LastGenerateTime==0) {
            LastGenerateTime = System.currentTimeMillis();
        }

        if ((System.currentTimeMillis() - LastGenerateTime) > TimePerGenerate) {

            // TODO: GridLogic
            DiscoJetpackZombie zombie ;
            zombie = new DiscoJetpackZombie();
            zombie.setX(getX());
            zombie.setY(getY());
            Game.addZombie(zombie);
            zombie = new DiscoJetpackZombie();
            zombie.setX(getX());
            zombie.setY(getY()+GridLogic.getZombieHeight());
            Game.addZombie(zombie);
            zombie = new DiscoJetpackZombie();
            zombie.setX(getX()+GridLogic.getZombieWidth());
            zombie.setY(getY());
            Game.addZombie(zombie);
            zombie = new DiscoJetpackZombie();
            zombie.setX(getX()+GridLogic.getZombieWidth());
            zombie.setY(getY()+GridLogic.getZombieHeight());
            Game.addZombie(zombie);
            LastGenerateTime += TimePerGenerate;}
        }

}
