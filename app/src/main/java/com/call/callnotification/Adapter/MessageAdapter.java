package com.call.callnotification.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.call.callnotification.Classes.MyNotificationModel;
import com.call.callnotification.R;
import com.call.callnotification.databinding.RowMessageBinding;

import java.util.ArrayList;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private Context context;
    private ArrayList<MyNotificationModel> list;

    public MessageAdapter(Context context, ArrayList<MyNotificationModel> list) {
        this.context = context;
        this.list = list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RowMessageBinding itemView = RowMessageBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MyNotificationModel myNotificationModel = list.get(position);

        holder.binding.messageTv.setText(context.getString(R.string.mech)+" "+ myNotificationModel.getMechNo());
        Log.i("tag","Log list "+list.size());


    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RowMessageBinding binding;
        ViewHolder(RowMessageBinding view) {
            super(view.getRoot());
            binding = view;

        }



    }

}
