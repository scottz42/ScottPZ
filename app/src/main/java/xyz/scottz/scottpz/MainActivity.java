package xyz.scottz.scottpz;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;


// TODO: new plant placement: only allowed positions; track space usage(one plant per space)
// TODO: object interaction: zombie eats plant
// TODO: placement of multiple types of plants (plant selection)
// TODO: zombie generation
// 1. one zombie 2.
// TODO: transparency:
// TODO: temporary object generation eg. flying beans; pea kills zombie
// TODO: background & transparency
// TODO: falling sun
// TODO: sun accounting
// TODO: state saving/loading


public class MainActivity extends AppCompatActivity implements View.OnTouchListener {


    int x, y;
    int rockX, rockY;
    Timer timer;

    boolean right;
    boolean collision = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Game.setMajors(new ArrayList<MajorObject>());

        FrameLayout frame = (FrameLayout) findViewById(R.id.MainLayout);
        final CustomView vFrame = new CustomView(this);
        vFrame.setOnTouchListener(this);
        frame.addView(vFrame);

/*
        Button b1=(Button)findViewById(R.id.left);
        b1.setX((float)100);  // TODO: not proper way
        b1.setY((float)800);
        Button b2=(Button)findViewById(R.id.right);
        b2.setX((float)500); b2.setY((float)800);

        // left button
        b1.setOnClickListener(new View.OnClickListener() {

                                  @Override
                                  public void onClick(View v) {
                                      if (x > 20) {
                                          x -= 20;
                                      } else {
                                          x = 20;
                                      }
                                      vFrame.invalidate();
                                  }
                              });
            // right button
        b2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (collision) {
                    collision = false ;
                    rockX = -1 ;

                } else {
                    if (x < 600) {
                        x += 20;
                    } else {
                        x = 600;
                    }
                }
                vFrame.invalidate();
            }
        });
*/
        rockX = (int) (Math.random() * 600);
        rockY = 10;

        x = 400;
        y = 500;
        right = true;

        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                /*
                if (!collision) {
                    if (rockX < 0) {  // no rock right now
                        rockX = (int) (Math.random() * 600);
                        rockY = 10;
                    } else {
                        rockY += 10;
                    }
                    if (rockY > 600) {
                        rockX = -1;
                    }
                    collision = collide(rockX, rockY, x, y);
                }
                */
                for (MajorObject o : Game.getMajors()) {
                    o.Move();   // zombie move; sunflower generate flower; zombie damages plant
                }

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        vFrame.invalidate();
                    }
                });
            }
        };
        timer.schedule(task, 50, 50);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d(null, "OnTouchListener--onTouch-- action=" + event.getAction() + " --" + v);

        // TODO: align to grid; check emptiness
        // TODO: pick different plants
        NormalPea pea = new NormalPea(getResources()) ;
        pea.setX((int)event.getX()) ;
        pea.setY((int)event.getY()) ;
        Game.getMajors().add(pea) ;

        v.invalidate();
        return false;
    }


    class CustomView extends View {

        Paint p;

        public CustomView(Context context) {
            super(context);
            p = new Paint();
            p.setColor(Color.BLUE);
            p.setStrokeWidth(20);
            p.setStyle(Paint.Style.STROKE);
            //x =100; y = 600 ;


            for (x = 100; x < 300; x += 100) {
                for (y = 100; y < 500; y += 100) {
                    Sunflower sunflower = new Sunflower(this.getResources());
                    sunflower.setX(x);
                    sunflower.setY(y);
                    Game.getMajors().add(sunflower);
                }
            }

            for (x = 300; x < 500; x += 100) {
                for (y = 100; y < 500; y += 100) {
                    NormalPea pea1 = new NormalPea(this.getResources());
                    pea1.setX(x);
                    pea1.setY(y);
                    Game.getMajors().add(pea1);
                }
            }

            for (x = 1000; x < 1100; x += 100) {
                for (y = 100; y < 200; y += 100) {
                    NormalZombie pea1 = new NormalZombie(this.getResources());
                    pea1.setX(x);
                    pea1.setY(y);
                    Game.getMajors().add(pea1);
                }
            }

            ConeheadZombie z2 = new ConeheadZombie(this.getResources());
            z2.setX(1100);
            z2.setY(300);
            Game.getMajors().add(z2);
        }


        @Override
        protected void onDraw(Canvas canvas) {

            for (MajorObject o : Game.getMajors()) {
                o.Draw(canvas, p);
            }
            Log.d("x=", Integer.toString(x));

        }
    }
}
