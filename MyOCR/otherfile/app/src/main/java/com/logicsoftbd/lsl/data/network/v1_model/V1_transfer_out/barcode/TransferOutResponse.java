package com.logicsoftbd.lsl.data.network.v1_model.V1_transfer_out.barcode;

import com.google.gson.annotations.SerializedName;

public class TransferOutResponse{

	@SerializedName("resultset")
	private Resultset resultset;

	@SerializedName("status")
	private String status;

	@SerializedName("msg")
	private String msg;

	public Resultset getResultset(){
		return resultset;
	}

	public String getStatus(){
		return status;
	}

	public String getMsg() {
		return msg;
	}
}