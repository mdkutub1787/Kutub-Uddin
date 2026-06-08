package com.logicsoftbd.lsl.ui.v_1_ui.bunlde_wise_sewing;

public class V1_SewingInputConstant {
    /*COLUMN*/

    static  final  String ROW_ID = "id";
    static  final  String DATE = "date";
    static  final  String BARCODE = "barcode";

    /*DB PROPERTY*/
    static final String DB_NAME="sewing_tv_DB";
    static final String TB_NAME="sewing_tv_TB";
    static final int DB_VERSION=1;

    /*TAB:E CREATION STATEMENT*/
    static final String CREATE_TB="CREATE TABLE sewing_tv_TB(id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "date TEXT NOT NULL,barcode TEXT NOT NULL);";


    /*TABLE DELETION STMT*/
    static final String DROP_TB = "DROP TABLE IF EXISTS "+TB_NAME;
}
