package xyz.scottz.scottpz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by scott on 9/2/2017.
 */

public class GlitterZombie extends Zombie {
    private static Bitmap bitmap=null ;
    private RainbowEnvironment rainbow=null;

    GlitterZombie()
    {
        super();
        life = 23.5 ;
        damagePerAttack = 1000;     // instant kill
        if (bitmap==null) {
            bitmap = BitmapFactory.decodeResource(Game.getResources(), R.drawable.glitterzombie);
        }

        // rect to be updated later in Move()
        rainbow = new RainbowEnvironment(0,0,0,0);
      //  GridLogic.addEnv(rainbow);
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

        rainbow.onDraw(canvas,p);
    }

    @Override
    void Move() {
        super.Move();

        // rainbow moves along
        rainbow.getArea().set(getX() , getY() , GridLogic.getGridWidth()*3,GridLogic.getGridHeight());
    }

    @Override
    void cleanup()
    {
        GridLogic.removeEnv(rainbow);
    }
}
