package xyz.scottz.scottpz;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import java.util.ArrayList;

/**
 * Created by lei on 2017/6/18.
 */

public class GenZombieLogic extends Logic {
    static Resources resources ;
    static private ArrayList<ZombieInfo> zombies;
    static private long levelStartTime ;

    // zombies to be generated for one level ;
    static private ArrayList level ;

    // level is already filled in
    @Override
    public void init() {
        super.init();

        resources = Game.getResources();

        levelStartTime = 0;

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
                info.zombie.setX(GridLogic.getZombieX());
                info.zombie.setY(GridLogic.getZombieY(row-1));
                info.setTime((time*15 + ((int)(Math.random()*10))-5)*1000);
                zombies.add(info);
            }
        }
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        return super.onTouch(event) ;

    }

    @Override
    public boolean onTimer()
    {
        super.onTimer();

        if (Game.isNormalPlay() && levelStartTime==0) {
            levelStartTime = System.currentTimeMillis() ;
        }
        if (Game.isNormalPlay()) {
            return generateZombies();
        } else {
            return false;
        }
    }

    @Override
    public void onDraw(Canvas canvas, Paint paint)
    {
        super.onDraw(canvas, paint);
    }

    public boolean finished()
    {
        return zombies.isEmpty();
    }


    // add zombie to screen as time arrives
    // true if there is zombie added
    public static boolean generateZombies()
    {
        ArrayList<ZombieInfo> removeList = new ArrayList<>();
        boolean newZombie = false;

        for (ZombieInfo info: zombies) {
            if ((System.currentTimeMillis()-levelStartTime)>info.getTime()) {
                info.getZombie().setLastMoveTime(System.currentTimeMillis());
                Game.addZombie(info.getZombie());
                newZombie = true;
                removeList.add(info);
            }
        }
        zombies.removeAll(removeList);

        return newZombie ;
    }



    public static void generateHouse4(Resources resources)
    {
        ArrayList wave = new ArrayList();
        level = new ArrayList() ;

        // wave 1
        wave.add(new ZombieInfo(new NormalZombie() , 3)) ;
        level.add(wave) ;

        // wave 2
        wave = new ArrayList() ;
        wave.add(new ZombieInfo(new BreakdancerZombie() , 3));
        level.add(wave) ;

        // wave 3
        wave = new ArrayList();
        wave.add(new ZombieInfo(new NormalZombie() , 2)) ;
        wave.add(new ZombieInfo(new NormalZombie() , 0)) ;
        level.add(wave) ;

        // wave 4
        wave = new ArrayList();
        wave.add(new ZombieInfo(new NormalZombie() , 2)) ;
        wave.add(new ZombieInfo(new NormalZombie() , 0)) ;
        level.add(wave) ;

        // wave 5
        wave = new ArrayList();
        wave.add(new ZombieInfo(new NormalZombie() , 0)) ;
        wave.add(new ZombieInfo(new ConeheadZombie() , 0)) ;
        level.add(wave) ;

        // wave 6
        wave = new ArrayList();
        wave.add(new ZombieInfo(new NormalZombie() , 0)) ;
        wave.add(new ZombieInfo(new NormalZombie() , 0)) ;
        wave.add(new ZombieInfo(new NormalZombie() , 0)) ;
        level.add(wave) ;

        // wave 7
        wave = new ArrayList();
        wave.add(new ZombieInfo(new BucketheadZombie() , 3)) ;
        level.add(wave) ;

        // wave 8
        wave = new ArrayList();
        wave.add(new ZombieInfo(new NormalZombie() , 0)) ;
        wave.add(new ZombieInfo(new NormalZombie() , 0)) ;
        wave.add(new ZombieInfo(new NormalZombie() , 0)) ;
        wave.add(new ZombieInfo(new ConeheadZombie() , 0)) ;
        wave.add(new ZombieInfo(new ConeheadZombie() , 0)) ;
        level.add(wave) ;
  }

    public static void generateEgypt1(Resources resources)
    {
        ArrayList wave = new ArrayList();
        level = new ArrayList() ;

        // wave 1
        wave.add(new ZombieInfo(new BucketheadZombie() , 2)) ;
        level.add(wave) ;

        // wave 2
        wave = new ArrayList() ;
        wave.add(new ZombieInfo(new NormalZombie() , 1));
        level.add(wave) ;

        // wave 3
        wave = new ArrayList();
        wave.add(new ZombieInfo(new NormalZombie() , 2)) ;
        wave.add(new ZombieInfo(new NormalZombie() , 0)) ;
        level.add(wave) ;

        // wave 4
        wave = new ArrayList();
        wave.add(new ZombieInfo(new NormalZombie() , 2)) ;
        wave.add(new ZombieInfo(new NormalZombie() , 0)) ;
        level.add(wave) ;

        // wave 5
        wave = new ArrayList();
        wave.add(new ZombieInfo(new NormalZombie() , 0)) ;
        wave.add(new ZombieInfo(new ConeheadZombie() , 0)) ;
        level.add(wave) ;

        // wave 6
        wave = new ArrayList();
        wave.add(new ZombieInfo(new NormalZombie() , 0)) ;
        wave.add(new ZombieInfo(new NormalZombie() , 0)) ;
        wave.add(new ZombieInfo(new NormalZombie() , 0)) ;
        level.add(wave) ;

        // wave 7
        wave = new ArrayList();
        wave.add(new ZombieInfo(new BucketheadZombie() , 3)) ;
        level.add(wave) ;

        // wave 8
        wave = new ArrayList();
        wave.add(new ZombieInfo(new NormalZombie() , 0)) ;
        wave.add(new ZombieInfo(new NormalZombie() , 0)) ;
        wave.add(new ZombieInfo(new NormalZombie() , 0)) ;
        wave.add(new ZombieInfo(new ConeheadZombie() , 0)) ;
        wave.add(new ZombieInfo(new ConeheadZombie() , 0)) ;
        level.add(wave) ;

    }
}
