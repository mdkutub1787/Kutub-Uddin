package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V1_ShipmentModelClass {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }
    public class Datum {

        @SerializedName("COMPANY_NAME")
        @Expose
        private String cOMPANYNAME;
        @SerializedName("BUYER_NAME")
        @Expose
        private String bUYERNAME;
        @SerializedName("QUANTITY")
        @Expose
        private String qUANTITY;
        @SerializedName("QUANTITY_VALUE")
        @Expose
        private String qUANTITYVALUE;
        @SerializedName("QUANTITY_VALUE_PERCENTAGE")
        @Expose
        private String qUANTITYVALUEPERCENTAGE;
        @SerializedName("FULL_SHIPPED")
        @Expose
        private String fULLSHIPPED;
        @SerializedName("PARTIAL_SHIPPED")
        @Expose
        private String pARTIALSHIPPED;
        @SerializedName("RUNNING")
        @Expose
        private String rUNNING;
        @SerializedName("EX_FACTORY_PERCENTAGE")
        @Expose
        private String eXFACTORYPERCENTAGE;

        public String getCOMPANYNAME() {
            return cOMPANYNAME;
        }

        public void setCOMPANYNAME(String cOMPANYNAME) {
            this.cOMPANYNAME = cOMPANYNAME;
        }

        public String getBUYERNAME() {
            return bUYERNAME;
        }

        public void setBUYERNAME(String bUYERNAME) {
            this.bUYERNAME = bUYERNAME;
        }

        public String getQUANTITY() {
            return qUANTITY;
        }

        public void setQUANTITY(String qUANTITY) {
            this.qUANTITY = qUANTITY;
        }

        public String getQUANTITYVALUE() {
            return qUANTITYVALUE;
        }

        public void setQUANTITYVALUE(String qUANTITYVALUE) {
            this.qUANTITYVALUE = qUANTITYVALUE;
        }

        public String getQUANTITYVALUEPERCENTAGE() {
            return qUANTITYVALUEPERCENTAGE;
        }

        public void setQUANTITYVALUEPERCENTAGE(String qUANTITYVALUEPERCENTAGE) {
            this.qUANTITYVALUEPERCENTAGE = qUANTITYVALUEPERCENTAGE;
        }

        public String getFULLSHIPPED() {
            return fULLSHIPPED;
        }

        public void setFULLSHIPPED(String fULLSHIPPED) {
            this.fULLSHIPPED = fULLSHIPPED;
        }

        public String getPARTIALSHIPPED() {
            return pARTIALSHIPPED;
        }

        public void setPARTIALSHIPPED(String pARTIALSHIPPED) {
            this.pARTIALSHIPPED = pARTIALSHIPPED;
        }

        public String getRUNNING() {
            return rUNNING;
        }

        public void setRUNNING(String rUNNING) {
            this.rUNNING = rUNNING;
        }

        public String getEXFACTORYPERCENTAGE() {
            return eXFACTORYPERCENTAGE;
        }

        public void setEXFACTORYPERCENTAGE(String eXFACTORYPERCENTAGE) {
            this.eXFACTORYPERCENTAGE = eXFACTORYPERCENTAGE;
        }

    }
}
