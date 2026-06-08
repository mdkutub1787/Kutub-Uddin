/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.logicsoftbd.lsl.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by janisharali on 28/01/17.
 */

public class StoreResponse implements Serializable {

    private boolean isFirst = true;

    @Expose
    @SerializedName("status")
    private String status;


    @Expose
    @SerializedName("resultset")
    private Challan data;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public Challan getData() {
        return data;
    }

    public void setData(Challan data) {
        this.data = data;
    }

    public static class Challan implements Serializable{


        @Expose
        @SerializedName("status")
        private String status;

        @Expose
        @SerializedName("MasterPart")
        private List<MasterPart> masterPart;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<MasterPart> getMasterPart() {
            return masterPart;
        }

        public void setMasterPart(List<MasterPart> masterPart) {
            this.masterPart = masterPart;
        }

        public static class MasterPart implements Serializable {
            @Expose
            @SerializedName("ID")
            private Integer id;

            @Expose
            @SerializedName("STORE_NAME")
            private String storeName;

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public String getStoreName() {
                return storeName;
            }

            public void setStoreName(String storeName) {
                this.storeName = storeName;
            }
        }


    }
}
