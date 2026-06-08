package com.logicsoftbd.lsl.data.network.v1_model.V1_GreyRollIssue.V1_Issue_Purpose;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V1_GreyRollIssuePurposeModel {
	@SerializedName("resultset")
	private Resultset resultset;

	@SerializedName("status")
	private boolean status;

	public void setResultset(Resultset resultset){
		this.resultset = resultset;
	}

	public Resultset getResultset(){
		return resultset;
	}

	public void setStatus(boolean status){
		this.status = status;
	}

	public boolean isStatus(){
		return status;
	}

	public class MasterPartItem{

		@SerializedName("PURPOSE")
		private String pURPOSE;

		@SerializedName("ID")
		private int iD;

		public void setPURPOSE(String pURPOSE){
			this.pURPOSE = pURPOSE;
		}

		public String getPURPOSE(){
			return pURPOSE;
		}

		public void setID(int iD){
			this.iD = iD;
		}

		public int getID(){
			return iD;
		}
	}

	public class Resultset{

		@SerializedName("MasterPart")
		private List<MasterPartItem> masterPart;

		public void setMasterPart(List<MasterPartItem> masterPart){
			this.masterPart = masterPart;
		}

		public List<MasterPartItem> getMasterPart(){
			return masterPart;
		}
	}
}
