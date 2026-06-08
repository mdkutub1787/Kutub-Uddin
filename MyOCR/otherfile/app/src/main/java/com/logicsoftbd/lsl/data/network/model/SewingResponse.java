package com.logicsoftbd.lsl.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SewingResponse implements Serializable {
    private boolean isFirst = true;
    @Expose
    @SerializedName("status")
    private String status;


    @Expose
    @SerializedName("resultset")
    private Result data;

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Result getData() {
        return data;
    }

    public void setData(Result data) {
        this.data = data;
    }

    public static class Result implements Serializable{
        @Expose
        @SerializedName("msg")
        private String msg;

        @Expose
        @SerializedName("status")
        private String status;

        @Expose
        @SerializedName("MasterPart")
        private MasterPart masterPart;



        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public MasterPart getMasterPart() {
            return masterPart;
        }

        public void setMasterPart(MasterPart masterPart) {
            this.masterPart = masterPart;
        }

        public  static class MasterPart implements Serializable{

            @SerializedName("COMPANY_ID")
            @Expose
            public Integer companyId;
            @SerializedName("SERVING_COMPANY")
            @Expose
            public Integer servingCompany;
            @SerializedName("PRODUCTION_TYPE")
            @Expose
            public Integer productionType;

            @SerializedName("BUNDLE_NO")
            @Expose
            public String bundleNo;

            @SerializedName("BARCODE_NO")
            @Expose
            public String barcodeNo;

            @SerializedName("YEAR")
            @Expose
            public Integer yEAR;

            @SerializedName("COLOR_SIZE_ID")
            @Expose
            public Integer cOLORSIZEID;

            @SerializedName("ORDER_ID")
            @Expose
            public Integer oRDERID;

            @SerializedName("ITEM_ID")
            @Expose
            public Integer iTEMID;

            @SerializedName("COUNTRY_ID")
            @Expose
            public Integer cOUNTRYID;

            @SerializedName("SIZE_ID")
            @Expose
            public Integer sIZEID;

            @SerializedName("COLOR_ID")
            @Expose
            public Integer cOLORID;

            @SerializedName("CUT_NO")
            @Expose
            public String cUTNO;

            @SerializedName("JOB_NO")
            @Expose
            public String jOBNO;

            @SerializedName("LOCATION_ID")
            @Expose
            public int locationId;

            @SerializedName("FLOOR_ID")
            @Expose
            public int floorId;

            @SerializedName("SEWING_LINE")
            @Expose
            public int sEWINGLINE;

            @SerializedName("BUYER")
            @Expose
            public String bUYER;

            @SerializedName("ORDER_NO")
            @Expose
            public String oRDERNO;

            @SerializedName("ITEM")
            @Expose
            public String iTEM;

            @SerializedName("COUNTRY")
            @Expose
            public String cOUNTRY;

            @SerializedName("COLOR")
            @Expose
            public String cOLOR;

            @SerializedName("SIZE")
            @Expose
            public String sIZE;

            @SerializedName("QTY")
            @Expose
            public Integer qTY;

            @SerializedName("REJECT")
            @Expose
            public Integer reject;

            @SerializedName("ALTER")
            @Expose
            public Integer alter;

            @SerializedName("SPOT")
            @Expose
            public Integer spot;

            @SerializedName("REPLACE")
            @Expose
            public Integer replace;

            @SerializedName("QC_QNTY")
            @Expose
            public Integer qcQuantity;
            @Expose
            @SerializedName("REJECT_STR")
            private String rejectStr;
            @Expose
            @SerializedName("ALTER_STR")
            private String alterStr;
            @Expose
            @SerializedName("SPOT_STR")
            private String spotStr;

            @SerializedName("IS_RESCAN")
            @Expose
            public Integer iSRESCAN;

            @SerializedName("COLOR_TYPE_ID")
            @Expose
            public Integer cOLORTYPEID;

            public Integer getReject() {
                return reject;
            }

            public void setReject(Integer reject) {
                this.reject = reject;
            }

            public Integer getAlter() {
                return alter;
            }

            public void setAlter(Integer alter) {
                this.alter = alter;
            }

            public Integer getSpot() {
                return spot;
            }

            public void setSpot(Integer spot) {
                this.spot = spot;
            }

            public Integer getReplace() {
                return replace;
            }

            public void setReplace(Integer replace) {
                this.replace = replace;
            }

            public Integer getQcQuantity() {
                return qcQuantity;
            }

            public void setQcQuantity(Integer qcQuantity) {
                this.qcQuantity = qcQuantity;
            }

            public Integer getCompanyId() {
                return companyId;
            }

            public void setCompanyId(Integer companyId) {
                this.companyId = companyId;
            }

            public Integer getServingCompany() {
                return servingCompany;
            }

            public void setServingCompany(Integer servingCompany) {
                this.servingCompany = servingCompany;
            }

            public Integer getProductionType() {
                return productionType;
            }

            public void setProductionType(Integer productionType) {
                this.productionType = productionType;
            }

            public String getBundleNo() {
                return bundleNo;
            }

            public void setBundleNo(String bundleNo) {
                this.bundleNo = bundleNo;
            }

            public String getBarcodeNo() {
                return barcodeNo;
            }

            public void setBarcodeNo(String barcodeNo) {
                this.barcodeNo = barcodeNo;
            }

            public Integer getyEAR() {
                return yEAR;
            }

            public void setyEAR(Integer yEAR) {
                this.yEAR = yEAR;
            }

            public Integer getcOLORSIZEID() {
                return cOLORSIZEID;
            }

            public void setcOLORSIZEID(Integer cOLORSIZEID) {
                this.cOLORSIZEID = cOLORSIZEID;
            }

            public Integer getoRDERID() {
                return oRDERID;
            }

            public void setoRDERID(Integer oRDERID) {
                this.oRDERID = oRDERID;
            }

            public Integer getiTEMID() {
                return iTEMID;
            }

            public void setiTEMID(Integer iTEMID) {
                this.iTEMID = iTEMID;
            }

            public Integer getcOUNTRYID() {
                return cOUNTRYID;
            }

            public void setcOUNTRYID(Integer cOUNTRYID) {
                this.cOUNTRYID = cOUNTRYID;
            }

            public Integer getsIZEID() {
                return sIZEID;
            }

            public void setsIZEID(Integer sIZEID) {
                this.sIZEID = sIZEID;
            }

            public Integer getcOLORID() {
                return cOLORID;
            }

            public void setcOLORID(Integer cOLORID) {
                this.cOLORID = cOLORID;
            }

            public String getcUTNO() {
                return cUTNO;
            }

            public void setcUTNO(String cUTNO) {
                this.cUTNO = cUTNO;
            }

            public String getjOBNO() {
                return jOBNO;
            }

            public void setjOBNO(String jOBNO) {
                this.jOBNO = jOBNO;
            }

            public int getLocationId() {
                return locationId;
            }

            public void setLocationId(int locationId) {
                this.locationId = locationId;
            }

            public int getFloorId() {
                return floorId;
            }

            public void setFloorId(int floorId) {
                this.floorId = floorId;
            }

            public int getsEWINGLINE() {
                return sEWINGLINE;
            }

            public void setsEWINGLINE(int sEWINGLINE) {
                this.sEWINGLINE = sEWINGLINE;
            }

            public String getbUYER() {
                return bUYER;
            }

            public void setbUYER(String bUYER) {
                this.bUYER = bUYER;
            }

            public String getoRDERNO() {
                return oRDERNO;
            }

            public void setoRDERNO(String oRDERNO) {
                this.oRDERNO = oRDERNO;
            }

            public String getiTEM() {
                return iTEM;
            }

            public void setiTEM(String iTEM) {
                this.iTEM = iTEM;
            }

            public String getcOUNTRY() {
                return cOUNTRY;
            }

            public void setcOUNTRY(String cOUNTRY) {
                this.cOUNTRY = cOUNTRY;
            }

            public String getcOLOR() {
                return cOLOR;
            }

            public void setcOLOR(String cOLOR) {
                this.cOLOR = cOLOR;
            }

            public String getsIZE() {
                return sIZE;
            }

            public void setsIZE(String sIZE) {
                this.sIZE = sIZE;
            }

            public Integer getqTY() {
                return qTY;
            }

            public void setqTY(Integer qTY) {
                this.qTY = qTY;
            }

            public Integer getiSRESCAN() {
                return iSRESCAN;
            }

            public void setiSRESCAN(Integer iSRESCAN) {
                this.iSRESCAN = iSRESCAN;
            }

            public Integer getcOLORTYPEID() {
                return cOLORTYPEID;
            }

            public void setcOLORTYPEID(Integer cOLORTYPEID) {
                this.cOLORTYPEID = cOLORTYPEID;
            }

            public String getAlterStr() {
                return alterStr;
            }

            public void setAlterStr(String alterStr) {
                this.alterStr = alterStr;
            }

            public String getSpotStr() {
                return spotStr;
            }

            public void setSpotStr(String spotStr) {
                this.spotStr = spotStr;
            }

            public String getRejectStr() {
                return rejectStr;
            }

            public void setRejectStr(String rejectStr) {
                this.rejectStr = rejectStr;
            }
        }


    }


}
