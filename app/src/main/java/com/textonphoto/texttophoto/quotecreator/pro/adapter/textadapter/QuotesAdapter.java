package com.textonphoto.texttophoto.quotecreator.pro.adapter.textadapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.textonphoto.texttophoto.quotecreator.pro.R;
import com.textonphoto.texttophoto.quotecreator.pro.callback.ICallBackItem;

import java.util.ArrayList;

public class QuotesAdapter extends RecyclerView.Adapter<QuotesAdapter.QuotesHolder> {

    private Context context;
    private ArrayList<String> lstQuotes;
    private ICallBackItem callBack;

    public QuotesAdapter(Context context, ICallBackItem callBack) {
        this.context = context;
        this.callBack = callBack;
        lstQuotes = new ArrayList<>();
    }

    public void setData(ArrayList<String> lstQuotes) {
        this.lstQuotes = lstQuotes;
        changeNotify();
    }

    @NonNull
    @Override
    public QuotesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuotesHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quotes, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull QuotesHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (!lstQuotes.isEmpty()) return lstQuotes.size();
        return 0;
    }

    public class QuotesHolder extends RecyclerView.ViewHolder {

        private final TextView tvQuotes, tvCopy;

        public QuotesHolder(@NonNull View itemView) {
            super(itemView);

            tvQuotes = itemView.findViewById(R.id.tvQuotes);
            tvCopy = itemView.findViewById(R.id.tvCopy);
        }

        private void onBind(int position) {
            String quote = lstQuotes.get(position);
            if (quote.equals("")) return;

            tvQuotes.setText(quote);

            tvCopy.setOnClickListener(v -> callBack.callBackItem(quote, position));
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void changeNotify() {
        notifyDataSetChanged();
    }
}
