package com.logicsoftbd.lsl.data.network.v1_model;

public class V1_Shipment_Schedule_Model {
    private static String TotalQuantity;
    private static String TotalQuantityValue;
    private static String TotalFullshipped;
    private static String TotalPartialShipped;
    private static String TotalRunning;
    private int id;
   private String COMPANY_NAME;
   private String BUYER_NAME;
   private String QUANTITY;
    private String QUANTITY_VALUE;
    private String QUANTITY_VALUE_PERCENTAGE;
    private String FULL_SHIPPED;
    private String PARTIAL_SHIPPED;
    private String RUNNING;
    private String EX_FACTORY_PERCENTAGE;


    public static String getTotalQuantity() {
        return TotalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        TotalQuantity = totalQuantity;
    }
    public static String getTotalQuantityValue() {
        return TotalQuantityValue;
    }

    public static void setTotalQuantityValue(String totalQuantityValue) {
        TotalQuantityValue = totalQuantityValue;
    }

    public static String getTotalFullshipped() {
        return TotalFullshipped;
    }

    public static void setTotalFullshipped(String totalFullshipped) {
        TotalFullshipped = totalFullshipped;
    }

    public static String getTotalPartialShipped() {
        return TotalPartialShipped;
    }

    public static void setTotalPartialShipped(String totalPartialShipped) {
        TotalPartialShipped = totalPartialShipped;
    }

    public static String getTotalRunning() {
        return TotalRunning;
    }

    public static void setTotalRunning(String totalRunning) {
        TotalRunning = totalRunning;
    }




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCOMPANY_NAME() {
        return COMPANY_NAME;
    }

    public void setCOMPANY_NAME(String COMPANY_NAME) {
        this.COMPANY_NAME = COMPANY_NAME;
    }

    public String getBUYER_NAME() {
        return BUYER_NAME;
    }

    public void setBUYER_NAME(String BUYER_NAME) {
        this.BUYER_NAME = BUYER_NAME;
    }

    public String getQUANTITY() {
        return QUANTITY;
    }

    public void setQUANTITY(String QUANTITY) {
        this.QUANTITY = QUANTITY;
    }

    public String getQUANTITY_VALUE() {
        return QUANTITY_VALUE;
    }

    public void setQUANTITY_VALUE(String QUANTITY_VALUE) {
        this.QUANTITY_VALUE = QUANTITY_VALUE;
    }

    public String getQUANTITY_VALUE_PERCENTAGE() {
        return QUANTITY_VALUE_PERCENTAGE;
    }

    public void setQUANTITY_VALUE_PERCENTAGE(String QUANTITY_VALUE_PERCENTAGE) {
        this.QUANTITY_VALUE_PERCENTAGE = QUANTITY_VALUE_PERCENTAGE;
    }

    public String getFULL_SHIPPED() {
        return FULL_SHIPPED;
    }

    public void setFULL_SHIPPED(String FULL_SHIPPED) {
        this.FULL_SHIPPED = FULL_SHIPPED;
    }

    public String getPARTIAL_SHIPPED() {
        return PARTIAL_SHIPPED;
    }

    public void setPARTIAL_SHIPPED(String PARTIAL_SHIPPED) {
        this.PARTIAL_SHIPPED = PARTIAL_SHIPPED;
    }

    public String getRUNNING() {
        return RUNNING;
    }

    public void setRUNNING(String RUNNING) {
        this.RUNNING = RUNNING;
    }

    public String getEX_FACTORY_PERCENTAGE() {
        return EX_FACTORY_PERCENTAGE;
    }

    public void setEX_FACTORY_PERCENTAGE(String EX_FACTORY_PERCENTAGE) {
        this.EX_FACTORY_PERCENTAGE = EX_FACTORY_PERCENTAGE;
    }


}
