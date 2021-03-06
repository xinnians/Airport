package com.whatmedia.ttia.page.main.home.departure;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.whatmedia.ttia.connect.ApiConnect;
import com.whatmedia.ttia.connect.MyResponse;
import com.whatmedia.ttia.response.GetFlightsInfoResponse;
import com.whatmedia.ttia.response.data.FlightSearchData;
import com.whatmedia.ttia.response.data.FlightsInfoData;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;

/**
 * Created by neo_mac on 2017/6/17.
 */

public class DepartureFlightsPresenter implements DepartureFlightsContract.Presenter {
    private final static String TAG = DepartureFlightsPresenter.class.getSimpleName();

    private static DepartureFlightsPresenter mDepartureFlightsPresenter;
    private static ApiConnect mApiConnect;
    private static DepartureFlightsContract.View mView;


    public static DepartureFlightsPresenter getInstance(Context context, DepartureFlightsContract.View view) {
        mDepartureFlightsPresenter = new DepartureFlightsPresenter();
        mApiConnect = ApiConnect.getInstance(context);
        mView = view;

        return mDepartureFlightsPresenter;
    }

    @Override
    public void getDepartureFlightAPI(FlightSearchData searchData) {
        mApiConnect.getSearchFlightsInfoByDate(searchData, new ApiConnect.MyCallback() {
            @Override
            public void onFailure(Call call, IOException e, boolean timeout) {
                mView.getDepartureFlightFailed(e.toString(), timeout);
            }

            @Override
            public void onResponse(Call call, MyResponse response) throws IOException {
                if (response.code() == 200) {
                    String result = response.body().string();
                    Log.d(TAG, result);
                    List<FlightsInfoData> list = GetFlightsInfoResponse.newInstance(result);
                    mView.getDepartureFlightSucceed(list);
                } else {
                    mView.getDepartureFlightFailed(!TextUtils.isEmpty(response.message()) ? response.message() : "", false);
                }
            }
        });
    }

    @Override
    public void saveMyFlightsAPI(FlightsInfoData data) {
        mApiConnect.doMyFlights(data, new ApiConnect.MyCallback() {
            @Override
            public void onFailure(Call call, IOException e, boolean timeout) {
                mView.saveMyFlightFailed(e.toString(), timeout);
            }

            @Override
            public void onResponse(Call call, MyResponse response) throws IOException {
                if (response.code() == 200) {
                    String result = response.body().string();
                    Log.d(TAG, result);
                    mView.saveMyFlightSucceed(result);
                } else {
                    mView.saveMyFlightFailed(!TextUtils.isEmpty(response.message()) ? response.message() : "", false);
                }
            }
        });

    }
}
