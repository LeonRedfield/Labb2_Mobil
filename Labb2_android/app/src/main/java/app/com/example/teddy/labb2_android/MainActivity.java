package app.com.example.teddy.labb2_android;

import android.content.Context;
import android.content.Intent;
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
    boolean move;
    boolean remove;
    int checkerRemoved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("OnCreate", "yes!");
        nineMensActivityLayout = new NineMensMorrisGameLayout(this);
        setContentView(nineMensActivityLayout);
        initScreenSize();
        nineMenMorrisRules = new NineMenMorrisRules();
        from =-1;
        whiteCheckers = new int[9];
        blackCheckers = new int[9];
        Arrays.fill(whiteCheckers, -1);
        Arrays.fill(blackCheckers, -1);
        color = nineMenMorrisRules.getTurn();
        checkerRemoved = -1;
        //fromSelected = false;

    }

    private void initScreenSize() {
        Display display = ((WindowManager)this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        this.width = size.x;
        this.height = size.y;
    }

    @Override
    protected void onStart() {
        super.onResume();
        showPlayersTurn(color);
        Log.v("onRESUME", "yes");
        initScreenSize();
        nineMensActivityLayout.updateScreen(whiteCheckers, blackCheckers);
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
            move = false;
            Arrays.fill(whiteCheckers, -1);
            Arrays.fill(blackCheckers, -1);
           Toast.makeText(this, ((nineMenMorrisRules.getTurn()==1)?"white's turn":"black's turn"), Toast.LENGTH_SHORT);
            initScreenSize();
            float viewX =  event.getX()-(width-nineMensActivityLayout.getWidthSize());
            float viewY = event.getY()-(height-nineMensActivityLayout.getHeightSize());


            int result = nineMensActivityLayout.getAvailablePos(viewX,viewY);

            Log.v("board Position"," =  "+ result+"\n");
            if(remove)
            {
                int markerColor = nineMenMorrisRules.getTurn();
                Log.v("markerColo=", ""+markerColor);
                if(markerColor == 1){color = 4;}
                else if(markerColor == 2){color = 5;}

                if(nineMenMorrisRules.remove(result, color))
                {
                    checkerRemoved = result;
                    remove = false;
                    move = true;
                    Log.v("toRemove=", "=" + checkerRemoved);
                    if(nineMenMorrisRules.win(color))
                    {
                        Toast.makeText(this,((nineMenMorrisRules.getTurn()==1)?"black's the Winner":"whites's the Winner"), Toast.LENGTH_LONG);
                        Log.v("winn!!","!!!!!");
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                }
                else
                {
                    Log.v("remove failed","!!!!!!");
                }

            }
            else if(nineMenMorrisRules.getAmountOfBlackCheckers() == 0 && nineMenMorrisRules.getAmountOfWhiteCheckers() ==0)
            {
                if(!fromSelected)
                {
                    from = result;
                    fromSelected = true;
                    color = nineMenMorrisRules.getPlayerWithZone(result);
                    Log.v("Selected", " from ="+from+" color=" +color+" result="+result);
                }
                else
                {
                    move = nineMenMorrisRules.legalMove(result, from, color);
                    fromSelected = false;
                }
                Log.v("board from"," =  "+ from+"\n");
            }
            else
            {
                //Log.v("ib´n","ELSE");
                move = nineMenMorrisRules.legalMove(result, from, color);
                color = nineMenMorrisRules.getTurn();
            }


            if(move && nineMenMorrisRules.remove(result))
            {
                Log.v("in remove", "erwwrewr00");
                remove = true;
                Toast.makeText(this,"3 in a row remove any oponent checker",Toast.LENGTH_SHORT);
            }
                    if(move)
                    {
                        Log.v("board from", " =  " + from + " TO=" + result);

                        int i = 0;
                        int white_index = 0;
                        int black_index = 0;

                        for (int checker : nineMenMorrisRules.getGameplan()) {
                            //Log.v("checker =", "" + checker + " i="+i);
                            if (checker == 4) //WHITE_MARKER
                            {
                                whiteCheckers[white_index] = i;
                                // Log.v("white_c", "="+ whiteCheckers[white_index] + "i="+i + "Windex="+white_index);
                                white_index++;
                            } else if (checker == 5)//BLACK_MARKER
                            {
                                blackCheckers[black_index] = i;
                                // Log.v("black_c", "="+ blackCheckers[black_index] + "i="+i+"bindex=" + black_index);
                                black_index++;
                            }

                            i++;
                        }


                        if(nineMenMorrisRules.getAmountOfWhiteCheckers() == 0)
                        {
                            nineMensActivityLayout.setMainPhaseWhitePlayer();
                        }
                        if( nineMenMorrisRules.getAmountOfBlackCheckers() ==  0)
                        {
                            nineMensActivityLayout.setMainPhaseBlackPlayer();
                        }
                        nineMensActivityLayout.drawCheckers(whiteCheckers, blackCheckers);

                    }

        }
        return super.onTouchEvent(event);
    }
}
