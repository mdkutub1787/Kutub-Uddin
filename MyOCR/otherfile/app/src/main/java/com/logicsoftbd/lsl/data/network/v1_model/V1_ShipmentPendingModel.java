package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class V1_ShipmentPendingModel {

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

    public class PREMONTH {

        @SerializedName("MONTH")
        @Expose
        private String mONTH;
        @SerializedName("PO_QTY")
        @Expose
        private String pOQTY;
        @SerializedName("PO_VALUE")
        @Expose
        private String pOVALUE;
        @SerializedName("CUT_QTY")
        @Expose
        private String cUTQTY;
        @SerializedName("CUT_BAL_ACCESS")
        @Expose
        private String cUTBALACCESS;
        @SerializedName("SEWING_QTY")
        @Expose
        private String sEWINGQTY;
        @SerializedName("SEWING_BALANCE")
        @Expose
        private String sEWINGBALANCE;
        @SerializedName("FINIS_QTY")
        @Expose
        private String fINISQTY;
        @SerializedName("FINISHING_BALANCE")
        @Expose
        private String fINISHINGBALANCE;
        @SerializedName("SHIP_OUT")
        @Expose
        private String sHIPOUT;
        @SerializedName("EXPORT_FOB_VALUE")
        @Expose
        private String eXPORTFOBVALUE;
        @SerializedName("SHIP_BAL_TO_PO_QTY")
        @Expose
        private String sHIPBALTOPOQTY;
        @SerializedName("SHIP_BAL_TO_PO_FOB_VALUE")
        @Expose
        private String sHIPBALTOPOFOBVALUE;
        @SerializedName("SEW_TO_SHIP_BALQTY")
        @Expose
        private String sEWTOSHIPBALQTY;
        @SerializedName("SEW_TO_SHIP_BAL_FOB_VALUE")
        @Expose
        private String sEWTOSHIPBALFOBVALUE;

        public String getMONTH() {
            return mONTH;
        }

        public void setMONTH(String mONTH) {
            this.mONTH = mONTH;
        }

        public String getPOQTY() {
            return pOQTY;
        }

        public void setPOQTY(String pOQTY) {
            this.pOQTY = pOQTY;
        }

        public String getPOVALUE() {
            return pOVALUE;
        }

        public void setPOVALUE(String pOVALUE) {
            this.pOVALUE = pOVALUE;
        }

        public String getCUTQTY() {
            return cUTQTY;
        }

        public void setCUTQTY(String cUTQTY) {
            this.cUTQTY = cUTQTY;
        }

        public String getCUTBALACCESS() {
            return cUTBALACCESS;
        }

        public void setCUTBALACCESS(String cUTBALACCESS) {
            this.cUTBALACCESS = cUTBALACCESS;
        }

        public String getSEWINGQTY() {
            return sEWINGQTY;
        }

        public void setSEWINGQTY(String sEWINGQTY) {
            this.sEWINGQTY = sEWINGQTY;
        }

        public String getSEWINGBALANCE() {
            return sEWINGBALANCE;
        }

        public void setSEWINGBALANCE(String sEWINGBALANCE) {
            this.sEWINGBALANCE = sEWINGBALANCE;
        }

        public String getFINISQTY() {
            return fINISQTY;
        }

        public void setFINISQTY(String fINISQTY) {
            this.fINISQTY = fINISQTY;
        }

        public String getFINISHINGBALANCE() {
            return fINISHINGBALANCE;
        }

        public void setFINISHINGBALANCE(String fINISHINGBALANCE) {
            this.fINISHINGBALANCE = fINISHINGBALANCE;
        }

        public String getSHIPOUT() {
            return sHIPOUT;
        }

        public void setSHIPOUT(String sHIPOUT) {
            this.sHIPOUT = sHIPOUT;
        }

        public String getEXPORTFOBVALUE() {
            return eXPORTFOBVALUE;
        }

        public void setEXPORTFOBVALUE(String eXPORTFOBVALUE) {
            this.eXPORTFOBVALUE = eXPORTFOBVALUE;
        }

        public String getSHIPBALTOPOQTY() {
            return sHIPBALTOPOQTY;
        }

        public void setSHIPBALTOPOQTY(String sHIPBALTOPOQTY) {
            this.sHIPBALTOPOQTY = sHIPBALTOPOQTY;
        }

        public String getSHIPBALTOPOFOBVALUE() {
            return sHIPBALTOPOFOBVALUE;
        }

        public void setSHIPBALTOPOFOBVALUE(String sHIPBALTOPOFOBVALUE) {
            this.sHIPBALTOPOFOBVALUE = sHIPBALTOPOFOBVALUE;
        }

        public String getSEWTOSHIPBALQTY() {
            return sEWTOSHIPBALQTY;
        }

        public void setSEWTOSHIPBALQTY(String sEWTOSHIPBALQTY) {
            this.sEWTOSHIPBALQTY = sEWTOSHIPBALQTY;
        }

        public String getSEWTOSHIPBALFOBVALUE() {
            return sEWTOSHIPBALFOBVALUE;
        }

        public void setSEWTOSHIPBALFOBVALUE(String sEWTOSHIPBALFOBVALUE) {
            this.sEWTOSHIPBALFOBVALUE = sEWTOSHIPBALFOBVALUE;
        }

    }

    public class Data {

        @SerializedName("PRE_MONTH")
        @Expose
        private PREMONTH pREMONTH;
        @SerializedName("CRR_MONTH")
        @Expose
        private CRRMONTH cRRMONTH;

        public PREMONTH getPREMONTH() {
            return pREMONTH;
        }

        public void setPREMONTH(PREMONTH pREMONTH) {
            this.pREMONTH = pREMONTH;
        }

        public CRRMONTH getCRRMONTH() {
            return cRRMONTH;
        }

        public void setCRRMONTH(CRRMONTH cRRMONTH) {
            this.cRRMONTH = cRRMONTH;
        }

    }

    public class CRRMONTH {

        @SerializedName("MONTH")
        @Expose
        private String mONTH;
        @SerializedName("PO_QTY")
        @Expose
        private String pOQTY;
        @SerializedName("PO_VALUE")
        @Expose
        private String pOVALUE;
        @SerializedName("CUT_QTY")
        @Expose
        private String cUTQTY;
        @SerializedName("CUT_BAL_ACCESS")
        @Expose
        private String cUTBALACCESS;
        @SerializedName("SEWING_QTY")
        @Expose
        private String sEWINGQTY;
        @SerializedName("SEWING_BALANCE")
        @Expose
        private String sEWINGBALANCE;
        @SerializedName("FINIS_QTY")
        @Expose
        private String fINISQTY;
        @SerializedName("FINISHING_BALANCE")
        @Expose
        private String fINISHINGBALANCE;
        @SerializedName("SHIP_OUT")
        @Expose
        private String sHIPOUT;
        @SerializedName("EXPORT_FOB_VALUE")
        @Expose
        private String eXPORTFOBVALUE;
        @SerializedName("SHIP_BAL_TO_PO_QTY")
        @Expose
        private String sHIPBALTOPOQTY;
        @SerializedName("SHIP_BAL_TO_PO_FOB_VALUE")
        @Expose
        private String sHIPBALTOPOFOBVALUE;
        @SerializedName("SEW_TO_SHIP_BALQTY")
        @Expose
        private String sEWTOSHIPBALQTY;
        @SerializedName("SEW_TO_SHIP_BAL_FOB_VALUE")
        @Expose
        private String sEWTOSHIPBALFOBVALUE;

        public String getMONTH() {
            return mONTH;
        }

        public void setMONTH(String mONTH) {
            this.mONTH = mONTH;
        }

        public String getPOQTY() {
            return pOQTY;
        }

        public void setPOQTY(String pOQTY) {
            this.pOQTY = pOQTY;
        }

        public String getPOVALUE() {
            return pOVALUE;
        }

        public void setPOVALUE(String pOVALUE) {
            this.pOVALUE = pOVALUE;
        }

        public String getCUTQTY() {
            return cUTQTY;
        }

        public void setCUTQTY(String cUTQTY) {
            this.cUTQTY = cUTQTY;
        }

        public String getCUTBALACCESS() {
            return cUTBALACCESS;
        }

        public void setCUTBALACCESS(String cUTBALACCESS) {
            this.cUTBALACCESS = cUTBALACCESS;
        }

        public String getSEWINGQTY() {
            return sEWINGQTY;
        }

        public void setSEWINGQTY(String sEWINGQTY) {
            this.sEWINGQTY = sEWINGQTY;
        }

        public String getSEWINGBALANCE() {
            return sEWINGBALANCE;
        }

        public void setSEWINGBALANCE(String sEWINGBALANCE) {
            this.sEWINGBALANCE = sEWINGBALANCE;
        }

        public String getFINISQTY() {
            return fINISQTY;
        }

        public void setFINISQTY(String fINISQTY) {
            this.fINISQTY = fINISQTY;
        }

        public String getFINISHINGBALANCE() {
            return fINISHINGBALANCE;
        }

        public void setFINISHINGBALANCE(String fINISHINGBALANCE) {
            this.fINISHINGBALANCE = fINISHINGBALANCE;
        }

        public String getSHIPOUT() {
            return sHIPOUT;
        }

        public void setSHIPOUT(String sHIPOUT) {
            this.sHIPOUT = sHIPOUT;
        }

        public String getEXPORTFOBVALUE() {
            return eXPORTFOBVALUE;
        }

        public void setEXPORTFOBVALUE(String eXPORTFOBVALUE) {
            this.eXPORTFOBVALUE = eXPORTFOBVALUE;
        }

        public String getSHIPBALTOPOQTY() {
            return sHIPBALTOPOQTY;
        }

        public void setSHIPBALTOPOQTY(String sHIPBALTOPOQTY) {
            this.sHIPBALTOPOQTY = sHIPBALTOPOQTY;
        }

        public String getSHIPBALTOPOFOBVALUE() {
            return sHIPBALTOPOFOBVALUE;
        }

        public void setSHIPBALTOPOFOBVALUE(String sHIPBALTOPOFOBVALUE) {
            this.sHIPBALTOPOFOBVALUE = sHIPBALTOPOFOBVALUE;
        }

        public String getSEWTOSHIPBALQTY() {
            return sEWTOSHIPBALQTY;
        }

        public void setSEWTOSHIPBALQTY(String sEWTOSHIPBALQTY) {
            this.sEWTOSHIPBALQTY = sEWTOSHIPBALQTY;
        }

        public String getSEWTOSHIPBALFOBVALUE() {
            return sEWTOSHIPBALFOBVALUE;
        }

        public void setSEWTOSHIPBALFOBVALUE(String sEWTOSHIPBALFOBVALUE) {
            this.sEWTOSHIPBALFOBVALUE = sEWTOSHIPBALFOBVALUE;
        }

    }

}
