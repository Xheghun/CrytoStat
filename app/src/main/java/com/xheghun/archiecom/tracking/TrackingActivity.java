package com.xheghun.archiecom.tracking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class TrackingActivity extends AppCompatActivity {

    protected Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTracker = new Tracker(this);
    }
}
