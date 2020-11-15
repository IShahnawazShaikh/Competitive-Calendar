package com.shahnawazshaikh.competitive.adapter;

import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.shahnawazshaikh.competitive.R;
import com.shahnawazshaikh.competitive.all_interface.RecyclerViewInterFace;
import com.shahnawazshaikh.competitive.models.ContestBean;
import com.squareup.picasso.Picasso;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.VHolder> {
    @NonNull
   List<ContestBean> list;
//
//    ONNoteListener mOnNotelistener;
    RecyclerViewInterFace recyclerViewInterFace;
    public RecyclerAdapter(@NonNull List<ContestBean> data, RecyclerViewInterFace recyclerViewInterFace) {
        this.list = data;
        this.recyclerViewInterFace=recyclerViewInterFace;

    }
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater= LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.recyclecontent,parent,false);
        return new VHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull VHolder holder, int position) {

        ContestBean contestBean= list.get(position);
        holder.event.setText(contestBean.getEvent());
        Picasso.get().load("https://clist.by"+contestBean.getIcon()).into(holder.icon);

        Instant s = Instant.parse((contestBean.getStart()+"Z"));
        ZoneId.of("Asia/Kolkata");
        LocalDateTime startTime = LocalDateTime.ofInstant(s, ZoneId.of("Asia/Kolkata"));
        holder.start.setText(startTime.getHour()+":"+startTime.getMinute());
        holder.start_date.setText(startTime.getDayOfMonth()+"-"+startTime.getMonth()+"-"+startTime.getYear());

        s = Instant.parse(contestBean.getEnd()+"Z");
        ZoneId.of("Asia/Kolkata");
        LocalDateTime endTime = LocalDateTime.ofInstant(s, ZoneId.of("Asia/Kolkata"));

        holder.end.setText(endTime.getHour()+":"+endTime.getMinute());
        holder.end_date.setText(endTime.getDayOfMonth()+"-"+endTime.getMonth()+"-"+endTime.getYear());

        Calendar cal1=Calendar.getInstance();
        Calendar cal_start=Calendar.getInstance();
        Calendar cal_end=Calendar.getInstance();

        cal_start.clear();
        cal_start.set(startTime.getYear(), startTime.getMonthValue()-1, startTime.getDayOfMonth(),
                startTime.getHour(), startTime.getMinute(), startTime.getSecond());

        cal_end.clear();
        cal_end.set(endTime.getYear(), endTime.getMonthValue()-1, endTime.getDayOfMonth(),
                endTime.getHour(), endTime.getMinute(), endTime.getSecond());

        if(cal1.compareTo(cal_start)>=0 && cal_end.compareTo(cal1)>=0){
            holder.status.setText("Running");
            holder.status.setTextColor(Color.parseColor("#7d56b0"));
            contestBean.setStatus("Running");
        }
        else if(cal_start.compareTo(cal1)>0){
            holder.status.setText("Upcomming");
            holder.status.setTextColor(Color.RED);
            contestBean.setStatus("Upcomming");
        }
        else{
            holder.status.setText("Contest end");
            holder.status.setTextColor(Color.RED);
            contestBean.setStatus("End");
        }
}

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class VHolder extends RecyclerView.ViewHolder  {
        ImageView icon;
        TextView event,start,end,start_date,end_date,status;
        public VHolder(@NonNull View itemView) {
            super(itemView);
             icon=(ImageView) itemView.findViewById(R.id.icon);
             event=(TextView) itemView.findViewById(R.id.event);
            start=(TextView) itemView.findViewById(R.id.start);
            start_date=(TextView) itemView.findViewById(R.id.start_date);
            end=(TextView) itemView.findViewById(R.id.end);
            end_date=(TextView) itemView.findViewById(R.id.end_date);
            status=(TextView) itemView.findViewById(R.id.status);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                      recyclerViewInterFace.onItemClicked(getAdapterPosition());
                }
            });
         }
    }


}
