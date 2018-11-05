package com.dr.screen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MyGallaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.dr.screen.MyGallaryLayout myGallaryLayout =new com.dr.screen.MyGallaryLayout(this);
        setContentView(myGallaryLayout);
    }
}
