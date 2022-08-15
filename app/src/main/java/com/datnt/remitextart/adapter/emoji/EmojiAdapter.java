package com.datnt.remitextart.adapter.emoji;

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
import com.datnt.remitextart.R;
import com.datnt.remitextart.callback.ICallBackItem;
import com.datnt.remitextart.model.EmojiModel;

import java.util.ArrayList;

public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.EmojiHolder> {

    private Context context;
    private ArrayList<EmojiModel> lstEmoji;
    private ICallBackItem callBack;

    public EmojiAdapter(Context context, ICallBackItem callBack) {
        this.context = context;
        this.callBack = callBack;
    }

    public void setData(ArrayList<EmojiModel> lstEmoji) {
        this.lstEmoji = lstEmoji;
        notifyChange();
    }

    @NonNull
    @Override
    public EmojiHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EmojiHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_emoji, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EmojiHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (!lstEmoji.isEmpty()) return lstEmoji.size();
        return 0;
    }

    class EmojiHolder extends RecyclerView.ViewHolder {

        private final ImageView ivEmoji;

        public EmojiHolder(@NonNull View itemView) {
            super(itemView);

            ivEmoji = itemView.findViewById(R.id.ivEmoji);
        }

        public void onBind(int position) {
            EmojiModel emoji = lstEmoji.get(position);
            if (emoji == null) return;

            Glide.with(context)
                    .load(Uri.parse("file:///android_asset/emoji/" + emoji.getFolder() + "/" + emoji.getNameEmoji()))
                    .into(ivEmoji);

            itemView.setOnClickListener(v ->
                    callBack.callBackItem(emoji, position));
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void notifyChange() {
        notifyDataSetChanged();
    }
}
