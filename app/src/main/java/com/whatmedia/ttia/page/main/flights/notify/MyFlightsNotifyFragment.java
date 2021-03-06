package com.whatmedia.ttia.page.main.flights.notify;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.whatmedia.ttia.R;
import com.whatmedia.ttia.component.MyToolbar;
import com.whatmedia.ttia.interfaces.IOnItemClickListener;
import com.whatmedia.ttia.page.BaseFragment;
import com.whatmedia.ttia.page.IActivityTools;
import com.whatmedia.ttia.response.data.ClockData;
import com.whatmedia.ttia.response.GetClockDataResponse;
import com.whatmedia.ttia.response.data.ClockTimeData;
import com.whatmedia.ttia.utility.Preferences;
import com.whatmedia.ttia.utility.Util;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyFlightsNotifyFragment extends BaseFragment implements MyFlightsNotifyContract.View {
    private static final String TAG = MyFlightsNotifyFragment.class.getSimpleName();
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.layout_delete)
    RelativeLayout mLayoutDelete;

    private IActivityTools.ILoadingView mLoadingView;
    private IActivityTools.IMainActivity mMainActivity;
    private MyFlightsNotifyContract.Presenter mPresenter;
    private List<ClockData> mDataList;

    private MyFlightsNotifyRecyclerViewAdapter mAdapter;

    public MyFlightsNotifyFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MyFlightsNotifyFragment newInstance() {
        MyFlightsNotifyFragment fragment = new MyFlightsNotifyFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_flights_notify, container, false);
        ButterKnife.bind(this, view);

        mPresenter = MyFlightsNotifyPresenter.getInstance(getContext(), this);

        mDataList = GetClockDataResponse.newInstance(Preferences.getClockData(getContext()));
        mAdapter = new MyFlightsNotifyRecyclerViewAdapter(getContext(), mDataList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setClickListener(new IOnItemClickListener() {
            @Override
            public void onClick(View view) {
                final ClockData recyclerViewItem = (ClockData) view.getTag();
                Util.showTimePicker(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        HashMap<String, Long> diffTime = Util.getDifferentTimeWithNowTime(hourOfDay + ":" + minute);
                        ClockTimeData clockTimeData = new ClockTimeData();
                        clockTimeData.setHour(diffTime.get(Util.TAG_HOUR));
                        clockTimeData.setMin(diffTime.get(Util.TAG_MIN));
                        clockTimeData.setSec(diffTime.get(Util.TAG_SEC));

                        mDataList = Util.modifyNotification(getContext(), hourOfDay, minute, recyclerViewItem.getId(), mDataList);
                        mAdapter.setData(mDataList);
                    }
                });
            }
        });

        mMainActivity.getMyToolbar().clearState()
                .setTitleText(getString(R.string.title_flight_notify))
                .setBackground(ContextCompat.getColor(getContext(), R.color.colorSubTitle))
                .setBackVisibility(View.VISIBLE)
                .setOnBackClickListener(new MyToolbar.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.imageView_back:
                                mMainActivity.backPress();
                                break;
                        }
                    }
                })
                .setRightSingleIcon(ContextCompat.getDrawable(getContext(), R.drawable.increase))
                .setOnRightSingleClickListener(new MyToolbar.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addNotification();
                    }
                });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        mMainActivity.getMyToolbar().setOnBackClickListener(null);
        super.onDestroy();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
//            mLoadingView = (IActivityTools.ILoadingView) context;
            mMainActivity = (IActivityTools.IMainActivity) context;
        } catch (ClassCastException castException) {
            Log.e(TAG, castException.toString());
            /** The activity does not implement the listener. */
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * Add notification
     */
    private void addNotification() {
        Util.showTimePicker(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (view.isShown()) {
                    mDataList = Util.addNotificationClock(getContext(), hourOfDay, minute);
                    mAdapter.setData(mDataList);
                }
            }
        });
    }

    @OnClick(R.id.layout_delete)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_delete:
                if (mDataList.size() > 0) {
                    new AlertDialog.Builder(getContext())
                            .setTitle(R.string.note)
                            .setMessage(R.string.my_flights_notify_delete_check_message)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mAdapter.setData(null);
                                    mDataList = Util.deleteNotification(getContext(), mDataList);
                                    mAdapter.setData(mDataList);
                                }
                            })
                            .show();
                }
                break;
        }
    }
}

