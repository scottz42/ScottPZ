package xyz.scottz.scottpz;

import android.app.usage.UsageEvents;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by lei on 2017/6/9.
 */

// TODO: plant recharge time
// TODO: shovel


// global functionality for whole game
public class Game {
    static private Resources resources ;

    static int noPlants ;
    static ArrayList plantSelections ;

    static int currentPlantSelection ;

    static private ArrayList<MajorObject> majors ;
    static private ArrayList<MajorObject> deletions ;

    static int noSun = 50 ;

    public static int getNoSun() {
        return noSun;
    }

    public static void setNoSun(int inNoSun) {
        noSun = inNoSun;
    }

    public static ArrayList<MajorObject> getMajors() {
        return majors;
    }

    public static void setMajors(ArrayList<MajorObject> majors) {
        Game.majors = majors;
    }

    public static void init(Resources res)
    {
        resources = res ;
        noPlants = 3 ;
        currentPlantSelection = 0 ;
        plantSelections = new ArrayList() ;
        Sunflower sunflower = new Sunflower(resources);
        NormalPea pea = new NormalPea(resources);  // TODO: change
        Wallnut nut = new Wallnut(resources);
        plantSelections.add(sunflower);
        plantSelections.add(pea);
        plantSelections.add(nut);

        setMajors(new ArrayList<MajorObject>());
        deletions = new ArrayList<MajorObject>();
    }


    public static ArrayList<MajorObject> getDeletions() {
        return deletions;
    }

    public static void setDeletions(ArrayList<MajorObject> deletions) {
        Game.deletions = deletions;
    }

    public static void onTimer()
    {
        generateZombies() ;

        // TODO: falling suns

        for (MajorObject o : majors) {
            // zombie: move, damage plant
            // plant: shoot zombie, generate sun, etc.
            o.Move();
        }

        // TODO: use delete flag in each object instead?
        for (MajorObject o : deletions) {
            majors.remove(o) ;
        }
        deletions.clear() ;
    }

    public static boolean noZombie()
    {
        // TODO: make more efficient
        for (MajorObject o: majors) {
            if (!o.isPlant()) {
                return false ;
            }
        }
        return true ;
    }

    public static void generateZombies()
    {
        if (noZombie()) {
            Zombie zombie = new NormalZombie(resources);
            int row = (int)(5*Math.random());
            int zombieKind = (int)(3*Math.random());
            if (zombieKind==0){
                zombie = new NormalZombie(resources);
            }
            if (zombieKind==1){
                zombie = new ConeheadZombie(resources);
            }
            if (zombieKind==2){
                zombie = new BucketheadZombie(resources);
            }
            zombie.setX(1000);
            zombie.setY((row+1)*100);
            majors.add(zombie);

        }
    }

    // on touch: based on what is on screen at that position
    // possibilities: suns, plant new plant
    public static void onTouch(MotionEvent event)
    {
        int x = (int) event.getX() ;
        int y = (int) event.getY() ;
        x = (x/100)*100 ;
        y = (y/100)*100 ;

        // TODO: click on sun: 2 types: plant & sky
        // TODO: add falling sun


        for (MajorObject o : majors) {
            o.checkSun(event) ;
        }

        // select plant
        if (x==0) {
            currentPlantSelection = y/100 -1 ;
        // TODO: visual indication for current selection
        }


        // plant new plant
        // TODO: adjust for screen size
        // TODO: use constants as appropriate

        if (x>=100 && x<=900 && y>=100 && y<=500 && existPlant(x,y)==null) {
            Plant newPlant ;
            if (currentPlantSelection==0) {
                newPlant = new Sunflower(resources);
            } else if(currentPlantSelection==1) {
                newPlant = new NormalPea(resources);
            } else if(currentPlantSelection==2) {
                newPlant = new Wallnut(resources);
            } else {
                newPlant = new Sunflower(resources);
            }
            newPlant.setX(x);
            newPlant.setY(y);
            if (getNoSun()>=newPlant.getSunNeeded()) {
                setNoSun(getNoSun()-newPlant.getSunNeeded());
                majors.add(newPlant);
            }
        }
    }


    public static boolean removePlant(Plant plant)
    {
        deletions.add(plant) ;
        return true ;
    }

    public static boolean removeZombie(Zombie zombie)
    {
        deletions.add(zombie) ;
        return true ;
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
        // TODO: better display of sun left
        p.setColor(Color.BLUE);
        p.setTextSize(50);
        String s = String.format("Suns: %d" , getNoSun()) ;
        canvas.drawText(s , 10 , 70 , p) ;

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


