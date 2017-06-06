package xyz.scottz.scottpz;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by lei on 2017/5/8.
 * base class for plants and zombies and other major objects that show on screen
 */

// TODO: reduce bitmap memory usage: at least share bitmap for each type, also reduce original size of image

public class MajorObject {
    public MajorObject(Resources res) {
    }

    void Draw(Canvas c, Paint p){}

    void Move(){}
}
