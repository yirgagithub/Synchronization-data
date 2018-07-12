package com.the.example.synchronization;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by what on 11/30/2017.
 */

public class UnSynchRecyclerViewAdapter extends RecyclerView.Adapter<UnSynchRecyclerViewAdapter.ListViewHolder>{


     private List<Record> mList;
     private Context mContext;
    UnSynchRecyclerViewAdapter()
    {

    }
    UnSynchRecyclerViewAdapter(Context context, List<Record> list)
    {
        mList=list;
        mContext = context;

    }
     void deleteItems(List<Record> list)
    {
        for(int i=0;i<list.size();i++)
        {
            mList.remove(i);
            notifyItemRemoved(i);
        }
    }

     class ListViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitleTextView;
        private TextView msubTitle;
        private Button mSynchIndividual;


         ListViewHolder(View itemView) {
            super(itemView);

            mTitleTextView = (TextView) itemView.findViewById(R.id.list_titleTextView_id);
            msubTitle = (TextView) itemView.findViewById(R.id.list_subTitle_id);
            mSynchIndividual=(Button)itemView.findViewById(R.id.synchIndividual_Button_id);

            mSynchIndividual.setOnClickListener(new View.OnClickListener()
            {
                @Override
                        public void onClick(View view)
                {
                    boolean isConnected=CheckConnection.checkConnection(mContext.getApplicationContext());
                    if(!isConnected) {
                        noInternetAccessDialog();
                    }
                    else {
                        int position = getAdapterPosition();
                        Record record = mList.get(position);
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("firstName",record.getFirstName());
                            jsonObject.put("lastName",record.getLastName());
                            jsonObject.put("age", record.getAge());
                            jsonObject.put("sex",record.getSex());
                            jsonObject.put("comment",record.getComment());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String message = jsonObject.toString();
                        String sendTo="http://192.168.4.103/receive_json";
                        boolean isSent=false;
                        SynchronizeData synchronizeData = new SynchronizeData(mContext.getApplicationContext());
                        AsyncTask<String,Void,Boolean> asyncResult= synchronizeData.execute(sendTo,message);
                        try {
                            isSent = asyncResult.get();
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                        if(isSent) {
                            record.setSynchronized(true);
                            mList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position,mList.size());
                            RecordList recordList = new RecordList(mContext.getApplicationContext());
                            recordList.updateRecord(record);
                        }

                    }
                }
            });
        }


    }


    @Override
    public UnSynchRecyclerViewAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view= inflater.inflate(R.layout.list_row_layout,parent,false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UnSynchRecyclerViewAdapter.ListViewHolder holder, int position) {
        Record record= mList.get(position);
        holder.mTitleTextView.setText(record.getFirstName()+" "+record.getLastName());
        holder.msubTitle.setText(record.getComment());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

      void noInternetAccessDialog() {

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(mContext, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(mContext);
        }
        builder.setTitle("turn Data On or Connect to Wifi")
                .setMessage("You need to turn data on or connect to wifi to synchronize your data to remote server?")
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mContext.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();



    }




}
