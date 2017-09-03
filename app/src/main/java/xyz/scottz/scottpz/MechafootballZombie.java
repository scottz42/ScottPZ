package xyz.scottz.scottpz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by scott on 9/3/2017.
 */

public class MechafootballZombie extends Zombie {
    private static Bitmap bitmap=null ;

    MechafootballZombie()
    {
        super();
        life = 120 ;
        damagePerAttack = 0;
        if (bitmap==null) {
            bitmap = BitmapFactory.decodeResource(Game.getResources(), R.drawable.mechafootballzombie);
        }
    }

    @Override
    void Draw(Canvas canvas , Paint p) {
        super.Draw(canvas,p);

        Rect src = new Rect() ;
        Rect dst = new Rect() ;
        src.set(0,0,bitmap.getWidth()-1,bitmap.getHeight()-1);
        // TODO: GridLogic
        dst.set(getX(), shrunk?getY():(getY()-GridLogic.getZombieHeight()), getX() + (shrunk?GridLogic.getZombieWidth():GridLogic.getZombieWidth()*2),
                getY() + GridLogic.getZombieHeight());

        canvas.drawBitmap(bitmap, src,dst,p);
    }

    @Override
    void Move()
    {
        super.Move();

        // kick plants

        Plant plant = (Plant) Game.findPlant(getX() , getY() , false);
        if (plant!=null) {
            pushPlant(plant);
        }
    }

    void pushPlant(Plant plant)
    {
        int col = GridLogic.calcCol(plant.getX());
        int row = GridLogic.calcRow(plant.getY());

        if (col==0) {
            Game.removePlant(plant);
        } else {
            Plant leftPlant = Game.existPlant(col-1 , row);
            if (leftPlant!=null) {
                pushPlant(leftPlant);
            }
            plant.setX(plant.getX()-GridLogic.getGridWidth());
        }
    }

}
