package com.call.callnotification.ApiHandler;


import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.call.callnotification.Classes.Constants;
import com.call.callnotification.Classes.ResultAPIModel;
import com.call.callnotification.Classes.SharedPManger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.net.NoRouteToHostException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DataFeacher {
    final String TAG = "Log";
    Context activity;
   DataFetcherCallBack dataFetcherCallBack;
    ApiInterface apiService;
    SharedPManger sharedPManger;
    //    int city;
    String accessToken;
    String lang;
    Map<String, Object> headerMap = new HashMap<>();
    private Callback callbackApi;


    public DataFeacher(Activity activity) {
        this.activity = activity;
        apiService = ApiClient.getClient().create(ApiInterface.class);
        this.dataFetcherCallBack = (obj, func, IsSuccess) -> {

        };

        callbackApi = new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

//                T result = response.body();
                if (response.isSuccessful()) {

                    if (dataFetcherCallBack != null)
                        dataFetcherCallBack.Result(response.body(), "getStarPeople", true);

                } else {
                    ResultAPIModel errorModel = null;
                    try {
                        String error = response.errorBody().string();
                        Log.e("Log", "Log error " + error);
                        errorModel = new Gson().fromJson(error, new TypeToken<ResultAPIModel>() {
                        }.getType());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    dataFetcherCallBack.Result(errorModel, Constants.ERROR, false);

                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                t.printStackTrace();
                if ((t instanceof UnknownHostException || t instanceof NoRouteToHostException) && dataFetcherCallBack != null) {
                    dataFetcherCallBack.Result(null, Constants.NO_CONNECTION, false);
                } else {
                    if (dataFetcherCallBack != null)
                        dataFetcherCallBack.Result(null, Constants.FAIL, false);
                }
            }
        };

    }

    public DataFeacher(Context activity, DataFetcherCallBack dataFetcherCallBack) {
        this.activity = activity;
        this.dataFetcherCallBack = dataFetcherCallBack;
        apiService = ApiClient.getClient().create(ApiInterface.class);
        sharedPManger = new SharedPManger(activity);
        headerMap.put("ApiKey", Constants.api_key);
        headerMap.put("Content-Type", "application/json");

        callbackApi = new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                if (response.isSuccessful()) {
                    if (dataFetcherCallBack != null)
                        dataFetcherCallBack.Result(response.body(), "getStarPeople", true);

                } else {
                    ResultAPIModel errorModel = null;
                    try {
                        String error = response.errorBody().string();
                        Log.e("Log", "Log error " + error);
                        errorModel = new Gson().fromJson(error, new TypeToken<ResultAPIModel>() {
                        }.getType());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    dataFetcherCallBack.Result(errorModel, Constants.ERROR, false);

                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                t.printStackTrace();
                if ((t instanceof UnknownHostException || t instanceof NoRouteToHostException) && dataFetcherCallBack != null) {
                    dataFetcherCallBack.Result(null, Constants.NO_CONNECTION, false);
                } else {
                    if (dataFetcherCallBack != null)
                        dataFetcherCallBack.Result(null, Constants.FAIL, false);
                }
            }
        };


    }


    public void NotificationHandle(int last_id) {

        Log.i(TAG, "Log NotificationHandle");
        Log.i(TAG, "Log headerMap " + headerMap);
        Log.i(TAG, "Log last_id " + last_id);
        Call call = apiService.getNotifications(headerMap, last_id);
        call.enqueue(callbackApi);
    }



}
