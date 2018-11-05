package com.dr.screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    private Button start_button;
    private Button update_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start_button = (Button)findViewById(R.id.button_start);
        update_button = (Button)findViewById(R.id.button_update);
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent start_intent = new Intent(MainActivity.this,ScreenActivity.class);
                startActivity(start_intent);
            }
        });
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent update_intent = new Intent(MainActivity.this,UpdateActivity.class);
                startActivity(update_intent);
            }
        });

    }
}
