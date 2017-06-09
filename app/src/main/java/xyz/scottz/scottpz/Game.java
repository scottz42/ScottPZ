package xyz.scottz.scottpz;

import android.app.usage.UsageEvents;
import android.content.res.Resources;
import android.view.MotionEvent;

import java.util.ArrayList;

/**
 * Created by lei on 2017/6/9.
 */


// global functionality for whole game
public class Game {
    static private Resources resources ;

    static private ArrayList<MajorObject> majors ;

    public static ArrayList<MajorObject> getMajors() {
        return majors;
    }

    public static void setMajors(ArrayList<MajorObject> majors) {
        Game.majors = majors;
    }

    public static void init(Resources res)
    {
        resources = res ;
        setMajors(new ArrayList<MajorObject>());
    }


    public static void onTimer()
    {
        for (MajorObject o : majors) {
            o.Move();   // zombie move; sunflower generate flower; zombie damages plant
        }
    }

    // on touch: based on what is on screen at that position
    // possibilities: suns, plant new plant
    public static void onTouch(MotionEvent event)
    {
        // plant new plant
        int x = (int) event.getX() ;
        int y = (int) event.getY() ;

        // align to grid
        // TODO: adjust for screen size
        // TODO: use constatns as appropriate
        x = (x/100)*100 ;
        y = (y/100)*100 ;

        if (x>=100 && x<=900 && y>=100 && y<=500 && existPlant(x,y)==null) {
            NormalPea pea = new NormalPea(resources);
            pea.setX(x);
            pea.setY(y);
            majors.add(pea);
        }
    }


    public static boolean removePlant(Plant plant)
    {
        return majors.remove(plant) ;
    }

    public static Plant findPlant(int x , int y)
    {
        for (MajorObject o : majors) {
            if (o.isPlant()&& o.canBite(x , y)) {
                return (Plant) o ;
            }
        }

        return null ;
    }

    // is there a plant at normalized cooordinate (x,y)
    // TODO: make it more efficient
    public static Plant existPlant(int x , int y)
    {
        for (MajorObject o : majors) {
            if (o.isPlant() && o.getX()==x && o.getY()==y) {
                return (Plant)o ;
            }
        }
        return null ;
    }
}
