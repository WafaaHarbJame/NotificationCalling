package com.call.callnotification.Classes;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyNotificationModel {

    @SerializedName("mech_no")
    @Expose
    private Integer mechNo;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("id")
    @Expose
    private Integer id;

    public Integer getMechNo() {
        return mechNo;
    }

    public void setMechNo(Integer mechNo) {
        this.mechNo = mechNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}