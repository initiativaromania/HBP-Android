package com.example.claudiu.investitiipublice.IRUserInterface;

import android.widget.SeekBar;

import com.google.android.gms.maps.model.Circle;

/**
 * Created by claudiu on 2/9/16.
 */
public class IRSeekBarListener implements SeekBar.OnSeekBarChangeListener {


    private Circle circle;

    public IRSeekBarListener(Circle circle) {
        setCircle(circle);
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (this.circle != null)
            this.circle.setRadius(MainActivity.CIRCLE_MIN_RADIUS + progress * (MainActivity.CIRCLE_MAX_RADIUS - MainActivity.CIRCLE_MIN_RADIUS) / 100);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        System.out.println("Start");
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        System.out.println("End");
    }
}
