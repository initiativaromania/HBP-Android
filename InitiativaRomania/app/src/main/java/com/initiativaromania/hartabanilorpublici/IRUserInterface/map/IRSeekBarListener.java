/**
 This file is part of "Harta Banilor Publici".

 "Harta Banilor Publici" is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 "Harta Banilor Publici" is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.initiativaromania.hartabanilorpublici.IRUserInterface.map;

import android.graphics.Rect;
import android.widget.SeekBar;

import com.google.android.gms.maps.model.Circle;
import com.initiativaromania.hartabanilorpublici.IRUserInterface.activities.MainActivity;

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
        if (this.circle == null)
            return;

        this.circle.setRadius(MainActivity.CIRCLE_MIN_RADIUS + progress * (MainActivity.CIRCLE_MAX_RADIUS - MainActivity.CIRCLE_MIN_RADIUS) / 100);

        Rect thumbRect = seekBar.getThumb().getBounds();
        MainActivity.seekBarValue.setX(thumbRect.exactCenterX());
        MainActivity.seekBarValue.setText(" " + String.valueOf(progress) + " EURO ");
        MainActivity.seekBarValue.startAnimation(MainActivity.animationFadeIn);
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
