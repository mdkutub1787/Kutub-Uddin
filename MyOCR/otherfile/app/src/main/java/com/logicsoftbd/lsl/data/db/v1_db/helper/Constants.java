package com.logicsoftbd.lsl.data.db.v1_db.helper;

public class Constants {

    /*COLUMN*/

    static  final  String ROW_ID = "id";
    static  final  String URL = "url";
    static  final  String USER_NAME = "user_name";
    static  final  String PASSWORD = "password";
    static  final  String USER_ID = "user_id";

    /*DB PROPERTY*/
    static final String DB_NAME="tv_DB";
    static final String TB_NAME="tv_TB";
    static final int DB_VERSION=1;

    /*TAB:E CREATION STATEMENT*/
    static final String CREATE_TB="CREATE TABLE tv_TB(id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "url TEXT NOT NULL,user_name TEXT NOT NULL,password TEXT NOT NULL, user_id TEXT NOT NULL);";


    /*TABLE DELETION STMT*/
    static final String DROP_TB = "DROP TABLE IF EXISTS "+TB_NAME;
}
