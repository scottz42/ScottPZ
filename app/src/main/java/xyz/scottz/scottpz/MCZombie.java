package xyz.scottz.scottpz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by scott on 9/5/2017.
 */

public class MCZombie extends Zombie {
        private static Bitmap bitmap=null ;
        private static Bitmap spinBitmap=null;
        private long lastSpinTime =0;
        private long spinDuration = 1000;   // ms
        private double spinDamage=40;
        private boolean inflictedDamage=false;  // true if damage has been inflicted already

        MCZombie()
        {
            super();
            life = 12 ;
            damagePerAttack=0;  // no direct attack in jam mode
            lastSpinTime = 0;
            if (bitmap==null) {
                bitmap = BitmapFactory.decodeResource(Game.getResources(), R.drawable.mczomb);
                spinBitmap = BitmapFactory.decodeResource(Game.getResources(), R.drawable.mczombiespin);
            }
        }

        @Override
        void Draw(Canvas canvas , Paint p) {
            super.Draw(canvas,p);

            Rect src = new Rect() ;
            Rect dst = new Rect() ;
            src.set(0,0,bitmap.getWidth()-1,bitmap.getHeight()-1);
            dst.set(getX(), getY(), getX() + (shrunk?GridLogic.getZombieWidth()/2:GridLogic.getZombieWidth()),
                    getY() + (shrunk?GridLogic.getZombieHeight()/2:GridLogic.getZombieHeight()));

            canvas.drawBitmap(bitmap, src,dst,p);

            // draw spin
            if ((System.currentTimeMillis()-lastSpinTime)<=spinDuration) {
                src.set(0, 0, spinBitmap.getWidth() - 1, spinBitmap.getHeight() - 1);
                if (shrunk) {
                    dst.set(getX() , getY() , getX() + GridLogic.getGridWidth(),
                            getY() + GridLogic.getGridHeight());
                } else {
                    dst.set(getX() - GridLogic.getGridWidth(), getY() - GridLogic.getGridHeight(), getX() + 2 * GridLogic.getGridWidth(),
                            getY() + 2 * GridLogic.getGridHeight());
                }
                canvas.drawBitmap(spinBitmap, src, dst, p);
            }
        }

        @Override
        void Move()
        {
            super.Move();

            int row = GridLogic.calcRow(getY());
            int col = GridLogic.calcCol(getX());

            // just finished spin, inflict damage
            if (!inflictedDamage && lastSpinTime>0 && (System.currentTimeMillis()-lastSpinTime)>spinDuration) {
                for (MajorObject o : Game.getMajors()) {
                    if (o.isPlant()) {
                        Plant plant = (Plant) o;
                        // hurt plants in 3x3 square
                        int pRow = GridLogic.calcRow(o.getY());
                        int pCol = GridLogic.calcCol(o.getX());
                        if (shrunk?(pRow==row && pCol==col):((pRow - row) >= -1 && (pRow - row) <= 1) &&
                                ((pCol - col) >= -1 && (pCol - col) <= 1)) {
                            plant.setLife(plant.getLife() - spinDamage);
                            if (plant.getLife()<0) {
                                Game.removePlant(plant);
                            }
                        }
                    }
                }
                inflictedDamage = true;
            }

            // initiate new spin
            if ((System.currentTimeMillis()-lastSpinTime)>spinDuration && (lastSpinTime==0 || inflictedDamage) &&
                    (shrunk?GridLogic.existPlant(col,row):GridLogic.existPlant3x3(row,col))!=null) {
                lastSpinTime = System.currentTimeMillis();
                inflictedDamage = false;
            }

    }

}
