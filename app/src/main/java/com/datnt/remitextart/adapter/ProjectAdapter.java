package com.datnt.remitextart.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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
    private ArrayList<Project> lstProject;
    private final ICallBackItem callBack;
    private final int layoutResource;
    private PopupWindow myPopup;

    public ProjectAdapter(Context context, int layoutResource, ICallBackItem callBack) {
        this.context = context;
        this.layoutResource = layoutResource;
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
        return new ProjectHolder(LayoutInflater.from(parent.getContext()).inflate(layoutResource, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectHolder holder, int position) {
        holder.onBind(position);
        if (holder.ivMore != null)
            holder.ivMore.setOnClickListener(v -> {
                holder.setPopUpWindow(position);
                myPopup.showAsDropDown(v, (int) (context.getResources().getDimension(com.intuit.sdp.R.dimen._60sdp)) * -1,
                        (int) (context.getResources().getDimension(com.intuit.sdp.R.dimen._10sdp)) * -1);
            });
    }

    @Override
    public int getItemCount() {
        if (!lstProject.isEmpty()) return lstProject.size();
        return 0;
    }

    class ProjectHolder extends RecyclerView.ViewHolder {

        private final RoundedImageView ivProject;
        private ImageView ivMore;

        public ProjectHolder(@NonNull View itemView) {
            super(itemView);

            if (layoutResource == R.layout.item_template) {
                ivProject = itemView.findViewById(R.id.ivTemp);
                ivMore = itemView.findViewById(R.id.ivMore);
            } else ivProject = itemView.findViewById(R.id.ivProject);
        }

        public void onBind(int position) {
            Project project = lstProject.get(position);
            if (project == null) return;

            ivProject.setImageBitmap(BitmapFactory.decodeFile(project.getUriThumb()));

            itemView.setOnClickListener(v -> {
                 callBack.callBackItem(project, position);
            });
        }

        private void setPopUpWindow(int position) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams")
            View view = inflater.inflate(R.layout.pop_up_project, null);

            TextView tvDuplicate = view.findViewById(R.id.duplicateProject);
            TextView tvDel = view.findViewById(R.id.delProject);

            myPopup = new PopupWindow(view, (int) (context.getResources().getDimension(com.intuit.sdp.R.dimen._100sdp)), LinearLayout.LayoutParams.WRAP_CONTENT, true);

            tvDuplicate.setOnClickListener(v -> {
                editListProject(position, 1);
                myPopup.dismiss();
            });
            tvDel.setOnClickListener(v -> {
                editListProject(position, 0);
                myPopup.dismiss();
            });
        }

        private void editListProject(int index, int numCase) {
            switch (numCase) {
                case 0:
                    lstProject.remove(index);
                    break;
                case 1:
                    Project project = new Project(lstProject.get(index));
                    lstProject.add(0, project);
                    break;
            }
            DataLocalManager.setListProject(context, lstProject, Utils.LIST_PROJECT);
            changeNotify();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void changeNotify() {
        notifyDataSetChanged();
    }
}
