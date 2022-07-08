package com.datnt.remitextart.adapter.textadapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.datnt.remitextart.R;
import com.datnt.remitextart.callback.ICallBackItem;
import com.datnt.remitextart.model.text.TypeFontModel;

import java.util.ArrayList;

public class TypeFontAdapter extends RecyclerView.Adapter<TypeFontAdapter.StyleFontHolder> {

    private final Context context;
    private ArrayList<TypeFontModel> lstTypeFont;
    private final ICallBackItem callBack;

    public TypeFontAdapter(Context context, ICallBackItem callBack) {
        this.context = context;
        this.callBack = callBack;
        lstTypeFont = new ArrayList<>();
    }

    public void setData(ArrayList<TypeFontModel> lstTypeFont) {
        this.lstTypeFont = lstTypeFont;
        notifyChange();
    }

    @NonNull
    @Override
    public StyleFontHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StyleFontHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_font, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StyleFontHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (!lstTypeFont.isEmpty()) return lstTypeFont.size();
        return 0;
    }

    class StyleFontHolder extends RecyclerView.ViewHolder {

        private final TextView tvStyleFont;

        public StyleFontHolder(@NonNull View itemView) {
            super(itemView);

            tvStyleFont = itemView.findViewById(R.id.tvFont);
            itemView.findViewById(R.id.ivFavorite).setVisibility(View.INVISIBLE);
        }

        public void onBind(int position) {
            TypeFontModel styleFont = lstTypeFont.get(position);
            if (styleFont == null) return;

            tvStyleFont.setText(styleFont.getName());
            tvStyleFont.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/"
                    + styleFont.getFont().toLowerCase() + "/"
                    + styleFont.getFont().toLowerCase() + "_" + styleFont.getName().toLowerCase().trim().replaceAll(" ", "_") + ".ttf"));
            if (styleFont.isSelected())
                tvStyleFont.setTextColor(context.getResources().getColor(R.color.pink));
            else tvStyleFont.setTextColor(context.getResources().getColor(R.color.black));

            itemView.setOnClickListener(v -> {
                callBack.callBackItem(styleFont, position);
                setCurrent(position);
            });

        }
    }

    public void setCurrent(int pos) {
        for (int i = 0; i < lstTypeFont.size(); i++) {
            if (i != pos) {
                if (lstTypeFont.get(i).isSelected()) {
                    lstTypeFont.get(i).setSelected(false);
                    notifyItemChanged(i);
                }
            } else {
                lstTypeFont.get(i).setSelected(true);
                notifyItemChanged(i);
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void notifyChange() {
        notifyDataSetChanged();
    }
}
