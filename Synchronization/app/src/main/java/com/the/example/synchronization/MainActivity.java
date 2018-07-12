package com.the.example.synchronization;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar=(Toolbar)findViewById(R.id.mainActivity_toolbar_id) ;
        setSupportActionBar(toolbar);
        Button createRecordButton=(Button)findViewById(R.id.createRecord_Button_id);
        createRecordButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent= new Intent(MainActivity.this,CreateNewRecord.class);
                startActivity(intent);
            }
        });
         Button synchronizationButton=(Button)findViewById(R.id.synchronizationButton_id);
        synchronizationButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent intent= new Intent(MainActivity.this,Synchronization.class);
                startActivity(intent);
            }
        });
            }


}
