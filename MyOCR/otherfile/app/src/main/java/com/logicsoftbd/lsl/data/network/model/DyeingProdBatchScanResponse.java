package com.logicsoftbd.lsl.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DyeingProdBatchScanResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("resultset")
    @Expose
    private Resultset resultset;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Resultset getResultset() {
        return resultset;
    }

    public void setResultset(Resultset resultset) {
        this.resultset = resultset;
    }
    public class HiddenIndex {

        @SerializedName("LAST_RESULT")
        @Expose
        private String lastResult;
        @SerializedName("LAST_LOAD_UNLOAD_ID")
        @Expose
        private String lastLoadUnloadId;
        @SerializedName("hidden_service_company")
        @Expose
        private String hiddenServiceCompany;

        public String getLastResult() {
            return lastResult;
        }

        public void setLastResult(String lastResult) {
            this.lastResult = lastResult;
        }

        public String getLastLoadUnloadId() {
            return lastLoadUnloadId;
        }

        public void setLastLoadUnloadId(String lastLoadUnloadId) {
            this.lastLoadUnloadId = lastLoadUnloadId;
        }

        public String getHiddenServiceCompany() {
            return hiddenServiceCompany;
        }

        public void setHiddenServiceCompany(String hiddenServiceCompany) {
            this.hiddenServiceCompany = hiddenServiceCompany;
        }

    }

    public class InputAreaIndex {

        @SerializedName("BATCH_ID")
        @Expose
        private String batchId;
        @SerializedName("ENTRY_FORM")
        @Expose
        private String entryForm;
        @SerializedName("COMPANY_ID")
        @Expose
        private String companyId;
        @SerializedName("SERVICE_COMPANY")
        @Expose
        private String serviceCompany;
        @SerializedName("BATCH_NO")
        @Expose
        private String batchNo;
        @SerializedName("load_unload")
        @Expose
        private String loadUnload;
        @SerializedName("multi_dyeing")
        @Expose
        private String multiDyeing;
        @SerializedName("funtional_batch_no")
        @Expose
        private String funtionalBatchNo;
        @SerializedName("PROCESS_ID")
        @Expose
        private String processId;
        @SerializedName("SERVICE_SOURCE")
        @Expose
        private String serviceSource;
        @SerializedName("EXTENTION_NO")
        @Expose
        private String extentionNo;
        @SerializedName("WORKING_COMPANY_ID")
        @Expose
        private String workingCompanyId;
        @SerializedName("MACHINE_ID")
        @Expose
        private String machineId;
        @SerializedName("MACHINE_NAME")
        @Expose
        private String machineName;
        @SerializedName("FLOOR_ID")
        @Expose
        private String floorId;
        @SerializedName("FLOOR_NAME")
        @Expose
        private String floorName;
        @SerializedName("RESPONSIBILITY")
        @Expose
        private Responsibility responsibility;

        public String getMachineId() {
            return machineId;
        }

        public void setMachineId(String machineId) {
            this.machineId = machineId;
        }

        public String getMachineName() {
            return machineName;
        }

        public void setMachineName(String machineName) {
            this.machineName = machineName;
        }

        public String getFloorId() {
            return floorId;
        }

        public void setFloorId(String floorId) {
            this.floorId = floorId;
        }

        public String getFloorName() {
            return floorName;
        }

        public void setFloorName(String floorName) {
            this.floorName = floorName;
        }

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

        public String getLoadUnload() {
            return loadUnload;
        }

        public void setLoadUnload(String loadUnload) {
            this.loadUnload = loadUnload;
        }

        public String getMultiDyeing() {
            return multiDyeing;
        }

        public void setMultiDyeing(String multiDyeing) {
            this.multiDyeing = multiDyeing;
        }

        public String getFuntionalBatchNo() {
            return funtionalBatchNo;
        }

        public void setFuntionalBatchNo(String funtionalBatchNo) {
            this.funtionalBatchNo = funtionalBatchNo;
        }

        public String getProcessId() {
            return processId;
        }

        public void setProcessId(String processId) {
            this.processId = processId;
        }

        public String getServiceSource() {
            return serviceSource;
        }

        public void setServiceSource(String serviceSource) {
            this.serviceSource = serviceSource;
        }

        public String getExtentionNo() {
            return extentionNo;
        }

        public void setExtentionNo(String extentionNo) {
            this.extentionNo = extentionNo;
        }

        public String getWorkingCompanyId() {
            return workingCompanyId;
        }

        public void setWorkingCompanyId(String workingCompanyId) {
            this.workingCompanyId = workingCompanyId;
        }

        public Responsibility getResponsibility() {
            return responsibility;
        }

        public void setResponsibility(Responsibility responsibility) {
            this.responsibility = responsibility;
        }

    }
    public class ReferenceIndex {

        @SerializedName("BATCH_ID")
        @Expose
        private String batchId;
        @SerializedName("EXTENTION_NO")
        @Expose
        private String extentionNo;
        @SerializedName("JOB_NO")
        @Expose
        private String jobNo;
        @SerializedName("PO_NO")
        @Expose
        private String poNo;
        @SerializedName("FILE_NO")
        @Expose
        private String fileNo;
        @SerializedName("REF_NO")
        @Expose
        private String refNo;
        @SerializedName("BUYER")
        @Expose
        private String buyer;
        @SerializedName("loading_date")
        @Expose
        private String loadingDate;
        @SerializedName("loading_time")
        @Expose
        private String loadingTime;
        @SerializedName("COLOR_ID")
        @Expose
        private String colorId;
        @SerializedName("BATCH_TYPE")
        @Expose
        private String batchType;

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

        public String getFileNo() {
            return fileNo;
        }

        public void setFileNo(String fileNo) {
            this.fileNo = fileNo;
        }

        public String getRefNo() {
            return refNo;
        }

        public void setRefNo(String refNo) {
            this.refNo = refNo;
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

        public String getColorId() {
            return colorId;
        }

        public void setColorId(String colorId) {
            this.colorId = colorId;
        }

        public String getBatchType() {
            return batchType;
        }

        public void setBatchType(String batchType) {
            this.batchType = batchType;
        }

    }
    public class Responsibility {

        @SerializedName("is_disable")
        @Expose
        private String isDisable;

        public String getIsDisable() {
            return isDisable;
        }

        public void setIsDisable(String isDisable) {
            this.isDisable = isDisable;
        }

    }
    public class Resultset {

        @SerializedName("input_area_index")
        @Expose
        private InputAreaIndex inputAreaIndex;
        @SerializedName("hidden_index")
        @Expose
        private HiddenIndex hiddenIndex;
        @SerializedName("reference_index")
        @Expose
        private ReferenceIndex referenceIndex;
        @SerializedName("dtls_index")
        @Expose
        private List<DtlsIndex> dtlsIndex = null;

        public InputAreaIndex getInputAreaIndex() {
            return inputAreaIndex;
        }

        public void setInputAreaIndex(InputAreaIndex inputAreaIndex) {
            this.inputAreaIndex = inputAreaIndex;
        }

        public HiddenIndex getHiddenIndex() {
            return hiddenIndex;
        }

        public void setHiddenIndex(HiddenIndex hiddenIndex) {
            this.hiddenIndex = hiddenIndex;
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
    public class DtlsIndex {

        @SerializedName("CHECKED")
        @Expose
        private String checked;
        @SerializedName("PROD_ID")
        @Expose
        private String prodId;
        @SerializedName("CONS_COMPS")
        @Expose
        private String consComps;
        @SerializedName("GSM")
        @Expose
        private String gsm;
        @SerializedName("DIA_WIDTH")
        @Expose
        private String diaWidth;
        @SerializedName("FABRIC_TYPEE")
        @Expose
        private String fabricTypee;
        @SerializedName("FABRIC_TYPEE_ID")
        @Expose
        private String fabricTypeeId;
        @SerializedName("ROLL_ID")
        @Expose
        private String rollId;
        @SerializedName("BARCODE_NO")
        @Expose
        private String barcodeNo;
        @SerializedName("BATCH_QNTY")
        @Expose
        private String batchQnty;
        @SerializedName("BATCH_ROLLNO")
        @Expose
        private String batchRollno;
        @SerializedName("PROD_QTY")
        @Expose
        private String prodQty;
        @SerializedName("PROD_QTY_READONLY")
        @Expose
        private String prodQtyReadonly;

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

        public String getConsComps() {
            return consComps;
        }

        public void setConsComps(String consComps) {
            this.consComps = consComps;
        }

        public String getGsm() {
            return gsm;
        }

        public void setGsm(String gsm) {
            this.gsm = gsm;
        }

        public String getDiaWidth() {
            return diaWidth;
        }

        public void setDiaWidth(String diaWidth) {
            this.diaWidth = diaWidth;
        }

        public String getFabricTypee() {
            return fabricTypee;
        }

        public void setFabricTypee(String fabricTypee) {
            this.fabricTypee = fabricTypee;
        }

        public String getFabricTypeeId() {
            return fabricTypeeId;
        }

        public void setFabricTypeeId(String fabricTypeeId) {
            this.fabricTypeeId = fabricTypeeId;
        }

        public String getRollId() {
            return rollId;
        }

        public void setRollId(String rollId) {
            this.rollId = rollId;
        }

        public String getBarcodeNo() {
            return barcodeNo;
        }

        public void setBarcodeNo(String barcodeNo) {
            this.barcodeNo = barcodeNo;
        }

        public String getBatchQnty() {
            return batchQnty;
        }

        public void setBatchQnty(String batchQnty) {
            this.batchQnty = batchQnty;
        }

        public String getBatchRollno() {
            return batchRollno;
        }

        public void setBatchRollno(String batchRollno) {
            this.batchRollno = batchRollno;
        }

        public String getProdQty() {
            return prodQty;
        }

        public void setProdQty(String prodQty) {
            this.prodQty = prodQty;
        }

        public String getProdQtyReadonly() {
            return prodQtyReadonly;
        }

        public void setProdQtyReadonly(String prodQtyReadonly) {
            this.prodQtyReadonly = prodQtyReadonly;
        }

    }
}