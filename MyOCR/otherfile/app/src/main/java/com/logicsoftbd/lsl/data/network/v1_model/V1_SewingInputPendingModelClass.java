package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V1_SewingInputPendingModelClass {
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
    public class SewingOutputPending {

        @SerializedName("PO_NUMBER")
        @Expose
        private String pONUMBER;
        @SerializedName("BARCODE_NO")
        @Expose
        private Long bARCODENO;
        @SerializedName("BUNDLE_NO")
        @Expose
        private String bUNDLENO;
        @SerializedName("CUT_NO")
        @Expose
        private String cUTNO;
        @SerializedName("PRODUCTION_QNTY")
        @Expose
        private String pRODUCTIONQNTY;
        @SerializedName("JOB_NO")
        @Expose
        private String jOBNO;

        public String getpONUMBER() { return pONUMBER; }

        public void setpONUMBER(String pONUMBER) {
            this.pONUMBER = pONUMBER;
        }

        public Long getBARCODENO() {
            return bARCODENO;
        }

        public void setBARCODENO(Long bARCODENO) {
            this.bARCODENO = bARCODENO;
        }

        public String getBUNDLENO() {
            return bUNDLENO;
        }

        public void setBUNDLENO(String bUNDLENO) {
            this.bUNDLENO = bUNDLENO;
        }

        public String getCUTNO() {
            return cUTNO;
        }

        public void setCUTNO(String cUTNO) {
            this.cUTNO = cUTNO;
        }

        public String getPRODUCTIONQNTY() {
            return pRODUCTIONQNTY;
        }

        public void setPRODUCTIONQNTY(String pRODUCTIONQNTY) {
            this.pRODUCTIONQNTY = pRODUCTIONQNTY;
        }
        public String getjOBNO() {
            return jOBNO;
        }

        public void setjOBNO(String jOBNO) {
            this.jOBNO = jOBNO;
        }

    }
    public class Data {

        @SerializedName("sewing_output_pending")
        @Expose
        private List<SewingOutputPending> sewingOutputPending = null;

        public List<SewingOutputPending> getSewingOutputPending() {
            return sewingOutputPending;
        }

        public void setSewingOutputPending(List<SewingOutputPending> sewingOutputPending) {
            this.sewingOutputPending = sewingOutputPending;
        }

    }
}
