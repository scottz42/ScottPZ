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

// TODO: allow multiple levels
// TODO: Dave & truck

// TODO: clean up Game class
// TODO: ice shouldn't freeze tombstone
// TODO: lawnmower shouldn't remove tombstone
// TODO: clean up positioning of everything on screen
// TODO: transparent zombies
// TODO: Ra zombie
// TODO: cleanup plant selection code
// TODO: cabbage pult
// TODO: plant food
// TODO: transparency
// TODO: background
// TODO: ambush zombies
// TODO: many animations
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

    static SunLogic sunLogic ;
    static ShovelLogic shovelLogic ;
    static LawnmowerLogic mowerLogic ;
    static PlantSelectLogic selectLogic;

    static private ArrayList<MajorObject> majors ;
    static private ArrayList<MajorObject> deletions ;
    static private ArrayList<ZombieInfo> zombies;
    static private long levelStartTime ;


    // zombies to be generated for one level ;
    static private ArrayList level ;
    public static Resources getResources() {
        return resources;
    }


    public static ArrayList<MajorObject> getMajors() {
        return majors;
    }

    public static void setMajors(ArrayList<MajorObject> majors) {
        Game.majors = majors;
    }

    public static int getNoSun() {
        return sunLogic.getNoSun() ;
    }
    public static void setNoSun(int inNoSun) {
        sunLogic.setNoSun(inNoSun);
    }


    public static void init(Resources res)
    {
        resources = res ;

        setMajors(new ArrayList<MajorObject>());
        deletions = new ArrayList<MajorObject>();

        // sun
        sunLogic = new SunLogic();
        sunLogic.init();

        // shovel
        shovelLogic = new ShovelLogic();
        shovelLogic.init() ;

        // lawnmower
        mowerLogic = new LawnmowerLogic();
        mowerLogic.init();

        // plant selection
        selectLogic = new PlantSelectLogic();
        selectLogic.init();

        // TODO: test tombstone
        Tombstone stone1 = new Tombstone(2 , 5) ;
        Tombstone stone2 = new Tombstone(3 , 0) ;
        majors.add(stone1) ;
        majors.add(stone2) ;

        generateHouse4();
        levelStartTime = System.currentTimeMillis() ;

        long time = 0 ;
        zombies = new ArrayList<ZombieInfo>();

        for (Object o: level) {
            ArrayList wave = (ArrayList) o ;
            time++ ;
            for (Object o2: wave) {
                ZombieInfo info = (ZombieInfo) o2 ;
                int row = info.getRow() ;
                if (row==0) {
                    row = ((int)(Math.random()*5))+1;
                }
                info.zombie.setX(1000);
                info.zombie.setY(row*100);
                info.setTime((time*15 + ((int)(Math.random()*10))-5)*1000);
                zombies.add(info);
            }
        }
    }


    public static ArrayList<MajorObject> getDeletions() {
        return deletions;
    }

    public static void setDeletions(ArrayList<MajorObject> deletions) {
        Game.deletions = deletions;
    }



    public static void onTimer()
    {
        if (lost || won) return ;
        generateZombies() ;

        sunLogic.onTimer();
        mowerLogic.onTimer();

        for (MajorObject o : majors) {
            // zombie: move, damage plant
            // plant: shoot zombie, generate sun, etc.
            o.Move();
            if (!mowerLogic.hasMovingMower() && !o.isPlant() && o.getX()<20) {
                lost = !mowerLogic.checkLawnmower(o) ;
            }
        }

        // TODO: use delete flag in each object instead?
        for (MajorObject o : deletions) {
            majors.remove(o) ;
        }
        deletions.clear() ;

        if (zombies.isEmpty() && noZombie()) {
            won = true ;
        }
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

    public static void generateHouse4()
    {
        ArrayList wave = new ArrayList();
        level = new ArrayList() ;

        // wave 1
        wave.add(new ZombieInfo(new NormalZombie(resources) , 3)) ;
        level.add(wave) ;

        // wave 2
        wave = new ArrayList() ;
        wave.add(new ZombieInfo(new NormalZombie(resources) , 4));
        level.add(wave) ;

        // wave 3
        wave = new ArrayList();
        wave.add(new ZombieInfo(new NormalZombie(resources) , 2)) ;
        wave.add(new ZombieInfo(new NormalZombie(resources) , 0)) ;
        level.add(wave) ;

        // wave 4
        wave = new ArrayList();
        wave.add(new ZombieInfo(new NormalZombie(resources) , 2)) ;
        wave.add(new ZombieInfo(new NormalZombie(resources) , 0)) ;
        level.add(wave) ;

        // wave 5
        wave = new ArrayList();
        wave.add(new ZombieInfo(new NormalZombie(resources) , 0)) ;
        wave.add(new ZombieInfo(new ConeheadZombie(resources) , 0)) ;
        level.add(wave) ;

        // wave 6
        wave = new ArrayList();
        wave.add(new ZombieInfo(new NormalZombie(resources) , 0)) ;
        wave.add(new ZombieInfo(new NormalZombie(resources) , 0)) ;
        wave.add(new ZombieInfo(new NormalZombie(resources) , 0)) ;
        level.add(wave) ;

        // wave 7
        wave = new ArrayList();
        wave.add(new ZombieInfo(new BucketheadZombie(resources) , 3)) ;
        level.add(wave) ;

        // wave 8
        wave = new ArrayList();
        wave.add(new ZombieInfo(new NormalZombie(resources) , 0)) ;
        wave.add(new ZombieInfo(new NormalZombie(resources) , 0)) ;
        wave.add(new ZombieInfo(new NormalZombie(resources) , 0)) ;
        wave.add(new ZombieInfo(new ConeheadZombie(resources) , 0)) ;
        wave.add(new ZombieInfo(new ConeheadZombie(resources) , 0)) ;
        level.add(wave) ;
    }

    public static void generateEgypt1()
    {

    }

    public static void generateZombies()
    {
        ArrayList<ZombieInfo> removeList = new ArrayList<>();

        for (ZombieInfo info: zombies) {
            if ((System.currentTimeMillis()-levelStartTime)>info.getTime()) {
                info.getZombie().setLastMoveTime(System.currentTimeMillis());
                majors.add(info.getZombie());
                removeList.add(info);
            }
        }
        zombies.removeAll(removeList);

    }

    // on touch: based on what is on screen at that position
    // possibilities: suns, plant new plant
    public static void onTouch(MotionEvent event) {
        if (lost || won) return;

        if (shovelLogic.onTouch(event)) {
            return;
        }

        if (sunLogic.onTouch(event)) {
            return;
        }

        for (MajorObject o : majors) {
            o.checkSun(event);
        }

        selectLogic.onTouch(event);
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

    // TODO: currently unused
    public static int noZombies()
    {
        int result = 0 ;
        for (MajorObject o : majors) {
            if (!o.isPlant()) result++ ;
        }
        return result ;
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

    // is there a plant at normalized cooordinate (x,y)
    // if there is already tombstone, can not plant either
    // TODO: make it more efficient
    public static boolean canPlant(int x , int y)
    {
        for (MajorObject o : majors) {
            if ((o.isPlant()||o.isTombstone()) && o.getX()==x && o.getY()==y) {
                return false ;
            }
        }
        return true ;
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
        shovelLogic.onDraw(canvas , p);
        mowerLogic.onDraw(canvas , p);
        selectLogic.onDraw(canvas ,p);


        // plants & zombies ;
        for (MajorObject o : Game.getMajors()) {
            o.Draw(canvas, p);
        }

        // falling suns
        sunLogic.onDraw(canvas , p);
    }
}

