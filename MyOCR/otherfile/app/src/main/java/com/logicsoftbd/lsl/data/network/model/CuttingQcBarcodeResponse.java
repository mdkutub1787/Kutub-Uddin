package com.logicsoftbd.lsl.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CuttingQcBarcodeResponse implements Serializable {
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
        @Expose
        @SerializedName("BundleNos")
        private BundleNos bundleNos;

        @Expose
        @SerializedName("DtlsPart")
        private List<DetailsPart> detailsPart;

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

        public List<DetailsPart> getDetailsPart() {
            return detailsPart;
        }

        public void setDetailsPart(List<DetailsPart> detailsPart) {
            this.detailsPart = detailsPart;
        }

        public BundleNos getBundleNos() {
            return bundleNos;
        }

        public void setBundleNos(BundleNos bundleNos) {
            this.bundleNos = bundleNos;
        }

        public static class BundleNos implements  Serializable {
            @Expose
            @SerializedName("BUNDLE_NO")
            private String bundleString;

            public String getBundleString() {
                return bundleString;
            }

            public void setBundleString(String bundleString) {
                this.bundleString = bundleString;
            }
        }

        public  static class MasterPart implements Serializable{
            @Expose
            @SerializedName("UPDATE_ID")
            private int updateId;
            @Expose
            @SerializedName("USER_ID")
            private int userId;

            @Expose
            @SerializedName("CUT_MST_ID")
            private int cutMstId;

            @Expose
            @SerializedName("CUT_DTLS_ID")
            private int cutDtlsID;

            @Expose
            @SerializedName("CUTTING_NO")
            private String cuttingNo;

            @Expose
            @SerializedName("LOCATION_ID")
            private int locationId;

            @Expose
            @SerializedName("FLOOR_ID")
            private int floorId;

            @Expose
            @SerializedName("QC_DATE")
            private String qcDate;

            @Expose
            @SerializedName("QC_HOUR")
            private String qcHour;

            @Expose
            @SerializedName("TABLE_NO")
            private String tableNo;

            @Expose
            @SerializedName("JOB_NO")
            private String jobNo;

            @Expose
            @SerializedName("BATCH_ID")
            private String batchId;

            @Expose
            @SerializedName("COMPANY_ID")
            private int companyId;

            @Expose
            @SerializedName("ENTRY_DATE")
            private String entryDate;

            @Expose
            @SerializedName("START_TIME")
            private String startTime;
            @Expose
            @SerializedName("END_DATE")
            private String endDate;

            @Expose
            @SerializedName("END_TIME")
            private String endTime;

            @Expose
            @SerializedName("MARKER_LENGTH")
            private String markerLength;

            @Expose
            @SerializedName("MARKER_WIDTH")
            private String markerWidth;

            @Expose
            @SerializedName("FABRIC_WIDTH")
            private String fabricWidth;

            @Expose
            @SerializedName("GSM")
            private String gsm;

            @Expose
            @SerializedName("WIDTH_DIA")
            private String widthDta;

            @Expose
            @SerializedName("SERVING_COMPANY")
            private int SERVING_COMPANY;

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public int getUpdateId() {
                return updateId;
            }

            public void setUpdateId(int updateId) {
                this.updateId = updateId;
            }

            public int getCutMstId() {
                return cutMstId;
            }

            public void setCutMstId(int cutMstId) {
                this.cutMstId = cutMstId;
            }

            public int getCutDtlsID() {
                return cutDtlsID;
            }

            public void setCutDtlsID(int cutDtlsID) {
                this.cutDtlsID = cutDtlsID;
            }

            public String getCuttingNo() {
                return cuttingNo;
            }

            public void setCuttingNo(String cuttingNo) {
                this.cuttingNo = cuttingNo;
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

            public String getQcDate() {
                return qcDate;
            }

            public void setQcDate(String qcDate) {
                this.qcDate = qcDate;
            }

            public String getQcHour() {
                return qcHour;
            }

            public void setQcHour(String qcHour) {
                this.qcHour = qcHour;
            }

            public String getTableNo() {
                return tableNo;
            }

            public void setTableNo(String tableNo) {
                this.tableNo = tableNo;
            }

            public String getJobNo() {
                return jobNo;
            }

            public void setJobNo(String jobNo) {
                this.jobNo = jobNo;
            }

            public String getBatchId() {
                return batchId;
            }

            public void setBatchId(String batchId) {
                this.batchId = batchId;
            }

            public int getCompanyId() {
                return companyId;
            }

            public void setCompanyId(int companyId) {
                this.companyId = companyId;
            }

            public String getEntryDate() {
                return entryDate;
            }

            public void setEntryDate(String entryDate) {
                this.entryDate = entryDate;
            }

            public String getStartTime() {
                return startTime;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }

            public String getEndDate() {
                return endDate;
            }

            public void setEndDate(String endDate) {
                this.endDate = endDate;
            }

            public String getEndTime() {
                return endTime;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }

            public String getMarkerLength() {
                return markerLength;
            }

            public void setMarkerLength(String markerLength) {
                this.markerLength = markerLength;
            }

            public String getMarkerWidth() {
                return markerWidth;
            }

            public void setMarkerWidth(String markerWidth) {
                this.markerWidth = markerWidth;
            }

            public String getFabricWidth() {
                return fabricWidth;
            }

            public void setFabricWidth(String fabricWidth) {
                this.fabricWidth = fabricWidth;
            }

            public String getGsm() {
                return gsm;
            }

            public void setGsm(String gsm) {
                this.gsm = gsm;
            }

            public String getWidthDta() {
                return widthDta;
            }

            public void setWidthDta(String widthDta) {
                this.widthDta = widthDta;
            }

            public int getSERVING_COMPANY() {
                return SERVING_COMPANY;
            }

            public void setSERVING_COMPANY(int SERVING_COMPANY) {
                this.SERVING_COMPANY = SERVING_COMPANY;
            }
        }

        public static  class DetailsPart implements Serializable{
            @Expose
            @SerializedName("ORDER_ID")
            private int orderId;

            @Expose
            @SerializedName("GMT_ITEM_ID")
            private int gmtItemId;

            @Expose
            @SerializedName("COLOR_ID")
            private int colorId;

            @Expose
            @SerializedName("BUNDLE_DATA")
            private List<BundleData> bundleDataList;

            public int getOrderId() {
                return orderId;
            }

            public void setOrderId(int orderId) {
                this.orderId = orderId;
            }

            public int getGmtItemId() {
                return gmtItemId;
            }

            public void setGmtItemId(int gmtItemId) {
                this.gmtItemId = gmtItemId;
            }

            public int getColorId() {
                return colorId;
            }

            public void setColorId(int colorId) {
                this.colorId = colorId;
            }

            public List<BundleData> getBundleDataList() {
                return bundleDataList;
            }

            public void setBundleDataList(List<BundleData> bundleDataList) {
                this.bundleDataList = bundleDataList;
            }

            public  static class BundleData implements Serializable{

                private transient int detailsPos;
                private transient int bundlePos;
                @Expose
                @SerializedName("COUNTRY_ID")
                private int countryId;

                @Expose
                @SerializedName("BUNDLE_NO")
                private String bundleNo;

                @Expose
                @SerializedName("BARCODE_NO")
                private String barcodeNo;

                @Expose
                @SerializedName("SIZE_ID")
                private int sizeId;

                @Expose
                @SerializedName("NUMBER_START")
                private int numberStart;

                @Expose
                @SerializedName("NUMBER_END")
                private int numberEnd;

                @Expose
                @SerializedName("QC_PASS_QTY")
                private Integer qcPassQty;

                @Expose
                @SerializedName("DEFECT_STR")
                private String defectStr;

                @Expose
                @SerializedName("REJECT_QNTY")
                private Integer rejectQty;

                @Expose
                @SerializedName("REPLACE_QNTY")
                private Integer replaceQty;

                private Integer quantity;

                public int getCountryId() {
                    return countryId;
                }

                public void setCountryId(int countryId) {
                    this.countryId = countryId;
                }

                public String getBundleNo() {
                    return bundleNo;
                }

                public void setBundleNo(String bundleNo) {
                    this.bundleNo = bundleNo;
                }

                public int getSizeId() {
                    return sizeId;
                }

                public void setSizeId(int sizeId) {
                    this.sizeId = sizeId;
                }

                public int getNumberStart() {
                    return numberStart;
                }

                public void setNumberStart(int numberStart) {
                    this.numberStart = numberStart;
                }

                public int getNumberEnd() {
                    return numberEnd;
                }

                public void setNumberEnd(int numberEnd) {
                    this.numberEnd = numberEnd;
                }

                public Integer getQcPassQty() {
                    return qcPassQty;
                }

                public void setQcPassQty(Integer qcPassQty) {
                    this.qcPassQty = qcPassQty;
                }

                public String getDefectStr() {
                    return defectStr;
                }

                public void setDefectStr(String defectStr) {
                    this.defectStr = defectStr;
                }

                public Integer getRejectQty() {
                    return rejectQty;
                }

                public void setRejectQty(Integer rejectQty) {
                    this.rejectQty = rejectQty;
                }

                public Integer getReplaceQty() {
                    return replaceQty;
                }

                public void setReplaceQty(Integer replaceQty) {
                    this.replaceQty = replaceQty;
                }

                public String getBarcodeNo() {
                    return barcodeNo;
                }

                public void setBarcodeNo(String barcodeNo) {
                    this.barcodeNo = barcodeNo;
                }

                public int getDetailsPos() {
                    return detailsPos;
                }

                public void setDetailsPos(int detailsPos) {
                    this.detailsPos = detailsPos;
                }

                public int getBundlePos() {
                    return bundlePos;
                }

                public void setBundlePos(int bundlePos) {
                    this.bundlePos = bundlePos;
                }

                public Integer getQuantity() {
                    return quantity;
                }

                public void setQuantity(Integer quantity) {
                    this.quantity = quantity;
                }
            }

        }
    }


}
