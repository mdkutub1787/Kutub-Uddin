/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.logicsoftbd.lsl.data.network;

import com.logicsoftbd.lsl.BuildConfig;

/**
 * Created by amitshekhar on 01/02/17.
 */

public final class ApiEndPoint {

    public static final String ENDPOINT_GOOGLE_LOGIN = BuildConfig.BASE_URL
            + "/588d14f4100000a9072d2943";

    public static final String ENDPOINT_FACEBOOK_LOGIN = BuildConfig.BASE_URL
            + "/588d15d3100000ae072d2944";



    public static final String ENDPOINT_LOGOUT = BuildConfig.BASE_URL
            + "/588d161c100000a9072d2946";

    public static final String ENDPOINT_BLOG = BuildConfig.BASE_URL
            + "/5926ce9d11000096006ccb30";

    public static final String ENDPOINT_OPEN_SOURCE = BuildConfig.BASE_URL
            + "/5926c34212000035026871cd";

    public static final String ENDPOINT_SERVER_LOGIN = BuildConfig.BASE_URL
            + "/auth/auth.php";

    public static final String ENDPOINT_BUNDLE = BuildConfig.BASE_URL
            + "/bundle/get_data.php";

    public static final String ENDPOINT_DDL = BuildConfig.BASE_URL
            + "/bundle/get_ddl_data.php ";

    // Akh Api End Point

    public static final String ENDPOINT_MENU = BuildConfig.BASE_URL
            + "/login";
//    public static final String ENDPOINT_MENU = BASE_URL_NEW
//            + "/login";

    public static final String ENDPOINT_BARCODE = BuildConfig.BASE_URL
            + "/get_roll_info_for_receive/delivery_challan";

    public static final String ENDPOINT_ROLL_RECEIVE = BuildConfig.BASE_URL
            + "/roll_receive";

    public static final String ENDPOINT_STORES = BuildConfig.BASE_URL
            + "/get_all_store";

    public static final String ENDPOINT_BARCODE_ISSUE = BuildConfig.BASE_URL
            + "/get_roll_info_for_issue/barcode_no/";
    public static final String ENDPOINT_CHALLAN_SCAN_ISSUE = BuildConfig.BASE_URL
            + "/finish_roll_rcv_scan/challan_no/";
    public static final String ENDPOINT_KNITTING = BuildConfig.BASE_URL
            + "/knitting_qc_result_entry_scan/barcode_no/";
    public static final String ENDPOINT_PURPOSES = BuildConfig.BASE_URL
            + "/get_all_issue_purpose";

    public static final String ENDPOINT_ROLL_ISSUE = BuildConfig.BASE_URL
            + "/roll_issue";

    public static final String ENDPOINT_LOCATION = BuildConfig.BASE_URL
            + "/company_wise_location_v1/company_id/";
    public static final String ENDPOINT_MACHINE = BuildConfig.BASE_URL
            + "/machine_arr/";
    public static final String ENDPOINT_SHIFT = BuildConfig.BASE_URL
            + "/shift_arr/";
    public static final String ENDPOINT_FLOOR = BuildConfig.BASE_URL
            + "/location_wise_floor_v1/location_id/";

    public static final String ENDPOINT_LINE = BuildConfig.BASE_URL
            + "/sewing_line_v1/";

    public static final String ENDPOINT_GMTS_BARCODE = BuildConfig.BASE_URL
            + "/sewing_barcode_scan/barcode_no/";

    public static final String ENDPOINT_REJECT = BuildConfig.BASE_URL
            + "/get_all_reject_name_v1";

    public static final String ENDPOINT_CUTTINGQC_BARCODE = BuildConfig.BASE_URL
            + "/qc_bundle_scan/bundle_no/";

    public static final String ENDPOINT_FINISH_FABRIC_QC = BuildConfig.BASE_URL
            + "/fin_fab_barcode_scan/barcode_no/";

