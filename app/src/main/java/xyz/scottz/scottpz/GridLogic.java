package xyz.scottz.scottpz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by lei on 2017/6/20.
 */


// TODO: layout based on screen size
// TODO: zombie can be almost anywhere, be careful about row/col calculation for zombies

// mower, plant selection panel, grid, shovel

// whole layout & plant/zombie positioning
// TODO: separare out plant/zombie part?

public class GridLogic extends Logic {
    static private int noRows = 5 ;
    static private int noCols = 9 ;
    static private int plantWidth = 88;
    static private int plantHeight = 92;
    static private int zombieWidth = 88;
    static private int zombieHeight = 92;
    static private int mowerWidth = 88;
    static private int mowerHeight = 92;
    static private int shovelWidth = 83;
    static private int shovelHeight = 83;

    static private int gridX = 200;
    static private int gridY = 100;
    static private int gridWidth = 100;
    static private int gridHeight = 100;

    static private int mowerX = 0;

    static private int selectX = 100;
    static private int selectY = 100;
    static private int selectWidth = 100;
    static private int selectHeight = 60;

    static private int selectAllCols = 5;
    static private int selectAllX = 300;
    static private int selectAllY = 200;
    static private int selectAllWidth = 570;
    static private int selectAllHeight = 600;
    static private int selectOKX = 900;
    static private int selectOKY = 740;
    static private int selectOKWidth = 200;
    static private int selectOKHeihgt = 60;

    static private int shovelX = 1100;
    static private int shovelY = 600;

    static private int peaWidth = 40;
    static private int peaHeight = 40;

    // cabbage shot
    static private int cabbageWidth = 60;
    static private int cabbageHeight = 60;

    static private int zombieX = 1100;

    static private ArrayList<MajorObject> majors;
    static private ArrayList<MajorObject> deletions;

    static private Bitmap lawn;

    @Override
    public void init() {
        super.init();

        lawn = BitmapFactory.decodeResource(Game.getResources(), R.drawable.lawn);

        majors = new ArrayList<MajorObject>();
        deletions = new ArrayList<MajorObject>();
    }

    @Override
    public boolean onTimer() {
        if (!Game.isNormalPlay()) return false;

        for (MajorObject o : majors) {
            // zombie: move, damage plant
            // plant: shoot zombie, generate sun, etc.
            o.Move();
            // TODO: GridLogic
            if (!Game.hasMovingMower() && !o.isPlant() && o.getX() < 20) {
                Game.setLost(!Game.checkLawnmower(o));
            }
        }

        // TODO: use delete flag in each object instead?
        for (MajorObject o : deletions) {
            majors.remove(o);
        }

        deletions.clear();
        return true;
    }

    @Override
    public void onDraw(Canvas canvas, Paint paint) {
        super.onDraw(canvas, paint);

        // draw lawn
        for (int row=0 ; row<noRows ; row++) {
            for (int col=0 ; col<noCols; col++) {
                Rect src = new Rect() ;
                Rect dst = new Rect() ;
                src.set(0,0,lawn.getWidth()-1,lawn.getHeight()-1);
                dst.set(gridX+gridWidth*col , gridY+gridHeight*row, gridX+gridWidth*(col+1) , gridY+gridHeight*(row+1));
                canvas.drawBitmap(lawn, src,dst,paint);

            }
        }

        // plants & zombies ;
        for (MajorObject o : majors) {
            o.Draw(canvas, paint);
        }

    }


    /*
    these methods deal with more complicated grid logic, not simple positioning
     */


    public static boolean noZombie() {
        // TODO: make more efficient
        for (MajorObject o : majors) {
            if (!o.isPlant()) {
                return false;
            }
        }
        return true;
    }

    // TODO: should avoid; current uses are for looping over each MajorObject
    public ArrayList<MajorObject> getMajors() {
        return majors;
    }


    public static boolean removePlant(Plant plant) {
        deletions.add(plant);
        plant.onFinal();
        return true;
    }

    public static boolean addPlant(Plant plant) {
        majors.add(plant);
        return true;
    }

    public static boolean addZombie(Zombie zombie) {
        majors.add(zombie);
        return true;
    }

    public static boolean removeZombie(Zombie zombie) {
        deletions.add(zombie);
        return true;
    }

    // TODO: currently unused
    public static int noZombies() {
        int result = 0;
        for (MajorObject o : majors) {
            if (!o.isPlant()) result++;
        }
        return result;
    }

