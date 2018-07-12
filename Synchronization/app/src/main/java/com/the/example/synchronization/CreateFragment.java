package com.the.example.synchronization;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by what on 11/28/2017.
 */

public class CreateFragment extends Fragment {

    public CreateFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
    View view = inflater.inflate(R.layout.fragment_create, container, false);
        Button createButton =(Button) view.findViewById(R.id.createRecord_Button_id);
        createButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent= new Intent(getActivity(),CreateNewRecord.class);
                startActivity(intent);
            }
        });
        return view;

    }
}