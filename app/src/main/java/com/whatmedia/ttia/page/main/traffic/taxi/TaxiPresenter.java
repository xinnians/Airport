package com.whatmedia.ttia.page.main.traffic.taxi;

import android.content.Context;
import android.text.TextUtils;

import com.whatmedia.ttia.connect.ApiConnect;
import com.whatmedia.ttia.connect.MyResponse;
import com.whatmedia.ttia.response.GetTaxiResponse;
import com.whatmedia.ttia.response.data.TaxiData;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;

/**
 * Created by neo_mac on 2017/6/17.
 */

public class TaxiPresenter implements TaxiContract.Presenter {
    private final static String TAG = TaxiPresenter.class.getSimpleName();

    private static TaxiPresenter mTaxiPresenter;
    private static ApiConnect mApiConnect;
    private static TaxiContract.View mView;


    public static TaxiPresenter getInstance(Context context, TaxiContract.View view) {
        mTaxiPresenter = new TaxiPresenter();
        mApiConnect = ApiConnect.getInstance(context);
        mView = view;
        return mTaxiPresenter;
    }

    @Override
    public void getTaxiAPI() {
        mApiConnect.getTaxi(new ApiConnect.MyCallback() {
            @Override
            public void onFailure(Call call, IOException e, boolean timeout) {
                mView.getTaxiFailed(e.toString(), timeout);
            }

            @Override
            public void onResponse(Call call, MyResponse response) throws IOException {
                if (response.code() == 200) {
                    String result = response.body().string();
                    List<TaxiData> list = GetTaxiResponse.newInstance(result);
                    mView.getTaxiSucceed(list);
                } else {
                    mView.getTaxiFailed(!TextUtils.isEmpty(response.message()) ? response.message() : "", false);
                }
            }
        });

    }
}
