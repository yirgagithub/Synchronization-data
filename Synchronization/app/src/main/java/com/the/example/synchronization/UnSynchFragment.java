package com.the.example.synchronization;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by what on 11/28/2017.
 */

public class UnSynchFragment extends Fragment   {

    List<Record> list ;
    public UnSynchFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater,final ViewGroup container,
                             Bundle savedInstanceState) {
       getUnsynchRecord();
         if(list.size()>0)
        {
            View view = inflater.inflate(R.layout.unsynch_fragment,container,false);
            Button synchAllButton=(Button)view.findViewById(R.id.synchAllButton_id);
            RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.list_recyclerView_id);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            int spanCount = 1; // 1 columns
            int spacing = 10;
            recyclerView.addItemDecoration(new SpaceItemDocoration(spanCount, spacing, true));
            final UnSynchRecyclerViewAdapter unSynchRecyclerViewAdapter = new UnSynchRecyclerViewAdapter(getContext(),list);
            recyclerView.setAdapter(unSynchRecyclerViewAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            boolean isConnected=CheckConnection.checkConnection(getContext());
           if(!isConnected)
                noInternetAccessDialog();
            synchAllButton.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    boolean isConnected=CheckConnection.checkConnection(getContext().getApplicationContext());
                  if(!isConnected) {
                      noInternetAccessDialog();

                 }
                    else {

                      JSONArray jsonArray = new JSONArray();
                      for (Record record : list) {
                          try {
                              JSONObject jsonObject = new JSONObject();
                              jsonObject.put("firstName", record.getFirstName());
                              jsonObject.put("lastName", record.getLastName());
                              jsonObject.put("age", String.valueOf(record.getAge()));
                              jsonObject.put("sex", record.getSex());
                              jsonObject.put("comment", record.getComment());
                              jsonArray.put(jsonObject);
                          } catch (JSONException e) {
                              e.printStackTrace();
                          }

                      }
                      String message = jsonArray.toString();
                      String sendTo = "http://192.168.137.75/receive_json.php";
                      boolean isSent = false;
                      SynchronizeData synchronizeData = new SynchronizeData(getActivity());
                      AsyncTask<String, Void, Boolean> asyncResult = synchronizeData.execute(sendTo, message);
                      try {
                          isSent = asyncResult.get();
                      } catch (InterruptedException | ExecutionException e) {
                          e.printStackTrace();
                      }

                      if (isSent) {
                          Toast.makeText(getActivity().getApplicationContext(), "All data are sent", Toast.LENGTH_LONG).show();
                          for (Record record : list) {
                              record.setSynchronized(true);
                              RecordList recordList = new RecordList(getActivity().getApplicationContext());
                              recordList.updateRecord(record);

                          }
                          unSynchRecyclerViewAdapter.deleteItems(list);




                      }
                  }

                    }

            });

            return view;

        }
        else
        {
            return inflater.inflate(R.layout.unsynch_empty,container,false);
        }

    }

    public  void noInternetAccessDialog() {

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getActivity());
        }
        builder.setTitle("turn Data On or Connect to Wifi")
                .setMessage("You need to turn data on or connect to wifi to synchronize your data to remote server?")
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();



    }


    public void getUnsynchRecord() {
        list = new ArrayList<>();
        RecordList recordList = new RecordList(getContext());
        RecordCursorWrapper cursor=  recordList.queryRecord();

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Record record=cursor.getRecord();
                if(!record.isSynchronized())
                    list.add(record);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
    }
    @Override
    public void onResume()
    {
        super.onResume();


    }
    @Override
    public  void onPause()
    {
        super.onPause();

    }

}

