package com.textonphoto.texttophoto.quotecreator.pro.adapter.decor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.textonphoto.texttophoto.quotecreator.pro.R;
import com.textonphoto.texttophoto.quotecreator.pro.callback.ICallBackItem;
import com.textonphoto.texttophoto.quotecreator.pro.model.DecorModel;

import java.util.ArrayList;

public class TitleDecorAdapter extends RecyclerView.Adapter<TitleDecorAdapter.TitleDecorHolder> {

    private Context context;
    private ArrayList<DecorModel> lstDecor;
    private ICallBackItem callBack;

    public TitleDecorAdapter(Context context, ICallBackItem callBack) {
        this.context = context;
        this.callBack = callBack;
        lstDecor = new ArrayList<>();
    }

    public void setData(ArrayList<DecorModel> lstDecor) {
        this.lstDecor = lstDecor;
        changeNotify();
    }

    @NonNull
    @Override
    public TitleDecorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TitleDecorHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TitleDecorHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (!lstDecor.isEmpty()) return lstDecor.size();
        return 0;
    }

    class TitleDecorHolder extends RecyclerView.ViewHolder {

        private ImageView ivTitleDecor;

        public TitleDecorHolder(@NonNull View itemView) {
            super(itemView);

            ivTitleDecor = itemView.findViewById(R.id.ivTitleEmoji);
        }

        public void onBind(int position) {
            DecorModel decor = lstDecor.get(position);
            if (decor == null) return;

            if (decor.isSelected())
                ivTitleDecor.setBackgroundResource(R.drawable.border_title);
            else ivTitleDecor.setBackgroundResource(R.color.white);

            Glide.with(context)
                    .load(Uri.parse("file:///android_asset/decor/" + decor.getNameFolder() + "/" + decor.getNameDecor()))
                    .into(ivTitleDecor);

            itemView.setOnClickListener(v -> callBack.callBackItem(decor, position));
        }
    }

    public void setCurrent(int pos) {
        for (int i = 0; i < lstDecor.size(); i++) {
            lstDecor.get(i).setSelected(i == pos);
            notifyItemChanged(i);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void changeNotify() {
        notifyDataSetChanged();
    }
}
