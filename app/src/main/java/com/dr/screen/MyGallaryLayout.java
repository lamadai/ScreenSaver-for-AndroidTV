package com.dr.screen;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyGallaryLayout extends LinearLayout {
    private Context mContext;
    private ViewPager mViewPager;
    private List<TextView> imgs;
    private MyAdapter myAdapter;
    private TextView textview;
    private ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(420, 530);


    public MyGallaryLayout(Context context) {
        super(context);
        this.mContext = context;
        this.setOrientation(VERTICAL);
        this.setBackgroundColor(Color.BLACK);
        this.setClipChildren(false);
        createLayout();
    }

    private void createLayout() {
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 80);
        TextView topT = new TextView(mContext);
        this.addView(topT, lp);

        mViewPager = new ViewPager(mContext);
        initViewPager();
        lp = new LayoutParams(420, 530);
        lp.leftMargin = 425;
        mViewPager.setClipChildren(false);
        this.addView(mViewPager, lp);

        TextView bottomT = new TextView(mContext);
        lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100);
        this.addView(bottomT, lp);
    }

    private void initViewPager() {

        imgs = new ArrayList<TextView>();
        addView(19);
        for (int i = 0; i < 20; i++) {
            addView(i);
        }
        addView(0);

        mViewPager.setAdapter(myAdapter = new MyAdapter(mContext, imgs));
        mViewPager.setCurrentItem(2);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setPageMargin(65);
//        mViewPager.setPageTransformer(true,new MyTransformation());

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int pageIndex = position;
                if (position == 0) {
                    // 当视图在第一个时，将页面号设置为图片的最后一张。
                    pageIndex = 20;
                    mViewPager.setCurrentItem(pageIndex, false);
                } else if (position == 21) {
                    // 当视图在最后一个是,将页面号设置为图片的第一张。
                    pageIndex = 1;
                    mViewPager.setCurrentItem(pageIndex, false);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void addView(int i) {
        textview = new TextView(mContext);
        textview.setLayoutParams(lp);
        textview.setText("海报" + i);
        textview.setTag(i);
        textview.setFocusable(true);
        textview.setBackgroundColor(Color.WHITE);
        textview.setGravity(Gravity.CENTER);
        textview.setTextSize(30);
        imgs.add(textview);
    }


    private class MyAdapter extends PagerAdapter {
        private Context mContext;
        private List<TextView> imgs;

        public MyAdapter(Context mContext, List<TextView> imgs) {
            this.mContext = mContext;
            this.imgs = imgs;
        }

        @Override
        public int getCount() {
            return imgs.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imgs.get(position));
        }

        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {
            final TextView tv = imgs.get(position);
//            final TextView letf_tv = imgs.get(position+1);
//            final TextView R2_tv = imgs.get(position+2);
//            final TextView l1_tv = imgs.get(position-1);
            tv.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        tv.setScaleX(1.2f);
                        tv.setScaleY(1.2f);

                    } else {
                        tv.setScaleX(1.0f);
                        tv.setScaleY(1.0f);

                    }
                }
            });
            container.addView(imgs.get(position));
            return tv;
        }

      /*  @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            View currentView = (View) object;
//            currentView.setScaleX(0.4f );
//            currentView.setScaleY(0.4f );

        }*/
    }
}
