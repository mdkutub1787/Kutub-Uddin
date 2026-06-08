package com.logicsoftbd.lsl.ui.v_1_ui.bunlde_wise_sewing;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class V1_SewingInputDBHelper extends SQLiteOpenHelper {
    public V1_SewingInputDBHelper(Context context) {
        super(context, V1_SewingInputConstant.DB_NAME, null, V1_SewingInputConstant.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try
        {
            db.execSQL(V1_SewingInputConstant.CREATE_TB);
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(V1_SewingInputConstant.DROP_TB);
            db.execSQL(V1_SewingInputConstant.CREATE_TB);
        }catch (SQLException e)
        {
            e.printStackTrace();
        }

    }
}
