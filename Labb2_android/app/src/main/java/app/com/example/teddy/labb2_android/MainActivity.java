package app.com.example.teddy.labb2_android;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
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
    private int oldHeight;
    private int oldWidth;
    int[] whiteCheckers;
    int[] blackCheckers;
    int[] whiteCheckersBackUp;
    int[] blackCheckersBackUp;
    boolean move;
    boolean remove;
    int checkerRemoved;
    boolean rotation;

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
        this.width = getResources().getDisplayMetrics().widthPixels;
        this.height = getResources().getDisplayMetrics().heightPixels;
    }

    @Override
    protected void onResume() {
        super.onResume();
        showPlayersTurn(color);
        Log.v("onRESUME", "yes");
        //initScreenSize();
        //nineMensActivityLayout.updateScreen(whiteCheckers, blackCheckers);
        // Obtain MotionEvent object
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(whiteCheckersBackUp != null && blackCheckersBackUp !=null)
        {
            nineMensActivityLayout.drawCheckers(whiteCheckersBackUp, blackCheckersBackUp);
        }
    }

    private void updateView()
    {
        long downTime = 100;
        long eventTime = 100;
        int nodeWithChecker = nineMenMorrisRules.getAnyCheckerInBoard();
        Log.v("node","="+nodeWithChecker);
        float[] coor = nineMensActivityLayout.getCoordinatesFromNode(24);
        Log.v("coor"," X="+coor[0]+" Y="+coor[1]);
        float x = coor[0];
        float y = coor[1];

// List of meta states found here:     developer.android.com/reference/android/view/KeyEvent.html#getMetaState()
        int metaState = 0;
        MotionEvent motionEvent = MotionEvent.obtain(
                downTime,
                eventTime,
                MotionEvent.ACTION_UP,
                x,
                y,
                metaState
        );
        this.dispatchTouchEvent(motionEvent);
        Log.v("updateScreen","!!!!!!!!!!!!!!!!!!1");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE || newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //initScreenSize();
            //updateView();

        }
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
            Log.v("Touch UP", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1");
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
                    Log.v("toRemove=", "=" + checkerRemoved + "color="+color);
                    if(nineMenMorrisRules.win(color))
                    {
                        Toast.makeText(this,((nineMenMorrisRules.getTurn()==1)?"black's the Winner":"whites's the Winner"), Toast.LENGTH_LONG);
                        Log.v("winn!!","!!!!!");
                        restartActivity();
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
                        whiteCheckersBackUp = whiteCheckers;
                        blackCheckersBackUp = blackCheckers;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.settings)
        {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        GameSaver saver = new GameSaver(this);
        saver.saveGame(nineMenMorrisRules);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        GameSaver loader = new GameSaver(this);
        this.nineMenMorrisRules = loader.loadGame();
    }

    public void restartActivity()
    {
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage( getBaseContext().getPackageName() );
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}
