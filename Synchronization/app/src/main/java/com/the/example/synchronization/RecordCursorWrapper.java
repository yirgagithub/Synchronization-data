package com.the.example.synchronization;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.the.example.synchronization.RecordTableSchema.CrimeTable;

import java.util.UUID;

/**
 * Created by what on 11/29/2017.
 */

public class RecordCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
     RecordCursorWrapper(Cursor cursor) {
        super(cursor);
    }
    public Record getRecord()
    {
        int id = getInt(getColumnIndex(CrimeTable.Cols.ID));
        String FName=getString(getColumnIndex(CrimeTable.Cols.FNAME));
        String LName=getString(getColumnIndex(CrimeTable.Cols.LNAME));
        int Age=getInt(getColumnIndex(CrimeTable.Cols.AGE));
        String Sex=getString(getColumnIndex(CrimeTable.Cols.SEX));
        String comment=getString(getColumnIndex(CrimeTable.Cols.COMMENT));
        int intIsSync=getInt(getColumnIndex(CrimeTable.Cols.ISSYNCHRONIZED));
        boolean isSync;
        isSync = intIsSync != 0;
        Record record = new Record();
        record.setId(id);
        record.setFirstName(FName);
        record.setLastName(LName);
        record.setAge(Age);
        record.setSex(Sex);
        record.setComment(comment);
        record.setSynchronized(isSync);
        return record;


    }
}
