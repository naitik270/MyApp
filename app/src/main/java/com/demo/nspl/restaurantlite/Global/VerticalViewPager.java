package com.demo.nspl.restaurantlite.Global;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

class VerticalViewPager extends ViewPager {

    public VerticalViewPager(Context context) {
        super(context);
     //   init();
    }

    public VerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
     //   init();
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        return false;
    }


    // swap x and y
    private MotionEvent swapXY(MotionEvent ev) {
        float width = getWidth();
        float height = getHeight();

        float newX = (ev.getY() / height) * width;
        float newY = (ev.getX() / width) * height;

        ev.setLocation(newX, newY);

        return ev;
    }


}
