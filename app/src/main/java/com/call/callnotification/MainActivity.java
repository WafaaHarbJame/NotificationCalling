package com.call.callnotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.call.callnotification.Adapter.MessageAdapter;
import com.call.callnotification.ApiHandler.DataFeacher;
import com.call.callnotification.Classes.Constants;
import com.call.callnotification.Classes.GlobalData;
import com.call.callnotification.Classes.Notification;
import com.call.callnotification.Classes.NotificationModel;
import com.call.callnotification.Classes.SharedPManger;
import com.call.callnotification.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActivityBase {
    ActivityMainBinding binding;
    ArrayList<Notification> list;
    LinearLayoutManager linearLayoutManager;
    MessageAdapter messageAdapter;
    SharedPManger sharedPManger;
    int last_id;
    boolean isRefersh= false;
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent){
            if (intent.getAction().equals(Constants.newMessage)) {
                isRefersh=true;
              getList();

            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
        list=new ArrayList<>();
        sharedPManger=new SharedPManger(getActiviy());
        last_id=sharedPManger.getDataInt(Constants.last_id,0);

        linearLayoutManager=new LinearLayoutManager(getActiviy());
        binding.rv.setLayoutManager(linearLayoutManager);
        getList();
        startService(new Intent(this, MessageService.class));

        binding.refresh.setOnRefreshListener(() -> {
            isRefersh=true;
            list.clear();
            binding.rv.setVisibility(View.GONE);
            getList();


        });


    }

    private void initAdapter() {
        messageAdapter=new MessageAdapter(getActiviy(),list);
        binding.rv.setAdapter(messageAdapter);

    }
     void getList() {
         if(isRefersh){
             binding.refresh.setRefreshing(true);
         }
         else {
             GlobalData.progressDialog(getActiviy(),R.string.upload_date,R.string.please_wait_upload);
         }
        new DataFeacher(getActiviy(), (obj, func, IsSuccess) -> {
            GlobalData.hideProgressDialog();
            NotificationModel result = (NotificationModel) obj;
            if (func.equals(Constants.ERROR)) {
                String message = getString(R.string.fail_to_get_data);
                if (result != null && result.getMessage() != null) {
                    message = result.getMessage();
                }
                binding.refresh.setRefreshing(false);
                binding.rv.setVisibility(View.GONE);
                binding.noData.setVisibility(View.VISIBLE);
                binding.noData.setText(message);


            } else {

                if (IsSuccess) {
                    binding.refresh.setRefreshing(false);
                    binding.rv.setVisibility(View.VISIBLE);
                    binding.noData.setVisibility(View.GONE);
                    list = result.getData();
//                    Log.i("tag","Log list "+list.size());
//                    if(list.size()==0){
//                        binding.noData.setVisibility(View.VISIBLE);
//                        binding.rv.setVisibility(View.GONE);
//                    }
//                    else {
//                        binding.noData.setVisibility(View.GONE);
//                        binding.rv.setVisibility(View.VISIBLE);
//                        initAdapter();
//
//                    }
                    list.add(new Notification(1,"message1","",200));
                    list.add(new Notification(2,"message2","",200));
                    list.add(new Notification(3,"message3","",200));
                    list.add(new Notification(4,"message4","",200));
                    sharedPManger.SetData(Constants.last_id,list.get(list.size()-1).getId());
                    initAdapter();
                    Log.i("tag","Log last id Main  "+list.get(list.size()-1).getId());


                } else {
                    Toast(getString(R.string.fail_to_get_data));
                    binding.refresh.setRefreshing(false);
                    binding.noData.setVisibility(View.VISIBLE);
                    binding.noData.setText(getString(R.string.fail_to_get_data));
                    binding.rv.setVisibility(View.GONE);

                }
            }


        }).NotificationHandle(last_id);

         binding.refresh.setRefreshing(false);


     }
    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(Constants.newMessage);
       registerReceiver(broadcastReceiver,filter);

    }


    @Override
    public void onPause() {
        super.onPause();

        if (broadcastReceiver != null) {
           unregisterReceiver(broadcastReceiver);
        }


    }

}