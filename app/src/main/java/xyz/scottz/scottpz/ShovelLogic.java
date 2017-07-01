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

        shovel = new Shovel(Game.getResources() , GridLogic.getShovelX() , GridLogic.getShovelY()) ;
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

        if (shovel.isShovelMode()) {
            Plant plant = Game.existPlant(GridLogic.calcCol(x), GridLogic.calcRow(y));
            if (plant != null) {
                Sun sun = new Sun(Game.getResources(),x,y);
                SunLogic.addFallingSun(sun);
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
