package com.logicsoftbd.lsl.ui.v_1_ui.bunlde_wise_sewing;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class V1_SewingOutputDBHelper extends SQLiteOpenHelper {
    public V1_SewingOutputDBHelper(Context context) {
        super(context, V1_SewingOutputConstant.DB_NAME, null, V1_SewingOutputConstant.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try
        {
            db.execSQL(V1_SewingOutputConstant.CREATE_TB);
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(V1_SewingOutputConstant.DROP_TB);
            db.execSQL(V1_SewingOutputConstant.CREATE_TB);
        }catch (SQLException e)
        {
            e.printStackTrace();
        }

    }
}
