package com.the.example.synchronization;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;
import java.util.UUID;


import com.the.example.synchronization.RecordTableSchema.CrimeTable;

/**
 * Created by what on 11/29/2017.
 */

 class RecordList {

    private  RecordList mRecordList;
    private List<Record> recordList;
    private Context mContext;
    private SQLiteDatabase sqLiteDatabase;
     RecordList(Context context)
    {
        mContext=context.getApplicationContext();
        sqLiteDatabase=new RecordsDbHelper(mContext).getWritableDatabase();

    }
     RecordCursorWrapper queryRecord()
    {
      Cursor cursor = sqLiteDatabase.query(CrimeTable.NAME,null,null,null,null,null,null);
        return new RecordCursorWrapper(cursor);
    }

     boolean addRecord(Record record)
    {
        ContentValues values=getContentValues(record);
        sqLiteDatabase.insert(CrimeTable.NAME,null,values);

        return true;
    }

    private ContentValues getContentValues(Record record) {
        ContentValues contentValues= new ContentValues();
        contentValues.put(CrimeTable.Cols.FNAME,record.getFirstName());
        contentValues.put(CrimeTable.Cols.LNAME,record.getLastName());
        contentValues.put(CrimeTable.Cols.AGE,record.getAge());
        contentValues.put(CrimeTable.Cols.SEX,record.getSex());
        contentValues.put(CrimeTable.Cols.COMMENT,record.getComment());
        contentValues.put(CrimeTable.Cols.ISSYNCHRONIZED,record.isSynchronized()?1:0);

        return contentValues;
    }
     int deleteItem(Record record)
    {

        return sqLiteDatabase.delete(CrimeTable.NAME,CrimeTable.Cols.ID+" = "+record.getId(),null);

    }
    int deleteAllItems()
    {
        return sqLiteDatabase.delete(CrimeTable.NAME,null,null);
    }
    int updateRecord( Record record)
    {
        ContentValues contentValues=getContentValues(record);

        int result= sqLiteDatabase.update(CrimeTable.NAME,contentValues,CrimeTable.Cols.ID+" = "+record.getId(),null);

        return result;
    }
}
