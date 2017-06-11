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


// TODO: zombie generation // 1. one zombie 2.
// TODO: sun accounting
// TODO: transparency:
// TODO: background
// TODO: falling sun
// TODO: state saving/loading


public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    int x, y;
    Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Game.init(getResources()) ;

        FrameLayout frame = (FrameLayout) findViewById(R.id.MainLayout);
        final CustomView vFrame = new CustomView(this);
        vFrame.setOnTouchListener(this);
        frame.addView(vFrame);

        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Game.onTimer();

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

        Game.onTouch(event);

        v.invalidate();
        return false;
    }


    class CustomView extends View {

        Paint p;

        public CustomView(Context context) {
            super(context);
            p = new Paint();

            for (x = 1000; x < 1100; x += 100) {
                for (y = 100; y < 200; y += 100) {
                    NormalZombie pea1 = new NormalZombie(this.getResources());
                    pea1.setX(x);
                    pea1.setY(y);
                    Game.getMajors().add(pea1);
                }
            }
/*
            ConeheadZombie z2 = new ConeheadZombie(this.getResources());
            z2.setX(1100);
            z2.setY(300);
            Game.getMajors().add(z2);
  */
        }


        @Override
        protected void onDraw(Canvas canvas)
        {
            Game.onDraw(canvas , p);
        }
    }
}
