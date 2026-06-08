package com.logicsoftbd.lsl.ui.v_1_ui.bunlde_wise_sewing;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.logicsoftbd.lsl.data.network.v1_model.V1_SewingOutputModel;

import java.util.ArrayList;

public class V1_SewingOutputDBAdapter {
    Context c;
    SQLiteDatabase db;
    V1_SewingOutputDBHelper helper;

    /*
    1. INITIALIZE DB HELPER AND PASS IT A CONTEXT

     */

    public V1_SewingOutputDBAdapter(Context c) {
        this.c = c;
        this.helper = new V1_SewingOutputDBHelper(c);
    }

    /*SAVE DATA TO DB*/

    public boolean saveLoginData(V1_SewingOutputModel sewingOutputModel)
    {
        try {
            db = helper.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put(V1_SewingOutputConstant.DATE, sewingOutputModel.getDate());
            cv.put(V1_SewingOutputConstant.BARCODE, sewingOutputModel.getBarcode());

            long result = db.insert(V1_SewingOutputConstant.TB_NAME, V1_SewingOutputConstant.ROW_ID, cv);
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

    public ArrayList<V1_SewingOutputModel> getSewingOfflineData()
    {
        ArrayList<V1_SewingOutputModel> sewingOutputModels = new ArrayList<>();
        String[] columns = {
                V1_SewingOutputConstant.ROW_ID,
                V1_SewingOutputConstant.DATE,
                V1_SewingOutputConstant.BARCODE
        };
        try {
            db = helper.getWritableDatabase();
            Cursor c = db.query(V1_SewingOutputConstant.TB_NAME,columns,
                    null, null,
                    null, null, null);
            V1_SewingOutputModel l;
            if(c != null)
            {
                while (c.moveToNext())
                {
                    String m_date = c.getString(1);
                    String m_barcode = c.getString(2);

                    l = new V1_SewingOutputModel();
                    l.setDate(m_date);
                    l.setBarcode(m_barcode);

                    sewingOutputModels.add(l);
                }
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return sewingOutputModels;
    }

    public void deleteUsers() {
        db = helper.getWritableDatabase();
        // Delete All Rows
        db.delete(V1_SewingOutputConstant.TB_NAME, null, null);
        db.close();
    }
}
