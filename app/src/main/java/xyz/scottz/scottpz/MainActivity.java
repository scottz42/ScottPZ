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


// TODO: dropping rock
// TODO: fix layout
// TODO: timer
// TODO: object storage
// TODO: new plant placement
// TODO: object movement
// TODO: object interaction
// TODO: temporary object generation eg. flying beans
// TODO: state saving/loading
// TODO: multiple types of plants
// TODO: multiple types of zombies


public class MainActivity extends AppCompatActivity implements View.OnTouchListener{


    int x , y ;
    boolean right ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout frame = (FrameLayout) findViewById(R.id.MainLayout);
        final CustomView vFrame = new CustomView(this);
        vFrame.setOnTouchListener(this);
        frame.addView(vFrame);


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
                if (x <600) {
                    x += 20;
                }else{
                    x=600;}
                vFrame.invalidate();
            }
        });



        x = 400 ;
        y = 300;
        right = true;
    }



    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d(null, "OnTouchListener--onTouch-- action="+event.getAction()+" --"+v);


        v.invalidate();
        return false;
    }



    class CustomView extends View {

        Paint p;

        public CustomView (Context context) {
            super(context);
            p = new Paint();
            p.setColor(Color.BLUE);
            p.setStrokeWidth(20);
            p.setStyle(Paint.Style.STROKE);
            //x =100; y = 600 ;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            int arm = 100 ;
            int leg = 200;
            int head=100;
            int leftLegX = x-63-arm/2;
            int leftLegY = y+63+arm/2;
            int rightLegX = x+63+arm/2;
            int rightLegY = y+63+arm/2;

            /*

            canvas.drawCircle(x,y-90-head,head,p);
            canvas.drawCircle(x,y,90,p);
            canvas.drawLine(x-63,y+63,x-63-arm,y+63+arm,p);
            canvas.drawLine(x+63,y+63,x+63+arm,y+63+arm,p);
            canvas.drawLine(leftLegX,leftLegY,leftLegX,leftLegY+leg,p);
            canvas.drawLine(rightLegX,rightLegY,rightLegX,rightLegY+leg,p);

            */


            // bitmap of pea
            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.pea1);
            Rect src = new Rect() ;
            Rect dst = new Rect() ;
            src.set(0,0,bitmap.getWidth()-1,bitmap.getHeight()-1);
            dst.set(x,y,x+92,y+88);
            canvas.drawBitmap(bitmap, src,dst,p);
            //判断图片是否回收,木有回收的话强制收回图片
            if(bitmap.isRecycled())
            {
                bitmap.recycle();
            }


            // bitmap of pea
            Bitmap bitmapRock = BitmapFactory.decodeResource(this.getResources(), R.drawable.rock1);
            Rect srcRock = new Rect() ;
            Rect dstRock = new Rect() ;
            srcRock.set(0,0,bitmapRock.getWidth()-1,bitmapRock.getHeight()-1);
            dstRock.set(200,200,260,280);
            canvas.drawBitmap(bitmapRock, srcRock,dstRock,p);
            //判断图片是否回收,木有回收的话强制收回图片
            if(bitmapRock.isRecycled())
            {
                bitmapRock.recycle();
            }


//            x += 100
// ;
//            y += 100 ;
            Log.d("x=",Integer.toString(x));
        }
    }


}
