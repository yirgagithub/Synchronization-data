package com.the.example.synchronization;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.the.example.synchronization.RecordTableSchema.CrimeTable;

/**
 * Created by what on 11/29/2017.
 */

public class RecordsDbHelper extends SQLiteOpenHelper {
    private static String DATABASE_NAME="record.db";
    private static int VERSION=1;

    public RecordsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_COMMAND = "create table " + CrimeTable.NAME + "(" +
                CrimeTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CrimeTable.Cols.FNAME + ", " +
                CrimeTable.Cols.LNAME + ", " +
                CrimeTable.Cols.AGE + ", " +
                CrimeTable.Cols.SEX + ", " +
                CrimeTable.Cols.COMMENT + ", " +
                CrimeTable.Cols.ISSYNCHRONIZED +
                ")";
        db.execSQL(CREATE_TABLE_COMMAND);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
