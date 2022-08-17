package com.textonphoto.texttophoto.quotecreator.pro.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.textonphoto.texttophoto.quotecreator.pro.R;
import com.textonphoto.texttophoto.quotecreator.pro.callback.ICallBackItem;
import com.textonphoto.texttophoto.quotecreator.pro.model.OverlayModel;

import java.util.ArrayList;

public class OverlayAdapter extends RecyclerView.Adapter<OverlayAdapter.OverlayHolder> {

    private Context context;
    private ArrayList<OverlayModel> lstOverlay;
    private ICallBackItem callBack;

    public OverlayAdapter(Context context, ICallBackItem callBack) {
        this.context = context;
        this.callBack = callBack;
        lstOverlay = new ArrayList<>();
    }

    public void setData(ArrayList<OverlayModel> lstOverlay) {
        this.lstOverlay = lstOverlay;
        changeNotify();
    }

    @NonNull
    @Override
    public OverlayHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OverlayHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picture, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OverlayHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (!lstOverlay.isEmpty()) return lstOverlay.size();
        return 0;
    }

    class OverlayHolder extends RecyclerView.ViewHolder {

        private RoundedImageView ivOverlay;

        public OverlayHolder(@NonNull View itemView) {
            super(itemView);

            ivOverlay = itemView.findViewById(R.id.ivPic);
        }

        public void onBind(int position) {
            OverlayModel overlay = lstOverlay.get(position);
            if (overlay == null) return;

            Glide.with(context)
                    .load(Uri.parse("file:///android_asset/overlay/" + overlay.getNameOverlay()))
                    .into(ivOverlay);

            itemView.setOnClickListener(v -> callBack.callBackItem(overlay, position));
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void changeNotify() {
        notifyDataSetChanged();
    }
}
