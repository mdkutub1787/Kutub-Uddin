package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V1_ObsKnittingModelClass {
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

        @SerializedName("MODE")
        @Expose
        private String mODE;
        @SerializedName("MST_ID")
        @Expose
        private String mSTID;
        @SerializedName("COMPANY_ID")
        @Expose
        private String cOMPANYID;
        @SerializedName("BUYER_ID")
        @Expose
        private String bUYERID;
        @SerializedName("DTLS_ID")
        @Expose
        private String dTLSID;
        @SerializedName("ROLL_MAINTAINED")
        @Expose
        private String rOLLMAINTAINED;
        @SerializedName("BARCODE_NO")
        @Expose
        private Long bARCODENO;
        @SerializedName("ROLL_ID")
        @Expose
        private String rOLLID;
        @SerializedName("ROLL_NO")
        @Expose
        private String rOLLNO;
        @SerializedName("GSM")
        @Expose
        private String gSM;
        @SerializedName("DIA")
        @Expose
        private String dIA;
        @SerializedName("MC_DIA")
        @Expose
        private String mCDIA;
        @SerializedName("COLOR")
        @Expose
        private String cOLOR;
        @SerializedName("CONSTRUCTION")
        @Expose
        private String cONSTRUCTION;
        @SerializedName("YARN_COUNT")
        @Expose
        private String yARNCOUNT;
        @SerializedName("YARN_LOT")
        @Expose
        private String yARNLOT;
        @SerializedName("SPINNING_MILL")
        @Expose
        private String sPINNINGMILL;
        @SerializedName("QC_NAME")
        @Expose
        private String qCNAME;
        @SerializedName("ROLL_STATUS")
        @Expose
        private String rOLLSTATUS;
        @SerializedName("UPDATE_ID")
        @Expose
        private String uPDATEID;
        @SerializedName("ROLL_KG")
        @Expose
        private String rOLLKG;
        @SerializedName("ROLL_INCH")
        @Expose
        private String rOLLINCH;
        @SerializedName("ROLL_YDS")
        @Expose
        private String rOLLYDS;
        @SerializedName("REJECT_QNTY")
        @Expose
        private String rEJECTQNTY;
        @SerializedName("TOTAL_PENALTY_POINT")
        @Expose
        private String tOTALPENALTYPOINT;
        @SerializedName("TOTAL_POINT")
        @Expose
        private String tOTALPOINT;
        @SerializedName("FABRIC_GRADE")
        @Expose
        private String fABRICGRADE;
        @SerializedName("COMMENTS")
        @Expose
        private String cOMMENTS;
        @SerializedName("QC_DATE")
        @Expose
        private String qCDATE;
        @SerializedName("array_ref_data")
        @Expose
        private ArrayRefData arrayRefData;

        public String getMODE() {
            return mODE;
        }

        public void setMODE(String mODE) {
            this.mODE = mODE;
        }

        public String getMSTID() {
            return mSTID;
        }

        public void setMSTID(String mSTID) {
            this.mSTID = mSTID;
        }

        public String getCOMPANYID() {
            return cOMPANYID;
        }

        public void setCOMPANYID(String cOMPANYID) {
            this.cOMPANYID = cOMPANYID;
        }

        public String getBUYERID() {
            return bUYERID;
        }

        public void setBUYERID(String bUYERID) {
            this.bUYERID = bUYERID;
        }

        public String getDTLSID() {
            return dTLSID;
        }

        public void setDTLSID(String dTLSID) {
            this.dTLSID = dTLSID;
        }

        public String getROLLMAINTAINED() {
            return rOLLMAINTAINED;
        }

        public void setROLLMAINTAINED(String rOLLMAINTAINED) {
            this.rOLLMAINTAINED = rOLLMAINTAINED;
        }

        public Long getBARCODENO() {
            return bARCODENO;
        }

        public void setBARCODENO(Long bARCODENO) {
            this.bARCODENO = bARCODENO;
        }

        public String getROLLID() {
            return rOLLID;
        }

        public void setROLLID(String rOLLID) {
            this.rOLLID = rOLLID;
        }

        public String getROLLNO() {
            return rOLLNO;
        }

        public void setROLLNO(String rOLLNO) {
            this.rOLLNO = rOLLNO;
        }

        public String getGSM() {
            return gSM;
        }

        public void setGSM(String gSM) {
            this.gSM = gSM;
        }

        public String getDIA() {
            return dIA;
        }

        public void setDIA(String dIA) {
            this.dIA = dIA;
        }

        public String getMCDIA() {
            return mCDIA;
        }

        public void setMCDIA(String mCDIA) {
            this.mCDIA = mCDIA;
        }

        public String getCOLOR() {
            return cOLOR;
        }

        public void setCOLOR(String cOLOR) {
            this.cOLOR = cOLOR;
        }

        public String getCONSTRUCTION() {
            return cONSTRUCTION;
        }

        public void setCONSTRUCTION(String cONSTRUCTION) {
            this.cONSTRUCTION = cONSTRUCTION;
        }

        public String getYARNCOUNT() {
            return yARNCOUNT;
        }

        public void setYARNCOUNT(String yARNCOUNT) {
            this.yARNCOUNT = yARNCOUNT;
        }

        public String getYARNLOT() {
            return yARNLOT;
        }

        public void setYARNLOT(String yARNLOT) {
            this.yARNLOT = yARNLOT;
        }

        public String getSPINNINGMILL() {
            return sPINNINGMILL;
        }

        public void setSPINNINGMILL(String sPINNINGMILL) {
            this.sPINNINGMILL = sPINNINGMILL;
        }

        public String getQCNAME() {
            return qCNAME;
        }

        public void setQCNAME(String qCNAME) {
            this.qCNAME = qCNAME;
        }

        public String getROLLSTATUS() {
            return rOLLSTATUS;
        }

        public void setROLLSTATUS(String rOLLSTATUS) {
            this.rOLLSTATUS = rOLLSTATUS;
        }

        public String getUPDATEID() {
            return uPDATEID;
        }

        public void setUPDATEID(String uPDATEID) {
            this.uPDATEID = uPDATEID;
        }

        public String getROLLKG() {
            return rOLLKG;
        }

        public void setROLLKG(String rOLLKG) {
            this.rOLLKG = rOLLKG;
        }

        public String getROLLINCH() {
            return rOLLINCH;
        }

        public void setROLLINCH(String rOLLINCH) {
            this.rOLLINCH = rOLLINCH;
        }

        public String getROLLYDS() {
            return rOLLYDS;
        }

        public void setROLLYDS(String rOLLYDS) {
            this.rOLLYDS = rOLLYDS;
        }

        public String getREJECTQNTY() {
            return rEJECTQNTY;
        }

        public void setREJECTQNTY(String rEJECTQNTY) {
            this.rEJECTQNTY = rEJECTQNTY;
        }

        public String getTOTALPENALTYPOINT() {
            return tOTALPENALTYPOINT;
        }

        public void setTOTALPENALTYPOINT(String tOTALPENALTYPOINT) {
            this.tOTALPENALTYPOINT = tOTALPENALTYPOINT;
        }

        public String getTOTALPOINT() {
            return tOTALPOINT;
        }

        public void setTOTALPOINT(String tOTALPOINT) {
            this.tOTALPOINT = tOTALPOINT;
        }

        public String getFABRICGRADE() {
            return fABRICGRADE;
        }

        public void setFABRICGRADE(String fABRICGRADE) {
            this.fABRICGRADE = fABRICGRADE;
        }

        public String getCOMMENTS() {
            return cOMMENTS;
        }

        public void setCOMMENTS(String cOMMENTS) {
            this.cOMMENTS = cOMMENTS;
        }

        public String getQCDATE() {
            return qCDATE;
        }

        public void setQCDATE(String qCDATE) {
            this.qCDATE = qCDATE;
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
