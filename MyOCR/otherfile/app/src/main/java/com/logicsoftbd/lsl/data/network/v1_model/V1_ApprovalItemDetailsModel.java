package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V1_ApprovalItemDetailsModel {
    @SerializedName("DATA")
    @Expose
    private List<Datum> data;
    @SerializedName("MSG")
    @Expose
    private String msg;
    @SerializedName("STATUS")
    @Expose
    private String status;

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

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
    public class Datum {

        @SerializedName("ITEM_CATEGORY")
        @Expose
        private String itemCategory;
        @SerializedName("ITEM_GROUP")
        @Expose
        private String itemGroup;
        @SerializedName("ITEM_SUB_GROUP")
        @Expose
        private String itemSubGroup;
        @SerializedName("ITEM_SIZE")
        @Expose
        private String itemSize;
        @SerializedName("ITEM_DESCRIPTION")
        @Expose
        private String itemDescription;
        @SerializedName("PRODUCT_NAME_DETAILS")
        @Expose
        private String productNameDetails;
        @SerializedName("CONS_UOM")
        @Expose
        private String consUom;
        @SerializedName("QUANTITY")
        @Expose
        private String quantity;
        @SerializedName("RATE")
        @Expose
        private String rate;
        @SerializedName("AMOUNT")
        @Expose
        private String amount;
        @SerializedName("STOCK")
        @Expose
        private String stock;
        @SerializedName("LAST_RATE")
        @Expose
        private String lastRate;
        @SerializedName("LAST_SUPPLIER")
        @Expose
        private String lastSupplier;

        public String getItemCategory() {
            return itemCategory;
        }

        public void setItemCategory(String itemCategory) {
            this.itemCategory = itemCategory;
        }

        public String getItemGroup() {
            return itemGroup;
        }

        public void setItemGroup(String itemGroup) {
            this.itemGroup = itemGroup;
        }

        public String getItemSubGroup() {
            return itemSubGroup;
        }

        public void setItemSubGroup(String itemSubGroup) {
            this.itemSubGroup = itemSubGroup;
        }

        public String getItemSize() {
            return itemSize;
        }

        public void setItemSize(String itemSize) {
            this.itemSize = itemSize;
        }

        public String getItemDescription() {
            return itemDescription;
        }

        public void setItemDescription(String itemDescription) {
            this.itemDescription = itemDescription;
        }

        public String getProductNameDetails() {
            return productNameDetails;
        }

        public void setProductNameDetails(String productNameDetails) {
            this.productNameDetails = productNameDetails;
        }

        public String getConsUom() {
            return consUom;
        }

        public void setConsUom(String consUom) {
            this.consUom = consUom;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getStock() {
            return stock;
        }

        public void setStock(String stock) {
            this.stock = stock;
        }

        public String getLastRate() {
            return lastRate;
        }

        public void setLastRate(String lastRate) {
            this.lastRate = lastRate;
        }

        public String getLastSupplier() {
            return lastSupplier;
        }

        public void setLastSupplier(String lastSupplier) {
            this.lastSupplier = lastSupplier;
        }

    }
}
