package xyz.scottz.scottpz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;


/**
 * Created by scott on 8/17/2017.
 */

public class Blover extends Plant {

        private static long rechargeTime = 5000 ;      // ms
        private static long explodeTime = 200 ;    // ms

        private long plantTime;
        private static Bitmap bitmap=null;
        private static Bitmap selectBitmap=null;

        public static long getRechargeTime()
        {
            return rechargeTime ;
        }

        Blover()
        {
            super();
            setSunNeeded(50);
            damagePerShot = 0 ;    // nds
            if (bitmap==null) {
                bitmap = BitmapFactory.decodeResource(Game.getResources(), R.drawable.blover);
                selectBitmap = BitmapFactory.decodeResource(Game.getResources(), R.drawable.bloverslect);
            }
            plantTime = System.currentTimeMillis();
        }

        @Override
        void Draw(Canvas canvas , Paint p) {
            super.Draw(canvas,p);

            Rect src = new Rect() ;
            Rect dst = new Rect() ;
            src.set(0,0,bitmap.getWidth()-1,bitmap.getHeight()-1);
            dst.set(getX(), getY(), getX() + GridLogic.getPlantWidth(), getY() + GridLogic.getPlantHeight());

            canvas.drawBitmap(bitmap, src,dst,p);
        }


        @Override
        void drawSelect(Canvas canvas , Paint p) {
            super.Draw(canvas,p);

            Rect src = new Rect() ;
            Rect dst = new Rect() ;
            src.set(0,0,selectBitmap.getWidth()-1,selectBitmap.getHeight()-1);
            dst.set(getX(), getY(), getX() + GridLogic.getSelectWidth(), getY() + GridLogic.getSelectHeight());

            canvas.drawBitmap(selectBitmap, src,dst,p);
        }



        @Override
        void Move()
        {
            if ((System.currentTimeMillis()-plantTime)>explodeTime) {

                int row = GridLogic.calcRow(getY());
                int col = GridLogic.calcCol(getX());

                for (MajorObject o : Game.getMajors()) {
                    if (!o.isPlant()) {
                        Zombie zombie = (Zombie) o;
                        if (zombie.isFlying()) {
                            zombie.takeDamage(1000);
                        }
                    }
                }
                Game.removePlant(this);

            }
        }


    }


