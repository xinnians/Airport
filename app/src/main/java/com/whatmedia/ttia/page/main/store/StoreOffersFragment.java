package com.whatmedia.ttia.page.main.store;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whatmedia.ttia.R;
import com.whatmedia.ttia.enums.FlightInfo;
import com.whatmedia.ttia.enums.StoreOffers;
import com.whatmedia.ttia.interfaces.IOnItemClickListener;
import com.whatmedia.ttia.page.BaseFragment;
import com.whatmedia.ttia.page.IActivityTools;
import com.whatmedia.ttia.page.Page;
import com.whatmedia.ttia.page.main.terminals.store.search.StoreSearchContract;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StoreOffersFragment extends BaseFragment implements StoreOffersContract.View, IOnItemClickListener {
    private static final String TAG = StoreOffersFragment.class.getSimpleName();

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private IActivityTools.ILoadingView mLoadingView;
    private IActivityTools.IMainActivity mMainActivity;
    private StoreOffersContract.Presenter mPresenter;

    private StoreOffersRecyclerViewAdapter mAdapter;

    public StoreOffersFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static StoreOffersFragment newInstance() {
        StoreOffersFragment fragment = new StoreOffersFragment();
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
        View view = inflater.inflate(R.layout.fragment_store_offer_info, container, false);
        ButterKnife.bind(this, view);

        mPresenter = StoreOffersPresenter.getInstance(getContext(), this);

        mAdapter = new StoreOffersRecyclerViewAdapter(getContext());
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
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

    @Override
    public void onClick(View view) {
        if (view.getTag() instanceof StoreOffers) {
            StoreOffers info = (StoreOffers) view.getTag();
            int page = -1;
            Bundle bundle = new Bundle();
            bundle.clear();
            switch (info) {
                case TAG_SOUVENIR://紀念品專區
                    page = Page.TAG_SOUVENIR_AREA;
                    break;
                case TAG_STORE_INFO://商店資訊
                    bundle.putInt(StoreSearchContract.TAG_FROM_PAGE, Page.TAG_STORE_OFFERS);
                    page = Page.TAG_STORE_SEARCH;
                    break;
            }

            if (page != -1)
                mMainActivity.addFragment(page, bundle, true);
        } else {
            Log.e(TAG, "View.getTag() is not instance of FlightInfo");
        }
    }
}
