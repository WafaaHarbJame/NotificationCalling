package com.call.callnotification.ApiHandler;


import com.call.callnotification.Classes.NotificationModel;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("v1/Notification")
    Call<NotificationModel> getNotifications(@HeaderMap() Map<String, Object> headerParams, @Query("last_id") int last_id);

}

