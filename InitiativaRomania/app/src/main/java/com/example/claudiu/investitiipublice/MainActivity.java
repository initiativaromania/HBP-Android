package com.example.claudiu.investitiipublice;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.claudiu.initiativaromania.R;

public class MainActivity extends FragmentActivity{
    private static final String TAB_MAP         = "Harta";
    private static final String TAB_STATISTICS  = "Statistici";


    /* Setup tab navigation bar */
    private void tabSetup() {
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("map");
        tabSpec.setContent(R.id.tabMap);
        tabSpec.setIndicator(TAB_MAP);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("statistics");
        tabSpec.setContent(R.id.tabMap);
        tabSpec.setIndicator(TAB_STATISTICS);
        tabHost.addTab(tabSpec);

        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
            tv.setTextColor(Color.WHITE);
            tv.setTextSize(18);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);

        tabSetup();
    }
}
