package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V1_StyleWiseOperationResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("resultset")
    @Expose
    private Resultset resultset;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Resultset getResultset() {
        return resultset;
    }

    public void setResultset(Resultset resultset) {
        this.resultset = resultset;
    }

    public class Size {

        @SerializedName("po_break_down_id")
        @Expose
        private String po_break_down_id;
        @SerializedName("item_id")
        @Expose
        private String item_id;
        @SerializedName("country_id")
        @Expose
        private String country_id;
        @SerializedName("country_name")
        @Expose
        private String country_name;
        @SerializedName("color_size_breakdown_id")
        @Expose
        private String colorSizeBreakdownId;
        @SerializedName("colour_id")
        @Expose
        private String colourId;
        @SerializedName("colour_name")
        @Expose
        private String colour_name;
        @SerializedName("size_id")
        @Expose
        private String sizeId;
        @SerializedName("size_name")
        @Expose
        private String sizeName;
        @SerializedName("input_qnty")
        @Expose
        private String inputQnty;
        @SerializedName("output_qnty")
        @Expose
        private String outputQnty;
        @SerializedName("good")
        @Expose
        private String good;
        @SerializedName("reject")
        @Expose
        private String reject;
        @SerializedName("alter")
        @Expose
        private String alter;
        @SerializedName("spot")
        @Expose
        private String spot;
        @SerializedName("rectified")
        @Expose
        private String rectified;

        public String getPo_break_down_id() {
            return po_break_down_id;
        }

        public void setPo_break_down_id(String po_break_down_id) {
            this.po_break_down_id = po_break_down_id;
        }

        public String getItem_id() {
            return item_id;
        }

        public void setItem_id(String item_id) {
            this.item_id = item_id;
        }

        public String getCountry_id() {
            return country_id;
        }

        public void setCountry_id(String country_id) {
            this.country_id = country_id;
        }

        public String getCountry_name() {
            return country_name;
        }

        public void setCountry_name(String country_name) {
            this.country_name = country_name;
        }

        public String getColorSizeBreakdownId() {
            return colorSizeBreakdownId;
        }

        public void setColorSizeBreakdownId(String colorSizeBreakdownId) {
            this.colorSizeBreakdownId = colorSizeBreakdownId;
        }

        public String getColourId() {
            return colourId;
        }

        public void setColourId(String colourId) {
            this.colourId = colourId;
        }

        public String getColour_name() {
            return colour_name;
        }

        public void setColour_name(String colour_name) {
            this.colour_name = colour_name;
        }

        public String getSizeId() {
            return sizeId;
        }

        public void setSizeId(String sizeId) {
            this.sizeId = sizeId;
        }

        public String getSizeName() {
            return sizeName;
        }

        public void setSizeName(String sizeName) {
            this.sizeName = sizeName;
        }

        public String getInputQnty() {
            return inputQnty;
        }

        public void setInputQnty(String inputQnty) {
            this.inputQnty = inputQnty;
        }

        public String getOutputQnty() {
            return outputQnty;
        }

        public void setOutputQnty(String outputQnty) {
            this.outputQnty = outputQnty;
        }

        public String getGood() {
            return good;
        }

        public void setGood(String good) {
            this.good = good;
        }

        public String getReject() {
            return reject;
        }

        public void setReject(String reject) {
            this.reject = reject;
        }

        public String getAlter() {
            return alter;
        }

        public void setAlter(String alter) {
            this.alter = alter;
        }

        public String getSpot() {
            return spot;
        }

        public void setSpot(String spot) {
            this.spot = spot;
        }

        public String getRectified() {
            return rectified;
        }

        public void setRectified(String rectified) {
            this.rectified = rectified;
        }
    }

    public class Country {

        @SerializedName("po_break_down_id")
        @Expose
        private String poBreakDownId;
        @SerializedName("colour_id")
        @Expose
        private String colourId;
        @SerializedName("country_id")
        @Expose
        private String countryId;
        @SerializedName("country_name")
        @Expose
        private String countryName;
        @SerializedName("country_output_qnty")
        @Expose
        private String countryOutputQnty;

        public String getPoBreakDownId() {
            return poBreakDownId;
        }

        public void setPoBreakDownId(String poBreakDownId) {
            this.poBreakDownId = poBreakDownId;
        }

        public String getColourId() {
            return colourId;
        }

        public void setColourId(String colourId) {
            this.colourId = colourId;
        }

        public String getCountryId() {
            return countryId;
        }

        public void setCountryId(String countryId) {
            this.countryId = countryId;
        }

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public String getCountryOutputQnty() {
            return countryOutputQnty;
        }

        public void setCountryOutputQnty(String countryOutputQnty) {
            this.countryOutputQnty = countryOutputQnty;
        }

    }

    public class Resultset {

        @SerializedName("total_input_qnty")
        @Expose
        private Integer inputQnty;
        @SerializedName("total_output_qnty")
        @Expose
        private Integer outputQnty;
        @SerializedName("job_id")
        @Expose
        private String jobId;
        @SerializedName("company_id")
        @Expose
        private String companyId;

        @SerializedName("po_break_down_id")
        @Expose
        private Integer poBreakDownId;
        @SerializedName("color")
        @Expose
        private List<Color> color;
        @SerializedName("po_ids")
        @Expose
        private List<PoId> poIds;
        @SerializedName("country")
        @Expose
        private List<Country> country;
        @SerializedName("size")
        @Expose
        private List<Size> size;
        @SerializedName("operation")
        @Expose
        private List<Operation> operation;
        @SerializedName("reject_list")
        @Expose
        private List<Reject> rejectList;
        @SerializedName("alter_list")
        @Expose
        private List<Alter> alterList;
        @SerializedName("spot_list")
        @Expose
        private List<Spot> spotList;

        public Integer getInputQnty() {
            return inputQnty;
        }

        public void setInputQnty(Integer inputQnty) {
            this.inputQnty = inputQnty;
        }

        public Integer getOutputQnty() {
            return outputQnty;
        }

        public void setOutputQnty(Integer outputQnty) {
            this.outputQnty = outputQnty;
        }

        public String getJobId() {
            return jobId;
        }

        public void setJobId(String jobId) {
            this.jobId = jobId;
        }

        public String getCompanyId() {
            return companyId;
        }

        public void setCompanyId(String companyId) {
            this.companyId = companyId;
        }

        public Integer getPoBreakDownId() {
            return poBreakDownId;
        }

        public void setPoBreakDownId(Integer poBreakDownId) {
            this.poBreakDownId = poBreakDownId;
        }

        public List<Color> getColor() {
            return color;
        }

        public void setColor(List<Color> color) {
            this.color = color;
        }

        public List<PoId> getPoIds() {
            return poIds;
        }

        public void setPoIds(List<PoId> poIds) {
            this.poIds = poIds;
        }

        public List<Size> getSize() {
            return size;
        }

        public void setSize(List<Size> size) {
            this.size = size;
        }

        public List<Country> getCountry() {
            return country;
        }

        public void setCountry(List<Country> country) {
            this.country = country;
        }

        public List<Operation> getOperation() {
            return operation;
        }

        public void setOperation(List<Operation> operation) {
            this.operation = operation;
        }

        public List<Reject> getRejectList() {
            return rejectList;
        }

        public void setRejectList(List<Reject> rejectList) {
            this.rejectList = rejectList;
        }

        public List<Alter> getAlterList() {
            return alterList;
        }

        public void setAlterList(List<Alter> alterList) {
            this.alterList = alterList;
        }

        public List<Spot> getSpotList() {
            return spotList;
        }

        public void setSpotList(List<Spot> spotList) {
            this.spotList = spotList;
        }

    }

    public class Reject {

        @SerializedName("ID")
        @Expose
        private Integer id;
        @SerializedName("NAME")
        @Expose
        private String name;
        @SerializedName("DEFECT_SERIAL_NO")
        @Expose
        private String defectSerialNo;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDefectSerialNo() {
            return defectSerialNo;
        }

        public void setDefectSerialNo(String defectSerialNo) {
            this.defectSerialNo = defectSerialNo;
        }
    }

    public class PoId {

        @SerializedName("po_id")
        @Expose
        private String poId;
        @SerializedName("po_number")
        @Expose
        private String poNumber;

        public String getPoId() {
            return poId;
        }

        public void setPoId(String poId) {
            this.poId = poId;
        }

        public String getPoNumber() {
            return poNumber;
        }

        public void setPoNumber(String poNumber) {
            this.poNumber = poNumber;
        }

    }

    public class Alter {

        @SerializedName("ID")
        @Expose
        private Integer id;
        @SerializedName("NAME")
        @Expose
        private String name;
        @SerializedName("DEFECT_SERIAL_NO")
        @Expose
        private String defectSerialNo;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDefectSerialNo() {
            return defectSerialNo;
        }

        public void setDefectSerialNo(String defectSerialNo) {
            this.defectSerialNo = defectSerialNo;
        }
    }

    public class Spot {

        @SerializedName("ID")
        @Expose
        private Integer id;
        @SerializedName("NAME")
        @Expose
        private String name;
        @SerializedName("DEFECT_SERIAL_NO")
        @Expose
        private String defectSerialNo;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDefectSerialNo() {
            return defectSerialNo;
        }

        public void setDefectSerialNo(String defectSerialNo) {
            this.defectSerialNo = defectSerialNo;
        }
    }

    public class Operation {

        @SerializedName("ID")
        @Expose
        private Integer id;
        @SerializedName("NAME")
        @Expose
        private String name;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    public class Defect {

        @SerializedName("ID")
        @Expose
        private Integer id;
        @SerializedName("NAME")
        @Expose
        private String name;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    public class Color {

        @SerializedName("po_break_down_id")
        @Expose
        private String po_break_down_id;
        @SerializedName("colour_id")
        @Expose
        private String colourId;
        @SerializedName("colour_name")
        @Expose
        private String colourName;
        @SerializedName("color_output_qnty")
        @Expose
        private String color_output_qnty;
        @SerializedName("good")
        @Expose
        private String good;
        @SerializedName("reject")
        @Expose
        private String reject;
        @SerializedName("alter")
        @Expose
        private String alter;
        @SerializedName("spot")
        @Expose
        private String spot;
        @SerializedName("rectified")
        @Expose
        private String rectified;

        public String getGood() {
            return good;
        }

        public void setGood(String good) {
            this.good = good;
        }

        public String getReject() {
            return reject;
        }

        public void setReject(String reject) {
            this.reject = reject;
        }

        public String getAlter() {
            return alter;
        }

        public void setAlter(String alter) {
            this.alter = alter;
        }

        public String getSpot() {
            return spot;
        }

        public void setSpot(String spot) {
            this.spot = spot;
        }

        public String getRectified() {
            return rectified;
        }

        public void setRectified(String rectified) {
            this.rectified = rectified;
        }

        public String getPo_break_down_id() {
            return po_break_down_id;
        }

        public void setPo_break_down_id(String po_break_down_id) {
            this.po_break_down_id = po_break_down_id;
        }

        public String getColourId() {
            return colourId;
        }

        public void setColourId(String colourId) {
            this.colourId = colourId;
        }

        public String getColourName() {
            return colourName;
        }

        public void setColourName(String colourName) {
            this.colourName = colourName;
        }

        public String getColor_output_qnty() {
            return color_output_qnty;
        }

        public void setColor_output_qnty(String color_output_qnty) {
            this.color_output_qnty = color_output_qnty;
        }
    }

}
