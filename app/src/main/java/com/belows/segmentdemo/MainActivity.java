package com.belows.segmentdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.belows.segment.SegmentView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SegmentView segmentView = (SegmentView) findViewById(R.id.sv_test);
        segmentView.addItem(segmentView.newItem().setText("Tab1"));
        segmentView.addItem(segmentView.newItem().setText("Tab2"));
        segmentView.addItem(segmentView.newItem().setText("Tab3"));
        segmentView.addItem(segmentView.newItem().setText("Tab4"));
        segmentView.addItem(segmentView.newItem().setText("Tab5"));
        segmentView.addItem(segmentView.newItem().setText("Tab6"));

        segmentView.notifyDataChanged();

        segmentView.setOnItemClickListener(new SegmentView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Intent _intent = new Intent(MainActivity.this, CustomActivity.class);
                startActivity(_intent);
            }
        });
    }
}
