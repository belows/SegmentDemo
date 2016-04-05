package com.belows.segmentdemo;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.belows.segment.SegmentView;

/**
 * Created by belows on 16/4/5.
 */
public class CustomActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_custom);

        SegmentView segmentView = (SegmentView) findViewById(R.id.sv_custom);
        segmentView.addItem(segmentView.newItem().setCustomView(customItem("Tab1")));
        segmentView.addItem(segmentView.newItem().setCustomView(customItem("Tab2")));
        segmentView.addItem(segmentView.newItem().setCustomView(customItem("Tab3")));
        segmentView.addItem(segmentView.newItem().setCustomView(customItem("Tab4")));
        segmentView.addItem(segmentView.newItem().setCustomView(customItem("Tab5")));
        segmentView.notifyDataChanged();
    }

    View customItem(String tab) {
        TextView textView = new TextView(this);
        textView.setText(tab);
        int selected = android.R.attr.state_selected;
        StateListDrawable drawable = new StateListDrawable();
        ColorDrawable selectedDrawable = new ColorDrawable(Color.YELLOW);
        ColorDrawable normalDrawable = new ColorDrawable(Color.BLUE);
        drawable.addState(new int[]{selected},selectedDrawable);
        drawable.addState(new int[]{-selected}, normalDrawable);
        textView.setBackgroundDrawable(drawable);

        ColorStateList color = new ColorStateList(new int[][]{{selected},{-selected}},new int[]{Color.WHITE,Color.RED});
        textView.setTextColor(color);
        return textView;
    }
}
