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

// TODO: falling sun: 6s per sun, falling time 3s, disappearing time, 3s for initial sun
// TODO: Ra zombie
// TODO: allow multiple levels
// TODO: cleanup plant selection code
// TODO: plant levels
// TODO: tombstone
// TODO: cabbage pult
// TODO: plant food
// TODO: transparency
// TODO: background
// TODO: ambush zombies
// TODO: state saving/loading


// global functionality for whole game
public class Game {
    static private Resources resources ;

    static boolean lost = false ;
    static boolean won = false ;

    static Shovel shovel ;

    static int noPlants ;
    static ArrayList plantSelections ;

    static int currentPlantSelection ;

    static private ArrayList<MajorObject> majors ;
    static private ArrayList<MajorObject> deletions ;
    static private ArrayList<ZombieInfo> zombies;
    static private long levelStartTime ;
    static private long rechargeTime[] ;
    static private long rechargeStartTime[] ;


    // zombies to be generated for one level ;
    static private ArrayList level ;

    // TODO: normal is 50
    static int noSun = 200 ;

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
        noPlants = 4 ;
        currentPlantSelection = 0 ;
        plantSelections = new ArrayList() ;
        Sunflower sunflower = new Sunflower(resources);
        NormalPea pea = new NormalPea(resources);  // TODO: change
        Wallnut nut = new Wallnut(resources);
        PotatoMine mine = new PotatoMine(resources);
        plantSelections.add(sunflower);
        plantSelections.add(pea);
        plantSelections.add(nut);
        plantSelections.add(mine) ;
        rechargeTime = new long[noPlants] ;
        rechargeTime[0] = Sunflower.getRechargeTime() ;
        rechargeTime[1] = NormalPea.getRechargeTime() ;
        rechargeTime[2] = Wallnut.getRechargeTime() ;
        rechargeTime[3] = PotatoMine.getRechargeTime() ;
        rechargeStartTime = new long[noPlants] ;
        for (int i=0 ; i<noPlants ; i++) {
            rechargeStartTime[i] = System.currentTimeMillis() ;
        }

        setMajors(new ArrayList<MajorObject>());
        deletions = new ArrayList<MajorObject>();

        // shovel
        shovel = new Shovel(resources , 1000 , 600) ;

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

        for (int i=0 ; i<noPlants ; i++) {
            Plant plant = (Plant) plantSelections.get(i) ;
        }

        // TODO: falling suns

        for (MajorObject o : majors) {
            // zombie: move, damage plant
            // plant: shoot zombie, generate sun, etc.
            o.Move();
            if (!o.isPlant() && o.getX()<20) {
                lost = true ;
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
    public static void onTouch(MotionEvent event)
    {
        if (lost || won) return ;

        int x = (int) event.getX() ;
        int y = (int) event.getY() ;
        x = (x/100)*100 ;
        y = (y/100)*100 ;

        if (shovel.isShovelMode()) {
            Plant plant = existPlant(x , y) ;
            if (plant!=null) {
                // TODO: generate suns
                removePlant(plant) ;
                shovel.setShovelMode(false);
            }
            return ;
        }

        shovel.onTouch(event) ;

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
            } else if (currentPlantSelection==3) {
                newPlant = new PotatoMine(resources);
            } else {
                newPlant = new Sunflower(resources);
            }
            if ((System.currentTimeMillis()-rechargeStartTime[currentPlantSelection])>=rechargeTime[currentPlantSelection]) {
                newPlant.setX(x);
                newPlant.setY(y);
                if (getNoSun() >= newPlant.getSunNeeded()) {
                    setNoSun(getNoSun() - newPlant.getSunNeeded());
                    majors.add(newPlant);
                    rechargeStartTime[currentPlantSelection] = System.currentTimeMillis() ;
                }
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

    // TODO: currently unusedd
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

        shovel.Draw(canvas , p);

        // plant selection panel
        for (int i = 0 ; i<noPlants ; i++) {
            Plant plant = (Plant)plantSelections.get(i) ;
            plant.setX(0);
            plant.setY((i+1)*100);
            plant.Draw(canvas , p) ;
            p.setColor(0xc0ffffff);
            long diff = (System.currentTimeMillis()-rechargeStartTime[i])*100/rechargeTime[i] ;
            if (diff>100) diff=100 ;
            diff = 100-diff ;
            canvas.drawRect(0 , (i+1)*100 , 100 , (i+1)*100+diff , p) ;
            p.setAlpha(255);
        }

        // plants & zombies ;
        for (MajorObject o : Game.getMajors()) {
            o.Draw(canvas, p);
        }

    }
}

