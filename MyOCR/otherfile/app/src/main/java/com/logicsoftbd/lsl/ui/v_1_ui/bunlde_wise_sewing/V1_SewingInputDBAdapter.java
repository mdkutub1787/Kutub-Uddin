package com.logicsoftbd.lsl.ui.v_1_ui.bunlde_wise_sewing;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class V1_SewingInputDBAdapter {
    Context c;
    SQLiteDatabase db;
    V1_SewingInputDBHelper helper;

    /*
    1. INITIALIZE DB HELPER AND PASS IT A CONTEXT

     */

    public V1_SewingInputDBAdapter(Context c) {
        this.c = c;
        this.helper = new V1_SewingInputDBHelper(c);
    }

    /*SAVE DATA TO DB*/

    public boolean saveLoginData(V1_SewingInputModel sewingInputModel)
    {
        try {
            db = helper.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put(V1_SewingInputConstant.DATE, sewingInputModel.getDate());
            cv.put(V1_SewingInputConstant.BARCODE, sewingInputModel.getBarcode());

            long result = db.insert(V1_SewingInputConstant.TB_NAME, V1_SewingInputConstant.ROW_ID, cv);
            if (result > 0) {
                return true;
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    /*retrive data*/

    public ArrayList<V1_SewingInputModel> getSewingOfflineData()
    {
        ArrayList<V1_SewingInputModel> sewingInputModels = new ArrayList<>();
        String[] columns = {
                V1_SewingInputConstant.ROW_ID,
                V1_SewingInputConstant.DATE,
                V1_SewingInputConstant.BARCODE
        };
        try {
            db = helper.getWritableDatabase();
            Cursor c = db.query(V1_SewingInputConstant.TB_NAME,columns,
                    null, null,
                    null, null, null);
            V1_SewingInputModel l;
            if(c != null)
            {
                while (c.moveToNext())
                {
                    String m_date = c.getString(1);
                    String m_barcode = c.getString(2);

                    l = new V1_SewingInputModel();
                    l.setDate(m_date);
                    l.setBarcode(m_barcode);

                    sewingInputModels.add(l);
                }
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return sewingInputModels;
    }

    public void deleteUsers() {
        db = helper.getWritableDatabase();
        // Delete All Rows
        db.delete(V1_SewingInputConstant.TB_NAME, null, null);
        db.close();
    }
}
