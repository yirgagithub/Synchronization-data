package com.the.example.synchronization;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import static com.the.example.synchronization.ConnectivityReceiver.IS_NETWORK_AVAILABLE;

public class Synchronization extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synchronization);
        Toolbar toolbar =(Toolbar)findViewById(R.id.SynchActivity_toolbar_id);
        setSupportActionBar(toolbar);
        IntentFilter intentFilter = new IntentFilter(ConnectivityReceiver.NETWORK_AVAILABLE_ACTION);
        if(intentFilter!=null) {
            LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    boolean isNetworkAvailable = intent.getBooleanExtra(IS_NETWORK_AVAILABLE, false);
                    if (isNetworkAvailable)
                        showNotification(getApplicationContext());
                }
            }, intentFilter);
        }
        ViewPager viewPager=(ViewPager)findViewById(R.id.synchronizationActivity_viewPager_id);
        setupViewPager(viewPager);
        TabLayout tabLayout=(TabLayout)findViewById(R.id.synchronizationActivity_tablayout_id);
        tabLayout.setupWithViewPager(viewPager);

    }
    private void setupViewPager(ViewPager viewPager) {


        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.add(new UnSynchFragment(),getResources().getString(R.string.unsynch_title));
        viewPagerAdapter.add(new SynchFragment(),getResources().getString(R.string.synch_title));
        viewPager.setAdapter(viewPagerAdapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        List<Fragment> listFragment= new ArrayList<>();
        List<String> listFragmentTitle=new ArrayList<>();

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return listFragment.get(position);
        }

        @Override
        public int getCount() {
            return listFragmentTitle.size();
        }
        @Override
        public CharSequence getPageTitle(int position)
        {
            return listFragmentTitle.get(position);
        }
        void add(Fragment fragment,String title)
        {
            listFragment.add(fragment);
            listFragmentTitle.add(title);
        }

    }


    @Override
    public void onResume()
    {
        super.onResume();

    }
    public  void showNotification(Context mContext) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.icon)
                        .setContentTitle("Network Connection available")
                        .setContentText("Please Synch your data now!");

        Intent notificationIntent = new Intent(this, Synchronization.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());


    }
}
