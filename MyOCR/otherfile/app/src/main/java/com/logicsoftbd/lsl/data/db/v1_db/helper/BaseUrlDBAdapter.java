package com.logicsoftbd.lsl.data.db.v1_db.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.logicsoftbd.lsl.data.db.v1_db.model.V1_BaseUrl;

import java.util.ArrayList;

public class BaseUrlDBAdapter {
    Context c;
    SQLiteDatabase db;
    BaseUrlDBHelper helper;

    /*
    1. INITIALIZE DB HELPER AND PASS IT A CONTEXT

     */

    public BaseUrlDBAdapter(Context c) {
        this.c = c;
        this.helper = new BaseUrlDBHelper(c);
    }

    /*SAVE DATA TO DB*/

    public boolean saveLoginData(V1_BaseUrl v1BaseUrl)
    {
        try {
            db = helper.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put(BaseUrlConstants.URL, v1BaseUrl.getUrl());

            long result = db.insert(BaseUrlConstants.TB_NAME, BaseUrlConstants.ROW_ID, cv);
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

    public ArrayList<V1_BaseUrl> getLoginData()
    {
        ArrayList<V1_BaseUrl> v1_baseUrlArrayList = new ArrayList<>();
        String[] columns = {
                BaseUrlConstants.ROW_ID,
                BaseUrlConstants.URL
        };
        try {
            db = helper.getWritableDatabase();
            Cursor c = db.query(BaseUrlConstants.TB_NAME,columns,
                    null, null,
                    null, null, null);
            V1_BaseUrl l;

            if(c != null)
            {
                while (c.moveToNext())
                {
                    String m_url = c.getString(1);

                    l = new V1_BaseUrl();
                    l.setUrl(m_url);

                    v1_baseUrlArrayList.add(l);
                }
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return v1_baseUrlArrayList;
    }

    public void deleteUsers() {
         db = helper.getWritableDatabase();
        // Delete All Rows
        db.delete(BaseUrlConstants.TB_NAME, null, null);
        db.close();
    }
}
