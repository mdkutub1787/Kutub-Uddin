package com.logicsoftbd.lsl.data.network.v1_model.V1_transfer_out;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class V1GreyFabricTransferOutStoreListResponse {

	@SerializedName("msg")
	private String msg;

	@SerializedName("data")
	private List<V1GreyFabricTransferOutStoreList> stores;

	@SerializedName("status")
	private boolean status;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<V1GreyFabricTransferOutStoreList> getStores() {
		return stores;
	}

	public void setStores(List<V1GreyFabricTransferOutStoreList> stores) {
		this.stores = stores;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}

