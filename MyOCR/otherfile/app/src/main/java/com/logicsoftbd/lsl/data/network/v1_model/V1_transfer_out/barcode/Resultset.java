package com.logicsoftbd.lsl.data.network.v1_model.V1_transfer_out.barcode;

import com.google.gson.annotations.SerializedName;

public class Resultset{

	@SerializedName("MasterPart")
	private MasterPart masterPart;

	@SerializedName("DtlsPart")
	private DtlsPart dtlsPart;

	public MasterPart getMasterPart(){
		return masterPart;
	}

	public DtlsPart getDtlsPart(){
		return dtlsPart;
	}
}