    public static final String ENDPOINT_FINISH_FABRIC_QC_RESULT_SET = BuildConfig.BASE_URL
            + "/fin_fab_barcode_scan_for_result_entry/barcode_no/";
    public static final String ENDPOINT_FINISH_FABRIC_QC_ISSUE_SET = BuildConfig.BASE_URL
            + "/finish_roll_issue_scan/barcode_no/";
    public static final String ENDPOINT_FINISH_FABRIC_QR_TWO = BuildConfig.BASE_URL
            + "/fin_fab_barcode_print?batch_no=";

    public static final String ENDPOINT_FINISH_FABRIC_QR = BuildConfig.BASE_URL
            + "/fin_fab_barcode_print/barcode_no/";

//    public static final String ENDPOINT_SWEING_BARCODE = BuildConfig.BASE_URL
//            + "/sewing_barcode_scan/barcode_no/";

    public static final String ENDPOINT_SWEING_BARCODE = BuildConfig.BASE_URL
            + "/sewing_barcode_scan_v2/barcode_no/";

    public static final String ENDPOINT_LOCATION_STORE = BuildConfig.BASE_URL
            + "/get_finish_location_wise_store/location_id/";
    public static final String ENDPOINT_LOCATION_LIST = BuildConfig.BASE_URL
            + "/get_finish_location/";
    public static final String ENDPOINT_SWEING_IO_SAVE = BuildConfig.BASE_URL
            + "/swing_input_output_v1";

    public static final String ENDPOINT_RESULT_ENTRY_IO_SAVE = BuildConfig.BASE_URL
            + "/fin_fab_qc_result_entry";
    public static final String ENDPOINT_KNITTING_RESULT_ENTRY_IO_SAVE = BuildConfig.BASE_URL
            + "/knitting_qc_result_entry";
    public static final String ENDPOINT_EMB_SP_BARCODE = BuildConfig.BASE_URL
            + "/print_emb_sp_barcode_data/barcode_no/";

    public static final String ENDPOINT_EMV_SP_SAVE = BuildConfig.BASE_URL
            + "/print_emb_sp_save";

    public static final String ENDPOINT_EMV_SP_RECEIVE_SAVE = BuildConfig.BASE_URL
            + "/print_emb_sp_receive_save";

    public static final String ENDPOINT_FINISH_FABRIC_SAVE = BuildConfig.BASE_URL
            + "/fin_fab_barcode_save";
    public static final String ENDPOINT_FINISH_FABRIC_ROLL_RECEIVE= BuildConfig.BASE_URL
            + "/finish_roll_rcv_save";
    public static final String ENDPOINT_REFERENCE_DATA= BuildConfig.BASE_URL
            + "/refference_data/type/";

    public static final String ENDPOINT_DEFECT_LIST= BuildConfig.BASE_URL
            + "/finish_qc_defect_array/";
    public static final String ENDPOINT_KNIT_DEFECT_LIST= BuildConfig.BASE_URL
            + "/knit_qc_defect_array";
    public static final String ENDPOINT_ISSUE_PURPOSE_LIST= BuildConfig.BASE_URL
            + "/issue_purpose_list";
    public static final String ENDPOINT_ISSUE_STORE_LIST= BuildConfig.BASE_URL
            + "/get_finish_store/";
    public static final String ENDPOINT_DEFECT_INCH= BuildConfig.BASE_URL
            + "/knit_defect_inchi_array/";
    public static final String ENDPOINT_FABRIC_GRADE= BuildConfig.BASE_URL
            + "/fabric_grade/";
    public static final String ENDPOINT_FABRIC_SHADE= BuildConfig.BASE_URL
            + "/finish_qc_fab_shade/";
    public static final String ENDPOINT_QC_SAVE = BuildConfig.BASE_URL
            + "/qc_bundle";
    public static final String ENDPOINT_ROLL_ISSUE_SAVE = BuildConfig.BASE_URL
            + "/finish_roll_issue_save";
    public static final String ENDPOINT_ALTER_DEFECT = BuildConfig.BASE_URL
            + "/sew_fin_alter_defect_type_v1";

    public static final String ENDPOINT_SPOT_DEFECT = BuildConfig.BASE_URL
            + "/sew_fin_spot_defect_type_v1";


    private ApiEndPoint() {
        // This class is not publicly instantiable
    }

}
