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
import com.initiativaromania.hartabanilorpublici.IRData.CommManager;
import com.initiativaromania.hartabanilorpublici.IRUserInterface.activities.MainActivity;
import com.initiativaromania.hartabanilorpublici.IRUserInterface.fragments.AroundStatisticsFragment;

import java.text.DecimalFormat;

/**
 * Created by claudiu on 2/9/16.
 */
public class IRSeekBarListener implements SeekBar.OnSeekBarChangeListener {


    private Circle circle;
    private static AroundStatisticsFragment aroundStatisticsInstance;

    public IRSeekBarListener(Circle circle) {
        setCircle(circle);
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    public static void registerAroundStatisticsInstance(AroundStatisticsFragment instance)  {
        aroundStatisticsInstance = instance;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (this.circle == null)
            return;

        this.circle.setRadius(MainActivity.CIRCLE_MIN_RADIUS + progress * (MainActivity.CIRCLE_MAX_RADIUS - MainActivity.CIRCLE_MIN_RADIUS) / 100);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar){ }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        /* Refresh the around buyers array */
        CommManager.updateAroundBuyers();

        /* Update the seekbar value with new total price sum */
        Rect thumbRect = seekBar.getThumb().getBounds();
        MainActivity.seekBarValue.setX(thumbRect.exactCenterX());
        DecimalFormat dm = new DecimalFormat("###,###.###");
        MainActivity.seekBarValue.setText(" " + String.valueOf(dm.format(CommManager.aroundTotalSum)) + " EURO ");
        MainActivity.seekBarValue.startAnimation(MainActivity.animationFadeIn);

        /* If around contracts fragment is NOT destroyed, refresh the ListView data */
        if (null != aroundStatisticsInstance) {
            aroundStatisticsInstance.currentBuyerToProcess = 0;
            AroundStatisticsFragment.previousTotal = 0;
            aroundStatisticsInstance.orderDetailsList.clear();
            aroundStatisticsInstance.getMoreAroundStatistics();
            aroundStatisticsInstance.getMoreAroundStatistics();
        }
    }
}
