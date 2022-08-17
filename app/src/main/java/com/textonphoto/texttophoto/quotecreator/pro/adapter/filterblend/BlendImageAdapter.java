package com.textonphoto.texttophoto.quotecreator.pro.adapter.filterblend;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.textonphoto.texttophoto.quotecreator.pro.R;
import com.textonphoto.texttophoto.quotecreator.pro.callback.ICallBackItem;
import com.textonphoto.texttophoto.quotecreator.pro.model.BlendModel;

import java.util.ArrayList;

public class BlendImageAdapter extends RecyclerView.Adapter<BlendImageAdapter.FilterImageHolder> {

    private Context context;
    private ArrayList<BlendModel> lstBlend;
    private final ICallBackItem callBack;

    public BlendImageAdapter(Context context, ICallBackItem callBack) {
        this.context = context;
        this.callBack = callBack;
        lstBlend = new ArrayList<>();
    }

    public void setData(ArrayList<BlendModel> lstFilter) {
        this.lstBlend = new ArrayList<>(lstFilter);
        changeNotify();
    }

    @NonNull
    @Override
    public FilterImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FilterImageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_filter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FilterImageHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (!lstBlend.isEmpty()) return lstBlend.size();
        return 0;
    }

    class FilterImageHolder extends RecyclerView.ViewHolder {

        private final RoundedImageView ivPic;
        private final TextView tvPic;

        public FilterImageHolder(@NonNull View itemView) {
            super(itemView);
            ivPic = itemView.findViewById(R.id.ivPic);
            tvPic = itemView.findViewById(R.id.tvPic);
        }

        public void onBind(int position) {
            BlendModel blendModel = lstBlend.get(position);
            if (blendModel == null) return;

            if (blendModel.isCheck()) ivPic.setBorderColor(context.getResources().getColor(R.color.pink));
            else ivPic.setBorderColor(context.getResources().getColor(R.color.white));

            if (position == 0) ivPic.setImageResource(R.drawable.ic_none);
            else ivPic.setImageBitmap(blendModel.getBitmap());

            tvPic.setText(blendModel.getNameBlend());

            itemView.setOnClickListener(v -> callBack.callBackItem(blendModel, position));
        }
    }

    public void setCurrent(int pos) {
        for (int i = 0; i < lstBlend.size(); i++) {
            lstBlend.get(i).setCheck(i == pos);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void changeNotify() {
        notifyDataSetChanged();
    }
}
