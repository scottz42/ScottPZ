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

    static int plantSlots = 7;
    static int defaultRechargeTime = 5000;     // TODO: move to Plant class?
    static int noPlants ;
    static ArrayList plantSelections ;
    static int currentPlantSelection ;

    static private long rechargeTime[] ;
    static private long rechargeStartTime[] ;

    @Override
    public void init() {
        super.init();

        Resources resources = Game.getResources();

        noPlants = 0 ;
        currentPlantSelection = 0 ;
        plantSelections = new ArrayList(plantSlots) ;
        rechargeTime = new long[plantSlots] ;
        rechargeStartTime = new long[plantSlots] ;
    }

    // init based on selected plants
    public void finishSelection()
    {

        for (int i=0 ; i<noPlants ; i++) {
            try {
                Object[] args = new Object[0];
                Class[] argsClass = new Class[0];
                rechargeTime[i] = ((Integer) plantSelections.get(i).getClass().getMethod("getRechargeTime", argsClass).invoke(null, args)).intValue();
            } catch (Exception e) {
                rechargeTime[i] = defaultRechargeTime;
            }
        }

        for (int i=0 ; i<noPlants ; i++) {
            rechargeStartTime[i] = System.currentTimeMillis() ;
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
            if (Game.isNormalPlay()) {
                currentPlantSelection = selection;
                // TODO: visual indication for current selection
            } else {
                Game.unselectPlant((Plant)plantSelections.get(selection));
                plantSelections.remove(selection);
                noPlants--;
            }
        }


        // plant new plant
        // TODO: adjust for screen size
        // TODO: use constants as appropriate

        if (Game.isNormalPlay() && x>=100 && x<=900 && y>=100 && y<=500 && Game.canPlant(x,y)) {
            Plant newPlant = null;
            try {
                newPlant = ((Plant) plantSelections.get(currentPlantSelection)).getClass().newInstance();
            } catch (Exception e) {
                newPlant = null;
            }

            if (newPlant!=null && (System.currentTimeMillis()-rechargeStartTime[currentPlantSelection])>=rechargeTime[currentPlantSelection]) {
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
            plant.setX(GridLogic.getSelectX());
            plant.setY(GridLogic.getSelectY(i));
            plant.drawSelect(canvas , paint); ;


            paint.setColor(0xc0ffffff);
            if (Game.isNormalPlay()) {
                long diff = (System.currentTimeMillis() - rechargeStartTime[i]) * 100 / rechargeTime[i];
                if (diff > 100) diff = 100;
                diff = 100 - diff;


                canvas.drawRect(GridLogic.getSelectX(), GridLogic.getSelectY(i),
                        GridLogic.getSelectX() + GridLogic.getSelectWidth(), GridLogic.getSelectY(i) + diff, paint);
            }
            paint.setAlpha(255);

        }

    }

    public boolean selectionFull()
    {
        return noPlants==plantSlots;
    }

    public boolean addPlantSelection(Plant plant)
    {
        if (noPlants<plantSlots) {
            plantSelections.add(plant);
            noPlants++;
            return true;
        } else {
            return false;
        }
    }
}
