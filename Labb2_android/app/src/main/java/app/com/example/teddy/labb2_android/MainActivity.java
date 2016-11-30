package app.com.example.teddy.labb2_android;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private int width, height;
    private NineMensMorrisGameLayout nineMensActivityLayout;
    private NineMenMorrisRules nineMenMorrisRules;
    private boolean fromSelected;
    private int from;
    private int color;
    int[] whiteCheckers;
    int[] blackCheckers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("OnCreate", "yes!");
        nineMensActivityLayout = new NineMensMorrisGameLayout(this);
        setContentView(nineMensActivityLayout);
        initScreenSize();
        nineMenMorrisRules = new NineMenMorrisRules();
        color = from =-1;
        whiteCheckers = new int[9];
        blackCheckers = new int[9];
        Arrays.fill(whiteCheckers, -1);
        Arrays.fill(blackCheckers, -1);

    }

    private void initScreenSize() {
        Display display = ((WindowManager)this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        this.width = size.x;
        this.height = size.y;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP)
        {

            //Log.v("screen size;" , "x="+width + " Y=" + height);
            //Log.v("View size;" , "x="+nineMensActivityLayout.getWidthSize() + " Y=" + nineMensActivityLayout.getHeightSize());

            float viewX =  event.getX()-(width-nineMensActivityLayout.getWidthSize());
            float viewY = event.getY()-(height-nineMensActivityLayout.getHeightSize());
            //Log.v("diff", " d=" + (height-nineMensActivityLayout.getHeight()));
            //Log.v("View size;" ,"viewX="+viewX + " viewY=" + viewY );

            int result = nineMensActivityLayout.getAvailablePos(viewX,viewY);
            Log.v("board Position"," =  "+ result+"\n");

            if(nineMenMorrisRules.board(result) >= 0)
            {
                fromSelected =  true;
                from = result;
                color = nineMenMorrisRules.board(result);
            }

            if(fromSelected && from >=0 && nineMenMorrisRules.legalMove(result, from, color))
            {
                //to, from, color
                if(nineMenMorrisRules.getAmountOfWhiteCheckers() >0 || nineMenMorrisRules.getAmountOfWhiteCheckers() >0)
                {
                    int i=0;
                    for(int checker : nineMenMorrisRules.getGameplan())
                    {

                        if(checker == 4) //WHITE_MARKER
                        {
                            whiteCheckers[i] = i;
                        }
                        else if(checker == 5)//BLACK_MARKER
                        {
                            blackCheckers[i] = i;
                        }
                        else
                        {
                            whiteCheckers[i] = blackCheckers[i] = checker;
                            Log.v("In for-loop:", "checker from board = " + checker);
                        }
                        i++;
                    }
                }
            }


            nineMensActivityLayout.drawCheckers(whiteCheckers, blackCheckers);
        }
        return super.onTouchEvent(event);
    }
}
