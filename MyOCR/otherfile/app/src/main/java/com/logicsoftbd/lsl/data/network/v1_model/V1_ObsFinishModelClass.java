package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V1_ObsFinishModelClass {


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
    public class Observation {

        @SerializedName("ID")
        @Expose
        private Integer iD;
        @SerializedName("DEFECT_NAME")
        @Expose
        private String dEFECTNAME;
        @SerializedName("FOUND_IN_INCH")
        @Expose
        private Integer fOUNDININCH;
        @SerializedName("DEPARTMENT")
        @Expose
        private Integer dEPARTMENT;

        public Integer getID() {
            return iD;
        }

        public void setID(Integer iD) {
            this.iD = iD;
        }

        public String getDEFECTNAME() {
            return dEFECTNAME;
        }

        public void setDEFECTNAME(String dEFECTNAME) {
            this.dEFECTNAME = dEFECTNAME;
        }

        public Integer getFOUNDININCH() {
            return fOUNDININCH;
        }

        public void setFOUNDININCH(Integer fOUNDININCH) {
            this.fOUNDININCH = fOUNDININCH;
        }

        public Integer getDEPARTMENT() {
            return dEPARTMENT;
        }

        public void setDEPARTMENT(Integer dEPARTMENT) {
            this.dEPARTMENT = dEPARTMENT;
        }

    }
    public class Index {

        @SerializedName("mode")
        @Expose
        private String mode;
        @SerializedName("receive_no")
        @Expose
        private String receiveNo;
        @SerializedName("mst_id")
        @Expose
        private String mstId;
        @SerializedName("dtls_id")
        @Expose
        private String dtlsId;
        @SerializedName("qc_mst_id")
        @Expose
        private String qcMstId;
        @SerializedName("buyer_id")
        @Expose
        private String buyerId;
        @SerializedName("buyer_name")
        @Expose
        private String buyerName;
        @SerializedName("machine_no")
        @Expose
        private String machineNo;
        @SerializedName("machine_id")
        @Expose
        private String machineId;
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
        @SerializedName("no_of_roll")
        @Expose
        private String noOfRoll;
        @SerializedName("qc_pass_total_roll")
        @Expose
        private String qcPassTotalRoll;
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
        @SerializedName("barcode_no")
        @Expose
        private String barcodeNo;
        @SerializedName("roll_id")
        @Expose
        private String rollId;
        @SerializedName("roll_no")
        @Expose
        private String rollNo;
        @SerializedName("batch_id")
        @Expose
        private String batchId;
        @SerializedName("batch_no")
        @Expose
        private String batchNo;
        @SerializedName("color")
        @Expose
        private String color;
        @SerializedName("width_dia_id")
        @Expose
        private String widthDiaId;
        @SerializedName("width_dia_val")
        @Expose
        private String widthDiaVal;
        @SerializedName("prod_qnty")
        @Expose
        private String prodQnty;
        @SerializedName("qc_pass_qty")
        @Expose
        private String qcPassQty;
        @SerializedName("body_part_id")
        @Expose
        private String bodyPartId;
        @SerializedName("body_part")
        @Expose
        private String bodyPart;
        @SerializedName("fab_des_id")
        @Expose
        private String fabDesId;
        @SerializedName("fab_des")
        @Expose
        private String fabDes;
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
        @SerializedName("source")
        @Expose
        private String source;
        @SerializedName("company_id")
        @Expose
        private String companyId;
        @SerializedName("location")
        @Expose
        private String location;
        @SerializedName("serving_company")
        @Expose
        private String servingCompany;
        @SerializedName("service_location")
        @Expose
        private String serviceLocation;
        @SerializedName("po_breakdown_id")
        @Expose
        private String poBreakdownId;
        @SerializedName("buyer_grade")
        @Expose
        private String buyer_grade;


        @SerializedName("po_number")
        @Expose
        private String poNumber;
        @SerializedName("job_number")
        @Expose
        private String jobNumber;
        @SerializedName("style_ref_no")
        @Expose
        private String styleRefNo;
        @SerializedName("booking_without_order")
        @Expose
        private String bookingWithoutOrder;
        @SerializedName("booking_no")
        @Expose
        private String bookingNo;
        @SerializedName("array_ref_data")
        @Expose
        private ArrayRefData arrayRefData;


        public String getBuyer_grade() {
            return buyer_grade;
        }

        public void setBuyer_grade(String buyer_grade) {
            this.buyer_grade = buyer_grade;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public String getReceiveNo() {
            return receiveNo;
        }

        public void setReceiveNo(String receiveNo) {
            this.receiveNo = receiveNo;
        }

        public String getMstId() {
            return mstId;
        }

        public void setMstId(String mstId) {
            this.mstId = mstId;
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

        public String getBuyerId() {
            return buyerId;
        }

        public void setBuyerId(String buyerId) {
            this.buyerId = buyerId;
        }

        public String getBuyerName() {
            return buyerName;
        }

        public void setBuyerName(String buyerName) {
            this.buyerName = buyerName;
        }

        public String getMachineNo() {
            return machineNo;
        }

        public void setMachineNo(String machineNo) {
            this.machineNo = machineNo;
        }

        public String getMachineId() {
            return machineId;
        }

        public void setMachineId(String machineId) {
            this.machineId = machineId;
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

        public String getNoOfRoll() {
            return noOfRoll;
        }

        public void setNoOfRoll(String noOfRoll) {
            this.noOfRoll = noOfRoll;
        }

        public String getQcPassTotalRoll() {
            return qcPassTotalRoll;
        }

        public void setQcPassTotalRoll(String qcPassTotalRoll) {
            this.qcPassTotalRoll = qcPassTotalRoll;
        }

        public String getRollWeight() {
            return rollWeight;
        }

        public void setRollWeight(String rollWeight) {
            this.rollWeight = rollWeight;
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

        public String getBatchId() {
            return batchId;
        }

        public void setBatchId(String batchId) {
            this.batchId = batchId;
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

        public String getProdQnty() {
            return prodQnty;
        }

        public void setProdQnty(String prodQnty) {
            this.prodQnty = prodQnty;
        }

        public String getQcPassQty() {
            return qcPassQty;
        }

        public void setQcPassQty(String qcPassQty) {
            this.qcPassQty = qcPassQty;
        }

        public String getBodyPartId() {
            return bodyPartId;
        }

        public void setBodyPartId(String bodyPartId) {
            this.bodyPartId = bodyPartId;
        }

        public String getBodyPart() {
            return bodyPart;
        }

        public void setBodyPart(String bodyPart) {
            this.bodyPart = bodyPart;
        }

        public String getFabDesId() {
            return fabDesId;
        }

        public void setFabDesId(String fabDesId) {
            this.fabDesId = fabDesId;
        }

        public String getFabDes() {
            return fabDes;
        }

        public void setFabDes(String fabDes) {
            this.fabDes = fabDes;
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

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getCompanyId() {
            return companyId;
        }

        public void setCompanyId(String companyId) {
            this.companyId = companyId;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
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
        private Integer serial;
        @SerializedName("grade")
        @Expose
        private String grade;

        public Integer getSerial() {
            return serial;
        }

        public void setSerial(Integer serial) {
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

        @SerializedName("ID")
        @Expose
        private Integer iD;
        @SerializedName("DEFECT_NAME")
        @Expose
        private String dEFECTNAME;
        @SerializedName("DEFECT_COUNT")
        @Expose
        private Integer dEFECTCOUNT;
        @SerializedName("FOUND_IN_INCH")
        @Expose
        private Integer fOUNDININCH;
        @SerializedName("PENALTY_POINT")
        @Expose
        private Integer pENALTYPOINT;

        public Integer getID() {
            return iD;
        }

        public void setID(Integer iD) {
            this.iD = iD;
        }

        public String getDEFECTNAME() {
            return dEFECTNAME;
        }

        public void setDEFECTNAME(String dEFECTNAME) {
            this.dEFECTNAME = dEFECTNAME;
        }

        public Integer getDEFECTCOUNT() {
            return dEFECTCOUNT;
        }

        public void setDEFECTCOUNT(Integer dEFECTCOUNT) {
            this.dEFECTCOUNT = dEFECTCOUNT;
        }

        public Integer getFOUNDININCH() {
            return fOUNDININCH;
        }

        public void setFOUNDININCH(Integer fOUNDININCH) {
            this.fOUNDININCH = fOUNDININCH;
        }

        public Integer getPENALTYPOINT() {
            return pENALTYPOINT;
        }

        public void setPENALTYPOINT(Integer pENALTYPOINT) {
            this.pENALTYPOINT = pENALTYPOINT;
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
    public class ArrayRefData {

        @SerializedName("defect")
        @Expose
        private List<Defect> defect = null;
        @SerializedName("grade")
        @Expose
        private List<Grade> grade = null;
        @SerializedName("observation")
        @Expose
        private List<Observation> observation = null;

        public List<Defect> getDefect() {
            return defect;
        }

        public void setDefect(List<Defect> defect) {
            this.defect = defect;
        }

        public List<Grade> getGrade() {
            return grade;
        }

        public void setGrade(List<Grade> grade) {
            this.grade = grade;
        }

        public List<Observation> getObservation() {
            return observation;
        }

        public void setObservation(List<Observation> observation) {
            this.observation = observation;
        }

    }
}
