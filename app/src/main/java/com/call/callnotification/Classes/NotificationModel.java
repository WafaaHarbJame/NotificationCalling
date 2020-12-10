package com.call.callnotification.Classes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NotificationModel {

        @SerializedName("data")
        @Expose
        private ArrayList<MyNotificationModel> data = null;
        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("message")
        @Expose
        private String message;

        public ArrayList<MyNotificationModel> getData() {
            return data;
        }

        public void setData(ArrayList<MyNotificationModel> data) {
            this.data = data;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

    }
