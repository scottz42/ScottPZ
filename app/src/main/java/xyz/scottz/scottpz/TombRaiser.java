package xyz.scottz.scottpz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by lei on 2017/8/6.
 */

public class TombRaiser extends Zombie {
    private static Bitmap bitmap=null ;
    private long LastGenerateTime =0;
    private long TimePerGenerate = 5000;   // ms


    TombRaiser()
    {
        super();
        life = 25.25 ;
        LastGenerateTime = 0;
        if (bitmap==null) {
            bitmap = BitmapFactory.decodeResource(Game.getResources(), R.drawable.tombraiser);
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

        // / create new tombstones
        if (LastGenerateTime==0) {
            LastGenerateTime = System.currentTimeMillis();
        }

        if ((System.currentTimeMillis() - LastGenerateTime) > TimePerGenerate) {

        int zRow = GridLogic.calcRow(getY());
        int zCol = GridLogic.calcCol(getX());

        int row = (int) (Math.random()*GridLogic.getNoRows());
        int col = (int) (Math.random()*zCol);

        if (Game.canPlant(row , col)) {
            Tombstone stone = new Tombstone(row , col);
            Game.addZombie(stone);
            LastGenerateTime += TimePerGenerate;}
        }
    }
}
