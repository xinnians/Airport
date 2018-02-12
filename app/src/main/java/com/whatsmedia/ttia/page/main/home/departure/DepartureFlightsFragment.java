package com.whatsmedia.ttia.page.main.home.departure;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.whatsmedia.ttia.R;
import com.whatsmedia.ttia.component.MyFlightsDetailInfo;
import com.whatsmedia.ttia.interfaces.IOnItemClickListener;
import com.whatsmedia.ttia.newresponse.data.FlightsListData;
import com.whatsmedia.ttia.page.BaseFragment;
import com.whatsmedia.ttia.page.IActivityTools;
import com.whatsmedia.ttia.page.Page;
import com.whatsmedia.ttia.page.main.flights.my.MyFlightsInfoContract;
import com.whatsmedia.ttia.page.main.flights.result.FlightsSearchResultRecyclerViewAdapter;
import com.whatsmedia.ttia.page.main.home.arrive.ArriveFlightsFragment;
import com.whatsmedia.ttia.response.data.DialogContentData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DepartureFlightsFragment extends BaseFragment implements DepartureFlightsContract.View, IOnItemClickListener {
    private static final String TAG = DepartureFlightsFragment.class.getSimpleName();

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.loadingView)
    ProgressBar mFragmentLoadingView;

    private IActivityTools.ILoadingView mLoadingView;
    private IActivityTools.IMainActivity mMainActivity;
    private DepartureFlightsContract.Presenter mPresenter;

    private FlightsSearchResultRecyclerViewAdapter mAdapter;
    private int mRetryCount = 0;
    private ArriveFlightsFragment.IAPIErrorListener errorListener;


    public DepartureFlightsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DepartureFlightsFragment newInstance() {
        DepartureFlightsFragment fragment = new DepartureFlightsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_info_departure, container, false);
        ButterKnife.bind(this, view);

        mPresenter = new DepartureFlightsPresenter(getContext(), this);

        mFragmentLoadingView.setVisibility(View.VISIBLE);
        mPresenter.getDepartureFlightAPI();

        mAdapter = new FlightsSearchResultRecyclerViewAdapter(getContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setClickListener(this);
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
            mLoadingView = (IActivityTools.ILoadingView) context;
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


    @Override
    public void getDepartureFlightSucceed(final List<FlightsListData> list) {
        if (isAdded() && !isDetached()) {
            mRetryCount = 0;
            mMainActivity.runOnUI(new Runnable() {
                @Override
                public void run() {
                    mFragmentLoadingView.setVisibility(View.GONE);
                    mAdapter.setData(list);
                }
            });
        } else {
            Log.d(TAG, "Fragment is not add");
        }
    }

    @Override
    public void getDepartureFlightFailed(String message, final int status) {
        Log.d(TAG, "getArriveFlightFailed : " + message);
        if (isAdded() && !isDetached()) {

            if (mRetryCount < 5) {
                mRetryCount++;
                mPresenter.getDepartureFlightAPI();
            } else {
                mRetryCount = 0;
                if (errorListener != null) {
                    errorListener.errorStatus(status);
                }
            }
        } else {
            Log.d(TAG, "Fragment is not add");
        }
    }

    @Override
    public void saveMyFlightSucceed(final String message, final String sentJson) {

        mLoadingView.goneLoadingView();
        if (isAdded() && !isDetached()) {
            mMainActivity.runOnUI(new Runnable() {
                @Override
                public void run() {
                    Bundle bundle = new Bundle();
                    bundle.putString(MyFlightsInfoContract.TAG_INSERT, sentJson);
                    mMainActivity.addFragment(Page.TAG_MY_FIGHTS_INFO, bundle, true);
                }
            });
        } else {
            Log.d(TAG, "Fragment is not add");
        }
    }

    @Override
    public void saveMyFlightFailed(final String message, final int status) {

        mLoadingView.goneLoadingView();
        if (isAdded() && !isDetached()) {
            if (errorListener != null) {
                errorListener.errorStatus(status);
            }
        } else {
            Log.d(TAG, "Fragment is not add");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_frame:
                if (mFragmentLoadingView.isShown())
                    return;
                if (view.getTag() instanceof FlightsListData) {
                    final FlightsListData tag = (FlightsListData) view.getTag();
                    mMainActivity.getFlightsDetailInfo()
                            .setTitle(getString(R.string.flight_dialog_title))
                            .setRecyclerContent(MyFlightsDetailInfo.TAG_FLIGHTS_DETAIL, DialogContentData.getFlightDetail(getContext(), tag))
                            .setClickListener(new IOnItemClickListener() {
                                @Override
                                public void onClick(View view) {
                                    switch (view.getId()) {
                                        case R.id.textView_right:
                                            if (tag != null &&
                                                    !TextUtils.isEmpty(tag.getAirlineCode()) &&
                                                    !TextUtils.isEmpty(tag.getShifts()) &&
                                                    !TextUtils.isEmpty(tag.getExpressDate()) &&
                                                    !TextUtils.isEmpty(tag.getExpressTime())) {
                                                mLoadingView.showLoadingView();
                                                mPresenter.saveMyFlightsAPI(tag);
                                            } else {
                                                Log.e(TAG, "view.getTag() content is error");
                                                showMessage(getString(R.string.data_error));
                                            }
                                            mMainActivity.getFlightsDetailInfo().setVisibility(View.GONE);
                                            break;
                                    }
                                }
                            })
                            .show();
                } else {
                    Log.e(TAG, "recycler view.getTag is error");
                    showMessage(getString(R.string.data_error));
                }
        }

    }

    public void setErrorListener(ArriveFlightsFragment.IAPIErrorListener errorListener) {
        this.errorListener = errorListener;
    }
}