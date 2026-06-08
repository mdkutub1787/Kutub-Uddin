package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class V1_BundleTrackingReportModelClass {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private Data data;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
    public class CutAndLayInfo {

        @SerializedName("cut_no")
        @Expose
        private String cutNo;
        @SerializedName("date_and_time")
        @Expose
        private String dateAndTime;
        @SerializedName("color")
        @Expose
        private String color;
        @SerializedName("size")
        @Expose
        private String size;
        @SerializedName("rmg_number")
        @Expose
        private String rmgNumber;
        @SerializedName("qty")
        @Expose
        private String qty;

        public String getCutNo() {
            return cutNo;
        }

        public void setCutNo(String cutNo) {
            this.cutNo = cutNo;
        }

        public String getDateAndTime() {
            return dateAndTime;
        }

        public void setDateAndTime(String dateAndTime) {
            this.dateAndTime = dateAndTime;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getRmgNumber() {
            return rmgNumber;
        }

        public void setRmgNumber(String rmgNumber) {
            this.rmgNumber = rmgNumber;
        }

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }

    }
    public class CuttingQcInfo {

        @SerializedName("cutting_qc_id")
        @Expose
        private String cuttingQcId;
        @SerializedName("date_and_time")
        @Expose
        private String dateAndTime;
        @SerializedName("bundle_qty")
        @Expose
        private String bundleQty;
        @SerializedName("qc_pass_qty")
        @Expose
        private String qcPassQty;
        @SerializedName("reject_qty")
        @Expose
        private String rejectQty;
        @SerializedName("replace_qty")
        @Expose
        private String replaceQty;

        public String getCuttingQcId() {
            return cuttingQcId;
        }

        public void setCuttingQcId(String cuttingQcId) {
            this.cuttingQcId = cuttingQcId;
        }

        public String getDateAndTime() {
            return dateAndTime;
        }

        public void setDateAndTime(String dateAndTime) {
            this.dateAndTime = dateAndTime;
        }

        public String getBundleQty() {
            return bundleQty;
        }

        public void setBundleQty(String bundleQty) {
            this.bundleQty = bundleQty;
        }

        public String getQcPassQty() {
            return qcPassQty;
        }

        public void setQcPassQty(String qcPassQty) {
            this.qcPassQty = qcPassQty;
        }

        public String getRejectQty() {
            return rejectQty;
        }

        public void setRejectQty(String rejectQty) {
            this.rejectQty = rejectQty;
        }

        public String getReplaceQty() {
            return replaceQty;
        }

        public void setReplaceQty(String replaceQty) {
            this.replaceQty = replaceQty;
        }

    }
    public class Data {

        @SerializedName("order_info")
        @Expose
        private OrderInfo orderInfo;
        @SerializedName("cut_and_lay_info")
        @Expose
        private CutAndLayInfo cutAndLayInfo;
        @SerializedName("cutting_qc_info")
        @Expose
        private CuttingQcInfo cuttingQcInfo;
        @SerializedName("print_issue_info")
        @Expose
        private PrintIssueInfo printIssueInfo;
        @SerializedName("print_receive_info")
        @Expose
        private PrintReceiveInfo printReceiveInfo;
        @SerializedName("embroidery_issue_info")
        @Expose
        private EmbroideryIssueInfo embroideryIssueInfo;
        @SerializedName("embroidery_receive_info")
        @Expose
        private EmbroideryReceiveInfo embroideryReceiveInfo;
        @SerializedName("sewing_input_info")
        @Expose
        private SewingInputInfo sewingInputInfo;
        @SerializedName("sewing_output_info")
        @Expose
        private SewingOutputInfo sewingOutputInfo;
        @SerializedName("line_input_info")
        @Expose
        private LineInputInfo lineInputInfo;
        @SerializedName("line_output_info")
        @Expose
        private LineOutputInfo lineOutputInfo;

        public OrderInfo getOrderInfo() {
            return orderInfo;
        }

        public void setOrderInfo(OrderInfo orderInfo) {
            this.orderInfo = orderInfo;
        }

        public CutAndLayInfo getCutAndLayInfo() {
            return cutAndLayInfo;
        }

        public void setCutAndLayInfo(CutAndLayInfo cutAndLayInfo) {
            this.cutAndLayInfo = cutAndLayInfo;
        }

        public CuttingQcInfo getCuttingQcInfo() {
            return cuttingQcInfo;
        }

        public void setCuttingQcInfo(CuttingQcInfo cuttingQcInfo) {
            this.cuttingQcInfo = cuttingQcInfo;
        }

        public PrintIssueInfo getPrintIssueInfo() {
            return printIssueInfo;
        }

        public void setPrintIssueInfo(PrintIssueInfo printIssueInfo) {
            this.printIssueInfo = printIssueInfo;
        }

        public PrintReceiveInfo getPrintReceiveInfo() {
            return printReceiveInfo;
        }

        public void setPrintReceiveInfo(PrintReceiveInfo printReceiveInfo) {
            this.printReceiveInfo = printReceiveInfo;
        }

        public EmbroideryIssueInfo getEmbroideryIssueInfo() {
            return embroideryIssueInfo;
        }

        public void setEmbroideryIssueInfo(EmbroideryIssueInfo embroideryIssueInfo) {
            this.embroideryIssueInfo = embroideryIssueInfo;
        }

        public EmbroideryReceiveInfo getEmbroideryReceiveInfo() {
            return embroideryReceiveInfo;
        }

        public void setEmbroideryReceiveInfo(EmbroideryReceiveInfo embroideryReceiveInfo) {
            this.embroideryReceiveInfo = embroideryReceiveInfo;
        }

        public SewingInputInfo getSewingInputInfo() {
            return sewingInputInfo;
        }

        public void setSewingInputInfo(SewingInputInfo sewingInputInfo) {
            this.sewingInputInfo = sewingInputInfo;
        }

        public SewingOutputInfo getSewingOutputInfo() {
            return sewingOutputInfo;
        }

        public void setSewingOutputInfo(SewingOutputInfo sewingOutputInfo) {
            this.sewingOutputInfo = sewingOutputInfo;
        }

        public LineInputInfo getLineInputInfo() {
            return lineInputInfo;
        }

        public void setLineInputInfo(LineInputInfo lineInputInfo) {
            this.lineInputInfo = lineInputInfo;
        }

        public LineOutputInfo getLineOutputInfo() {
            return lineOutputInfo;
        }

        public void setLineOutputInfo(LineOutputInfo lineOutputInfo) {
            this.lineOutputInfo = lineOutputInfo;
        }

    }
    public class EmbroideryIssueInfo {

        @SerializedName("issue_id")
        @Expose
        private String issueId;
        @SerializedName("date_and_time")
        @Expose
        private String dateAndTime;
        @SerializedName("issue_qty")
        @Expose
        private String issueQty;

        public String getIssueId() {
            return issueId;
        }

        public void setIssueId(String issueId) {
            this.issueId = issueId;
        }

        public String getDateAndTime() {
            return dateAndTime;
        }

        public void setDateAndTime(String dateAndTime) {
            this.dateAndTime = dateAndTime;
        }

        public String getIssueQty() {
            return issueQty;
        }

        public void setIssueQty(String issueQty) {
            this.issueQty = issueQty;
        }

    }
    public class EmbroideryReceiveInfo {

        @SerializedName("issue_id")
        @Expose
        private String issueId;
        @SerializedName("date_and_time")
        @Expose
        private String dateAndTime;
        @SerializedName("issue_qty")
        @Expose
        private String issueQty;
        @SerializedName("reject_qty")
        @Expose
        private String rejectQty;

        public String getIssueId() {
            return issueId;
        }

        public void setIssueId(String issueId) {
            this.issueId = issueId;
        }

        public String getDateAndTime() {
            return dateAndTime;
        }

        public void setDateAndTime(String dateAndTime) {
            this.dateAndTime = dateAndTime;
        }

        public String getIssueQty() {
            return issueQty;
        }

        public void setIssueQty(String issueQty) {
            this.issueQty = issueQty;
        }

        public String getRejectQty() {
            return rejectQty;
        }

        public void setRejectQty(String rejectQty) {
            this.rejectQty = rejectQty;
        }

    }
    public class LineInputInfo {

        @SerializedName("line_input_id")
        @Expose
        private String lineInputId;
        @SerializedName("date_and_time")
        @Expose
        private String dateAndTime;
        @SerializedName("line_no")
        @Expose
        private String lineNo;

        public String getLineInputId() {
            return lineInputId;
        }

        public void setLineInputId(String lineInputId) {
            this.lineInputId = lineInputId;
        }

        public String getDateAndTime() {
            return dateAndTime;
        }

        public void setDateAndTime(String dateAndTime) {
            this.dateAndTime = dateAndTime;
        }

        public String getLineNo() {
            return lineNo;
        }

        public void setLineNo(String lineNo) {
            this.lineNo = lineNo;
        }

    }
    public class LineOutputInfo {

        @SerializedName("line_output_id")
        @Expose
        private String lineOutputId;
        @SerializedName("date_and_time")
        @Expose
        private String dateAndTime;
        @SerializedName("Qty")
        @Expose
        private String qty;

        public String getLineOutputId() {
            return lineOutputId;
        }

        public void setLineOutputId(String lineOutputId) {
            this.lineOutputId = lineOutputId;
        }

        public String getDateAndTime() {
            return dateAndTime;
        }

        public void setDateAndTime(String dateAndTime) {
            this.dateAndTime = dateAndTime;
        }

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }

    }

    public class OrderInfo {

        @SerializedName("job_no")
        @Expose
        private String jobNo;
        @SerializedName("order_no")
        @Expose
        private String orderNo;
        @SerializedName("style_no")
        @Expose
        private String styleNo;
        @SerializedName("color_name")
        @Expose
        private String colorName;
        @SerializedName("order_qty")
        @Expose
        private String orderQty;

        public String getJobNo() {
            return jobNo;
        }

        public void setJobNo(String jobNo) {
            this.jobNo = jobNo;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getStyleNo() {
            return styleNo;
        }

        public void setStyleNo(String styleNo) {
            this.styleNo = styleNo;
        }

        public String getColorName() {
            return colorName;
        }

        public void setColorName(String colorName) {
            this.colorName = colorName;
        }

        public String getOrderQty() {
            return orderQty;
        }

        public void setOrderQty(String orderQty) {
            this.orderQty = orderQty;
        }

    }
    public class PrintIssueInfo {

        @SerializedName("issue_id")
        @Expose
        private String issueId;
        @SerializedName("date_and_time")
        @Expose
        private String dateAndTime;
        @SerializedName("issue_qty")
        @Expose
        private String issueQty;

        public String getIssueId() {
            return issueId;
        }

        public void setIssueId(String issueId) {
            this.issueId = issueId;
        }

        public String getDateAndTime() {
            return dateAndTime;
        }

        public void setDateAndTime(String dateAndTime) {
            this.dateAndTime = dateAndTime;
        }

        public String getIssueQty() {
            return issueQty;
        }

        public void setIssueQty(String issueQty) {
            this.issueQty = issueQty;
        }

    }
    public class PrintReceiveInfo {

        @SerializedName("issue_id")
        @Expose
        private String issueId;
        @SerializedName("date_and_time")
        @Expose
        private String dateAndTime;
        @SerializedName("issue_qty")
        @Expose
        private String issueQty;
        @SerializedName("reject_qty")
        @Expose
        private String rejectQty;

        public String getIssueId() {
            return issueId;
        }

        public void setIssueId(String issueId) {
            this.issueId = issueId;
        }

        public String getDateAndTime() {
            return dateAndTime;
        }

        public void setDateAndTime(String dateAndTime) {
            this.dateAndTime = dateAndTime;
        }

        public String getIssueQty() {
            return issueQty;
        }

        public void setIssueQty(String issueQty) {
            this.issueQty = issueQty;
        }

        public String getRejectQty() {
            return rejectQty;
        }

        public void setRejectQty(String rejectQty) {
            this.rejectQty = rejectQty;
        }

    }
    public class SewingInputInfo {

        @SerializedName("input_id")
        @Expose
        private String inputId;
        @SerializedName("date_and_time")
        @Expose
        private String dateAndTime;
        @SerializedName("input_qty")
        @Expose
        private String inputQty;
        @SerializedName("line_no")
        @Expose
        private String lineNo;

        public String getInputId() {
            return inputId;
        }

        public void setInputId(String inputId) {
            this.inputId = inputId;
        }

        public String getDateAndTime() {
            return dateAndTime;
        }

        public void setDateAndTime(String dateAndTime) {
            this.dateAndTime = dateAndTime;
        }

        public String getInputQty() {
            return inputQty;
        }

        public void setInputQty(String inputQty) {
            this.inputQty = inputQty;
        }

        public String getLineNo() {
            return lineNo;
        }

        public void setLineNo(String lineNo) {
            this.lineNo = lineNo;
        }

    }
    public class SewingOutputInfo {

        @SerializedName("output_id")
        @Expose
        private String outputId;
        @SerializedName("date_and_time")
        @Expose
        private String dateAndTime;
        @SerializedName("output_qty")
        @Expose
        private String outputQty;
        @SerializedName("Alter_spot_reject_qty")
        @Expose
        private String alterSpotRejectQty;

        public String getOutputId() {
            return outputId;
        }

        public void setOutputId(String outputId) {
            this.outputId = outputId;
        }

        public String getDateAndTime() {
            return dateAndTime;
        }

        public void setDateAndTime(String dateAndTime) {
            this.dateAndTime = dateAndTime;
        }

        public String getOutputQty() {
            return outputQty;
        }

        public void setOutputQty(String outputQty) {
            this.outputQty = outputQty;
        }

        public String getAlterSpotRejectQty() {
            return alterSpotRejectQty;
        }

        public void setAlterSpotRejectQty(String alterSpotRejectQty) {
            this.alterSpotRejectQty = alterSpotRejectQty;
        }

    }

}