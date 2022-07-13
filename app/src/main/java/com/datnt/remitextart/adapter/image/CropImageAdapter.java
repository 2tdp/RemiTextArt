package com.datnt.remitextart.adapter.image;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.datnt.remitextart.R;
import com.datnt.remitextart.callback.ICallBackItem;
import com.datnt.remitextart.customview.CustomViewPathData;

import java.util.ArrayList;
import java.util.List;

public class CropImageAdapter extends RecyclerView.Adapter<CropImageAdapter.CropImageHolder> {

    private Context context;
    private List<String> lstPathData;
    private ICallBackItem callBack;

    public CropImageAdapter(Context context, ICallBackItem callBack) {
        this.context = context;
        this.callBack = callBack;
        lstPathData = new ArrayList<>();
    }

    public void setData(List<String> lstPathData) {
        this.lstPathData = lstPathData;
        changeNotify();
    }

    @NonNull
    @Override
    public CropImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CropImageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_draw_path, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CropImageHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (!lstPathData.isEmpty()) return lstPathData.size();
        return 0;
    }

    class CropImageHolder extends RecyclerView.ViewHolder {

        CustomViewPathData ivPath;

        public CropImageHolder(@NonNull View itemView) {
            super(itemView);

            ivPath = itemView.findViewById(R.id.ivPath);
        }

        public void onBind(int position) {
            String pathData = lstPathData.get(position);
            if (pathData.equals("")) return;

            ArrayList<String> lstPath = new ArrayList<>();
            lstPath.add(pathData);
            ivPath.setDataPath(lstPath, true, false);

            itemView.setOnClickListener(v ->
                    callBack.callBackItem(pathData, position));
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void changeNotify() {
        notifyDataSetChanged();
    }
}
