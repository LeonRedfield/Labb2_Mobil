package app.com.game.teddy.labb3;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    private final double FILTER_FACTOR = 0.8;
    private final String DEGREE ="\u00b0";
    private Sensor accelerometerSensor;
    private SensorManager sensorManager;
    private double aX,aY,aZ;
    private TextView textX,textY,textZ, textAngleX, textAngleY, textAngle;
    private float prevAcc, currentAcc;
    private long lastUpdate = 0;
    private long timer = 1000;
    private long shakeTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        aX = aY = aZ = 0;
        /*textX = (TextView) findViewById(R.id.value_x);
        textY = (TextView) findViewById(R.id.value_y);
        textZ = (TextView) findViewById(R.id.value_z);
        textAngleX = (TextView) findViewById(R.id.angle_x);
        textAngleY = (TextView) findViewById(R.id.angle_y);*/
        textAngle = (TextView) findViewById(R.id.angle);
        prevAcc = currentAcc = 0;
    }

    @Override
    protected void onResume() {

        sensorManager.registerListener(this, accelerometerSensor, sensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener(this, accelerometerSensor);
        super.onPause();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        this.aX = filter(this.aX, event.values[0]);
        this.aY = filter(this.aY, event.values[1]);
        this.aZ = filter(this.aZ, event.values[2]);
        shakeDetection();
        textAngle.setText(calculateTiltAngle()+DEGREE);
        /*textX.setText("X = " + aX);
        textY.setText("Y = " + aY);
        textZ.setText("Z = " + aZ);
        textAngleX.setText("angle = " + this.calculateTiltAngle());*/
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void shakeDetection()
    {
        prevAcc = currentAcc;
        currentAcc = (float) Math.sqrt(aX*aX + aY*aY + aZ*aZ);
        float delta = Math.abs(currentAcc-prevAcc);

        if(System.currentTimeMillis() - lastUpdate >= 200)
        {
            lastUpdate = System.currentTimeMillis();
            Log.v("delta", "="+delta);
            if(delta > 0.8)
            {
                if(shakeTime == 0)
                {
                    shakeTime = System.currentTimeMillis();
                }

                if( System.currentTimeMillis() - shakeTime >= 1000)
                {
                    int red = new Random().nextInt(25)*10;
                    int green = new Random().nextInt(25)*10;
                    int blue = new Random().nextInt(25)*10;
                    textAngle.getRootView().setBackgroundColor(Color.rgb(Math.abs(red-125),Math.abs(green-125),Math.abs(blue-125)));
                    textAngle.setTextColor(Color.rgb(red, green, blue));
                    shakeTime = 0;
                }
                Log.v("Shaking", "alot");
            }
            else
            {
                shakeTime = 0;
            }
        }
    }

    private double filter(double filteredValue,double sensorValue)
    {
        //filtervalue(n) = F * filteredvalue(n-1) + (1-F) * sensorvalue(n)
        return FILTER_FACTOR * filteredValue + (1-FILTER_FACTOR) * sensorValue;
    }

    private int calculateTiltAngle()// not 100 working, jumps value for because Y isn't 0 when X has reached 90 degrees.
    {
        double anglesXandY[] = new double[2];
        double x2,y2,z2, result;
        double angleX,angleY;
        int resultAngle = -900;

        // Work out the squares
        x2 = this.aX*this.aX;
        y2 = this.aY*this.aY;
        z2 = this.aZ*this.aZ;

        //X Axis
        result=Math.sqrt(y2+z2);
        result=this.aX/result;
        anglesXandY[0] = Math.atan(result);

        //Y Axis
        result=Math.sqrt(x2+z2);
        result=this.aY/result;
        anglesXandY[1] = Math.atan(result);

        //test convert from radianer:
        //anglesXandY[0] = 57.3*anglesXandY[0];
        //anglesXandY[1] = 57.3*anglesXandY[1];
        angleX =  Math.round( Math.toDegrees(anglesXandY[0]));
        angleY =  Math.round( Math.toDegrees(anglesXandY[1]));
        //textAngleY.setText("Angle Y = " + angleY);
        if(angleY > 0)
        {
            resultAngle = (int) Math.abs(angleX);
        }
        else
        {
            resultAngle = 90 + (90-(int)Math.abs(angleX));
        }

        return resultAngle;
    }
}
