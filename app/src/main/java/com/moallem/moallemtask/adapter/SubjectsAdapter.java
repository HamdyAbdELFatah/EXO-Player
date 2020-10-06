package com.moallem.moallemtask.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.moallem.moallemtask.R;
import com.moallem.moallemtask.data.SubjectData;

import java.util.List;

public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.Holder> {
    public SubjectsAdapter(Context context, List<SubjectData> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    Context context;
    List<SubjectData> dataList;
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_list_subjects, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.subjectName.setText(dataList.get(position).getTitle());
        holder.subjectImage.setImageResource(dataList.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        ImageView subjectImage;
        TextView subjectName;

        public Holder(@NonNull View itemView) {
            super(itemView);
            subjectName=itemView.findViewById(R.id.subjectName);
            subjectImage=itemView.findViewById(R.id.subjectImage);
        }
    }
}
