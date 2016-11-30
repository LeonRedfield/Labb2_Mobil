package app.com.example.teddy.labb2_android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Teddy on 2016-11-29.
 */

public class NineMensMorrisGameLayout extends View {
    private Paint blackPaintBrush;
    private Paint blackPaintBrushFill;
    private Paint whitePaintBrushFill;
    private int screenWidth;
    private int screenHeight;
    private float[][] dotsXAndY;
    private float[][] player1Checkers;
    private float[][] player2Checkers;
    Rect outerRectangle ;
    Rect innerRect ;
    Rect middleRect ;
    //lines
    private int outerLeft ;
    private int outerTop ;
    private int outerRight ;
    private  int outerBottom ;
    private int middleLeft;
    private  int middleTop ;
    private  int middleRight ;
    private int middleBottom;
    private  int innerLeft ;
    private int innerTop ;
    private int innerRight ;
    private int innerBottom ;
    //
    float verticalStartAndStopX ;
    float horizontalStartAndStopY ;
    //
    int screenConstant;
    //
    boolean mainPhase;

    float player1cheskXPos = 0;
    float player1cheskYPos =0;
    float player2cheskXPos = 0;
    float player2cheskYPos =0;


    public NineMensMorrisGameLayout(Context context) {
        super(context);
        setBackgroundResource(R.drawable.ninemenboard);
        player1Checkers = new float[9][2];
        player2Checkers = new float[9][2];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        screenHeight = getHeight();
        screenWidth = getWidth();
        Log.v("height", "" + getHeight());
        Log.v("width", "" + getWidth());
        screenConstant = Math.min(screenHeight, screenWidth);
        initPaint(screenConstant);
        initLines();
        initBoardDots();
        drawNineBoard(screenConstant, canvas);
    }



    private void initPaint(int tmp)
    {
        blackPaintBrush = new Paint();
        blackPaintBrush.setColor(Color.BLACK);
        blackPaintBrush.setStyle(Paint.Style.STROKE);
        blackPaintBrush.setStrokeWidth(new Double(tmp*0.01).intValue());

        blackPaintBrushFill =  new Paint();
        blackPaintBrushFill.setColor(Color.BLACK);
        blackPaintBrushFill.setStyle(Paint.Style.FILL);
        blackPaintBrushFill.setStrokeWidth(new Double(tmp*0.02).intValue());

        whitePaintBrushFill =  new Paint();
        whitePaintBrushFill.setColor(Color.WHITE);
        whitePaintBrushFill.setStyle(Paint.Style.FILL);
    }

    private void drawNineBoard(int screenConstant, Canvas canvas)
    {
        canvas.drawRect(outerRectangle, blackPaintBrush);
        canvas.drawRect(middleRect, blackPaintBrush);
        canvas.drawRect(innerRect, blackPaintBrush);
        //vertical line:
        //startx, startY,Stopx, StopY, paint
        canvas.drawLine(verticalStartAndStopX, outerTop, verticalStartAndStopX, outerBottom, blackPaintBrush);
        //horizontal line:
        canvas.drawLine(outerLeft, horizontalStartAndStopY, outerRight, horizontalStartAndStopY, blackPaintBrush);
        //draw circles on board:
        //24 circles:
        for(int i = 0; i< dotsXAndY.length; i++)
        {
            canvas.drawCircle(dotsXAndY[i][0], dotsXAndY[i][1],(float) (screenConstant*0.01), blackPaintBrush);
        }


        int highestSize = Math.max(screenHeight, screenWidth);
        if(!mainPhase)
        {
            //begining phase not done
            if(screenWidth < screenHeight)
            {
                player1cheskXPos = (float)(screenConstant*0.2);
                player1cheskYPos = (float) (highestSize*0.65);

                player2cheskXPos = (float)(screenConstant*0.8);
                player2cheskYPos = (float) (highestSize*0.65);
            }
            else {
                player1cheskXPos = (float)(screenWidth*0.65);
                player1cheskYPos = (float) (screenHeight*0.8);

                player2cheskXPos = (float)(highestSize*0.65);
                player2cheskYPos = (float) (screenConstant*0.2);
            }
        }


        for(int i=0;i<player1Checkers.length;i++)
        {
            if(player1Checkers[i][0] >= 0)
            {
                canvas.drawCircle( player1Checkers[i][0] , player1Checkers[i][1], (float)(screenConstant*0.05),blackPaintBrushFill);
                canvas.drawCircle( player2Checkers[i][0] ,player2Checkers[i][0], (float)(screenConstant*0.05),whitePaintBrushFill);
            }
        }
    }

