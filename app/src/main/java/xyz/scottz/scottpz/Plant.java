package xyz.scottz.scottpz;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by lei on 2017/5/8.
 * base class for all plants
 */

public class Plant extends MajorObject {


    public Plant(Resources res) {
        super(res);
    }

    @Override
    void Draw(Canvas c ,Paint p) {
        super.Draw(c,p);
    }
}
