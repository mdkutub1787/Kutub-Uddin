package com.logicsoftbd.lsl.data.network.v1_model.V1_transfer_out.barcode;

import com.google.gson.annotations.SerializedName;

public class MasterPart{
	@SerializedName("COMPANY_ID")
	private String cOMPANYID;

	public String getCOMPANYID(){
		return cOMPANYID;
	}
}