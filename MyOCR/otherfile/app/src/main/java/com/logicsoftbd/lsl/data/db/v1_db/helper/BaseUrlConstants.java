package com.logicsoftbd.lsl.data.db.v1_db.helper;

public class BaseUrlConstants {

    /*COLUMN*/

    static  final  String ROW_ID = "id";
    static  final  String URL = "url";

    /*DB PROPERTY*/
    static final String DB_NAME="tv_url_DB";
    static final String TB_NAME="tv_url_TB";
    static final int DB_VERSION=1;

    /*TAB:E CREATION STATEMENT*/
    static final String CREATE_TB="CREATE TABLE tv_url_TB(id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "url TEXT NOT NULL);";


    /*TABLE DELETION STMT*/
    static final String DROP_TB = "DROP TABLE IF EXISTS "+TB_NAME;
}
