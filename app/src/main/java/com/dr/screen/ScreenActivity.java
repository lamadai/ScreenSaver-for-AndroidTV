package com.dr.screen;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScreenActivity extends Activity {

    private ViewPager mViewPaper;
    private List<ImageView> images;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            mViewPaper.setCurrentItem(currentItem);
            mScroller.setmDuration(1 * 800);
        }
    };
    private int currentItem;
    private ViewPagerAdapter adapter;
    private FixedSpeedScroller mScroller;

    private ScheduledExecutorService scheduledExecutorService;
    private String sd_path = "storage/emulated/0/Screen";
    private String internal_path = "data/data/com.dr.screen/Screen";

    private String[] pic = new String[]{
            "a","b","c","d","e"
    };

    int test_count = 5;

    int item_count = 0;
    int count = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);
        mViewPaper = (ViewPager) findViewById(R.id.vp);

        //显示的图片
        images = new ArrayList<ImageView>();
        for (int i = 0; i < count; i++) {
            ImageView imageView = new ImageView(this);
            String pic_path = sd_path+"/"+i+".jpg";
            File pic = new File(pic_path);
            if(pic.exists()){
                item_count++;
                Bitmap bmp_pic = BitmapFactory.decodeFile(pic_path,null);
                imageView.setImageBitmap(bmp_pic);
                images.add(imageView);
            }
        }

        mViewPaper.setAdapter(adapter = new ViewPagerAdapter(this, images));
        try{
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            mScroller = new FixedSpeedScroller(mViewPaper.getContext(),new AccelerateInterpolator());
            mField.set(mViewPaper, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //禁止手指控制滑动
        mViewPaper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 产生一个ExecutorService对象，这个对象只有一个线程可用来执行任务，若任务多于一个，任务将按先后顺序执行。
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(new ViewPageTask(), 10,10, TimeUnit.SECONDS);
    }

    private class ViewPageTask implements Runnable {
        @Override
        public void run() {
            currentItem = (currentItem + 1) % item_count;
            mHandler.sendEmptyMessage(0);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdown();
            scheduledExecutorService = null;
        }
    }

    public class ViewPagerAdapter extends PagerAdapter {
        private List<ImageView> images;
        private Context mContext;

        public ViewPagerAdapter(Context context, List<ImageView> img) {
            this.mContext = context;
            this.images = img;
        }

        //返回要滑动的VIew的个数
        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        //从当前container中删除指定位置（position）的View;
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(images.get(position));
        }

        //做了两件事，第一：将当前视图添加到container中，第二：返回当前View
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            container.addView(images.get(position));
            ImageView imageView = images.get(position);
            imageView.setBackgroundResource(R.drawable.button_style);

            imageView.setFocusable(true);
            imageView.setFocusableInTouchMode(true);
            imageView.setClickable(true);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "点击了第" + "" + position + "张图片", Toast.LENGTH_LONG).show();
                }
            });
            return imageView;
        }
    }
}
