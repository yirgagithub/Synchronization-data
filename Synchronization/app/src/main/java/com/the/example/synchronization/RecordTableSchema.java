package com.the.example.synchronization;

/**
 * Created by what on 11/29/2017.
 */

public class RecordTableSchema {
    public static final class CrimeTable {
        public static final String NAME = "record";

        public static final class Cols {
            public static final String ID= "id";
            public static final String FNAME = "FName";
            public static final String LNAME = "LName";
            public static final String AGE = "Age";
            public static final String SEX = "Sex";
            public static final String COMMENT="Comment";
            public static final String ISSYNCHRONIZED="isSynchronized";

        }

    }
}
