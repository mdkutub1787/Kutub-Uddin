package com.logicsoftbd.lsl.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FinishFabricRollIssueResponses implements Serializable {
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

    public static class Result implements Serializable {
        @Expose
        @SerializedName("msg")
        private String msg;

        @Expose
        @SerializedName("status")
        private String status;



        @Expose
        @SerializedName("Issue No")
        private String IssueNo;

        public String getIssueNo() {
            return IssueNo;
        }

        public void setIssueNo(String issueNo) {
            IssueNo = issueNo;
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





    }


}
