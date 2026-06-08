package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V1_BarcodeDetailsFromBatchFinishQCResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("shade_msg")
    @Expose
    private String shadeMsg;
    @SerializedName("data")
    @Expose
    private Data data;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getShadeMsg() {
        return shadeMsg;
    }

    public void setShadeMsg(String shadeMsg) {
        this.shadeMsg = shadeMsg;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Yarn {

        @SerializedName("ID")
        @Expose
        private String id;
        @SerializedName("DEFECT_NAME")
        @Expose
        private String defectName;
        @SerializedName("DEFECT_COUNT")
        @Expose
        private String defectCount;
        @SerializedName("FOUND_IN_INCH")
        @Expose
        private String foundInInch;
        @SerializedName("PENALTY_POINT")
        @Expose
        private String penaltyPoint;
        private boolean status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDefectName() {
            return defectName;
        }

        public void setDefectName(String defectName) {
            this.defectName = defectName;
        }

        public String getDefectCount() {
            return defectCount;
        }

        public void setDefectCount(String defectCount) {
            this.defectCount = defectCount;
        }

        public String getFoundInInch() {
            return foundInInch;
        }

        public void setFoundInInch(String foundInInch) {
            this.foundInInch = foundInInch;
        }

        public String getPenaltyPoint() {
            return penaltyPoint;
        }

        public void setPenaltyPoint(String penaltyPoint) {
            this.penaltyPoint = penaltyPoint;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }
    }
    public class Knitting {

        @SerializedName("ID")
        @Expose
        private String id;
        @SerializedName("DEFECT_NAME")
        @Expose
        private String defectName;
        @SerializedName("DEFECT_COUNT")
        @Expose
        private String defectCount;
        @SerializedName("FOUND_IN_INCH")
        @Expose
        private String foundInInch;
        @SerializedName("PENALTY_POINT")
        @Expose
        private String penaltyPoint;
        private boolean status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDefectName() {
            return defectName;
        }

        public void setDefectName(String defectName) {
            this.defectName = defectName;
        }

        public String getDefectCount() {
            return defectCount;
        }

        public void setDefectCount(String defectCount) {
            this.defectCount = defectCount;
        }

        public String getFoundInInch() {
            return foundInInch;
        }

        public void setFoundInInch(String foundInInch) {
            this.foundInInch = foundInInch;
        }

        public String getPenaltyPoint() {
            return penaltyPoint;
        }

        public void setPenaltyPoint(String penaltyPoint) {
            this.penaltyPoint = penaltyPoint;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }
    }
    public class Index {

        @SerializedName("mode")
        @Expose
        private String mode;
        @SerializedName("total_penalty_point")
        @Expose
        private String totalPenaltyPoint;
        @SerializedName("total_point")
        @Expose
        private String totalPoint;
        @SerializedName("fabric_grade")
        @Expose
        private String fabricGrade;
        @SerializedName("comments")
        @Expose
        private String comments;
        @SerializedName("roll_status")
        @Expose
        private String rollStatus;
        @SerializedName("qc_date")
        @Expose
        private String qcDate;
        @SerializedName("mst_id")
        @Expose
        private String mstId;
        @SerializedName("roll_weight")
        @Expose
        private String rollWeight;
        @SerializedName("roll_length")
        @Expose
        private String rollLength;
        @SerializedName("roll_width")
        @Expose
        private String rollWidth;
        @SerializedName("prod_id")
        @Expose
        private String prodId;
        @SerializedName("trans_id")
        @Expose
        private String transId;
        @SerializedName("dtls_id")
        @Expose
        private String dtlsId;
        @SerializedName("qc_mst_id")
        @Expose
        private String qcMstId;
        @SerializedName("barcode_no")
        @Expose
        private String barcodeNo;
        @SerializedName("roll_id")
        @Expose
        private String rollId;
        @SerializedName("roll_no")
        @Expose
        private String rollNo;
        @SerializedName("batch_no")
        @Expose
        private String batchNo;
        @SerializedName("color")
        @Expose
        private String color;
        @SerializedName("batch_id")
        @Expose
        private String batchId;
        @SerializedName("width_dia_id")
        @Expose
        private String widthDiaId;
        @SerializedName("width_dia_val")
        @Expose
        private String widthDiaVal;
        @SerializedName("qc_pass_qty")
        @Expose
        private String qcPassQty;
        @SerializedName("reject_qty")
        @Expose
        private String rejectQnty;
        @SerializedName("production_qnty_subprocess")
        @Expose
        private String productionQntySubprocess;
        @SerializedName("prod_qnty")
        @Expose
        private String prodQnty;
        @SerializedName("body_part")
        @Expose
        private String bodyPart;
        @SerializedName("body_part_id")
        @Expose
        private String bodyPartId;
        @SerializedName("deter_d")
        @Expose
        private String deterD;
        @SerializedName("gsm")
        @Expose
        private String gsm;
        @SerializedName("width")
        @Expose
        private String width;
        @SerializedName("is_sales")
        @Expose
        private String isSales;
        @SerializedName("construction")
        @Expose
        private String construction;
        @SerializedName("company_id")
        @Expose
        private String companyId;
        @SerializedName("source")
        @Expose
        private String source;
        @SerializedName("serving_company")
        @Expose
        private String servingCompany;
        @SerializedName("service_location")
        @Expose
        private String serviceLocation;
        @SerializedName("location")
        @Expose
        private String location;
        @SerializedName("po_breakdown_id")
        @Expose
        private String poBreakdownId;
        @SerializedName("po_number")
        @Expose
        private String poNumber;
        @SerializedName("job_number")
        @Expose
        private String jobNumber;
        @SerializedName("style_ref_no")
        @Expose
        private String styleRefNo;
        @SerializedName("qnty")
        @Expose
        private String qnty;
        @SerializedName("booking_without_order")
        @Expose
        private String bookingWithoutOrder;
        @SerializedName("booking_no")
        @Expose
        private String bookingNo;
        @SerializedName("rectify_no")
        @Expose
        private String rectifyNo;
        @SerializedName("buyer_name")
        @Expose
        private String buyerName;
        @SerializedName("ref_no")
        @Expose
        private String refNo;
        @SerializedName("file_no")
        @Expose
        private String fileNo;
        @SerializedName("gray_dia")
        @Expose
        private String grayDia;
        @SerializedName("array_ref_data")
        @Expose
        private ArrayRefData arrayRefData;

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public String getTotalPenaltyPoint() {
            return totalPenaltyPoint;
        }

        public void setTotalPenaltyPoint(String totalPenaltyPoint) {
            this.totalPenaltyPoint = totalPenaltyPoint;
        }

        public String getTotalPoint() {
            return totalPoint;
        }

        public void setTotalPoint(String totalPoint) {
            this.totalPoint = totalPoint;
        }

        public String getFabricGrade() {
            return fabricGrade;
        }

        public void setFabricGrade(String fabricGrade) {
            this.fabricGrade = fabricGrade;
        }

        public String getComments() {
            return comments;
        }

        public void setComments(String comments) {
            this.comments = comments;
        }

        public String getRollStatus() {
            return rollStatus;
        }

        public void setRollStatus(String rollStatus) {
            this.rollStatus = rollStatus;
        }

        public String getQcDate() {
            return qcDate;
        }

        public void setQcDate(String qcDate) {
            this.qcDate = qcDate;
        }

        public String getMstId() {
            return mstId;
        }

        public void setMstId(String mstId) {
            this.mstId = mstId;
        }

        public String getRollWeight() {
            return rollWeight;
        }

        public void setRollWeight(String rollWeight) {
            this.rollWeight = rollWeight;
        }

        public String getRejectQnty() {
            return rejectQnty;
        }

        public void setRejectQnty(String rejectQnty) {
            this.rejectQnty = rejectQnty;
        }

        public String getRollLength() {
            return rollLength;
        }

        public void setRollLength(String rollLength) {
            this.rollLength = rollLength;
        }

        public String getRollWidth() {
            return rollWidth;
        }

        public void setRollWidth(String rollWidth) {
            this.rollWidth = rollWidth;
        }

        public String getProdId() {
            return prodId;
        }

        public void setProdId(String prodId) {
            this.prodId = prodId;
        }

        public String getTransId() {
            return transId;
        }

        public void setTransId(String transId) {
            this.transId = transId;
        }

        public String getDtlsId() {
            return dtlsId;
        }

        public void setDtlsId(String dtlsId) {
            this.dtlsId = dtlsId;
        }

        public String getQcMstId() {
            return qcMstId;
        }

        public void setQcMstId(String qcMstId) {
            this.qcMstId = qcMstId;
        }

        public String getBarcodeNo() {
            return barcodeNo;
        }

        public void setBarcodeNo(String barcodeNo) {
            this.barcodeNo = barcodeNo;
        }

        public String getRollId() {
            return rollId;
        }

        public void setRollId(String rollId) {
            this.rollId = rollId;
        }

        public String getRollNo() {
            return rollNo;
        }

        public void setRollNo(String rollNo) {
            this.rollNo = rollNo;
        }

        public String getBatchNo() {
            return batchNo;
        }

        public void setBatchNo(String batchNo) {
            this.batchNo = batchNo;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getBatchId() {
            return batchId;
        }

        public void setBatchId(String batchId) {
            this.batchId = batchId;
        }

        public String getWidthDiaId() {
            return widthDiaId;
        }

        public void setWidthDiaId(String widthDiaId) {
            this.widthDiaId = widthDiaId;
        }

        public String getWidthDiaVal() {
            return widthDiaVal;
        }

        public void setWidthDiaVal(String widthDiaVal) {
            this.widthDiaVal = widthDiaVal;
        }

        public String getQcPassQty() {
            return qcPassQty;
        }

        public void setQcPassQty(String qcPassQty) {
            this.qcPassQty = qcPassQty;
        }

        public String getProductionQntySubprocess() {
            return productionQntySubprocess;
        }

        public void setProductionQntySubprocess(String productionQntySubprocess) {
            this.productionQntySubprocess = productionQntySubprocess;
        }

        public String getProdQnty() {
            return prodQnty;
        }

        public void setProdQnty(String prodQnty) {
            this.prodQnty = prodQnty;
        }

        public String getBodyPart() {
            return bodyPart;
        }

        public void setBodyPart(String bodyPart) {
            this.bodyPart = bodyPart;
        }

        public String getBodyPartId() {
            return bodyPartId;
        }

        public void setBodyPartId(String bodyPartId) {
            this.bodyPartId = bodyPartId;
        }

        public String getDeterD() {
            return deterD;
        }

        public void setDeterD(String deterD) {
            this.deterD = deterD;
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

        public String getIsSales() {
            return isSales;
        }

        public void setIsSales(String isSales) {
            this.isSales = isSales;
        }

        public String getConstruction() {
            return construction;
        }

        public void setConstruction(String construction) {
            this.construction = construction;
        }

        public String getCompanyId() {
            return companyId;
        }

        public void setCompanyId(String companyId) {
            this.companyId = companyId;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getServingCompany() {
            return servingCompany;
        }

        public void setServingCompany(String servingCompany) {
            this.servingCompany = servingCompany;
        }

        public String getServiceLocation() {
            return serviceLocation;
        }

        public void setServiceLocation(String serviceLocation) {
            this.serviceLocation = serviceLocation;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getPoBreakdownId() {
            return poBreakdownId;
        }

        public void setPoBreakdownId(String poBreakdownId) {
            this.poBreakdownId = poBreakdownId;
        }

        public String getPoNumber() {
            return poNumber;
        }

        public void setPoNumber(String poNumber) {
            this.poNumber = poNumber;
        }

        public String getJobNumber() {
            return jobNumber;
        }

        public void setJobNumber(String jobNumber) {
            this.jobNumber = jobNumber;
        }

        public String getStyleRefNo() {
            return styleRefNo;
        }

        public void setStyleRefNo(String styleRefNo) {
            this.styleRefNo = styleRefNo;
        }

        public String getQnty() {
            return qnty;
        }

        public void setQnty(String qnty) {
            this.qnty = qnty;
        }

        public String getBookingWithoutOrder() {
            return bookingWithoutOrder;
        }

        public void setBookingWithoutOrder(String bookingWithoutOrder) {
            this.bookingWithoutOrder = bookingWithoutOrder;
        }

        public String getBookingNo() {
            return bookingNo;
        }

        public void setBookingNo(String bookingNo) {
            this.bookingNo = bookingNo;
        }

        public String getRectifyNo() {
            return rectifyNo;
        }

        public String getBuyerName() {
            return buyerName;
        }

        public void setBuyerName(String buyerName) {
            this.buyerName = buyerName;
        }

        public String getRefNo() {
            return refNo;
        }

        public void setRefNo(String refNo) {
            this.refNo = refNo;
        }

        public String getFileNo() {
            return fileNo;
        }

        public void setFileNo(String fileNo) {
            this.fileNo = fileNo;
        }

        public String getGrayDia() {
            return grayDia;
        }

        public void setGrayDia(String grayDia) {
            this.grayDia = grayDia;
        }

        public void setRectifyNo(String rectifyNo) {
            this.rectifyNo = rectifyNo;
        }

        public ArrayRefData getArrayRefData() {
            return arrayRefData;
        }

        public void setArrayRefData(ArrayRefData arrayRefData) {
            this.arrayRefData = arrayRefData;
        }



    }
    public class Grade {

        @SerializedName("serial")
        @Expose
        private String serial;
        @SerializedName("grade")
        @Expose
        private String grade;

        public String getSerial() {
            return serial;
        }

        public void setSerial(String serial) {
            this.serial = serial;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

    }
    public class Defect {

        @SerializedName("Yarn")
        @Expose
        private List<Yarn> yarn;
        @SerializedName("Knitting")
        @Expose
        private List<Knitting> knitting;
        @SerializedName("Common")
        @Expose
        private List<Common> common;
        @SerializedName("AOP/Print")
        @Expose
        private List<AopPrint> aopprint;
        @SerializedName("Dyeing")
        @Expose
        private List<Dyeing> dyeing;
        @SerializedName("Finishing")
        @Expose
        private List<Finishing> finishings;

        public List<Yarn> getYarn() {
            return yarn;
        }

        public void setYarn(List<Yarn> yarn) {
            this.yarn = yarn;
        }

        public List<Knitting> getKnitting() {
            return knitting;
        }

        public void setKnitting(List<Knitting> knitting) {
            this.knitting = knitting;
        }

        public List<Common> getCommon() {
            return common;
        }

        public void setCommon(List<Common> common) {
            this.common = common;
        }

        public List<AopPrint> getAopprint() {
            return aopprint;
        }

        public void setAopprint(List<AopPrint> aopprint) {
            this.aopprint = aopprint;
        }

        public List<Dyeing> getDyeing() {
            return dyeing;
        }

        public void setDyeing(List<Dyeing> dyeing) {
            this.dyeing = dyeing;
        }

        public List<Finishing> getFinishings() {
            return finishings;
        }

        public void setFinishings(List<Finishing> finishings) {
            this.finishings = finishings;
        }
    }
    public class Data {

        @SerializedName("index")
        @Expose
        private Index index;

        public Index getIndex() {
            return index;
        }

        public void setIndex(Index index) {
            this.index = index;
        }

    }
    public class Common {

        @SerializedName("ID")
        @Expose
        private String id;
        @SerializedName("DEFECT_NAME")
        @Expose
        private String defectName;
        @SerializedName("DEFECT_COUNT")
        @Expose
        private String defectCount;
        @SerializedName("FOUND_IN_INCH")
        @Expose
        private String foundInInch;
        @SerializedName("PENALTY_POINT")
        @Expose
        private String penaltyPoint;
        private boolean status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDefectName() {
            return defectName;
        }

        public void setDefectName(String defectName) {
            this.defectName = defectName;
        }

        public String getDefectCount() {
            return defectCount;
        }

        public void setDefectCount(String defectCount) {
            this.defectCount = defectCount;
        }

        public String getFoundInInch() {
            return foundInInch;
        }

        public void setFoundInInch(String foundInInch) {
            this.foundInInch = foundInInch;
        }

        public String getPenaltyPoint() {
            return penaltyPoint;
        }

        public void setPenaltyPoint(String penaltyPoint) {
            this.penaltyPoint = penaltyPoint;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }
    }

    public class AopPrint {

        @SerializedName("ID")
        @Expose
        private String id;
        @SerializedName("DEFECT_NAME")
        @Expose
        private String defectName;
        @SerializedName("DEFECT_COUNT")
        @Expose
        private String defectCount;
        @SerializedName("FOUND_IN_INCH")
        @Expose
        private String foundInInch;
        @SerializedName("PENALTY_POINT")
        @Expose
        private String penaltyPoint;
        private boolean status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDefectName() {
            return defectName;
        }

        public void setDefectName(String defectName) {
            this.defectName = defectName;
        }

        public String getDefectCount() {
            return defectCount;
        }

        public void setDefectCount(String defectCount) {
            this.defectCount = defectCount;
        }

        public String getFoundInInch() {
            return foundInInch;
        }

        public void setFoundInInch(String foundInInch) {
            this.foundInInch = foundInInch;
        }

        public String getPenaltyPoint() {
            return penaltyPoint;
        }

        public void setPenaltyPoint(String penaltyPoint) {
            this.penaltyPoint = penaltyPoint;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }
    }

    public class Dyeing {

        @SerializedName("ID")
        @Expose
        private String id;
        @SerializedName("DEFECT_NAME")
        @Expose
        private String defectName;
        @SerializedName("DEFECT_COUNT")
        @Expose
        private String defectCount;
        @SerializedName("FOUND_IN_INCH")
        @Expose
        private String foundInInch;
        @SerializedName("PENALTY_POINT")
        @Expose
        private String penaltyPoint;
        private boolean status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDefectName() {
            return defectName;
        }

        public void setDefectName(String defectName) {
            this.defectName = defectName;
        }

        public String getDefectCount() {
            return defectCount;
        }

        public void setDefectCount(String defectCount) {
            this.defectCount = defectCount;
        }

        public String getFoundInInch() {
            return foundInInch;
        }

        public void setFoundInInch(String foundInInch) {
            this.foundInInch = foundInInch;
        }

        public String getPenaltyPoint() {
            return penaltyPoint;
        }

        public void setPenaltyPoint(String penaltyPoint) {
            this.penaltyPoint = penaltyPoint;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }
    }

    public class Finishing {

        @SerializedName("ID")
        @Expose
        private String id;
        @SerializedName("DEFECT_NAME")
        @Expose
        private String defectName;
        @SerializedName("DEFECT_COUNT")
        @Expose
        private String defectCount;
        @SerializedName("FOUND_IN_INCH")
        @Expose
        private String foundInInch;
        @SerializedName("PENALTY_POINT")
        @Expose
        private String penaltyPoint;
        private boolean status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDefectName() {
            return defectName;
        }

        public void setDefectName(String defectName) {
            this.defectName = defectName;
        }

        public String getDefectCount() {
            return defectCount;
        }

        public void setDefectCount(String defectCount) {
            this.defectCount = defectCount;
        }

        public String getFoundInInch() {
            return foundInInch;
        }

        public void setFoundInInch(String foundInInch) {
            this.foundInInch = foundInInch;
        }

        public String getPenaltyPoint() {
            return penaltyPoint;
        }

        public void setPenaltyPoint(String penaltyPoint) {
            this.penaltyPoint = penaltyPoint;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }
    }
    public class ArrayRefData {

        @SerializedName("defect")
        @Expose
        private Defect defect;
        @SerializedName("grade")
        @Expose
        private List<Grade> grade;

        public Defect getDefect() {
            return defect;
        }

        public void setDefect(Defect defect) {
            this.defect = defect;
        }

        public List<Grade> getGrade() {
            return grade;
        }

        public void setGrade(List<Grade> grade) {
            this.grade = grade;
        }

    }
}
