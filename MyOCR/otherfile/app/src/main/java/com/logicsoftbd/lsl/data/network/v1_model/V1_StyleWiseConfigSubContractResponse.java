package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V1_StyleWiseConfigSubContractResponse {

	@SerializedName("status")
	@Expose
	private Boolean status;
	@SerializedName("data")
	@Expose
	private List<V1_DataItem> data;

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public List<V1_DataItem> getData() {
		return data;
	}

	public void setData(List<V1_DataItem> data) {
		this.data = data;
	}

	public class V1_DataItem {

		@SerializedName("JOB_ID")
		@Expose
		private String jobId;
		@SerializedName("JOB_NO")
		@Expose
		private String jobNo;
		@SerializedName("STYLE_REF_NO")
		@Expose
		private String styleRefNo;
		@SerializedName("PO_NUMBER")
		@Expose
		private String poNumber;
		@SerializedName("PO_ID")
		@Expose
		private String poId;
		@SerializedName("ITEM_NUMBER_ID")
		@Expose
		private String itemNumberId;
		@SerializedName("ITEM_NAME")
		@Expose
		private String itemName;
		@SerializedName("COUNTRY_ID")
		@Expose
		private String countryId;
		@SerializedName("COUNTRY_NAME")
		@Expose
		private String countryName;
		@SerializedName("IR_NUMBER")
		@Expose
		private String irNumber;
		@SerializedName("BUYER_NAME")
		@Expose
		private String buyerName;

		public String getJobId() {
			return jobId;
		}

		public void setJobId(String jobId) {
			this.jobId = jobId;
		}

		public String getJobNo() {
			return jobNo;
		}

		public void setJobNo(String jobNo) {
			this.jobNo = jobNo;
		}

		public String getStyleRefNo() {
			return styleRefNo;
		}

		public void setStyleRefNo(String styleRefNo) {
			this.styleRefNo = styleRefNo;
		}

		public String getPoNumber() {
			return poNumber;
		}

		public void setPoNumber(String poNumber) {
			this.poNumber = poNumber;
		}

		public String getPoId() {
			return poId;
		}

		public void setPoId(String poId) {
			this.poId = poId;
		}

		public String getItemNumberId() {
			return itemNumberId;
		}

		public void setItemNumberId(String itemNumberId) {
			this.itemNumberId = itemNumberId;
		}

		public String getItemName() {
			return itemName;
		}

		public void setItemName(String itemName) {
			this.itemName = itemName;
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

		public String getIrNumber() {
			return irNumber;
		}

		public void setIrNumber(String irNumber) {
			this.irNumber = irNumber;
		}

		public String getBuyerName() {
			return buyerName;
		}

		public void setBuyerName(String buyerName) {
			this.buyerName = buyerName;
		}
	}
}