package com.datnt.remitextart.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.datnt.remitextart.R;
import com.datnt.remitextart.callback.ICallBackItem;
import com.datnt.remitextart.model.Project;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectHolder> {

    private Context context;
    private final boolean isHomeProject;
    private ArrayList<Project> lstProject;
    private final ICallBackItem callBack;

    public ProjectAdapter(Context context, boolean isHomeProject, ICallBackItem callBack) {
        this.context = context;
        this.isHomeProject = isHomeProject;
        this.callBack = callBack;
        lstProject = new ArrayList<>();
    }

    public void setData(ArrayList<Project> lstProject) {
        this.lstProject = lstProject;
        changeNotify();
    }

    @NonNull
    @Override
    public ProjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProjectHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_template, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (!lstProject.isEmpty()) return lstProject.size();
        return 0;
    }

    class ProjectHolder extends RecyclerView.ViewHolder {

        private final RoundedImageView ivProject;
        private final ImageView ivMore;

        public ProjectHolder(@NonNull View itemView) {
            super(itemView);

            ivProject = itemView.findViewById(R.id.ivTemp);
            ivMore = itemView.findViewById(R.id.ivMore);

            if (isHomeProject) ivMore.setVisibility(View.VISIBLE);
            else ivMore.setVisibility(View.GONE);
        }

        public void onBind(int position) {
            Project project = lstProject.get(position);
            if (project == null) return;

            ivProject.setImageBitmap(BitmapFactory.decodeFile(project.getUriThumb()));

            ivMore.setOnClickListener(v -> {
                Context wrapper = new ContextThemeWrapper(context, R.style.myPopup);
                PopupMenu popup = new PopupMenu(wrapper, ivMore, Gravity.START);

                popup.show();
            });
            itemView.setOnClickListener(v -> callBack.callBackItem(project, position));
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void changeNotify() {
        notifyDataSetChanged();
    }
}
