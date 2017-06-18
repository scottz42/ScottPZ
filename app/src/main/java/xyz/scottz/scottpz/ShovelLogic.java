package xyz.scottz.scottpz;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

/**
 * Created by lei on 2017/6/18.
 */

public class ShovelLogic extends Logic {
    private Shovel shovel ;

    @Override
    public void init() {
        super.init();

        shovel = new Shovel(Game.getResources() , 1000 , 600) ;
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        super.onTouch(event) ;

        int x = (int) event.getX() ;
        int y = (int) event.getY() ;
        x = (x/100)*100 ;
        y = (y/100)*100 ;

        if (shovel.isShovelMode()) {
            Plant plant = Game.existPlant(x, y);
            if (plant != null) {
                // TODO: generate suns
                Game.removePlant(plant);
                shovel.setShovelMode(false);
            }
            return true ;
        }

        return shovel.onTouch(event);
    }


    @Override
    public boolean onTimer()
    {
        return super.onTimer();
    }

    @Override
    public void onDraw(Canvas canvas, Paint paint)
    {
        super.onDraw(canvas, paint);
        shovel.onDraw(canvas , paint);
        // TODO: indicate shovel mode

    }
}
