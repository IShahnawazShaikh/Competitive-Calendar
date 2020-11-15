package com.shahnawazshaikh.competitive.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shahnawazshaikh.competitive.R;
import com.shahnawazshaikh.competitive.adapter.RecyclerAdapter;
import com.shahnawazshaikh.competitive.all_interface.RecyclerViewInterFace;
import com.shahnawazshaikh.competitive.models.ContestBean;
import com.shahnawazshaikh.competitive.view_models.FetchData;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;


public class TopCoder extends Fragment implements RecyclerViewInterFace {
    View view;
    RecyclerView recyclerView;
    LinearLayoutManager manager;
    RecyclerAdapter recyclerAdapter;
    List<ContestBean> topcoder;
    ProgressBar progress_bar;
    SwipeRefreshLayout refresh;
    public TopCoder() {
        // Required empty public constructor
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view= inflater.inflate(R.layout.fragment_topcoder, container, false);
        recyclerView=(RecyclerView) view.findViewById(R.id.hackerRank_recycle);
        progress_bar=(ProgressBar) view.findViewById(R.id.progressBar);
        refresh=(SwipeRefreshLayout) view.findViewById(R.id.refresh);
        manager=new LinearLayoutManager(getContext());

        FetchData model = ViewModelProviders.of(this).get(FetchData.class);
        model.getTopcoder(getActivity()).observe(getActivity(), new Observer<List<ContestBean>>() {
            @Override
            public void onChanged(List<ContestBean> contestBeans) {
                progress_bar.setVisibility(View.INVISIBLE);
                topcoder= new ArrayList<ContestBean>(contestBeans);
                recyclerView.setLayoutManager(manager);
                recyclerAdapter =new RecyclerAdapter(contestBeans, TopCoder.this);
                recyclerView.setAdapter(recyclerAdapter);
                recyclerAdapter.notifyDataSetChanged();
            }
        });

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                model.getTopcoder(getActivity()).observe(getActivity(), new Observer<List<ContestBean>>() {
                    @Override
                    public void onChanged(List<ContestBean> contestBeans) {
                        topcoder.clear();
                        topcoder= new ArrayList<ContestBean>(contestBeans);
                        recyclerView.setLayoutManager(manager);
                        recyclerAdapter =new RecyclerAdapter(contestBeans, TopCoder.this);
                        recyclerView.setAdapter(recyclerAdapter);
                        recyclerAdapter.notifyDataSetChanged();
                        refresh.setRefreshing(false);
                    }
                });
            }
        });
       return view;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClicked(int position) {


        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        View view=getLayoutInflater().inflate(R.layout.viewdetails,null);

        final TextView event=view.findViewById(R.id.event);

        final TextView start_date=view.findViewById(R.id.date);
        final TextView start_month_year=view.findViewById(R.id.month_year);
        final TextView start_time=view.findViewById(R.id.time);

        final TextView end_date=view.findViewById(R.id.end_date);
        final TextView end_month_year=view.findViewById(R.id.end_month_year);
        final TextView end_time=view.findViewById(R.id.end_time);
        final TextView link=view.findViewById(R.id.register_link);
        final ImageView share=view.findViewById(R.id.share_content);

        ContestBean obj=topcoder.get(position);

        event.setText(obj.getEvent());

        Instant s = Instant.parse(obj.getStart()+"Z");
        ZoneId.of("Asia/Kolkata");
        LocalDateTime startTime = LocalDateTime.ofInstant(s, ZoneId.of("Asia/Kolkata"));

        start_date.setText(""+startTime.getDayOfMonth());
        start_month_year.setText(""+startTime.getMonthValue()+"-"+startTime.getYear());
        start_time.setText(""+startTime.getHour()+":"+startTime.getMinute());



        s = Instant.parse(obj.getEnd()+"Z");
        ZoneId.of("Asia/Kolkata");
        LocalDateTime endTime = LocalDateTime.ofInstant(s, ZoneId.of("Asia/Kolkata"));


        end_date.setText(""+endTime.getDayOfMonth());
        end_month_year.setText(""+endTime.getMonthValue()+"-"+endTime.getYear());
        end_time.setText(""+endTime.getHour()+":"+endTime.getMinute());


        link.setText(obj.getHref());
        link.setLinkTextColor(Color.parseColor("#7d56b0"));


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean installed=appInstalledOrNot("com.whatsapp");
                String data="data";
                if(installed){
                    Intent intent=new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://api.whatsapp.com/send?&text="+obj.getHref()+"\n\n"+"Hey! Checkout this coding contest\n\n"+"Name: "+obj.getEvent()+"\n\nWebsite: "+obj.getContest_site()+"\n\nStart: "+startTime.getDayOfMonth()+"/"
                            +startTime.getMonth()+"/"+startTime.getYear()+"  "+startTime.getHour()+":"+startTime.getMinute()+"\nEnd: "+endTime.getDayOfMonth()+"/"
                            +endTime.getMonth()+"/"+endTime.getYear()+"  "+endTime.getHour()+":"+endTime.getMinute()+"\n\n\nRegards\nCompetitive Calendar"));
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getContext(),"Whatsapp not installed",Toast.LENGTH_LONG).show();
                }



            }
        });
        builder.setView(view);
        final AlertDialog dialog=builder.create();


        // dialog.setCanceledOnTouchOutside(false);
        //dialog.setCancelable(false);
        dialog.show();
    }

    private boolean appInstalledOrNot(String url) {
        PackageManager packageManager=getContext().getPackageManager();
        boolean installed;
        try{
            packageManager.getPackageInfo(url,PackageManager.GET_ACTIVITIES);
            installed=true;

        }
        catch(PackageManager.NameNotFoundException e){
            installed=false;
        }
        return  installed;
    }

}