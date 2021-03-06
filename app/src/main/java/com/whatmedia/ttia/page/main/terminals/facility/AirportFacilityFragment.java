package com.whatmedia.ttia.page.main.terminals.facility;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whatmedia.ttia.R;
import com.whatmedia.ttia.interfaces.IOnItemClickListener;
import com.whatmedia.ttia.page.BaseFragment;
import com.whatmedia.ttia.page.IActivityTools;
import com.whatmedia.ttia.page.Page;
import com.whatmedia.ttia.page.main.terminals.facility.detail.FacilityDetailContract;
import com.whatmedia.ttia.response.data.AirportFacilityData;
import com.whatmedia.ttia.utility.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AirportFacilityFragment extends BaseFragment implements AirportFacilityContract.View, IOnItemClickListener, ViewPager.OnPageChangeListener {
    private static final String TAG = AirportFacilityFragment.class.getSimpleName();
    @BindView(R.id.textView_subtitle)
    TextView mTextViewSubtitle;
    @BindView(R.id.imageView_left)
    ImageView mImageViewLeft;
    @BindView(R.id.imageView_right)
    ImageView mImageViewRight;
    //    @BindView(R.id.recyclerView)
//    RecyclerView mRecyclerView;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;


    private IActivityTools.ILoadingView mLoadingView;
    private IActivityTools.IMainActivity mMainActivity;
    private AirportFacilityContract.Presenter mPresenter;

    //    private AirportFacilityRecyclerViewAdapter mAdapter;
    private AirportFacilityViewPagerAdapter mViewPagerAdapter = new AirportFacilityViewPagerAdapter();

    public AirportFacilityFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AirportFacilityFragment newInstance() {
        AirportFacilityFragment fragment = new AirportFacilityFragment();
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
        View view = inflater.inflate(R.layout.fragment_airport_facility, container, false);
        ButterKnife.bind(this, view);

        mPresenter = AirportFacilityPresenter.getInstance(getContext(), this);
        mLoadingView.showLoadingView();
        mPresenter.getAirportFacilityAPI();

        mTextViewSubtitle.setText(getString(R.string.terminal_1));

        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPagerAdapter.setClickListener(this);
        mViewPager.addOnPageChangeListener(this);

//        mAdapter = new AirportFacilityRecyclerViewAdapter(getContext());
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        mRecyclerView.setAdapter(mAdapter);
//        mAdapter.setOnclickListener(this);

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
    public void getAirportFacilitySucceed(final List<AirportFacilityData> response) {
        mLoadingView.goneLoadingView();
        if (isAdded() && !isDetached()) {
            mMainActivity.runOnUI(new Runnable() {
                @Override
                public void run() {
                    mViewPagerAdapter.setData(response);
//                mTextViewSubtitle.setText(mAdapter.setData(response));
                }
            });
        } else {
            Log.d(TAG, "Fragment is not add");
        }
    }

    @Override
    public void getAirportFacilityFailed(String message, boolean timeout) {
        mLoadingView.goneLoadingView();
        if (timeout) {
            mMainActivity.runOnUI(new Runnable() {
                @Override
                public void run() {
                    Util.showTimeoutDialog(getContext());
                }
            });
        }
    }

    @OnClick({R.id.imageView_left, R.id.imageView_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView_left:
                setLeft();
                mViewPager.setCurrentItem(0, true);
                break;
            case R.id.imageView_right:
                setRight();
                mViewPager.setCurrentItem(1, true);
                break;
            case R.id.imageView_picture:
                if (view.getTag() != null && view.getTag() instanceof AirportFacilityData) {
                    AirportFacilityData facilityData = (AirportFacilityData) view.getTag();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(FacilityDetailContract.TAG_DATA, facilityData);
                    mMainActivity.addFragment(Page.TAG_AIRPORT_FACILITY_DETAIL, bundle, true);
                } else {
                    Log.e(TAG, "View.getTag() is error");
                    showMessage(getString(R.string.data_error));
                }
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            setLeft();
        } else {
            setRight();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void setLeft() {
        mImageViewLeft.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.left_on));
        mImageViewRight.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.right_off));
        mTextViewSubtitle.setText(getString(R.string.terminal_1));

    }

    private void setRight() {
        mImageViewLeft.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.left_off));
        mImageViewRight.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.right_on));
        mTextViewSubtitle.setText(getString(R.string.terminal_2));
    }
}
