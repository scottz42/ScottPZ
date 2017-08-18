package xyz.scottz.scottpz;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import java.util.ArrayList;

/**
 * Created by lei on 2017/7/4.
 */

// select plants from all plants

public class SelectAllLogic extends Logic {

    // all plants in the game
    static int noTotalPlants;
    static ArrayList allPlants;
    static boolean[] selected;

    @Override
    public void initOnce() {
        super.initOnce();

        Resources resources = Game.getResources();

        // all plants
        // change this part when adding a new plant
        noTotalPlants = 25;
        allPlants = new ArrayList();
        Sunflower sunflower = new Sunflower();
        NormalPea pea = new NormalPea();
        Wallnut nut = new Wallnut();
        PotatoMine mine = new PotatoMine();
        IcebergLettuce iceberg = new IcebergLettuce();
        ExplodeONut explodeONut = new ExplodeONut();
        Jalapeno jalapeno = new Jalapeno();
        Torchwood torchwood = new Torchwood();
        CabbagePult cabbage = new CabbagePult();
        LaserBean laserBean = new LaserBean();
        PrimalSunFlower primalSunFlower = new PrimalSunFlower();
        TwinSunflower twinSunflower = new TwinSunflower();
        Cherrybomb cherry = new Cherrybomb();
        Citron citron = new Citron();
        Repeater repeater=new Repeater();
        MelonPult melonPult=new MelonPult();
        PepperPult pepperPult=new PepperPult();
        SnapDragon snapDragon=new SnapDragon();
        GoldBloom goldBloom=new GoldBloom();
        WinterMelon winterMelon=new WinterMelon();
        BonkChoy bonkChoy=new BonkChoy();
        ShrinkingViolet shrinkingViolet=new ShrinkingViolet();
        HypnoShroom hypnoShroom=new HypnoShroom();
        Blover blover = new Blover();
        ColdSnapdragon coldSnapdragon=new ColdSnapdragon();
        allPlants.add(sunflower);
        allPlants.add(pea);
        allPlants.add(nut);
        allPlants.add(mine) ;
        allPlants.add(iceberg) ;
        allPlants.add(explodeONut);
        allPlants.add(jalapeno);
        allPlants.add(torchwood);
        allPlants.add(cabbage);
        allPlants.add(laserBean);
        allPlants.add(primalSunFlower);
        allPlants.add(twinSunflower);
        allPlants.add(cherry);
        allPlants.add(citron);
        allPlants.add(repeater);
        allPlants.add(melonPult);
        allPlants.add(pepperPult);
        allPlants.add(snapDragon);
        allPlants.add(goldBloom);
        allPlants.add(winterMelon);
        allPlants.add(bonkChoy);
        allPlants.add(shrinkingViolet);
        allPlants.add(hypnoShroom);
        allPlants.add(blover);
        allPlants.add(coldSnapdragon);
    }



    @Override
    public void init()
    {
        selected = new boolean[noTotalPlants];
        for (int i=0 ; i<noTotalPlants ; i++) {
            selected[i] = false;
        }
    }

    private boolean hitSelectOKButton(int x , int y)
    {
        int okX = GridLogic.getSelectOKX();
        int okY = GridLogic.getSelectOKY();
        int okWidth = GridLogic.getSelectOKWidth();
        int okHeight = GridLogic.getSelectOKHeihgt();

        return (x>okX && x<okX+okWidth && y>okY && y<okY+okHeight);
    }

    private boolean inPlantGrid(int x , int y , int row , int col)
    {
        // TODO: GridLogic, more accurate hit test
        return (row>=0 && col>=0 && col< GridLogic.getSelectAllCols());

    }

    public boolean unselectPlant(Plant plant)
    {
        for (int i=0 ; i<noTotalPlants ; i++) {
            if (plant==allPlants.get(i)) {
                selected[i] = false;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        super.onTouch(event) ;

        Resources resources = Game.getResources();
        int x = (int) event.getX();
        int y = (int) event.getY();

        // check ok button
        if (hitSelectOKButton(x,y)&&Game.selectionFull()) {
            Game.finishSelection();
            return true;
        }

        // TODO: GridLogic
        int allX = GridLogic.getSelectAllX();
        int allY = GridLogic.getSelectAllY();
        int row = (y-allY-20)/100;
        int col = (x-allX-20)/100 ;

        if (inPlantGrid(x,y,row,col)) {
            int plantNo = row*GridLogic.getSelectAllCols() + col ;
            if (plantNo<noTotalPlants && !selected[plantNo]) {
                if (!Game.selectionFull()) {
                    // add to plants
                    Game.addPlantSelection((Plant)allPlants.get(plantNo));
                    selected[plantNo] = true;
                    return true;
                }
            }
        }

        return false;
    }


    @Override
    public void onDraw(Canvas canvas, Paint paint)
    {
        super.onDraw(canvas, paint);

        // draw border
        int x = GridLogic.getSelectAllX();
        int y = GridLogic.getSelectAllY();
        int width = GridLogic.getSelectAllWidth();
        int height = GridLogic.getSelectAllHeight();
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(5);
        canvas.drawRect(x , y , x+width, y+height, paint);

        // draw ok button
        int okX = GridLogic.getSelectOKX();
        int okY = GridLogic.getSelectOKY();
        int okWidth = GridLogic.getSelectOKWidth();
        int okHeight = GridLogic.getSelectOKHeihgt();
        canvas.drawRect(okX , okY , okX+okWidth , okY+okHeight , paint);
        paint.setColor(Color.WHITE);
        paint.setTextSize(30);
        canvas.drawText("LET'S ROCK!" , okX+20 , okY+40 , paint) ;

        // draw each plant
        for (int i = 0 ; i<noTotalPlants ; i++) {
            Plant plant = (Plant)allPlants.get(i) ;

            int columnsPerRow = GridLogic.getSelectAllCols();
            int row = (i/columnsPerRow);
            int col = i - row*columnsPerRow;
            // TODO: GridLogic
            plant.setX(x+20+col*100);
            plant.setY(y+20+row*100);
            plant.drawSelect(canvas , paint); ;
            // TODO: indicate if already selected
        }
    }
}
