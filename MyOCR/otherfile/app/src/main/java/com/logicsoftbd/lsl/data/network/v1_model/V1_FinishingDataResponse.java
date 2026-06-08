package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V1_FinishingDataResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("mode")
    @Expose
    private String mode;
    @SerializedName("resultset")
    @Expose
    private Resultset resultset;
    @SerializedName("msg")
    @Expose
    private String msg;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Resultset getResultset() {
        return resultset;
    }

    public void setResultset(Resultset resultset) {
        this.resultset = resultset;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public class Resultset {
        @SerializedName("start_date")
        @Expose
        private String startDate;
        @SerializedName("start_hour")
        @Expose
        private String startHour;
        @SerializedName("start_minute")
        @Expose
        private String startMinute;
        @SerializedName("end_date")
        @Expose
        private String endDate;
        @SerializedName("end_hour")
        @Expose
        private String endHour;
        @SerializedName("end_minute")
        @Expose
        private String endMinute;

        @SerializedName("input_area_index")
        @Expose
        private InputAreaIndex inputAreaIndex;
        @SerializedName("reference_index")
        @Expose
        private ReferenceIndex referenceIndex;
        @SerializedName("dtls_index")
        @Expose
        private List<DtlsIndex> dtlsIndex;

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getStartHour() {
            return startHour;
        }

        public void setStartHour(String startHour) {
            this.startHour = startHour;
        }

        public String getStartMinute() {
            return startMinute;
        }

        public void setStartMinute(String startMinute) {
            this.startMinute = startMinute;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getEndHour() {
            return endHour;
        }

        public void setEndHour(String endHour) {
            this.endHour = endHour;
        }

        public String getEndMinute() {
            return endMinute;
        }

        public void setEndMinute(String endMinute) {
            this.endMinute = endMinute;
        }


        public InputAreaIndex getInputAreaIndex() {
            return inputAreaIndex;
        }

        public void setInputAreaIndex(InputAreaIndex inputAreaIndex) {
            this.inputAreaIndex = inputAreaIndex;
        }

        public ReferenceIndex getReferenceIndex() {
            return referenceIndex;
        }

        public void setReferenceIndex(ReferenceIndex referenceIndex) {
            this.referenceIndex = referenceIndex;
        }

        public List<DtlsIndex> getDtlsIndex() {
            return dtlsIndex;
        }

        public void setDtlsIndex(List<DtlsIndex> dtlsIndex) {
            this.dtlsIndex = dtlsIndex;
        }

    }
    public class ReferenceIndex {

        @SerializedName("BATCH_ID")
        @Expose
        private String batchId;
        @SerializedName("EXTENTION_NO")
        @Expose
        private String extentionNo;
        @SerializedName("COLOR")
        @Expose
        private String color;
        @SerializedName("JOB_NO")
        @Expose
        private String jobNo;
        @SerializedName("PO_NO")
        @Expose
        private String poNo;
        @SerializedName("BUYER")
        @Expose
        private String buyer;
        @SerializedName("loading_date")
        @Expose
        private String loadingDate;
        @SerializedName("loading_time")
        @Expose
        private String loadingTime;
        @SerializedName("un_loading_date")
        @Expose
        private String unLoadingDate;
        @SerializedName("un_loading_time")
        @Expose
        private String unLoadingTime;

        public String getBatchId() {
            return batchId;
        }

        public void setBatchId(String batchId) {
            this.batchId = batchId;
        }

        public String getExtentionNo() {
            return extentionNo;
        }

        public void setExtentionNo(String extentionNo) {
            this.extentionNo = extentionNo;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getJobNo() {
            return jobNo;
        }

        public void setJobNo(String jobNo) {
            this.jobNo = jobNo;
        }

        public String getPoNo() {
            return poNo;
        }

        public void setPoNo(String poNo) {
            this.poNo = poNo;
        }

        public String getBuyer() {
            return buyer;
        }

        public void setBuyer(String buyer) {
            this.buyer = buyer;
        }

        public String getLoadingDate() {
            return loadingDate;
        }

        public void setLoadingDate(String loadingDate) {
            this.loadingDate = loadingDate;
        }

        public String getLoadingTime() {
            return loadingTime;
        }

        public void setLoadingTime(String loadingTime) {
            this.loadingTime = loadingTime;
        }

        public String getUnLoadingDate() {
            return unLoadingDate;
        }

        public void setUnLoadingDate(String unLoadingDate) {
            this.unLoadingDate = unLoadingDate;
        }

        public String getUnLoadingTime() {
            return unLoadingTime;
        }

        public void setUnLoadingTime(String unLoadingTime) {
            this.unLoadingTime = unLoadingTime;
        }

    }
    public class InputAreaIndex {

        @SerializedName("BATCH_ID")
        @Expose
        private String batchId;
        @SerializedName("ENTRY_FORM")
        @Expose
        private String entryForm;
        @SerializedName("TRIMS_WGT")
        @Expose
        private String trimsWgt;
        @SerializedName("COMPANY_ID")
        @Expose
        private String companyId;
        @SerializedName("SERVICE_COMPANY")
        @Expose
        private String serviceCompany;
        @SerializedName("BATCH_NO")
        @Expose
        private String batchNo;
        @SerializedName("entry_form_no")
        @Expose
        private String entryFormNo;
        @SerializedName("machine_name")
        @Expose
        private String machineName;
        @SerializedName("machine_id")
        @Expose
        private String machineId;
        @SerializedName("RE_SLITING_NO")
        @Expose
        private String reSlitingNo;

        public String getBatchId() {
            return batchId;
        }

        public void setBatchId(String batchId) {
            this.batchId = batchId;
        }

        public String getEntryForm() {
            return entryForm;
        }

        public void setEntryForm(String entryForm) {
            this.entryForm = entryForm;
        }

        public String getTrimsWgt() {
            return trimsWgt;
        }

        public void setTrimsWgt(String trimsWgt) {
            this.trimsWgt = trimsWgt;
        }

        public String getCompanyId() {
            return companyId;
        }

        public void setCompanyId(String companyId) {
            this.companyId = companyId;
        }

        public String getServiceCompany() {
            return serviceCompany;
        }

        public void setServiceCompany(String serviceCompany) {
            this.serviceCompany = serviceCompany;
        }

        public String getBatchNo() {
            return batchNo;
        }

        public void setBatchNo(String batchNo) {
            this.batchNo = batchNo;
        }

        public String getEntryFormNo() {
            return entryFormNo;
        }

        public void setEntryFormNo(String entryFormNo) {
            this.entryFormNo = entryFormNo;
        }

        public String getMachineName() {
            return machineName;
        }

        public void setMachineName(String machineName) {
            this.machineName = machineName;
        }

        public String getMachineId() {
            return machineId;
        }

        public void setMachineId(String machineId) {
            this.machineId = machineId;
        }

        public String getReSlitingNo() {
            return reSlitingNo;
        }

        public void setReSlitingNo(String reSlitingNo) {
            this.reSlitingNo = reSlitingNo;
        }

    }
    public class DtlsIndex {

        @SerializedName("MST_ID")
        @Expose
        private String mstId;
        @SerializedName("CHECKED")
        @Expose
        private String checked;
        @SerializedName("PROD_ID")
        @Expose
        private String prodId;
        @SerializedName("FIN_DIA")
        @Expose
        private String finDia;
        @SerializedName("ROLL_NO")
        @Expose
        private String rollNo;
        @SerializedName("ROLL_ID")
        @Expose
        private String rollId;
        @SerializedName("NO_OF_ROLL")
        @Expose
        private String noOfRoll;
        @SerializedName("BATCH_QNTY")
        @Expose
        private String batchQnty;
        @SerializedName("PROD_QTY")
        @Expose
        private String prodQty;
        @SerializedName("BARCODE_NO")
        @Expose
        private String barcodeNo;
        @SerializedName("DIA_TYPE")
        @Expose
        private String diaType;
        @SerializedName("DIA_WIDTH")
        @Expose
        private String diaWidth;
        @SerializedName("GSM")
        @Expose
        private String gsm;
        @SerializedName("CONS_COMP")
        @Expose
        private String consComp;

        public String getMstId() {
            return mstId;
        }

        public void setMstId(String mstId) {
            this.mstId = mstId;
        }

        public String getChecked() {
            return checked;
        }

        public void setChecked(String checked) {
            this.checked = checked;
        }

        public String getProdId() {
            return prodId;
        }

        public void setProdId(String prodId) {
            this.prodId = prodId;
        }

        public String getFinDia() {
            return finDia;
        }

        public void setFinDia(String finDia) {
            this.finDia = finDia;
        }

        public String getRollNo() {
            return rollNo;
        }

        public void setRollNo(String rollNo) {
            this.rollNo = rollNo;
        }

        public String getRollId() {
            return rollId;
        }

        public void setRollId(String rollId) {
            this.rollId = rollId;
        }

        public String getNoOfRoll() {
            return noOfRoll;
        }

        public void setNoOfRoll(String noOfRoll) {
            this.noOfRoll = noOfRoll;
        }

        public String getBatchQnty() {
            return batchQnty;
        }

        public void setBatchQnty(String batchQnty) {
            this.batchQnty = batchQnty;
        }

        public String getProdQty() {
            return prodQty;
        }

        public void setProdQty(String prodQty) {
            this.prodQty = prodQty;
        }

        public String getBarcodeNo() {
            return barcodeNo;
        }

        public void setBarcodeNo(String barcodeNo) {
            this.barcodeNo = barcodeNo;
        }

        public String getDiaType() {
            return diaType;
        }

        public void setDiaType(String diaType) {
            this.diaType = diaType;
        }

        public String getDiaWidth() {
            return diaWidth;
        }

        public void setDiaWidth(String diaWidth) {
            this.diaWidth = diaWidth;
        }

        public String getGsm() {
            return gsm;
        }

        public void setGsm(String gsm) {
            this.gsm = gsm;
        }

        public String getConsComp() {
            return consComp;
        }

        public void setConsComp(String consComp) {
            this.consComp = consComp;
        }

    }
}
