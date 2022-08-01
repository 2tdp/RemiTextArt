package com.datnt.remitextart.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.datnt.remitextart.R;
import com.datnt.remitextart.callback.ICallBackItem;
import com.datnt.remitextart.model.Project;
import com.datnt.remitextart.sharepref.DataLocalManager;
import com.datnt.remitextart.utils.Utils;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectHolder> {

    private Context context;
    private final boolean isHomeProject;
    private ArrayList<Project> lstProject;
    private final ICallBackItem callBack;
    private PopupWindow myPopup;

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
        private TextView tvDuplicate, tvDel;

        public ProjectHolder(@NonNull View itemView) {
            super(itemView);

            ivProject = itemView.findViewById(R.id.ivTemp);
            ivMore = itemView.findViewById(R.id.ivMore);

            if (isHomeProject) ivMore.setVisibility(View.VISIBLE);
            else ivMore.setVisibility(View.GONE);

            setPopUpWindow();
        }

        public void onBind(int position) {
            Project project = lstProject.get(position);
            if (project == null) return;

            ivProject.setImageBitmap(BitmapFactory.decodeFile(project.getUriThumb()));

            ivMore.setOnClickListener(v -> myPopup.showAsDropDown(v, -155, 0));
            tvDuplicate.setOnClickListener(v -> {
                Project p = new Project(project);
                lstProject.add(p);
                DataLocalManager.setListProject(context, lstProject, Utils.LIST_PROJECT);
                changeNotify();
                myPopup.dismiss();
            });
            tvDel.setOnClickListener(v -> {
                lstProject.remove(position);
                DataLocalManager.setListProject(context, lstProject, Utils.LIST_PROJECT);
                changeNotify();
                myPopup.dismiss();
            });
            itemView.setOnClickListener(v -> callBack.callBackItem(project, position));
        }

        private void setPopUpWindow() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams")
            View view = inflater.inflate(R.layout.pop_up_project, null);

            tvDuplicate = view.findViewById(R.id.duplicateProject);
            tvDel = view.findViewById(R.id.delProject);

            myPopup = new PopupWindow(view, 255, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void changeNotify() {
        notifyDataSetChanged();
    }
}
