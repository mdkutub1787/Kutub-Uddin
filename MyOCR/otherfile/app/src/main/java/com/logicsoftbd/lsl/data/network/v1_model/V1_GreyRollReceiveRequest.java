package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V1_GreyRollReceiveRequest {
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

    public class Resultset {

        @SerializedName("MasterPart")
        @Expose
        private MasterPart masterPart;
        @SerializedName("DtlsPart")
        @Expose
        private List<DtlsPart> dtlsPart = null;

        public MasterPart getMasterPart() {
            return masterPart;
        }

        public void setMasterPart(MasterPart masterPart) {
            this.masterPart = masterPart;
        }

        public List<DtlsPart> getDtlsPart() {
            return dtlsPart;
        }

        public void setDtlsPart(List<DtlsPart> dtlsPart) {
            this.dtlsPart = dtlsPart;
        }

    }

    public class MasterPart {

        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("msg")
        @Expose
        private String msg;
        @SerializedName("COMPANY_ID")
        @Expose
        private String companyId;
        @SerializedName("SYS_NUMBER")
        @Expose
        private String sysNumber;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getCompanyId() {
            return companyId;
        }

        public void setCompanyId(String companyId) {
            this.companyId = companyId;
        }

        public String getSysNumber() {
            return sysNumber;
        }

        public void setSysNumber(String sysNumber) {
            this.sysNumber = sysNumber;
        }

    }

    public class DtlsPart {

        @SerializedName("BARCODE_NO")
        @Expose
        private String barcodeNo;
        @SerializedName("ROLL_NO")
        @Expose
        private String rollNo;
        @SerializedName("QNTY")
        @Expose
        private String qnty;
        @SerializedName("JOB_NO")
        @Expose
        private String jobNo;
        @SerializedName("BOOKING_NO")
        @Expose
        private String bookingNo;
        @SerializedName("PROGRAM_NO")
        @Expose
        private String programNo;
        @SerializedName("CONSTRUCTION")
        @Expose
        private String construction;
        @SerializedName("COMPOSITION")
        @Expose
        private String composition;
        @SerializedName("GSM")
        @Expose
        private String gsm;
        @SerializedName("WIDTH")
        @Expose
        private String width;
        @SerializedName("COLOR_NAME")
        @Expose
        private String colorName;
        @SerializedName("YARN_LOT")
        @Expose
        private String yarnLot;
        @SerializedName("STITCH_LENGTH")
        @Expose
        private String stitchLength;
        @SerializedName("BRAND_ID")
        @Expose
        private String brandId;
        @SerializedName("SYS_NUMBER")
        @Expose
        private String sysNumber;
        @SerializedName("MACHINE_NAME")
        @Expose
        private String machineName;
        @SerializedName("INTERNAL_REF")
        @Expose
        private String internalRef;

        public String getBarcodeNo() {
            return barcodeNo;
        }

        public void setBarcodeNo(String barcodeNo) {
            this.barcodeNo = barcodeNo;
        }

        public String getRollNo() {
            return rollNo;
        }

        public void setRollNo(String rollNo) {
            this.rollNo = rollNo;
        }

        public String getQnty() {
            return qnty;
        }

        public void setQnty(String qnty) {
            this.qnty = qnty;
        }

        public String getJobNo() {
            return jobNo;
        }

        public void setJobNo(String jobNo) {
            this.jobNo = jobNo;
        }

        public String getBookingNo() {
            return bookingNo;
        }

        public void setBookingNo(String bookingNo) {
            this.bookingNo = bookingNo;
        }

        public String getProgramNo() {
            return programNo;
        }

        public void setProgramNo(String programNo) {
            this.programNo = programNo;
        }

        public String getConstruction() {
            return construction;
        }

        public void setConstruction(String construction) {
            this.construction = construction;
        }

        public String getComposition() {
            return composition;
        }

        public void setComposition(String composition) {
            this.composition = composition;
        }

        public String getGsm() {
            return gsm;
        }

        public void setGsm(String gsm) {
            this.gsm = gsm;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }


        public String getColorName() {
            return colorName;
        }

        public void setColorName(String colorName) {
            this.colorName = colorName;
        }

        public String getYarnLot() {
            return yarnLot;
        }

        public void setYarnLot(String yarnLot) {
            this.yarnLot = yarnLot;
        }

        public String getStitchLength() {
            return stitchLength;
        }

        public void setStitchLength(String stitchLength) {
            this.stitchLength = stitchLength;
        }

        public String getBrandId() {
            return brandId;
        }

        public void setBrandId(String brandId) {
            this.brandId = brandId;
        }

        public String getSysNumber() {
            return sysNumber;
        }

        public void setSysNumber(String sysNumber) {
            this.sysNumber = sysNumber;
        }

        public String getMachineName() {
            return machineName;
        }

        public void setMachineName(String machineName) {
            this.machineName = machineName;
        }

        public String getInternalRef() {
            return internalRef;
        }

        public void setInternalRef(String internalRef) {
            this.internalRef = internalRef;
        }
    }
}
