package com.logicsoftbd.lsl.data.db.v1_db.helper;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class BaseUrlDBHelper extends SQLiteOpenHelper{
    public BaseUrlDBHelper(Context context) {
        super(context, BaseUrlConstants.DB_NAME, null, BaseUrlConstants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try
        {
            db.execSQL(BaseUrlConstants.CREATE_TB);
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(BaseUrlConstants.DROP_TB);
            db.execSQL(BaseUrlConstants.CREATE_TB);
        }catch (SQLException e)
        {
            e.printStackTrace();
        }

    }
}
