package com.the.example.synchronization;

import android.content.Context;
import android.os.AsyncTask;
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
 * Created by what on 12/26/2017.
 */

class SynchRecyclerViewAdapter extends RecyclerView.Adapter<SynchRecyclerViewAdapter.ListViewHolder> {

    private List<Record> mSynchList;
    private Context mContext;
    public SynchRecyclerViewAdapter()
    {
        //empty constructor
    }
        SynchRecyclerViewAdapter(Context context, List<Record> list)
        {
        mSynchList = list;
        mContext = context;
        }


    class ListViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView subTitleTextView;
        Button reSendButton;
        Button deleteButton;

        public ListViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView)itemView.findViewById(R.id.synchList_titleTextView_id);
            subTitleTextView = (TextView)itemView.findViewById(R.id.SynchList_subTitleTextView_id);
            reSendButton = (Button)itemView.findViewById(R.id.resend_Button_id);
            deleteButton = (Button)itemView.findViewById(R.id.delete_Button_id);
            reSendButton.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View view) {

                    boolean isConnected = CheckConnection.checkConnection(mContext.getApplicationContext());
                    if (!isConnected) {
                        UnSynchFragment unSynchFragment = new UnSynchFragment();
                        unSynchFragment.noInternetAccessDialog();
                    } else {
                        int position = getAdapterPosition();
                        Record record = mSynchList.get(position);
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("firstName", record.getFirstName());
                            jsonObject.put("lastName", record.getLastName());
                            jsonObject.put("age", record.getAge());
                            jsonObject.put("sex", record.getSex());
                            jsonObject.put("comment", record.getComment());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String message = jsonObject.toString();
                        String sendTo = "http://localhost/receive_json.php";
                        SynchronizeData synchronizeData = new SynchronizeData(mContext.getApplicationContext());
                        AsyncTask<String,Void,Boolean> asyncResult= synchronizeData.execute(sendTo,message);
                        boolean isSent = false;
                        try {
                            isSent = asyncResult.get();
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                        if (isSent) {
                            record.setSynchronized(true);
                            RecordList recordList = new RecordList(mContext.getApplicationContext());
                            recordList.updateRecord(record);
                            mSynchList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, mSynchList.size());
                        }

                    }
                }
            });
            deleteButton.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    Record record = mSynchList.get(getAdapterPosition());
                    RecordList recordList = new RecordList(mContext);
                   int rowAffected = recordList.deleteItem(record);

                        mSynchList.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                       notifyItemRangeChanged(getAdapterPosition(),mSynchList.size());

                }
            });


        }
    }

            @Override
            public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                View view = layoutInflater.inflate(R.layout.synch_list_row,parent,false);
                return new ListViewHolder(view);
            }

            @Override
            public void onBindViewHolder(ListViewHolder holder, int position) {
                Record record = mSynchList.get(position);
                holder.titleTextView.setText(record.getFirstName()+" "+record.getLastName());
                holder.subTitleTextView.setText(record.getComment());

            }

            @Override
            public int getItemCount() {
               return mSynchList.size();
            }



}


