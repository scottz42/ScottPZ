package xyz.scottz.scottpz;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import java.util.ArrayList;

/**
 * Created by lei on 2017/6/9.
 */

// TODO: cleanup plant selection code
// TODO: level selection
// TODO: Dave & truck
// TODO: bug: shovel-generated sun shouldn't drop
// TODO: use fullscreen
// TODO: Ra zombie
// TODO: cabbage pult
// TODO: plant food
// TODO: ambush zombies
// TODO: many animations
// TODO: clean up Game class
// TODO: state saving/loading
// TODO: gain new plant
// TODO: music
// TODO: plant levels

// reorganize logic:
// 5 major functions: init, finish, onTimer , OnTouch,  onDraw
// in each function, just call the same virtual function for each object: init will add the list of objects
// most variables will move into each object


// global functionality for whole game
public class Game {
    static private Resources resources ;

    static boolean lost = false ;
    static boolean won = false ;

    static ArrayList allLogic ;

    static SunLogic sunLogic ;
    static ShovelLogic shovelLogic ;
    static LawnmowerLogic mowerLogic ;
    static PlantSelectLogic selectLogic;
    static GenZombieLogic genZombieLogic;
    static GridLogic gridLogic;

    public static Resources getResources() {
        return resources;
    }

    public static int getNoSun() {
        return sunLogic.getNoSun() ;
    }
    public static void setNoSun(int inNoSun) {
        sunLogic.setNoSun(inNoSun);
    }


    public static void initOnce(Resources res)
    {
        allLogic = new ArrayList();
        sunLogic = new SunLogic();
        allLogic.add(sunLogic);
        shovelLogic = new ShovelLogic();
        allLogic.add(shovelLogic);
        mowerLogic = new LawnmowerLogic();
        allLogic.add(mowerLogic);
        selectLogic = new PlantSelectLogic();
        allLogic.add(selectLogic);
        genZombieLogic = new GenZombieLogic();
        allLogic.add(genZombieLogic);
        gridLogic = new GridLogic();
        allLogic.add(gridLogic);

        GenZombieLogic.generateHouse4(res);
//        GenZombieLogic.generateEgypt1(res);

        init(res);
    }

    public static void init(Resources res)
    {
        resources = res ;

        for (Object o: allLogic) {
            Logic logic = (Logic) o;
            logic.init();
        }
        lost = false ;
        won = false ;
    }


    public static void onTimer()
    {
        if (lost || won) return ;

        for (Object o: allLogic) {
            Logic logic = (Logic) o;
            logic.onTimer();
        }

        if (genZombieLogic.finished() && gridLogic.noZombie()) {
            won = true ;
        }
    }

    // on touch: based on what is on screen at that position
    // possibilities: suns, plant new plant, shovel
    public static void onTouch(MotionEvent event) {
        if (lost || won) {
            int x = (int) event.getX() ;
            int y = (int) event.getY() ;

            if (x>200 & x<600) {
                if (y>450 & y<500) {
                    GenZombieLogic.generateHouse4(resources);
                    init(resources);
                } else if (y>500 & y<550) {
                    GenZombieLogic.generateEgypt1(resources);
                    init(resources);
                }
            }

            return;
        }

        for (Object o: allLogic) {
            Logic logic = (Logic) o;
            if (logic.onTouch(event)) {
                return ;
            };
        }
    }

    public static void onDraw(Canvas canvas , Paint p)
    {
        String s ;

        if (lost) {
            p.setColor(Color.GREEN);
            p.setTextSize(100);
            s = String.format("THE ZOMBIES ATE YOUR BRAIN!") ;
            canvas.drawText(s , 100 , 300 , p) ;
        }

        if (won) {
            p.setColor(Color.RED);
            p.setTextSize(200);
            s = String.format("YOU WON!") ;
            canvas.drawText(s , 400 , 300 , p) ;
        }

        // temporary
        if (won||lost) {
            p.setColor(Color.GRAY);
            p.setTextSize(50);
            canvas.drawText("Player House Level 4", 200 , 500 , p);
            canvas.drawText("Egypt Level 1", 200 , 550 , p);
        }


        for (int i=allLogic.size()-1; i>=0 ; i--) {
            Logic logic = (Logic) allLogic.get(i);
            logic.onDraw(canvas , p);
        }
 }

    // TODO: any way to do it more generically? avoiding hardcoding the variable mowerLogic
    public static boolean hasMovingMower()
    {
        return mowerLogic.hasMovingMower();
    }


    public static boolean checkLawnmower(MajorObject o)
    {
        return mowerLogic.checkLawnmower(o);
    }

    // TODO: move to separate logic class？
    public static void setLost(boolean lost)
    {
        Game.lost = lost ;
    }

    public static ArrayList<MajorObject> getMajors()
    {
        return gridLogic.getMajors();
    }

    public static Plant findPlant(int x , int y)
    {
        return gridLogic.findPlant(x , y);
    }

    public static boolean addPlant(Plant plant)
    {
        return gridLogic.addPlant(plant);
    }

    public static boolean addZombie(Zombie zombie)
    {
        return gridLogic.addZombie(zombie);
    }

    public static boolean removePlant(Plant plant)
    {
        return gridLogic.removePlant(plant);
    }

    public static boolean removeZombie(Zombie zombie)
    {
        return gridLogic.removeZombie(zombie);
    }

    public static Plant existPlant(int col , int row) {
        return gridLogic.existPlant(col , row);
    }

    public static boolean canPlant(int x , int y)
    {
        return gridLogic.canPlant(x , y);
    }

    public static Zombie ExistZombieInFront(int column , int row)
    {
        return gridLogic.ExistZombieInFront(column , row);
    }
}
