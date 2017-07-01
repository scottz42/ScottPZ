package xyz.scottz.scottpz;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import java.util.ArrayList;

/**
 * Created by lei on 2017/6/18.
 */


// plant selection logic during normal play
public class PlantSelectLogic extends Logic {

    static int noPlants ;
    static ArrayList plantSelections ;
    static int currentPlantSelection ;

    static private long rechargeTime[] ;
    static private long rechargeStartTime[] ;

    @Override
    public void init() {
        super.init();

        Resources resources = Game.getResources();

        noPlants = 5 ;
        currentPlantSelection = 0 ;
        plantSelections = new ArrayList() ;
        Sunflower sunflower = new Sunflower(resources);
        NormalPea pea = new NormalPea(resources);  // TODO: change
        Wallnut nut = new Wallnut(resources);
        PotatoMine mine = new PotatoMine(resources);
        IcebergLettuce iceberg = new IcebergLettuce(resources);
        plantSelections.add(sunflower);
        plantSelections.add(pea);
        plantSelections.add(nut);
        plantSelections.add(mine) ;
        plantSelections.add(iceberg) ;
        rechargeTime = new long[noPlants] ;
        rechargeTime[0] = Sunflower.getRechargeTime() ;
        rechargeTime[1] = NormalPea.getRechargeTime() ;
        rechargeTime[2] = Wallnut.getRechargeTime() ;
        rechargeTime[3] = PotatoMine.getRechargeTime() ;
        rechargeTime[4] = IcebergLettuce.getRechargeTime() ;
        rechargeStartTime = new long[noPlants] ;
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

        if (x>=100 && x<=900 && y>=100 && y<=500 && Game.canPlant(x,y)) {
            Plant newPlant ;
            if (currentPlantSelection==0) {
                newPlant = new Sunflower(resources);
            } else if(currentPlantSelection==1) {
                newPlant = new NormalPea(resources);
            } else if(currentPlantSelection==2) {
                newPlant = new Wallnut(resources);
            } else if (currentPlantSelection==3) {
                newPlant = new PotatoMine(resources);
            } else if (currentPlantSelection==4) {
                newPlant = new IcebergLettuce(resources);
            } else {
                newPlant = new Sunflower(resources);
            }
            if ((System.currentTimeMillis()-rechargeStartTime[currentPlantSelection])>=rechargeTime[currentPlantSelection]) {
                newPlant.setX(x);
                newPlant.setY(y);
                if (Game.getNoSun() >= newPlant.getSunNeeded()) {
                    Game.setNoSun(Game.getNoSun() - newPlant.getSunNeeded());
                    Game.addPlant(newPlant);
                    rechargeStartTime[currentPlantSelection] = System.currentTimeMillis() ;
                    return true ;
                }
            }
        }
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

        // plant selection panel
        for (int i = 0 ; i<noPlants ; i++) {
            Plant plant = (Plant)plantSelections.get(i) ;
                 plant.Draw(canvas , paint) ;
            paint.setColor(0xc0ffffff);
            long diff = (System.currentTimeMillis()-rechargeStartTime[i])*100/rechargeTime[i] ;
            if (diff>100) diff=100 ;
            diff = 100-diff ;
            canvas.drawRect(GridLogic.getSelectX() , GridLogic.getSelectY(i) ,
                    GridLogic.getSelectX()+GridLogic.getSelectWidth() , GridLogic.getSelectY(i)+diff , paint) ;
            paint.setAlpha(255);
        }

    }
}