    //
    @Nullable
    public static Plant findPlant(int x, int y) {
        for (MajorObject o : majors) {
            if (o.isPlant() && o.canBite(x, y)) {
                return (Plant) o;
            }
        }

        return null;
    }

    // is there a plant at normalized cooordinate (x,y)
    // TODO: make it more efficient
    public static Plant existPlant(int col, int row) {
        for (MajorObject o : majors) {
            if (o.isPlant() && calcCol(o.getX()) == col && calcRow(o.getY()) == row) {
                return (Plant) o;
            }
        }
        return null;
    }

    // is there a plant at coordinate (x,y)
    // if there is already tombstone, can not plant either
    // TODO: make it more efficient
    public static boolean canPlant(int x, int y) {

        int row = calcRow(y);
        int col = calcCol(x);

        // outside of grid
        if (row<0 || row>=noRows || col<0 || col>=noCols) {
            return false ;
        }

        for (MajorObject o : majors) {
            if ((o.isPlant() || o.isTombstone()) &&calcCol(o.getX()) == col && calcRow(o.getY()) == row) {
                return false;
            }
        }
        return true;
    }


    // check to see if there's any zombie in front of this plant
    public static Zombie ExistZombieInFront(int column, int row) {
        for (MajorObject o : majors) {
            if (!o.isPlant() && (calcRow(o.getY())==row) && (calcCol(o.getX())>= column)) {
                return (Zombie) o;
            }
        }
        return null;
    }

    public static int calcRow(int y) {
        return (y - gridY) / gridHeight;
    }

    public static int calcCol(int x) {
        return (x - gridX) / gridWidth;
    }

    // check to see if the zombie is in this plant's square
    public static boolean isZombieInPlantSquare(Zombie zombie, Plant plant) {
        return calcRow(zombie.getY()) == calcRow(plant.getY()) && calcCol(zombie.getX()) == calcCol(plant.getX());
    }

    // check if hit in plant selection panel, returns row, -1 if not in selection panel
    public static int checkSelectPlant(int x , int y)
    {
        if (x>=selectX && x<selectX+selectWidth) {
            return (y-selectY)/selectHeight;
        } else {
            return -1;
        }
    }


    /*
    These methods are basic access functions
     */


    public static int getPlantWidth() {
        return plantWidth;
    }

    public static int getPlantHeight() {
        return plantHeight;
    }

    public static int getZombieWidth() {
        return zombieWidth;
    }

    public static int getZombieHeight() {
        return zombieHeight;
    }

    public static int getMowerWidth()
    {
        return mowerWidth;
    }

    public static int getMowerHeight()
    {
        return mowerHeight;
    }

    public static int getShovelWidth() {
        return shovelWidth;
    }

    public static int getShovelHeight() {
        return shovelHeight;
    }

    public static int getShovelX() {
        return shovelX;
    }

    public static int getShovelY() {
        return shovelY;
    }

    public static int getMowerX(int row) {
        return mowerX;
    }

    public static int getMowerY(int row)
    {
        return gridY+row*gridHeight;
    }

    public static int getSelectX()
    {
        return selectX;
    }

    public static int getSelectY(int row)
    {
        return selectY+row*selectHeight;
    }

    public static int getSelectWidth()
    {
        return selectWidth;
    }

    public static int getSelectHeight()
    {
        return selectHeight;
    }

    public static int getZombieX()
    {
        return zombieX;
    }

    public static int getZombieY(int row)
    {
        return gridY + row*gridHeight;
    }

    public static int getSelectAllCols() {
        return selectAllCols;
    }

    public static int getSelectAllX() {
        return selectAllX;
    }

    public static int getSelectAllY() {
        return selectAllY;
    }

    public static int getSelectAllWidth() {
        return selectAllWidth;
    }

    public static int getSelectAllHeight() {
        return selectAllHeight;
    }

    public static int getSelectOKX() {
        return selectOKX;
    }

    public static int getSelectOKY() {
        return selectOKY;
    }

    public static int getSelectOKWidth() {
        return selectOKWidth;
    }

    public static int getSelectOKHeihgt() {
        return selectOKHeihgt;
    }

    public static int getPeaWidth() { return peaWidth; }

    public static int getPeaHeight() { return peaHeight; }

    public static int getCabbageWidth() { return cabbageWidth; }

    public static int getCabbageHeight() { return cabbageHeight; }
}


