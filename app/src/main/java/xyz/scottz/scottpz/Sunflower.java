package xyz.scottz.scottpz;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;

/**
 * Created by lei on 2017/6/8.
 */

public class Sunflower extends Plant {
    private Bitmap bitmap ;
    private Resources res ;
    private ArrayList<Sun> suns ;
    private long LastGenerateTime ;
    private long TimePerGenerate = 10000 ;   // TODO: actually 30s or so

    public Sunflower(Resources res) {
        super(res);
        this.res = res ;

        suns = new ArrayList<Sun>();
        // bitmap of pea
        //  getResources
        bitmap = BitmapFactory.decodeResource(res, R.drawable.sunflower);
        // TODO: need to recycle bitmap?

        LastGenerateTime = System.currentTimeMillis();
    }

    @Override
    void Move()
    {
        if ((System.currentTimeMillis()-LastGenerateTime)>TimePerGenerate) {
            Sun sun = new Sun(res , x , y) ;
            suns.add(sun) ;
            LastGenerateTime += TimePerGenerate ;
        }
    }


    @Override
    void Draw(Canvas canvas , Paint p) {
        super.Draw(canvas,p);

        Rect src = new Rect() ;
        Rect dst = new Rect() ;
        src.set(0,0,bitmap.getWidth()-1,bitmap.getHeight()-1);
        dst.set(getX(), getY(), getX() + 92, getY() + 88);

        canvas.drawBitmap(bitmap, src,dst,p);

        for (Sun sun :suns) {
            if (sun.isDead()) {
                suns.remove(sun) ;
            } else {
                sun.Draw(canvas, p);
            }
        }

    }
}