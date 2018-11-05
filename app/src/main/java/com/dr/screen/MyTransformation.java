package com.dr.screen;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;


public class MyTransformation implements ViewPager.PageTransformer {
    private static final float MIN_SCALE=0.9f;
    @Override
    public void transformPage(View page, float position) {
        float scaleFactor=Math.max(MIN_SCALE,1-Math.abs(position));
        float rotate=60*Math.abs(position);
        if (position<-1){
            page.setScaleX(MIN_SCALE);
            page.setScaleY(MIN_SCALE);
//            page.setRotationY(-rotate);
            Log.i("111","--------------小于-1");

        }else if (position < 0){
            page.setScaleX(MIN_SCALE);
            page.setScaleY(MIN_SCALE);
            page.setRotationY(-rotate);
            Log.i("111","--------------小于0");

        }else if (position <=1){
            page.setScaleX(MIN_SCALE);
            page.setScaleY(MIN_SCALE);
            page.setRotationY(rotate);
            Log.i("111","--------------小于=1");

        }
        else if (position>1) {
            page.setScaleX(MIN_SCALE);
            page.setScaleY(MIN_SCALE);
//            page.setScaleX(scaleFactor);
//            page.setScaleY(scaleFactor);
//            page.setRotationY(rotate);
            Log.i("111","--------------大于1");
        }


    }
}
