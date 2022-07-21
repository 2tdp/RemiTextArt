package com.datnt.remitextart.adapter.decor;

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
import com.datnt.remitextart.model.DecorModel;

import java.util.ArrayList;

public class DecorAdapter extends RecyclerView.Adapter<DecorAdapter.DecorHolder> {

    private Context context;
    private ArrayList<DecorModel> lstDecor;
    private ICallBackItem callBack;

    public DecorAdapter(Context context, ICallBackItem callBack) {
        this.context = context;
        this.callBack = callBack;
    }

    public void setData(ArrayList<DecorModel> lstDecor) {
        this.lstDecor = lstDecor;
        notifyChange();
    }

    @NonNull
    @Override
    public DecorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DecorHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_draw_path, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DecorHolder holder, int position) {
        holder.onBind(position);

    }

    @Override
    public int getItemCount() {
        if (!lstDecor.isEmpty()) return lstDecor.size();
        return 0;
    }

    class DecorHolder extends RecyclerView.ViewHolder {

        private CustomViewPathData ivPath;

        public DecorHolder(@NonNull View itemView) {
            super(itemView);

            ivPath = itemView.findViewById(R.id.ivPath);
        }

        public void onBind(int position) {
            DecorModel decor = lstDecor.get(position);
            if (decor == null) return;

            ivPath.setDataPath(decor.getLstPathData(), true, false);

            itemView.setOnClickListener(v -> callBack.callBackItem(decor, position));
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void notifyChange() {
        notifyDataSetChanged();
    }
}
