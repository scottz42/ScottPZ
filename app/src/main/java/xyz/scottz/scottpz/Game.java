package xyz.scottz.scottpz;

import java.util.ArrayList;

/**
 * Created by lei on 2017/6/9.
 */


// global functionality for whole game
public class Game {
    static private ArrayList<MajorObject> majors ;


    public static ArrayList<MajorObject> getMajors() {
        return majors;
    }

    public static void setMajors(ArrayList<MajorObject> majors) {
        Game.majors = majors;
    }


    public static void onTimer()
    {
        for (MajorObject o : majors) {
            o.Move();   // zombie move; sunflower generate flower; zombie damages plant
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

}
