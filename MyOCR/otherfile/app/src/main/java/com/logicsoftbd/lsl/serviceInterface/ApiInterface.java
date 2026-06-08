package com.logicsoftbd.lsl.serviceInterface;

import com.logicsoftbd.lsl.data.network.model.CompactBatchScanResponse;
import com.logicsoftbd.lsl.data.network.model.CompactingDefaultResponse;
import com.logicsoftbd.lsl.data.network.model.CompanyWiseFloorResponse;
import com.logicsoftbd.lsl.data.network.model.DyeingProFunctionalBatchResponse;
import com.logicsoftbd.lsl.data.network.model.DyeingProdBatchScanResponse;
import com.logicsoftbd.lsl.data.network.model.DyeingProdSaveResponse;
import com.logicsoftbd.lsl.data.network.model.DyeingProductionLoadResponse;
import com.logicsoftbd.lsl.data.network.model.FabricShade;
import com.logicsoftbd.lsl.data.network.model.FinishFabricIssueSet;
import com.logicsoftbd.lsl.data.network.model.FinishFabricRollIssueResponses;
import com.logicsoftbd.lsl.data.network.model.FinishFabricRollReceive;
import com.logicsoftbd.lsl.data.network.model.FinishFabricRollRequest;
import com.logicsoftbd.lsl.data.network.model.FloorWiseMachineResponse;
import com.logicsoftbd.lsl.data.network.model.SlitteringDefaultResponse;
import com.logicsoftbd.lsl.data.network.model.SlitteringSequzBarCodeResponse;
import com.logicsoftbd.lsl.data.network.model.SlitteringSequzCompanyWiseFloorResponse;
import com.logicsoftbd.lsl.data.network.model.SlitteringSequzFunctionalbatchResponse;
import com.logicsoftbd.lsl.data.network.model.SlitteringSequzSaveResponse;
import com.logicsoftbd.lsl.data.network.model.StenteringBatchScanResponse;
import com.logicsoftbd.lsl.data.network.model.StenteringDefaultResponse;
import com.logicsoftbd.lsl.data.network.model.StenteringFunctionalBatchScanResponse;
import com.logicsoftbd.lsl.data.network.v1_model.*;
import com.logicsoftbd.lsl.data.network.v1_model.V1_GreyRollIssue.V1_GreyRollIssueRequest;
import com.logicsoftbd.lsl.data.network.v1_model.V1_GreyRollIssue.V1_GreyRollIssueSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_GreyRollIssue.V1_Issue_Purpose.V1_GreyRollIssuePurposeModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_transfer_out.V1GreyFabricTransferOutStoreListResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_transfer_out.barcode.TransferOutResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_finish_fabric_receive.FFRResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_finish_fabric_receive.Fff_save_response;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("login")
    Call<V1_LoginResponse> loginResponseCall(@Query("user_id") String user_id,
                                             @Query("pwd") String pwd,
                                             @Query("device_id") String mac_id,
                                             @Query("fcm_token") String fcm_token,
                                             @Query("device_type") String device_type);

    @GET("login_v1")
    Call<V1_LoginResponse> loginResponseCall_v1(@Query("user_id") String user_id,
                                                @Query("pwd") String pwd,
                                                @Query("device_id") String mac_id,
                                                @Query("fcm_token") String fcm_token,
                                                @Query("device_type") String device_type);

    @GET("user_wise_menu")
    Call<V1_MenuModelClass> menuResponseCall(@Query("user_id") String user_id);

    @GET("active_qc_session_list")
    Call<V1_ActiveDeviceModel> getActiveDeviceModelCall();

    @GET("home_data")
    Call<V1_LineWiseHourlyProductionResponse> homeDataResponseCall(@Query("company") String company,
                                                                   @Query("location") String location,
                                                                   @Query("floor") String floor,
                                                                   @Query("line") String line,
                                                                   @Query("country_id") String country_id,
                                                                   @Query("gmt_item_id") String gmt_item_id,
                                                                   @Query("job_id") String job_id);

    @GET("company_and_source_list")
    Call<V1_BundleWiseSewingInputClass> getBundkeWiseSewingInputClassCall();

    @GET("all_approval")
    Call<V1_ApprovalModel> getApprovalModelCall(@Query("user_id") Integer user_id,
                                                @Query("company_id") Integer company_id,
                                                @Query("page_id") Integer page_id,
                                                @Query("select_no") Integer select_no);

    @GET("all_approval")
    Call<V1_ApprovalDetailsModel> getFabricBookingModelCall(@Query("user_id") Integer user_id,
                                                            @Query("company_id") Integer company_id,
                                                            @Query("page_id") Integer page_id,
                                                            @Query("app_id") String app_id);

    @GET("company_wise_location")
    Call<V1_BundleWiseCompanyToLocationClass> getBundleWiseCompanyToLocationClassCall(@Query("company_id") Integer company_id);

    @GET("barcodes_by_batch_for_qc")
    Call<V1_BarcodeByBatchForQCResponse> getBarcodeByBatchForQCClassCall(@Query("batch_no") String batch_no);

    @GET("finish_fabric_qc_entry")
    Call<V1_BarcodeDetailsFromBatchFinishQCResponse> getBarcodeDetailsFromBatchForQCClassCall(@Query("code") String code);

    @GET("location_wise_floor")
    Call<V1_BundleWiseLocationWiseFloorClass> getBundleWiseLocationWiseFloorClassCall(@Query("location_id") Integer location_id);

    @GET("location_wise_floor_v1")
    Call<V1_FinishLocationWiseFloorClass> getFinishWiseLocationWiseFloorClassCall(@Query("location_id") Integer location_id,
                                                                                  @Query("production_process") Integer production_process);

    @GET("sewing_line")
    Call<V1_BundleWiseFloorWiseLineClass> getBundleWiseLocationWiseFloorClassCall(@Query("company_id") Integer company_id,
                                                                                  @Query("location_id") Integer location_id,
                                                                                  @Query("floor_id") Integer floor_id,
                                                                                  @Query("issue_date") String issue_date);

    @GET("monthly_capacity_vs_allocated_order")
    Call<V1_CapacityVsAllocatedOrderModel> getCapacityVsBookedModelCall(@Query("company_id") Integer company_id,
                                                                        @Query("location_id") Integer location_id,
                                                                        @Query("from_month_year") String from_month_year,
                                                                        @Query("to_month_year") String to_month_year);

    @GET("monthly_capacity_vs_plan")
    Call<V1_CapacityVsPlanModel> getCapacityVsPlanModelCall(@Query("company_id") Integer company_id,
                                                            @Query("location_id") Integer location_id,
                                                            @Query("from_month_year") String from_month_year,
                                                            @Query("to_month_year") String to_month_year);

    @GET("monthly_plan_vs_booked")
    Call<V1_PlanVsBookedModel> getPlanVsBookedModelCall(@Query("company_id") Integer company_id,
                                                        @Query("location_id") Integer location_id,
                                                        @Query("from_month_year") String from_month_year,
                                                        @Query("to_month_year") String to_month_year);

    @GET("monthly_plan_vs_booked_vs_capacity")
    Call<V1_PlanVsBookedVsCapacityModel> getPlanVsBookedVsCapacityModelCall(@Query("company_id") Integer company_id,
                                                                            @Query("location_id") Integer location_id,
                                                                            @Query("from_month_year") String from_month_year,
                                                                            @Query("to_month_year") String to_month_year);

    @GET("shipment_pending")
    Call<V1_ShipmentPendingModel> getPlanVsBookedVsCapacityModelCall(@Query("company_id") Integer company_id,
                                                                     @Query("year") String year,
                                                                     @Query("date_category") Integer date_category);

    @GET("shipment_schedule_management")
    Call<V1_ShipmentModelClass> getShipmentModelClassCall(@Query("company_id") Integer company_id,
                                                          @Query("date_category") Integer date_category,
                                                          @Query("start_date") String start_date,
                                                          @Query("end_date") String end_date);

    @GET("sewing_barcode_scan")
    Call<V1_SewingInputClass> getSewingInputModelClassCall(@Query("company_id") Integer company_id,
                                                           @Query("location") Integer location,
                                                           @Query("floor") Integer floor,
                                                           @Query("line") Integer line,
                                                           @Query("barcode") String barcode,
                                                           @Query("type") Integer type);

    @GET("sewing_barcode_pcs_scan_v2")
    Call<V1_BundeWiseSewingInputPCSResponse> getSewingInputPCSModelClassCall(@Query("company_id") String company_id,
                                                                             @Query("location") String location,
                                                                             @Query("floor") String floor,
                                                                             @Query("line") String line,
                                                                             @Query("barcode") String barcode,
                                                                             @Query("type") String type);

