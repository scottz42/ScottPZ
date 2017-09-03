package xyz.scottz.scottpz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by scott on 9/2/2017.
 */

public class RainbowEnvironment extends Environment {
    private static Bitmap bitmap=null ;
    Rect area;

    RainbowEnvironment(int x , int y , int width , int height)
    {
        if (bitmap==null) {
            bitmap = BitmapFactory.decodeResource(Game.getResources(), R.drawable.rainbow);
        }
        area = new Rect(x , y , x+width , y+height);
    }

    public Rect getArea() {
        return area;
    }

    public void setArea(Rect area) {
        this.area = area;
    }

    public void onDraw(Canvas canvas , Paint p)
    {

        Rect src = new Rect() ;
        Rect dst = new Rect() ;
        src.set(0,0,bitmap.getWidth()-1,bitmap.getHeight()-1);
        dst.set(area.left , area.top , area.left+GridLogic.getGridWidth()*3, area.top+GridLogic.getGridHeight());

        canvas.drawBitmap(bitmap, src,dst,p);
    }
}
