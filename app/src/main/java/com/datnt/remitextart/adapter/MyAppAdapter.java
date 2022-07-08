package com.datnt.remitextart.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.datnt.remitextart.R;
import com.datnt.remitextart.callback.ICallBackItem;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class MyAppAdapter extends RecyclerView.Adapter<MyAppAdapter.MyAppHolder> {

    private Context context;
    private ICallBackItem callBack;
    private ArrayList<String> lstPicMyApp;

    public MyAppAdapter(Context context, ICallBackItem callBack) {
        this.context = context;
        this.callBack = callBack;
        lstPicMyApp = new ArrayList<>();
    }

    public void setData(ArrayList<String> lstPicMyApp) {
        this.lstPicMyApp = lstPicMyApp;
        changeNotify();
    }

    @NonNull
    @Override
    public MyAppHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyAppHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picture, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyAppHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (!lstPicMyApp.isEmpty()) return lstPicMyApp.size();
        return 0;
    }

    class MyAppHolder extends RecyclerView.ViewHolder {

        private RoundedImageView ivPic;

        public MyAppHolder(@NonNull View itemView) {
            super(itemView);

            ivPic = itemView.findViewById(R.id.ivPic);
        }

        public void onBind(int position) {
            String pic = lstPicMyApp.get(position);
            if (pic.equals("")) return;

            Glide.with(context)
                    .load(Uri.parse("file:///android_asset/offline_myapp/" + pic))
                    .placeholder(R.drawable.ic_err)
                    .into(ivPic);

            itemView.setOnClickListener(v -> callBack.callBackItem(pic, position));
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void changeNotify() {
        notifyDataSetChanged();
    }
}