//    @GET("style_wise_operation_list")
//    Call<V1_StyleWiseOperationResponse> getStyleWiseOperationCall(@Query("JOB_NO_MST") String JOB_NO_MST,
//                                                                  @Query("PO_BREAK_DOWN_ID") String PO_BREAK_DOWN_ID,
//                                                                  @Query("ITEM_NUMBER_ID") String ITEM_NUMBER_ID,
//                                                                  @Query("STYLE_NUMBER") String STYLE_NUMBER,
//                                                                  @Query("LINE_ID") String LINE_ID,
//                                                                  @Query("USER_ID") String USER_ID);
    @GET("style_wise_operation_list_v2")
    Call<V1_StyleWiseOperationResponse> getStyleWiseOperationCall(@Query("JOB_NO_MST") String JOB_NO_MST,
                                                                  @Query("PO_BREAK_DOWN_ID") String PO_BREAK_DOWN_ID,
                                                                  @Query("ITEM_NUMBER_ID") String ITEM_NUMBER_ID,
                                                                  @Query("STYLE_NUMBER") String STYLE_NUMBER,
                                                                  @Query("LINE_ID") String LINE_ID,
                                                                  @Query("USER_ID") String USER_ID);

    @GET("style_wise_operation_list_v2")
    Call<V1_StyleWiseOperationResponse> getStyleWiseWithoutPOOperationV2Call(@Query("JOB_NO_MST") String JOB_NO_MST,
                                                                           @Query("ITEM_NUMBER_ID") String ITEM_NUMBER_ID,
                                                                           @Query("STYLE_NUMBER") String STYLE_NUMBER,
                                                                           @Query("LINE_ID") String LINE_ID,
                                                                           @Query("USER_ID") String USER_ID);
    @GET("style_wise_operation_list")
    Call<V1_StyleWiseOperationResponse> getStyleWiseWithoutPOOperationCall(@Query("JOB_NO_MST") String JOB_NO_MST,
                                                                           @Query("ITEM_NUMBER_ID") String ITEM_NUMBER_ID,
                                                                           @Query("STYLE_NUMBER") String STYLE_NUMBER,
                                                                           @Query("LINE_ID") String LINE_ID,
                                                                           @Query("USER_ID") String USER_ID);

    @GET("style_wise_operation_list_v2")
    Call<V1_StyleWiseOperationResponse> getStyleWiseWithoutPOOV2perationCall(@Query("JOB_NO_MST") String JOB_NO_MST,
                                                                           @Query("ITEM_NUMBER_ID") String ITEM_NUMBER_ID,
                                                                           @Query("STYLE_NUMBER") String STYLE_NUMBER,
                                                                           @Query("LINE_ID") String LINE_ID,
                                                                           @Query("USER_ID") String USER_ID);

    @GET("style_wise_operation_list_sub_contract")
    Call<V1_StyleWiseOperationResponse> getStyleWiseWithoutPOOperationSubContractCall(@Query("JOB_NO_MST") String JOB_NO_MST,
                                                                           @Query("ITEM_NUMBER_ID") String ITEM_NUMBER_ID,
                                                                           @Query("STYLE_NUMBER") String STYLE_NUMBER,
                                                                           @Query("LINE_ID") String LINE_ID,
                                                                           @Query("USER_ID") String USER_ID);

    @GET("sewing_input_output")
    Call<V1_LineWiseSewingInputModelClass> getLineWiseSewingInputModelClassCall(@Query("company_id") Integer company_id,
                                                                                @Query("barcode") String barcode,
                                                                                @Query("type") Integer type);

    @GET("color_size_wise_in_out")
    Call<V1_ColorBreakDownResponse> getColorBreakDownClassCall(@Query("COLOR_SIZE_ID") String COLOR_SIZE_ID,
                                                               @Query("LINE_ID") String LINE_ID,
                                                               @Query("PO_BREAK_DOWN_ID") String PO_BREAK_DOWN_ID,
                                                               @Query("ITEM_NUMBER_ID") String ITEM_NUMBER_ID,
                                                               @Query("COUNTRY_ID") String COUNTRY_ID,
                                                               @Query("USER_ID") String USER_ID);

    @GET("color_size_wise_in_outv2")
    Call<V1_ColorBreakDownResponse> getColorBreakDownClassCall_V2(@Query("COLOR_SIZE_ID") String COLOR_SIZE_ID,
                                                               @Query("LINE_ID") String LINE_ID,
                                                               @Query("PO_BREAK_DOWN_ID") String PO_BREAK_DOWN_ID,
                                                               @Query("ITEM_NUMBER_ID") String ITEM_NUMBER_ID,
                                                               @Query("COUNTRY_ID") String COUNTRY_ID,
                                                               @Query("USER_ID") String USER_ID,
                                                               @Query("COLOR_ID") String COLOR_ID,
                                                               @Query("SIZE_ID") String SIZE_ID);

    @GET("color_size_wise_in_out_subcontract")
    Call<V1_ColorBreakDownResponse> getColorBreakDownSubcontractClassCall(@Query("COLOR_SIZE_ID") String COLOR_SIZE_ID,
                                                               @Query("LINE_ID") String LINE_ID,
                                                               @Query("PO_BREAK_DOWN_ID") String PO_BREAK_DOWN_ID,
                                                               @Query("ITEM_NUMBER_ID") String ITEM_NUMBER_ID,
                                                               @Query("COUNTRY_ID") String COUNTRY_ID,
                                                               @Query("USER_ID") String USER_ID);

    @GET("sewing_input_output")
    Call<V1_BundleWiseSewingOutputClass> getLineWiseSewingOutputModelClassCall(@Query("company_id") Integer company_id,
                                                                               @Query("barcode") String barcode,
                                                                               @Query("type") Integer type);

    @GET("sewing_barcode_scan")
    Call<V1_BundleWiseSewingOutputClass> getSewingOutModelClassCall(@Query("company_id") Integer company_id,
                                                                    @Query("location") Integer location,
                                                                    @Query("floor") Integer floor,
                                                                    @Query("line") Integer line,
                                                                    @Query("barcode") String barcode,
                                                                    @Query("type") Integer type);

    @GET("linking_input_output_by_barcode")
    Call<V1_LinkingInputModelClass> gerLinkingInputModelClassCall(@Query("company_id") Integer company_id,
                                                                  @Query("barcode") String barcode,
                                                                  @Query("type") Integer type);

    @GET("linking_input_output_by_barcode")
    Call<V1_BundleWiseSewingOutputClass> getSewingOutputModelClassCall(@Query("company_id") Integer company_id,
                                                                       @Query("barcode") String barcode,
                                                                       @Query("type") Integer type);

    @GET("sewing_input_output_by_challan")
    Call<V1_ChallanModel> getChallanModelCall(@Query("company_id") Integer company_id,
                                              @Query("challan") String barcode,
                                              @Query("type") Integer type);

    @GET("consolidated_order_summery")
    Call<V1_ConsolitatedOrderSummeryModelClass> getConsolitatedOrderSummeryModelClassCall(@Query("company_id") Integer company_id,
                                                                                          @Query("date_category") Integer date_category,
                                                                                          @Query("start_date") String start_date,
                                                                                          @Query("end_date") String end_date);

    @GET("barcode_details")
    Call<V1_GreyFabricModelClass> getBarCodeModelClassCall(@Query("code") String code);

    @POST("create_qc_result")
    Call<V1_DataSaveResponse> saveUpdateknittingFabricCall(@Body RequestBody requestBody);
    @POST("roll_wise_gray_fab_delivery_to_store")
    Call<V1_RollWiseGrayDeliveryToStoreSaveResponse> saveUpdateGrayFabricDeliveryToStoreCall(@Body RequestBody requestBody);

    @POST("fab_hanger_archive_meeting")
    Call<V1_DataSaveResponse> saveHangerArchiveCall(@Body RequestBody requestBody);

    @POST("create_observation_kniting_qc_result")
    Call<V1_DataSaveResponse> saveUpdateknittingFabricObsCall(@Body RequestBody requestBody);

    @GET("finish_barcode_details")
    Call<V1_FinishFabricModelClass> getFinishFabricModelClassCall(@Query("code") String code);

    @GET("observation_kniting_barcode_details")
    Call<V1_ObsKnittingModelClass> getObsKnittingModelClassCall(@Query("code") String code);

    @POST("create_finish_qc_result")
    Call<V1_DataSaveResponse> saveupdatefinishFabriccall(@Body RequestBody requestBody);

    @POST("save_update_sewing_input")
    Call<V1_DataSaveResponse> saveUpdateBundleSewingInputCall(@Body RequestBody requestBody);

    @POST("save_update_sewing_input_barcode_pcs_v2")
    Call<V1_SewingInputPCSSaveResponse> saveUpdateBundleSewingInputPCSCall(@Body RequestBody requestBody);

    @POST("create_tabwise_line")
    Call<V1_DataSaveResponse> saveTabConfigCall(@Body RequestBody requestBody);

    @POST("create_tabwise_line_sub_contract")
    Call<V1_DataSaveResponse> saveTabConfigSubContractCall(@Body RequestBody requestBody);

    @POST("inactive_sewing_config")
    Call<V1_DataSaveResponse> inActiveTabConfigCall(@Body RequestBody requestBody);

    @POST("save_update_sewing_input")
    Call<V1_DataSaveResponse> saveUpdateBundleSewingOutputCall(@Body RequestBody requestBody);

    @POST("save_update_sewing_input_gross")
    Call<V1_GrossSewingSaveResponse> saveUpdateBundleSewingGrossOutputCall(@Body RequestBody requestBody);

    @POST("save_update_sewing_input_gross_v2")
    Call<V1_GrossSewingSaveResponse> saveUpdateBundleSewingGrossV2OutputCall(@Body RequestBody requestBody);

    @POST("save_update_sewing_input_gross_rectified")
    Call<V1_GrossSewingSaveResponse> saveUpdateBundleSewingGrossOutputRectifiedCall(@Body RequestBody requestBody);

    @POST("save_update_sewing_input_gross_gmts_rectified")
    Call<V1_GrossSewingSaveResponse> saveUpdateBundleSewingGrossOutputGmtsRectifiedCall(@Body RequestBody requestBody);

    @GET("observation_finish_batch_barcode_details")
    Call<V1_ObsFinishModelClass> getFinishFabricObsModelClassCall(@Query("code") String code);

    @GET("barcode_report")
    Call<V1_QcReportModelClass> getQcReportModelClassCall(@Query("code") String code);

    @GET("production_process_by_barcode")
    Call<V1_BundleTrackingReportModelClass> getBundleTrackingReportActivityCall(@Query("barcode") String barcode);

    @GET("company_wise_team_name")
    Call<V1_TeamResponseModel> getTeamResponseModelCall(@Query("company_id") String company_id,
                                                        @Query("location_id") String location_id);

    @GET("ovservation_knit_finish_defect_inchi")
    Call<V1_ObservationDefectClass> getObservationDefectClassCall();

    @GET("observation_finish_batch_barcode_details")
    Call<V1_BatchModelClass> getBatchModelClassCall(@Query("code") String code,
                                                    @Query("is_batch_dtls") Integer is_batch_dtls);

    @GET("defect_type")
    Call<V1_RejectSewingDefectResponse> getRejectSewingDefectResponseCall(@Query("defect_type_id") String defect_type_id,
                                                                          @Query("entry_form") Integer entry_form);

    @GET("defect_type")
    Call<V1_AlterSewingDefectResponse> getAlterSewingDefectResponseCall(@Query("defect_type_id") String defect_type_id,
                                                                        @Query("entry_form") Integer entry_form);

    @GET("defect_type")
    Call<V1_SpotSewingDefectResponse> getSpotSewingDefectResponseCall(@Query("defect_type_id") String defect_type_id,
                                                                      @Query("entry_form") Integer entry_form);

    @GET("sewing_pending")
    Call<V1_SewingInputPendingModelClass> getSewingInputPendingModelClassCall(@Query("company_id") Integer company_id,
                                                                              @Query("location_id") Integer location_id,
                                                                              @Query("floor_id") Integer floor_id,
                                                                              @Query("line_id") Integer line_id,
                                                                              @Query("start_date") String start_date,
                                                                              @Query("end_date") String end_date);
    @GET("shift_duration_data")
    Call<V1_BundleWiseCompanyToShiftClass> getBundleWiseCompanyToShiftClassCall();

    @GET("shift_data_from_arr")
    Call<V1_ShiftResponse> getShiftClassCall();

    @GET("fabric_shade_data")
    Call<FabricShade> getShadeClassCall();

    @GET("bag_keeping")
    Call<V1_BagKeepingResponse> getBagKeepingClassCall(@Query("batch_no") String batch_no,
                                                       @Query("bag_no") String bag_no,
                                                       @Query("color_id") String selectedColorId);

    @GET("bag_keeping_packing_list_print")
    Call<V1_BagPrintResponse> getBagKeepingPrintClassCall(@Query("batch_no") String batch_no,
                                                          @Query("bag_no") String bag_no);

    @GET("rfid_aop_after_bag_keeping_and_sticker_bag_rcvd")
    Call<V1_AOPBagKeepingResponse> getAOPBagKeepingClassCall(@Query("search_type") String search_type,
                                                             @Query("serach_query") String serach_query,
                                                             @Query("bag_no") String bag_no,
                                                             @Query("color_id") String color_id);

    @GET("bag_keeping_data_by_system_no")
    Call<V1_BagKeepingDataBySystemResponse> getBagKeepingBySystemClassCall(@Query("system_no") String system_no,
                                                                           @Query("bag_no") String bag_no,
                                                                           @Query("color_id") Integer selectedColorId);

    @GET("rfid_store_aop_dept_bag_rcvd")
    Call<V1_AOPDeptBagReceiveResponse> getAOPDeptBagReceiveClassCall(@Query("bag_no") String bag_no,
                                                                     @Query("color_id") Integer selectedColorId);

    @GET("rfid_store_aop_dept_bag_rcvd_challan")
    Call<V1_AOPDeptBagReceiveResponse> getAOPDeptBagReceiveBySystemNoClassCall(@Query("system_no") String system_no);

    @GET("rfid_store_finish_fabric_aop_dyed_bag_rcvd")
    Call<V1_DyedAOPBagReceiveResponse> getAOPDyedDeptBagReceiveClassCall(@Query("batch_no") String batch_no,
                                                                         @Query("bag_no") String bag_no,
                                                                         @Query("color_id") Integer selectedColorId);

    @GET("rfid_grey_store_dyeing_reject_fabric_bag_rcvd")
    Call<V1_GreyStoreRejectBagReceiveResponse> getGreyStoreRejectBagClassCall(@Query("bag_no") String bag_no,
                                                                              @Query("color_id") Integer selectedColorId);
    @GET("rfid_grey_store_dyeing_reject_fabric_bag_rcvd_challan")
    Call<V1_GreyStoreRejectBagReceiveResponse> getGreyStoreRejectBagBySystemNoClassCall(@Query("system_no") String system_no);

    @GET("rfid_store_receive")
    Call<V1_BagReceiveResponse> getBagReceiveClassCall(@Query("bag_no") String bag_no,
                                                       @Query("room_rack_id") String challan_no,
                                                       @Query("color_id") Integer selectedColorId,
                                                       @Query("item_category") Integer selectedCategoryId);

    @GET("rfid_bag_receive_challan")
    Call<V1_BagReceiveResponse> getBagChallanReceiveClassCall(@Query("challan_no") String challan_no,
                                                              @Query("room_rack_id") String room_rack_id,
                                                              @Query("item_category") Integer selectedCategoryId);

    @GET("rfid_bag_delivery")
    Call<V1_BagDeliveryResponse> getBagDeliveryClassCall(@Query("bag_no") String bag_no);

    @GET("rfid_empty_bag_receive")
    Call<V1_BagEmptyReceiveResponse> getBagEmptyReceiveClassCall(@Query("bag_no") String bag_no,
                                                                 @Query("color_id") Integer selectedColorId,
                                                                 @Query("item_category") Integer selectedCategoryId);

    @GET("rfid_store_issue")
    Call<V1_BagIssueResponse> getBagIssueClassCall(@Query("bag_no") String bag_no,
                                                   @Query("room_rack_id") String roomRackId,
                                                   @Query("color_id") String selectedColorId,
                                                   @Query("item_category") Integer selectedCategoryId);

    @GET("rfid_store_issue_challan")
    Call<V1_BagIssueResponse> getBagIssueByChallanClassCall(@Query("challan_no") String challan_no,
                                                   @Query("room_rack_id") String roomRackId,
                                                   @Query("color_id") String selectedColorId,
                                                   @Query("item_category") Integer selectedCategoryId);

    @GET("rfid_bag_return")
    Call<V1_BagReturnResponse> getBagReturnClassCall(@Query("bag_no") String bag_no,
                                                     @Query("color_id") Integer selectedColorId,
                                                     @Query("item_category") Integer selectedCategoryId);

    @GET("dying_production_load_list")
    Call<DyeingProductionLoadResponse> dyeingProductionLoadResponseCall();

    @GET("company_wise_floor")
    Call<CompanyWiseFloorResponse> companyWiseFloorResponseCall(@Query("company_id") String company_id);

    @GET("company_floor_machine")
    Call<FloorWiseMachineResponse> compnayFloorWiseMachineResponseCall(@Query("floor_id") String floor_id);

    @GET("dying_prod_batch_scan_list")
    Call<DyeingProdBatchScanResponse> deDyeingProdBatchScanResponseCall(@Query("load_unload") String load_unload,
                                                                        @Query("batch_no") String batch_no);

    @GET("dying_prod_functional_batch_scan_list")
    Call<DyeingProFunctionalBatchResponse> dyeingProFunctionalBatchResponseCall(@Query("load_unload") String load_unload,
                                                                                @Query("functional_no") String functional_no);

    @POST("save_update_dyeing_production")
    Call<DyeingProdSaveResponse> saveUpdateDyeingProductionCall(@Body RequestBody requestBody);

    @POST("bag_keeping_save")
    Call<V1_BagKeepingSaveResponse> saveUpdateBagKeepingCall(@Body RequestBody requestBody);

    @POST("rfid_aop_after_bag_keeping_and_sticker_bag_rcvd_save")
    Call<V1_AOPBagKeepingSaveResponse> saveUpdateAOPBagKeepingCall(@Body RequestBody requestBody);

    @POST("rfid_bag_qc_save")
    Call<V1_BagKeepingSaveResponse> saveUpdateBagKeepingQCCall(@Body RequestBody requestBody);

    @POST("rfid_store_aop_dept_bag_receive_save")
    Call<V1_AOPDeptBagSaveResponse> saveUpdateAopDeptRcvCall(@Body RequestBody requestBody);

    @POST("rfid_store_finish_fabric_aop_dyed_bag_receive_save")
    Call<V1_AOPDeptBagSaveResponse> saveUpdateAopDyedRcvCall(@Body RequestBody requestBody);

    @POST("rfid_grey_store_dyeing_reject_fabric_bag_receive_save")
    Call<V1_AOPDeptBagSaveResponse> saveUpdateGreyStoreRejectBagCall(@Body RequestBody requestBody);

    @POST("rfid_bag_delivery_save")
    Call<V1_BagDeliverySaveResponse> saveUpdateBagDeliveryCall(@Body RequestBody requestBody);

    @GET("aop_color_name_list")
    Call<V1_FabricBagColorModel> getFabricBagColorCall();

    @POST("rfid_empty_bag_receive_save")
    Call<V1_BagEmptyReceiveSaveResponse> saveUpdateBagEmptyReceiveCall(@Body RequestBody requestBody);

    @POST("rfid_store_receive_save")
    Call<V1_BagReceiveSaveResponse> saveUpdateBagReceiveCall(@Body RequestBody requestBody);

    @POST("rfid_store_issue_save")
    Call<V1_BagIssueSaveResponse> saveUpdateBagIssueCall(@Body RequestBody requestBody);

    @POST("rfid_bag_return_save")
    Call<V1_BagReturnSaveResponse> saveUpdateBagReturnCall(@Body RequestBody requestBody);

    @GET("finish_prod_company_defualt_data")
    Call<SlitteringDefaultResponse> slitteringDefaultResponseCall(@Query("entry_form_no") String entry_form_no);

    @GET("company_wise_fin_floor")
    Call<SlitteringSequzCompanyWiseFloorResponse> slitteringCompanyWiseFloorResponseCall(@Query("company_id") String company_id);

    @GET("company_floor_fin_machine")
    Call<FloorWiseMachineResponse> slitteringCompanyFloorMachineResponseCall(@Query("floor_id") String floor_id);

    @GET("fin_prod_batch_scan_data_list")
    Call<SlitteringSequzBarCodeResponse> slitteringSequzBarcodeResponseCall(@Query("batch_no") String batch_no,
                                                                            @Query("barcode_no") String barcode_no,
                                                                            @Query("entry_form_no") String entry_form_no);

    @POST("save_update_fin_production")
    Call<SlitteringSequzSaveResponse> saveUpdateSlittingSquzCall(@Body RequestBody requestBody);

    @POST("finish_fabric_qc_entry_save")
    Call<V1_DataSaveResponse> saveUpdateFinishFabricQCV2Call(@Body RequestBody requestBody);

    @GET("finish_prod_dtls_list_view")
    Call<SlitteringSequzFunctionalbatchResponse> SlitteringSequzFunctionalbatchResponseCall(@Query("batch_id") String batch_id,
                                                                                            @Query("entry_form_no") String entry_form_no);

    @GET("finish_prod_company_defualt_data")
    Call<StenteringDefaultResponse> stenteringDefaultResponseCall(@Query("entry_form_no") String entry_form_no);

    @GET("fin_prod_batch_scan_data_list")
    Call<StenteringBatchScanResponse> stenteringBarcodeResponseCall(@Query("batch_no") String batch_no,
                                                                    @Query("barcode_no") String barcode_no,
                                                                    @Query("entry_form_no") String entry_form_no);

    @GET("finish_prod_dtls_list_view")
    Call<StenteringFunctionalBatchScanResponse> stenteringFunctionalBatchResponse(@Query("batch_id") String batch_no,
                                                                                  @Query("entry_form_no") String entry_form_no);

    @GET("finish_prod_company_defualt_data")
    Call<CompactingDefaultResponse> compactingDefaultResponse(@Query("entry_form_no") String entry_form_no);

    @GET("fin_prod_batch_scan_data_list")
    Call<CompactBatchScanResponse> compactingBatchScanResponse(@Query("batch_no") String batch_no,
                                                               @Query("barcode_no") String barcode_no,
                                                               @Query("entry_form_no") String entry_form_no);

    @GET("finish_roll_rcv_scan")
    Call<FinishFabricRollReceive> finishReceiveBatchScan(@Query("challan_no") String challan_no);


    @Multipart
    @POST("image_upload")
    Call<ProfileImageResponse> putPostImage(@Part("bundle_id") RequestBody bundleId,
                                            @Part("defect_type") RequestBody defectType,
                                            @Part MultipartBody.Part file);


    @GET("bundle_wise_sewing_barcode_scan")
    Call<V1_OperationItemResponse> getSewingOperationModelClassCall(@Query("barcode_no") String barcode_no,
                                                                    @Query("operation_id") String operation_id,
                                                                    @Query("OPERATOR_ID") String OPERATOR_ID);

    @GET("get_barcode_info")
    Call<V1_GreyRollReceiveRequest> getGreyRollReceiveByChallanModelClassCall(@Query("delivery_challan") String delivery_challan,
                                                                              @Query("location_src") String location_src);

    @GET("get_barcode_info")
    Call<V1_GreyRollReceiveRequest> getGreyRollReceiveByBarcodeModelClassCall(@Query("barcode") String barcode,
                                                                              @Query("location_src") String location_src);

    @GET("get_roll_info_for_issue")
    Call<V1_GreyRollIssueRequest> getGreyRollIssueByBarcodeModelClassCall(@Query("barcode_no") String barcode);

    @GET("ws_operation")
    Call<V1_BundleWiseSewingOperationResponse> getSewingOutputOperationModelClassCall(@Query("style_ref") String style_ref,
                                                                                      @Query("bulletin_type_id") String bulletin_type_id,
                                                                                      @Query("gmts_item_id") String gmts_item_id);

    @GET("tabwise_sewingline_style_item")
    Call<V1_StyleWiseConfigResponse> getStyleWiseTabCongigClassCall(@Query("company_id") String company_id,
                                                                    @Query("location_id") String location_id,
                                                                    @Query("floor_id") String floor_id,
                                                                    @Query("line_id") String line_id);

    @GET("tabwise_sewingline_style_item_sub_contract")
    Call<V1_StyleWiseConfigResponse> getStyleWiseTabConfigSubContractClassCall(@Query("company_id") String company_id,
                                                                    @Query("location_id") String location_id,
                                                                    @Query("floor_id") String floor_id,
                                                                    @Query("line_id") String line_id);

    @GET("get_all_issue_purpose")
    Call<V1_GreyRollIssuePurposeModel> getGreyRollIssuePurposeModelClassCall();

    @GET("defected_details_list_to_rectify")
    Call<V1_DefectListOFRectifiedModel> getDefectedListForRectifiedModelCall(@Query("COLOR_SIZE_ID") String COLOR_SIZE_ID,
                                                                             @Query("LINE_ID") String LINE_ID,
                                                                             @Query("USER_ID") String USER_ID);

    @GET("defected_details_list_to_rectify")
    Call<V1_DefectListOFRectifiedModel> getDefectedListForRectifiedModelCall_V1(@Query("COLOR_ID") String COLOR_ID,
                                                                                @Query("LINE_ID") String LINE_ID,
                                                                                @Query("USER_ID") String USER_ID,
                                                                                @Query("PO_BREAK_DOWN_ID") String PO_BREAK_DOWN_ID,
                                                                                @Query("GMT_ITEM_ID") String GMT_ITEM_ID,
                                                                                @Query("JOB_ID") String JOB_ID);

    @GET("defected_details_list_to_rectify")
    Call<V1_DefectListOFRectifiedModel_V4> getDefectedListForRectifiedModelCall_V4(@Query("COLOR_ID") String COLOR_ID,
                                                                                   @Query("LINE_ID") String LINE_ID,
                                                                                   @Query("USER_ID") String USER_ID,
                                                                                   @Query("PO_BREAK_DOWN_ID") String PO_BREAK_DOWN_ID,
                                                                                   @Query("GMT_ITEM_ID") String GMT_ITEM_ID,
                                                                                   @Query("JOB_ID") String JOB_ID);

    @GET("defected_details_list_to_rectify")
    Call<V1_DefectListOfRectifiedModel_v5> getDefectedListForRectifiedModelCall_V5(@Query("COLOR_ID") String COLOR_ID,
                                                                                   @Query("LINE_ID") String LINE_ID,
                                                                                   @Query("USER_ID") String USER_ID,
                                                                                   @Query("PO_BREAK_DOWN_ID") String PO_BREAK_DOWN_ID,
                                                                                   @Query("GMT_ITEM_ID") String GMT_ITEM_ID,
                                                                                   @Query("JOB_ID") String JOB_ID);

    @GET("defected_details_list_to_rectify_subcontract")
    Call<V1_DefectListOfRectifiedModel_v5> getDefectedListForRectifiedSubContractModelCall_V5(@Query("COLOR_ID") String COLOR_ID,
                                                                                   @Query("LINE_ID") String LINE_ID,
                                                                                   @Query("USER_ID") String USER_ID,
                                                                                   @Query("PO_BREAK_DOWN_ID") String PO_BREAK_DOWN_ID,
                                                                                   @Query("GMT_ITEM_ID") String GMT_ITEM_ID,
                                                                                   @Query("JOB_ID") String JOB_ID);

    @GET("defected_gmts_details_list_to_rectify")
    Call<V1_DefectListOfRectifiedModel_v5> getDefectedListForGmtsRectifiedModelCall_V5(@Query("COLOR_ID") String COLOR_ID,
                                                                                       @Query("LINE_ID") String LINE_ID,
                                                                                       @Query("USER_ID") String USER_ID,
                                                                                       @Query("PO_BREAK_DOWN_ID") String PO_BREAK_DOWN_ID,
                                                                                       @Query("GMT_ITEM_ID") String GMT_ITEM_ID,
                                                                                       @Query("JOB_ID") String JOB_ID);

    @GET("defected_gmts_details_list_to_rectify_subcontract")
    Call<V1_DefectListOfRectifiedModel_v5> getDefectedListForGmtsRectifiedSubContractModelCall_V5(@Query("COLOR_ID") String COLOR_ID,
                                                                                       @Query("LINE_ID") String LINE_ID,
                                                                                       @Query("USER_ID") String USER_ID,
                                                                                       @Query("PO_BREAK_DOWN_ID") String PO_BREAK_DOWN_ID,
                                                                                       @Query("GMT_ITEM_ID") String GMT_ITEM_ID,
                                                                                       @Query("JOB_ID") String JOB_ID);

    @GET("type_wise_in_out")
    Call<V2_TypeWiseInOutResponseModel> getTypeWiseInOutModelCall(@Query("PO_BREAK_DOWN_ID") String PO_BREAK_DOWN_ID,
                                                                  @Query("ITEM_NUMBER_ID") String ITEM_NUMBER_ID,
                                                                  @Query("LINE_ID") String LINE_ID,
                                                                  @Query("USER_ID") String USER_ID,
                                                                  @Query("COLOR_ID") String COLOR_ID,
                                                                  @Query("TYPE") String TYPE,
                                                                  @Query("COUNTRY_ID") String COUNTRY_ID);

    @GET("type_wise_in_out_v2")
    Call<V2_TypeWiseInOutResponseModel> getTypeWiseInOutModel_v2lCall(@Query("PO_BREAK_DOWN_ID") String PO_BREAK_DOWN_ID,
                                                                  @Query("ITEM_NUMBER_ID") String ITEM_NUMBER_ID,
                                                                  @Query("LINE_ID") String LINE_ID,
                                                                  @Query("USER_ID") String USER_ID,
                                                                  @Query("COLOR_ID") String COLOR_ID,
                                                                  @Query("TYPE") String TYPE);

    @GET("type_wise_in_out_subcontract")
    Call<V2_TypeWiseInOutResponseModel> getTypeWiseInOutSubcontractModelCall(@Query("PO_BREAK_DOWN_ID") String PO_BREAK_DOWN_ID,
                                                                  @Query("ITEM_NUMBER_ID") String ITEM_NUMBER_ID,
                                                                  @Query("LINE_ID") String LINE_ID,
                                                                  @Query("USER_ID") String USER_ID,
                                                                  @Query("COLOR_ID") String COLOR_ID,
                                                                  @Query("TYPE") String TYPE,
                                                                  @Query("COUNTRY_ID") String COUNTRY_ID);

    @GET("version_check")
    Call<V1_ApkVersionResponse> getAppVersionCheckResponseCall();

    @GET("sewing_output_configaration_level")
    Call<ProConfigurationResponse> getProConfigurationResponseCall(@Query("company_id") String company_id);

    @POST("roll_delivery_barcode_receive")
    Call<V1_GreyRollSaveRequest> saveGreyRollReceiveCall(@Body RequestBody requestBody);

    @POST("roll_issue")
    Call<V1_GreyRollIssueSaveResponse> saveGreyRollIssueCall(@Body RequestBody requestBody);

    @POST("bundle_wise_sewing_barcode")
    Call<V1_OperationSaveResponse> saveOperationInputCall(@Body RequestBody requestBody);

    @POST("bundle_data_save_for_cutting_store")
    Call<V1_CuttingRollReceivePostResponse> saveCuttingRollReceiveCall(@Body RequestBody requestBody);

    @POST("bundle_data_issue_from_recv_rack")
    Call<V1_CuttingStoreIssueResponse> saveCuttingRollIssueCall(@Body RequestBody requestBody);


    @GET("approval_menu_by_privilege")
    Call<V1_ApprovalNotificationsModel> getApprovalNotificationModelCall(@Query("user_id") String user_id);

    @GET("notification_details")
    Call<V1_ApprovalMenuDetails> getApprovalNotificationDetailsModelCall(@Query("user_id") String user_id,
                                                                         @Query("menu_id") String menu_id);

    @GET("bundle_data_for_cutting_store_receive")
    Call<V1_CuttingStoreReceive> getCuttingStoreReceiveModelCall(@Query("barcode_no") String barcode_no);

    @GET("bundle_data_from_receive_rack")
    Call<V1_CuttingStoreIssue> getCuttingStoreIssueModelCall(@Query("barcode_no") String barcode_no);
    @GET("aop_store_name_list")
    Call<V1_AOPDepartmentStoreResponse> getAOPDepartmentStoreModelCall(@Query("user_id") String user_id,
                                                                       @Query("company_id") String company_id,
                                                                       @Query("item_category_id") String item_category_id);

    @Multipart
    @POST("approve_from_apps")
    Call<V1_ApprovalResponseModel> postApprovalModelCall(@Part("user_id") RequestBody user_id,
                                                         @Part("menu_id") RequestBody menu_id,
                                                         @Part("ref_id") RequestBody ref_id);

    @Multipart
    @POST("unapprove_from_apps")
    Call<V1_ApprovalResponseModel> postUnApprovalModelCall(@Part("user_id") RequestBody user_id,
                                                           @Part("menu_id") RequestBody menu_id,
                                                           @Part("ref_id") RequestBody ref_id);

    @Multipart
    @POST("deny_approve_from_apps")
    Call<V1_ApprovalResponseModel> postDenyApprovalModelCall(@Part("user_id") RequestBody user_id,
                                                             @Part("menu_id") RequestBody menu_id,
                                                             @Part("ref_id") RequestBody ref_id,
                                                             @Part("message") RequestBody message);

    @GET("purchase_req_dtls_by_mst_id")
    Call<V1_ApprovalItemDetailsModel> getApprovalItemDetailsCall(@Query("req_mst_id") String req_mst_id);

    @Multipart
    @POST("logout_from_apps")
    Call<V1_ApprovalResponseModel> postLogoutCall(@Part("user_id") RequestBody user_id,
                                                  @Part("device_id") RequestBody device_id);

    @Multipart
    @POST("order/woven_gmts/requires/fabric_hanger_archive_entry_controller.php")
    Call<String> hangerDocument(@Part("action") RequestBody action,
                                @Part("data") RequestBody data,
                                @Part("api_key") RequestBody api_ke);

    @GET("grn_wise_yarn_data")
    Call<V1_GRNWiseYarnModel> getGrnWiseYarnCall(@Query("grn_no") String grn_no);

    @GET("yarn_issue_return")
    Call<V1_YarnIssueReturnResponse> getYarnIssueReturnCall(@Query("yarn_issue_number") String yarn_issue_number);

    @GET("rfid_validity_details_for_issue_return")
    Call<V1_YarnIssueReturnRFIDValidityCheckResponse> getYarnIssueReturnRFIDValidityCall(@Query("rfid_no") String rfid_no);

    @GET("roll_wise_gray_fab_delivery_to_store")
    Call<V1_RollWiseGreyFabricDeliveryToStoreResponse> getRollWiseGrayFabricDeliveryToStoreCall(@Query("barcode_no") String barcode_no);

    @POST("rfid_yarn_store_location_update")
    Call<V1_GRNWiseYarnSaveResponse> saveGRNYarnReceiceSaveCall(@Body RequestBody requestBody);


    @GET("finish_roll_rcv_scan")
    Call<FinishFabricRollReceive> getFinishFabricReceiveCall(@Query("challan_no") String challan_no);

    @POST("finish_roll_rcv_save")
    Call<FinishFabricRollIssueResponses> saveFinishBarcodeReceiveCall(@Body RequestBody requestBody);

    @GET("finish_roll_issue_scan")
    Call<FinishFabricIssueSet> getFinishFabricIssueCall(@Query("barcode_no") String barcode_no);

    @POST("finish_roll_issue_save")
    Call<FinishFabricRollRequest> saveFinishBarcodeIssueCall(@Body RequestBody requestBody);


    //transfer out --------------------------------
    @GET("get_roll_info_for_transfer")
    Call<TransferOutResponse> transferOutBarcodeData(
            @Query("barcode_no") String barcode,
            @Query("room_rack_id") String room_rack_id,
            @Query("trans_in") String transfer_out_code);

    //transfer in --------------------------------
    @GET("get_roll_info_for_transfer")
    Call<TransferOutResponse> transferInBarcodeData(
            @Query("barcode_no") String barcode,
            @Query("room_rack_id") String room_rack_id,
            @Query("trans_in") String transfer_in_code
    );


    // finish fabric receive
    @GET("finish_feb_roll_receive_by_store")
    Call<FFRResponse> finishFabricReceiveBarcodeData(
            @Query("barcode_no") String barcode,
            @Query("room_rack_id") String room_rack_id
    );

    @GET("yarn_rfid_inside_store_transfer")
    Call<V1_RFIDTransferModel> rfidTransferStoreData(
            @Query("store_id") String store_id,
            @Query("floor_id") String floor_id,
            @Query("room_id") String room_id,
            @Query("rack_id") String rack_id,
            @Query("shelf_id") String shelf_id,
            @Query("bin_id") String bin_id
            );

    @GET("store_room_rack_dropdown_by_id")
    Call<V1_RFIDTransferDropdownModel> rfidTransferStoreDropdownData(
            @Query("company_id") String company_id,
            @Query("user_id") String user_id,
            @Query("store_id") String store_id,
            @Query("floor_id") String floor_id,
            @Query("room_id") String room_id,
            @Query("rack_id") String rack_id,
            @Query("shelf_id") String shelf_id,
            @Query("bin_id") String bin_id
    );

    @GET("store_name_list")
    Call<V1GreyFabricTransferOutStoreListResponse> store_name_list_for_grey_fabric_transfer_out(
            @Query("user_id") String user_id,
            @Query("item_category_id") String item_category_id,
            @Query("company_id") String company_id
    );

    @POST("yarn_rfid_inside_store_transfer_save")
    Call<V1_RFIDTransferSaveResponse> saveRFIDTransferStoreCall(@Body RequestBody requestBody);

    @POST("yarn_issue_return_save")
    Call<V1_RFIDIssueReturnSaveResponse> saveRFIDIssueReturnCall(@Body RequestBody requestBody);

    // finish fabric receive


    @POST("finish_feb_roll_receive_by_store_save")
    Call<Fff_save_response> finishFabricReceivePostBarcodes(@Body RequestBody requestBody, @HeaderMap Map<String, String> headers);

    @GET("gmts_finish_recv")
    Call<V1_GMTFinishReceiveResponse> getGmrFinishReceiveClassCall(@Query("barcode_no") String barcode);

    @POST("gmts_finish_recv_save")
    Call<V1_GMTFinishReceiveSaveResponse> saveGmtFinishReceiveCall(@Body RequestBody requestBody);

    @GET("fabric_finish_qc")
    Call<V1_FinishingQCModelResponse> getFinishingQCClassCall(@Query("batch") String batch);
    @GET("fabric_finish_qc_update")
    Call<V1_FabricFinishQCUpdateModel> getFinishingQCUpdateClassCall(@Query("roll_number") String roll_number,
                                                               @Query("barcode_no") String barcode_no,
                                                               @Query("batch_no") String batch_no);

    @GET("dying_finish_machine_data")
    Call<V1_FinishingMachineModelResponse> getFinishingMachineCall();

    @POST("fabric_finish_qc_save")
    Call<V1_FinishingQCSaveResponse> postFinishQCResponse(@Body RequestBody requestBody);

    @GET("fin_prod_batch_scan_data_list_v2")
    Call<V1_FinishingDataResponse> getFinishingClassCall(@Query("batch_no") String batch_no,
                                                         @Query("entry_form_no") String entry_form_no);
    @POST("save_update_fin_production_v2")
    Call<V1_FinishingSaveResponse> saveUpdateFinishingCall(@Body RequestBody requestBody);

    @GET("special_finish_sub_process_list")
    Call<V1_SpecialFinishSubProcessResponse> getSpecialFinishSubProcessList();

    @POST("dyeing_production_load_unload_save")
    Call<V1_DyeingProductionPDASaveResponse> saveUpdateDyeingProductionResponse(@Body RequestBody requestBody);

    @GET("dyeing_production_load_unload")
    Call<V1_DyeingProductionPDAResponse> getDyeingProductionResponseCall(@Query("batch_id") String batch_id);
}

