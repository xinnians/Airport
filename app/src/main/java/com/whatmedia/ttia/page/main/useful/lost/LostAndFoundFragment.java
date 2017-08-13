package com.whatmedia.ttia.page.main.useful.lost;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.whatmedia.ttia.R;
import com.whatmedia.ttia.page.BaseFragment;
import com.whatmedia.ttia.page.IActivityTools;
import com.whatmedia.ttia.response.data.LostAndFoundData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LostAndFoundFragment extends BaseFragment implements LostAndFoundContract.View{

    private static final String TAG = LostAndFoundFragment.class.getSimpleName();

    @BindView(R.id.webView)
    WebView mWebView;

    private IActivityTools.ILoadingView mLoadingView;
    private IActivityTools.IMainActivity mMainActivity;
    private LostAndFoundContract.Presenter mPresenter;

    public LostAndFoundFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static LostAndFoundFragment newInstance() {
        LostAndFoundFragment fragment = new LostAndFoundFragment();
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
        View view = inflater.inflate(R.layout.fragment_useful_lost, container, false);
        ButterKnife.bind(this, view);

        mPresenter = LostAndFoundPresenter.getInstance(getContext(), this);
        mLoadingView.showLoadingView();
        mPresenter.getLostAndFoundAPI();

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mLoadingView.goneLoadingView();
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
    public void getLostAndFoundSucceed(final List<LostAndFoundData> response) {
        if (response != null && response.size() > 0 && !TextUtils.isEmpty(response.get(0).getLostHtml())) {
            mMainActivity.runOnUI(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadData(response.get(0).getLostHtml(), "text/html; charset=utf-8", "UTF-8");
                    mWebView.setBackgroundColor(0);
                }
            });
        } else {
            mLoadingView.goneLoadingView();
            Log.e(TAG, "Response is error");
        }
    }

    @Override
    public void getLostAndFoundFailed(final String message) {
        Log.d(TAG, message);
        mLoadingView.goneLoadingView();
        mMainActivity.runOnUI(new Runnable() {
            @Override
            public void run() {
                showMessage(message);
            }
        });
    }
}