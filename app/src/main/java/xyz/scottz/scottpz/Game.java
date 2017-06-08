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


}
