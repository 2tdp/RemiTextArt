package com.datnt.remitextart.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.datnt.remitextart.R;
import com.datnt.remitextart.callback.ICallBackItem;
import com.datnt.remitextart.model.picture.PicModel;

import java.util.ArrayList;

public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.RecentHolder> {

    private Context context;
    private ArrayList<PicModel> lstPic;
    private ICallBackItem callBack;

    public RecentAdapter(Context context, ICallBackItem callBack) {
        this.context = context;
        this.callBack = callBack;
        lstPic = new ArrayList<>();
    }

    public void setData(ArrayList<PicModel> lstPic) {
        this.lstPic = lstPic;
        notifyChange();
    }

    @NonNull
    @Override
    public RecentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecentHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picture, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecentHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (!lstPic.isEmpty()) return lstPic.size();
        return 0;
    }

    class RecentHolder extends RecyclerView.ViewHolder {

        ImageView ivPic;

        public RecentHolder(@NonNull View itemView) {
            super(itemView);

            ivPic = itemView.findViewById(R.id.ivPic);
        }

        public void onBind(int position) {
            PicModel picModel = lstPic.get(position);
            if (picModel == null) return;

            itemView.setOnClickListener(v -> callBack.callBackItem(picModel, position));

            Glide.with(context)
                    .load(picModel.getUri())
                    .placeholder(R.drawable.ic_err)
                    .into(ivPic);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void notifyChange() {
        notifyDataSetChanged();
    }
}
