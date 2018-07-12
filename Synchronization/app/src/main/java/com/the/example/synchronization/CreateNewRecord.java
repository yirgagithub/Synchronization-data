package com.the.example.synchronization;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.UUID;

public class CreateNewRecord extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_record);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button saveRecordButton =(Button)findViewById(R.id.saveRecord_button_id);
        saveRecordButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String fName,lName,ageString,comment;
                int age;
                final String[] sex= new String[1];
                EditText fNameEditText=(EditText)findViewById(R.id.fName_EditText_id);
                EditText lNameEditText=(EditText)findViewById(R.id.lName_EditText_id);
                final RadioGroup sexRadioButton=(RadioGroup)findViewById(R.id.radioGroup_id);
                RadioButton firstRadioButton = (RadioButton)findViewById(R.id.first_RadioButton_id);
                RadioButton secondRadioButton = (RadioButton)findViewById(R.id.second_RadioButton_id);
                EditText commentEditText=(EditText)findViewById(R.id.comment_EditText_id);
                EditText ageEditText=(EditText)findViewById(R.id.age_EditText_id);
                fName=fNameEditText.getText().toString();
                lName=lNameEditText.getText().toString();
                int selectedRadioButton = sexRadioButton.getCheckedRadioButtonId();
                if(selectedRadioButton==R.id.first_RadioButton_id)
                    sex[0]="male";
                else if(selectedRadioButton==R.id.second_RadioButton_id)
                    sex[0]="female";
                comment=commentEditText.getText().toString();
                ageString=ageEditText.getText().toString();
                age=Integer.parseInt(ageString);
                Record record = new Record();
                 record.setFirstName(fName);
                 record.setLastName(lName);
                 record.setSex(sex[0]);
                 record.setAge(age);
                 record.setComment(comment);
                record.setSynchronized(false);
                RecordList recordList = new RecordList(getApplicationContext());
                boolean result=recordList.addRecord(record);
                if(result)
                Toast.makeText(getApplicationContext(),"New Record Saved into Database",Toast.LENGTH_LONG).show();
                fNameEditText.setText("");
                lNameEditText.setText("");
                if(sex[0].equals("male"))
                    firstRadioButton.setChecked(false);
                else if (sex[0].equals("female"))
                    secondRadioButton.setChecked(false);
                ageEditText.setText("");
                commentEditText.setText("");

            }
        });
    }

}
