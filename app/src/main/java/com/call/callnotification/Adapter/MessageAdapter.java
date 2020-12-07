package com.call.callnotification.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.call.callnotification.Classes.Notification;
import com.call.callnotification.databinding.RowMessageBinding;

import java.util.ArrayList;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Notification> list;

    public MessageAdapter(Context context, ArrayList<Notification> list) {
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
        Notification notification = list.get(position);

        holder.binding.messageTv.setText(notification.getMessage());

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
