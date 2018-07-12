package com.the.example.synchronization;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by what on 11/30/2017.
 */

 class SynchronizeData extends AsyncTask<String,Void,Boolean> {
    private Context mContext;

     SynchronizeData(Context context)
    {
        mContext = context;
    }

    protected void onPreExecute()
    {
        super.onPreExecute();
       // SynchFragment synchFragment = new SynchFragment();
        //synchFragment.showSendingProgress();

    }

    @Override
    protected Boolean doInBackground(String... params) {

        HttpURLConnection httpURLConnection = null;
        OutputStream outputStream = null;
        BufferedWriter bufferedWriter = null;
        try {
            String sendTo = "http://192.168.43.28/receive_json.php";
            URL url = new URL(sendTo);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            httpURLConnection.setFixedLengthStreamingMode(params[1].getBytes().length);
            httpURLConnection.connect();
            outputStream = new BufferedOutputStream(httpURLConnection.getOutputStream());
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            outputStream.flush();
            bufferedWriter.write(params[1]);
            bufferedWriter.flush();
            int httpResponse = httpURLConnection.getResponseCode();
            if (httpResponse == HttpURLConnection.HTTP_OK) {
                Log.d("TAG", "The respose is HTTP_OK");
                return true;
            }
            if (httpResponse == HttpURLConnection.HTTP_ACCEPTED) {
                Log.d("TAG", "The respose is HTTP_ACCEPTED");
                return true;
            }
            if (httpResponse == HttpURLConnection.HTTP_BAD_GATEWAY) {
                Log.d("TAG", "The respose is HTTP_BAD_GATEWAY");
                return false;
            }
            if (httpResponse == HttpURLConnection.HTTP_NOT_FOUND) {
                Log.d("Tag", "The resposnse is HTTP_NOT_FOUND");
                return false;
            }

            outputStream.close();

            //bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}