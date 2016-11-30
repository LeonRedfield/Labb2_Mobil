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
        from =-1;
        color = 2;
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
    protected void onResume() {
        super.onResume();
        showPlayersTurn(color);

    }

    private void showPlayersTurn(int color)
    {
        if(color == 2)
        {
            Toast.makeText(this,"black's turn", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this,"white's turn", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP)
        {

            //Log.v("screen size;" , "x="+width + " Y=" + height);
            //Log.v("View size;" , "x="+nineMensActivityLayout.getWidthSize() + " Y=" + nineMensActivityLayout.getHeightSize());
            initScreenSize();
            float viewX =  event.getX()-(width-nineMensActivityLayout.getWidthSize());
            float viewY = event.getY()-(height-nineMensActivityLayout.getHeightSize());
            //Log.v("diff", " d=" + (height-nineMensActivityLayout.getHeight()));
            //Log.v("View size;" ,"viewX="+viewX + " viewY=" + viewY );

            int result = nineMensActivityLayout.getAvailablePos(viewX,viewY);
            //Log.v("board Position"," =  "+ result+"\n");
            from = result;

            Log.v("result: ", "=" + result + "from = "+ from + "color= "+color );
            if(from >=0 && nineMenMorrisRules.legalMove(result, result, color))
            {
                int i=0;
                int white_index =0;
                int black_index =0;
                if(nineMenMorrisRules.getAmountOfBlackCheckers() != 0)
                {
                    whiteCheckers[whiteCheckers.length-1] = 24;
                }
                if(nineMenMorrisRules.getAmountOfWhiteCheckers() != 0)
                {
                    blackCheckers[blackCheckers.length-1] = 24;
                }
                Log.v("lastB",  "!!!!!!!!!!!!"+blackCheckers[blackCheckers.length-1]);
                    for(int  checker : nineMenMorrisRules.getGameplan())
                    {
                        //Log.v("checker =", "" + checker + " i="+i);
                        if(checker == 4) //WHITE_MARKER
                        {
                            whiteCheckers[white_index] = i;
                            Log.v("white_c", "="+ whiteCheckers[white_index] + "i="+i + "Windex="+white_index);
                            white_index++;
                        }
                        else if(checker == 5)//BLACK_MARKER
                        {
                            blackCheckers[black_index] = i;
                            Log.v("black_c", "="+ blackCheckers[black_index] + "i="+i+"bindex=" + black_index);
                            black_index++;
                        }
                        i++;
                    }
                if(color==1)
                {
                    color = 2;
                }
                else
                {
                    color=1;
                }
                showPlayersTurn(color);
            }

            nineMensActivityLayout.drawCheckers(whiteCheckers, blackCheckers);
        }
        return super.onTouchEvent(event);
    }
}