    private void initLines()
    {
        //OuterRect:
        outerRectangle = new Rect();
        //lef, top, right, bottom
        outerLeft = new Double((screenConstant * 0.1)).intValue();
        outerTop = new Double((screenConstant * 0.1)).intValue();
        outerRight = screenConstant - (new Double((screenConstant * 0.1)).intValue());
        outerBottom = screenConstant - (new Double((screenConstant * 0.1)).intValue());

        outerRectangle.set(outerLeft, outerTop, outerRight, outerBottom);
         verticalStartAndStopX = (float) ((outerRight + outerLeft) / 2);
         horizontalStartAndStopY = (float) ((outerBottom + outerTop) / 2);
        //middle rect:
        double middlePercentage = 0.22;
        middleRect = new Rect();
        middleLeft = new Double((screenConstant * middlePercentage)).intValue();
        middleTop = new Double((screenConstant * middlePercentage)).intValue();
        middleRight = screenConstant - (new Double((screenConstant * middlePercentage)).intValue());
        middleBottom = screenConstant - (new Double((screenConstant * middlePercentage)).intValue());
        middleRect.set(middleLeft, middleTop, middleRight, middleBottom);


        //inner Rect:
        double innerPercentage = 0.34;
        innerRect = new Rect();
        innerLeft = new Double((screenConstant * innerPercentage)).intValue();
        innerTop = new Double((screenConstant * innerPercentage)).intValue();
        innerRight = screenConstant - (new Double((screenConstant * innerPercentage)).intValue());
        innerBottom = screenConstant - (new Double((screenConstant * innerPercentage)).intValue());
        innerRect.set(innerLeft, innerTop, innerRight, innerBottom);
        Log.v("outer:" , "outerB="+outerBottom + "middleB=" + middleBottom);
    }

    private void initBoardDots()
    {
        //cx, cy, radius, paint;
        float middleX = verticalStartAndStopX;
        float middleY = horizontalStartAndStopY;

        float[][] cyAndcyTemp = { {outerLeft,outerTop},{middleX,outerTop}, {outerRight,outerTop},
                {outerRight,middleY}, { outerBottom,outerRight},
                {middleX,outerBottom},{outerLeft,outerBottom},
                {outerLeft,middleY},

                {middleLeft,middleTop},{middleX,middleTop}, {middleRight,middleTop},
                {middleRight,middleY}, { middleBottom,middleRight},
                {middleX,middleBottom},{middleLeft,middleBottom},
                {middleLeft,middleY},

                {innerLeft,innerTop},{middleX,innerTop}, {innerRight,innerTop},
                {innerRight,middleY}, {innerBottom,innerRight},
                {middleX,innerBottom},{innerLeft,innerBottom},
                {innerLeft,middleY},
        };
        dotsXAndY = cyAndcyTemp;
    }

    public int getAvailablePos(float posX, float posY) {

        for (int i=0;i<dotsXAndY.length;i++)
        {
            if(posX <= (dotsXAndY[i][0] +30) && posX >= (dotsXAndY[i][0] - 30 )&&
                posY>= dotsXAndY[i][1]-30 && posY <= dotsXAndY[i][1]+30)
            {
                return i;
            }
        }
        return 23;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.v("In view:", " X="+event.getY() + " Y=" + event.getY());
        return super.onTouchEvent(event);
    }

    public float getWidthSize()
    {
        return this.screenWidth;
    }
    public float getHeightSize()
    {
        return this.screenHeight;
    }

    public void setMainPhase()
    {
        this.mainPhase = true;
    }

    public void drawCheckers(int[] whiteCheckers , int[] blackCheckers) {
        for(int i=0;i<whiteCheckers.length;i++)
        {
            if(whiteCheckers[i] != -1)
            {
                player1Checkers[i][0] = dotsXAndY[whiteCheckers[i]][0];
                player1Checkers[i][1] = dotsXAndY[whiteCheckers[0]][1];
            }
            else
            {
                player1Checkers[i][0] = -50;
                player1Checkers[i][1] = -50;
            }

            if(blackCheckers[i] != -1)
            {
                player2Checkers[i][0] = dotsXAndY[blackCheckers[0]][0];
                player2Checkers[i][0] = dotsXAndY[blackCheckers[0]][1];
            }
            else
            {
                player2Checkers[i][0] = -50;
                player2Checkers[i][0] = -50;
            }
        }
        invalidate();
    }
}
