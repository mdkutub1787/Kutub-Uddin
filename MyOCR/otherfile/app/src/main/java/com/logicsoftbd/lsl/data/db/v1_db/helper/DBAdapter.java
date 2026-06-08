package com.logicsoftbd.lsl.data.db.v1_db.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.logicsoftbd.lsl.data.db.v1_db.model.V1_User;

import java.util.ArrayList;

public class DBAdapter {
    Context c;
    SQLiteDatabase db;
    DBHelper helper;

    /*
    1. INITIALIZE DB HELPER AND PASS IT A CONTEXT

     */

    public DBAdapter(Context c) {
        this.c = c;
        this.helper = new DBHelper(c);
    }

    /*SAVE DATA TO DB*/

    public boolean saveLoginData(V1_User loginData)
    {
        try {
            db = helper.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put(Constants.URL, loginData.getUrl());
            cv.put(Constants.USER_NAME, loginData.getUser());
            cv.put(Constants.PASSWORD, loginData.getPass());
            cv.put(Constants.USER_ID, loginData.getUserId());

            long result = db.insert(Constants.TB_NAME, Constants.ROW_ID, cv);
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

    public ArrayList<V1_User> getLoginData()
    {
        ArrayList<V1_User> loginData = new ArrayList<>();
        String[] columns = {
                Constants.ROW_ID,
                Constants.URL,
                Constants.USER_NAME,
                Constants.PASSWORD,
                Constants.USER_ID
        };
        try {
            db = helper.getWritableDatabase();
            Cursor c = db.query(Constants.TB_NAME,columns,
                    null, null,
                    null, null, null);
            V1_User l;

            if(c != null)
            {
                while (c.moveToNext())
                {
                    String m_url = c.getString(1);
                    String m_user = c.getString(2);
                    String m_pass = c.getString(3);
                    String m_userId = c.getString(4);

                    l = new V1_User();
                    l.setUrl(m_url);
                    l.setUser(m_user);
                    l.setPass(m_pass);
                    l.setUserId(m_userId);

                    loginData.add(l);
                }
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return loginData;
    }

    public void deleteUsers() {
         db = helper.getWritableDatabase();
        // Delete All Rows
        db.delete(Constants.TB_NAME, null, null);
        db.close();
    }
}
