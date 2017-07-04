package xyz.scottz.scottpz;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import java.util.ArrayList;

/**
 * Created by lei on 2017/7/4.
 */

// select plants from all plants

public class SelectAllLogic extends Logic {

    // all plants in the game
    static int noTotalPlants;
    static ArrayList allPlants;

    static int noPlants ;
    static ArrayList plantSelections ;
    static int currentPlantSelection ;

    static private long rechargeTime[] ;
    static private long rechargeStartTime[] ;

    @Override
    public void init() {
        super.init();

        Resources resources = Game.getResources();

        // all plants
        // change this part when adding a new plant
        noTotalPlants = 7;
        allPlants = new ArrayList();
        Sunflower sunflower = new Sunflower(resources);
        NormalPea pea = new NormalPea(resources);  // TODO: change
        Wallnut nut = new Wallnut(resources);
        PotatoMine mine = new PotatoMine(resources);
        IcebergLettuce iceberg = new IcebergLettuce(resources);
        ExplodeONut explodeONut = new ExplodeONut(resources);
        Jalapeno jalapeno = new Jalapeno(resources);
        allPlants.add(sunflower);
        allPlants.add(pea);
        allPlants.add(nut);
        allPlants.add(mine) ;
        allPlants.add(iceberg) ;
        allPlants.add(explodeONut);
        allPlants.add(jalapeno);

        for (int i=0 ; i<noPlants ; i++) {
            rechargeStartTime[i] = System.currentTimeMillis() ;
            Plant plant = (Plant) plantSelections.get(i);
            plant.setX(GridLogic.getSelectX());
            plant.setY(GridLogic.getSelectY(i));
        }

    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        super.onTouch(event) ;

        Resources resources = Game.getResources();
        int x = (int) event.getX();
        int y = (int) event.getY();

        // TODO: GridLogic
        x = (x / 100) * 100;
        y = (y / 100) * 100;

        // select plant
        int selection = GridLogic.checkSelectPlant(x,y);
        if (selection>=0 && selection<noPlants) {
            currentPlantSelection = selection ;
            // TODO: visual indication for current selection
        }

        // plant new plant
        // TODO: adjust for screen size
        // TODO: use constants as appropriate

       // TODO: pick new plant to add to plant selection

        // TODO: check ok button

        return false ;
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

        int x = GridLogic.getSelectAllX();
        int y = GridLogic.getSelectAllY();
        int width = GridLogic.getSelectAllWidth();
        int height = GridLogic.getSelectAllHeight();

        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(5);

        // draw border
        canvas.drawRect(x , y , x+width, y+height, paint);

        // draw border and draw grid

        // draw ok button
        int okX = GridLogic.getSelectOKX();
        int okY = GridLogic.getSelectOKY();
        int okWidth = GridLogic.getSelectOKWidth();
        int okHeight = GridLogic.getSelectOKHeihgt();
        canvas.drawRect(okX , okY , okX+okWidth , okY+okHeight , paint);
        paint.setColor(Color.WHITE);
        paint.setTextSize(30);
        canvas.drawText("LET'S ROCK" , okX+30 , okY+20 , paint) ;

        // draw each plant
        for (int i = 0 ; i<noTotalPlants ; i++) {
            Plant plant = (Plant)allPlants.get(i) ;

            int columnsPerRow = GridLogic.getSelectAllCols();
            int row = (i/columnsPerRow);
            int col = i - row*columnsPerRow;
            // TODO: GridLogic
            plant.setX(x+20+col*100);
            plant.setY(y+20+row*100);
            plant.Draw(canvas , paint) ;
        }

    }

}
