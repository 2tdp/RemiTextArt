package com.textonphoto.texttophoto.quotecreator.pro.adapter.textadapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.textonphoto.texttophoto.quotecreator.pro.R;
import com.textonphoto.texttophoto.quotecreator.pro.callback.ICallBackItem;
import com.textonphoto.texttophoto.quotecreator.pro.model.text.QuoteModel;

import java.util.ArrayList;

public class TitleQuotesAdapter extends RecyclerView.Adapter<TitleQuotesAdapter.TitleQuoteHolder> {

    private Context context;
    private ArrayList<QuoteModel> lstTitleQuotes;
    private ICallBackItem callBack;

    public TitleQuotesAdapter(Context context, ICallBackItem callBack) {
        this.context = context;
        this.callBack = callBack;
        lstTitleQuotes = new ArrayList<>();
    }

    public void setData(ArrayList<QuoteModel> lstTitleQuotes) {
        this.lstTitleQuotes = lstTitleQuotes;
        changeNotify();
    }

    @NonNull
    @Override
    public TitleQuoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TitleQuoteHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title_quotes, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TitleQuoteHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (!lstTitleQuotes.isEmpty()) return lstTitleQuotes.size();
        return 0;
    }

    class TitleQuoteHolder extends RecyclerView.ViewHolder {

        TextView tvTitleQuote;

        public TitleQuoteHolder(@NonNull View itemView) {
            super(itemView);
            tvTitleQuote = itemView.findViewById(R.id.tvTitleQuote);
        }

        private void onBind(int position) {
            QuoteModel quoteModel = lstTitleQuotes.get(position);
            if (quoteModel == null) return;

            tvTitleQuote.setText(quoteModel.getTypeQuote().replace("quotes_", "").replace(".json", ""));
            if (quoteModel.isSelected()) {
                tvTitleQuote.setBackgroundResource(R.drawable.border_click_font);
                tvTitleQuote.setTextColor(Color.WHITE);
            } else {
                tvTitleQuote.setBackgroundResource(R.drawable.border_unclick_font);
                tvTitleQuote.setTextColor(Color.BLACK);
            }

            tvTitleQuote.setOnClickListener(v -> callBack.callBackItem(quoteModel, position));
        }
    }

    public void setCurrent(int position) {
        for (int i = 0; i < lstTitleQuotes.size(); i++) {
            lstTitleQuotes.get(i).setSelected(i == position);
        }
        changeNotify();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void changeNotify() {
        notifyDataSetChanged();
    }
}
