package com.moallem.moallemtask.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.moallem.moallemtask.R;
import com.moallem.moallemtask.VideoActivity;

import java.util.List;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.Holder> {
    public VideosAdapter(Context context, List<String> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    Context context;
    List<String> dataList;
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_list_videos, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int pxWidth = displayMetrics.widthPixels;
        float width = pxWidth / displayMetrics.density;
        ViewGroup.LayoutParams layoutParams = holder.videoImageContainer.getLayoutParams();
        layoutParams.width = (int)( width/1.1);
        holder.videoImageContainer.setLayoutParams(layoutParams);
        Glide.with(context)
                .load(dataList.get(position))
                .placeholder(R.drawable.learn)
                .into(holder.subjectImage);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.subjectImage.setClipToOutline(true);
        }
        holder.videoImageContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, VideoActivity.class);
                intent.putExtra("url", dataList.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        return dataList.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        ImageView subjectImage;
        ConstraintLayout videoImageContainer;

        public Holder(@NonNull View itemView) {
            super(itemView);
            subjectImage=itemView.findViewById(R.id.videoImage);
            videoImageContainer=itemView.findViewById(R.id.videoImageContainer);
        }
    }

}
