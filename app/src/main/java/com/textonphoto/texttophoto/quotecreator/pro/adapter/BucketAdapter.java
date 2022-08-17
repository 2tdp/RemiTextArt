package com.textonphoto.texttophoto.quotecreator.pro.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.textonphoto.texttophoto.quotecreator.pro.R;
import com.textonphoto.texttophoto.quotecreator.pro.callback.ICallBackItem;
import com.textonphoto.texttophoto.quotecreator.pro.model.picture.BucketPicModel;

import java.util.ArrayList;

public class BucketAdapter extends RecyclerView.Adapter<BucketAdapter.BucketHolder> {

    private Context context;
    private ArrayList<BucketPicModel> lstBucket;
    private ICallBackItem callBack;

    public BucketAdapter(Context context, ICallBackItem callBack) {
        this.context = context;
        this.callBack = callBack;
    }

    public void setData(ArrayList<BucketPicModel> lstBucket) {
        this.lstBucket = new ArrayList<>(lstBucket);
        notifyChange();
    }

    @NonNull
    @Override
    public BucketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BucketHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bucket_pic, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BucketHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (!lstBucket.isEmpty()) return lstBucket.size();
        return 0;
    }

    class BucketHolder extends RecyclerView.ViewHolder {

        ImageView ivBucket;
        TextView tvName, tvQuantity;

        public BucketHolder(@NonNull View itemView) {
            super(itemView);

            ivBucket = itemView.findViewById(R.id.ivBucket);
            tvName = itemView.findViewById(R.id.tvNameBucket);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
        }

        public void onBind(int position) {
            BucketPicModel bucket = lstBucket.get(position);
            if (bucket == null) return;

            itemView.setOnClickListener(v -> callBack.callBackItem(bucket, position));

            int index = 0;
            Glide.with(context)
                    .load(Uri.parse(bucket.getLstPic().get(index).getUri()))
                    .placeholder(R.drawable.ic_err)
                    .skipMemoryCache(false)
                    .into(ivBucket);

            tvName.setText(bucket.getBucket());
            tvQuantity.setText(String.valueOf(bucket.getLstPic().size()));
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void notifyChange() {
        notifyDataSetChanged();
    }
}
