package com.datnt.remitextart.adapter.textadapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.datnt.remitextart.R;
import com.datnt.remitextart.callback.ICallBackItem;
import com.datnt.remitextart.model.text.FontModel;
import com.datnt.remitextart.sharepref.DataLocalManager;

import java.util.ArrayList;

public class FontsAdapter extends RecyclerView.Adapter<FontsAdapter.FontHolder> {

    private final Context context;
    private ArrayList<FontModel> lstFont;
    private final ICallBackItem callBack;

    public FontsAdapter(Context context, ICallBackItem callBack) {
        this.context = context;
        this.callBack = callBack;
    }

    public void setData(ArrayList<FontModel> lstFont) {
        this.lstFont = new ArrayList<>(lstFont);
        changeNotify();
    }

    @NonNull
    @Override
    public FontHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FontHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_font, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FontHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (!lstFont.isEmpty()) return lstFont.size();
        return 0;
    }

    class FontHolder extends RecyclerView.ViewHolder {

        private final TextView tvFont;
        private final ImageView ivFavorite;

        public FontHolder(@NonNull View itemView) {
            super(itemView);

            tvFont = itemView.findViewById(R.id.tvFont);
            ivFavorite = itemView.findViewById(R.id.ivFavorite);

        }

        public void onBind(int position) {
            FontModel font = lstFont.get(position);
            if (font == null) return;

            tvFont.setText(font.getNameFont());
            tvFont.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/"
                    + font.getNameFont().toLowerCase() + "/"
                    + font.getNameFont().toLowerCase() + "_"
                    + font.getLstType().get(0).getName().toLowerCase().trim() + ".ttf"));

            if (font.isSelected()) tvFont.setTextColor(context.getResources().getColor(R.color.pink));
            else tvFont.setTextColor(context.getResources().getColor(R.color.black));

            if (checkFavorite(font.getNameFont()))
                ivFavorite.setImageResource(R.drawable.ic_text_font_favorite);
            else ivFavorite.setImageResource(R.drawable.ic_text_font_un_favorite);

            itemView.setOnClickListener(v -> {
                callBack.callBackItem(font, position);
                setCurrent(position, 0);
            });

            ivFavorite.setOnClickListener(v -> {
                setCurrent(position, 1);
            });
        }
    }

    public void setCurrent(int pos, int type) {
        switch (type) {
            case 0:
                for (int i = 0; i < lstFont.size(); i++) {
                    if (i != pos) {
                        if (lstFont.get(i).isSelected()) {
                            lstFont.get(i).setSelected(false);
                            notifyItemChanged(i);
                        }
                    } else {
                        lstFont.get(i).setSelected(true);
                        notifyItemChanged(i);
                    }
                }
                break;
            case 1:
                if (lstFont.get(pos).isFavorite()) {
                    lstFont.get(pos).setFavorite(false);
                    removeFavorite(lstFont.get(pos));
                } else {
                    lstFont.get(pos).setFavorite(true);
                    addFavorite(lstFont.get(pos));
                }
                notifyItemChanged(pos);
                break;
        }
    }

    private boolean checkFavorite(String fontMame) {
        ArrayList<FontModel> lst = DataLocalManager.getListFont("lstFavoriteFont");
        for (FontModel f : lst) {
            if (fontMame.equals(f.getNameFont())) {
                return true;
            }
        }
        return false;
    }

    private void addFavorite(FontModel font) {
        boolean check = false;
        ArrayList<FontModel> lst = DataLocalManager.getListFont("lstFavoriteFont");
        for (FontModel f : lst) {
            if (font.getNameFont().equals(f.getNameFont())) {
                check = true;
                break;
            }
        }
        if (!check) {
            lst.add(font);
        }
        DataLocalManager.setListFont(lst, "lstFavoriteFont");
    }

    private void removeFavorite(FontModel font) {
        int count = 0;
        ArrayList<FontModel> lst = DataLocalManager.getListFont("lstFavoriteFont");
        for (int i = 0; i < lst.size(); i++) {
            if (font.getNameFont().equals(lst.get(i).getNameFont())) {
                count = i;
                break;
            }
        }
        lst.remove(count);
        DataLocalManager.setListFont(lst, "lstFavoriteFont");
    }

    @SuppressLint("NotifyDataSetChanged")
    public void changeNotify() {
        notifyDataSetChanged();
    }
}
