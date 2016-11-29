package app.com.example.teddy.labb2_android;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {
    int width, height;
    NineMensMorrisGameLayout nineMensActivityLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        nineMensActivityLayout = new NineMensMorrisGameLayout(this);
        setContentView(nineMensActivityLayout);
    }
}
