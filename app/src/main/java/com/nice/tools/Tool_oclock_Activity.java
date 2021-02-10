package com.nice.tools;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.ypz.bangscreentools.BangScreenTools;

public class Tool_oclock_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oclock);
        //BangScreenTools.getBangScreenTools().fullscreen(getWindow(), this);
    }
}
