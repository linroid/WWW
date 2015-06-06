package io.hackathon.www;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hugo.weaving.DebugLog;

/**
 * Created by linroid on 6/6/15.
 */
public class LookFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    @InjectView(R.id.top_list)
    RecyclerView topListRV;
    @InjectView(R.id.timeline_list)
    RecyclerView timelineListRV;

    @InjectView(R.id.refresher)
    SwipeRefreshLayout refreshLayout;

    TopAdapter topAdapter;
    TimelineAdapter timelineAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topAdapter = new TopAdapter();
        timelineAdapter = new TimelineAdapter(getActivity());
        onRefresh();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_look, container, false);
        ButterKnife.inject(this, view);
        RecyclerView.LayoutManager topListLayoutManager = new GridLayoutManager(getActivity(), 2);
        topListRV.setLayoutManager(topListLayoutManager);
        topListRV.setAdapter(topAdapter);
        refreshLayout.setOnRefreshListener(this);

        RecyclerView.LayoutManager timelineLayoutManager = new LinearLayoutManager(getActivity());
        timelineListRV.setLayoutManager(timelineLayoutManager);
        timelineListRV.setAdapter(timelineAdapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);

    }

    public static LookFragment newInstance(){
        LookFragment fragment = new LookFragment();
        return fragment;
    }

    @Override
    public void onRefresh() {
        if(refreshLayout!=null && !refreshLayout.isRefreshing()){
            refreshLayout.setRefreshing(true);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(calendar.getTimeInMillis()-1000*60*60*24);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        AVQuery<AVObject> queryTop = new AVQuery<AVObject>("FireLog");
//        query.whereGreaterThan("createdAt", dateFormat.format(calendar.getTime()));
        queryTop.orderByDescending("count");
        queryTop.setLimit(6);
        queryTop.findInBackground(new FindCallback<AVObject>() {
            @DebugLog
            @Override
            public void done(List<AVObject> list, AVException e) {
                topAdapter.setData(list);
                refreshLayout.setRefreshing(false);
            }
        });

        AVQuery<AVObject> queryTimeline = new AVQuery<AVObject>("FireLog");
        queryTimeline.orderByDescending("createdAt");
        queryTimeline.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                timelineAdapter.setData(list);
                refreshLayout.setRefreshing(false);
            }
        });
    }
}
