package com.the.example.synchronization;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SynchFragment extends Fragment {

    List<Record> synchList;
    Context context;

    public SynchFragment() {
    context=getActivity();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
       synchList = getSynchList();
        if(synchList.size()>0)
        {
            final View view = inflater.inflate(R.layout.fragment_synch, container, false);
            Button reSendAllButton =(Button)view.findViewById(R.id.reSendAll_Button_id);
            final Button deleteAllButton =(Button)view.findViewById(R.id.deleteAll_Button_id);
            RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.synchList_recyclerView_id);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            int spanCount = 1; // 1 columns
            int spacing = 10;
            recyclerView.addItemDecoration(new SpaceItemDocoration(spanCount, spacing, true));
            SynchRecyclerViewAdapter synchRecyclerViewAdapter = new SynchRecyclerViewAdapter(getContext(),synchList);
            recyclerView.setAdapter(synchRecyclerViewAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            reSendAllButton.setOnClickListener(new View.OnClickListener()
            {

                public void onClick(View view) {
                    boolean isConnected = CheckConnection.checkConnection(getContext().getApplicationContext());
                    if (!isConnected) {
                        UnSynchFragment fragment = new UnSynchFragment();
                        fragment.noInternetAccessDialog();
                    } else {

                        JSONArray jsonArray = new JSONArray();

                        for (Record record : synchList) {
                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("firstName", record.getFirstName());
                                jsonObject.put("lastName", record.getLastName());
                                jsonObject.put("age", record.getAge());
                                jsonObject.put("sex", record.getSex());
                                jsonObject.put("comment", record.getComment());
                                jsonArray.put(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        String message = jsonArray.toString();
                        String sendTo = "192.168.137.141/receive_json.php";
                        SynchronizeData synchronizeData = new SynchronizeData(getContext().getApplicationContext());
                        AsyncTask<String,Void,Boolean> asyncResult=synchronizeData.execute(sendTo,message);
                        boolean isSent = false;
                        try {
                            isSent = asyncResult.get();
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                        if (isSent) {
                            for (Record record : synchList) {
                                record.setSynchronized(true);
                                RecordList recordList = new RecordList(getActivity().getApplicationContext());
                                recordList.updateRecord(record);
                                //remove the the recyclerview items

                            }
                        }

                    }
                }
            });
            deleteAllButton.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    RecordList recordList = new RecordList(getContext().getApplicationContext());
                    recordList.deleteAllItems();
                    //remove the recyclerview items
                }
            });

            return view;

        }
        else
            return inflater.inflate(R.layout.unsynch_empty,container,false);


    }


    public List<Record> getSynchList() {
        synchList = new ArrayList<>();
        RecordList recordList = new RecordList(getContext());
        RecordCursorWrapper cursor=  recordList.queryRecord();

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Record record=cursor.getRecord();
                if(record.isSynchronized())
                    synchList.add(record);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return synchList;
    }


    public  void showSendingProgress()
    {
        Context mContext = context;
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setIndeterminate(true);
        dialog.setMessage("synchronizing  your data...");
        dialog.setCancelable(false);
        dialog.show();
    }
}

