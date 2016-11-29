package app.com.example.teddy.labb2_android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by Teddy on 2016-11-29.
 */

public class NineMensMorrisGameLayout extends View {
    private Paint blackPaintBrush;
    private int screenWidth;
    private int screenHeight;


    public NineMensMorrisGameLayout(Context context) {
        super(context);

        setBackgroundResource(R.drawable.ninemenboard);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        initScreenSizeWidthAndHeight(context);
        Log.v("screen size"," :  screenWidth=" + screenWidth + " & screenHeight=" + screenHeight);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initPaint();
        Rect test =  new Rect();
        //lef, top, right, bottom
        int left = new Double((screenWidth*0.1)).intValue();
        int top = new Double((screenHeight*0.1)).intValue();
        int right = new Double((screenWidth*0.1)).intValue();
        int bottom = new Double((screenHeight*0.1)).intValue();

        Log.v("aligns:", "left=" + left + " top=" + top + " rigth=" + right + " bottom=" +bottom);

        test.set(left, top, screenWidth-right, screenHeight - bottom);
        canvas.drawRect(test, blackPaintBrush);
    }

    private void initScreenSizeWidthAndHeight(Context context)
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
    }

    private void initPaint()
    {
        blackPaintBrush = new Paint();
        blackPaintBrush.setColor(Color.BLACK);
        blackPaintBrush.setStyle(Paint.Style.STROKE);
        blackPaintBrush.setStrokeWidth(new Double(screenWidth*0.02).intValue());
    }


}
