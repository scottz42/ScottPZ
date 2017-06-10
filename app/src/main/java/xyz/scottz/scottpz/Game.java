package xyz.scottz.scottpz;

import android.app.usage.UsageEvents;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by lei on 2017/6/9.
 */


// global functionality for whole game
public class Game {
    static private Resources resources ;

    static int noPlants ;
    static ArrayList plantSelections ;

    static int currentPlantSelection ;

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
        noPlants = 2 ;
        currentPlantSelection = 0 ;
        plantSelections = new ArrayList() ;
        Sunflower sunflower = new Sunflower(resources);
        NormalPea pea = new NormalPea(resources);  // TODO: change
        plantSelections.add(sunflower);
        plantSelections.add(pea);

        setMajors(new ArrayList<MajorObject>());
    }


    public static void onTimer()
    {
        generateZombies() ;

        for (MajorObject o : majors) {
            // zombie: move, damage plant
            // plant: shoot zombie, generate sun, etc.
            o.Move();
        }
    }

    public static void generateZombies()
    {
        // TODO: generate new zombies
    }

    // on touch: based on what is on screen at that position
    // possibilities: suns, plant new plant
    public static void onTouch(MotionEvent event)
    {
        int x = (int) event.getX() ;
        int y = (int) event.getY() ;
        x = (x/100)*100 ;
        y = (y/100)*100 ;

        // select plant
        if (x==0) {
            currentPlantSelection = y/100 -1 ;
        // TODO: visual indication for current selection
        }


        // plant new plant

        // align to grid
        // TODO: adjust for screen size
        // TODO: use constants as appropriate

        if (x>=100 && x<=900 && y>=100 && y<=500 && existPlant(x,y)==null) {
            Plant newPlant ;
            if (currentPlantSelection==0) {
                newPlant = new Sunflower(resources);
            } else if(currentPlantSelection==1) {
                newPlant = new NormalPea(resources);
            } else {
                newPlant = new Sunflower(resources);
            }
            newPlant.setX(x);
            newPlant.setY(y);
            majors.add(newPlant);
        }
    }


    public static boolean removePlant(Plant plant)
    {
        return majors.remove(plant) ;
    }

    public static boolean removeZombie(Zombie zombie)
    {
        return majors.remove(zombie) ;
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

    // check to see if there's any zombie in front of this plant
    public static Zombie ExistZombieInFront(int column , int row)
    {
        for (MajorObject o : majors) {
            if (!o.isPlant() && (o.getY()/100==row) && (o.getX()/100>=column)) {
                return (Zombie) o ;
            }
        }
        return null ;
    }


    public static void onDraw(Canvas canvas , Paint p)
    {
        // plant selection panel
        for (int i = 0 ; i<noPlants ; i++) {
            Plant plant = (Plant)plantSelections.get(i) ;
            plant.setX(0);
            plant.setY((i+1)*100);
            plant.Draw(canvas , p) ;
        }

        // plants & zombies ;
        for (MajorObject o : Game.getMajors()) {
            o.Draw(canvas, p);
        }

    }
}


