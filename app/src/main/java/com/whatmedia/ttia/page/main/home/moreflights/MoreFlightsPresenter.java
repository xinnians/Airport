package com.whatmedia.ttia.page.main.home.moreflights;

import android.content.Context;
import android.text.TextUtils;

import com.whatmedia.ttia.R;
import com.whatmedia.ttia.connect.NewApiConnect;
import com.whatmedia.ttia.newresponse.GetFlightsListResponse;
import com.whatmedia.ttia.newresponse.GetFlightsQueryResponse;
import com.whatmedia.ttia.newresponse.data.FlightsListData;
import com.whatmedia.ttia.newresponse.data.FlightsQueryData;
import com.whatmedia.ttia.utility.Util;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;

/**
 * Created by neo_mac on 2017/6/17.
 */

public class MoreFlightsPresenter implements MoreFlightsContract.Presenter {
    private final static String TAG = MoreFlightsPresenter.class.getSimpleName();

    private NewApiConnect mNewApiConnect;
    private MoreFlightsContract.View mView;
    private Context mContext;


    private String mQueryDate = Util.getNowDate();
    private int mQueryType = FlightsQueryData.TAG_DEPARTURE_ALL;


    MoreFlightsPresenter(Context context, MoreFlightsContract.View view) {
        mNewApiConnect = NewApiConnect.getInstance(context);
        mView = view;
        mContext = context;
    }

    @Override
    public void getFlightAPI() {
        FlightsQueryData data = new FlightsQueryData();
        GetFlightsQueryResponse flightsListResponse = new GetFlightsQueryResponse();


        data.setQueryType(mQueryType);
        data.setExpressDate(mQueryDate);
        flightsListResponse.setData(data);
        if (TextUtils.isEmpty(flightsListResponse.getJson())) {
            mView.getFlightFailed(mContext.getString(R.string.data_error), NewApiConnect.TAG_DEFAULT);
            return;
        }

        mNewApiConnect.getFlightsListInfo(flightsListResponse.getJson(), new NewApiConnect.MyCallback() {
            @Override
            public void onFailure(Call call, IOException e, int status) {
                mView.getFlightFailed(e.toString(), status);
            }

            @Override
            public void onResponse(Call call, String response) throws IOException {
                GetFlightsListResponse flightsListResponse = GetFlightsListResponse.getGson(response);
                List<FlightsListData> flightsListData = flightsListResponse.getFlightList();

                mView.getFlightSucceed(flightsListData);
            }
        });
    }

    @Override
    public void getFlightByQueryTypeAPI(int queryType) {
        mQueryType = queryType;
        getFlightAPI();
    }

    @Override
    public void getFlightByDateAPI(String date) {
        mQueryDate = date;
        getFlightAPI();
    }

    @Override
    public void saveMyFlightsAPI(FlightsListData flightsListData) {
        FlightsListData data = new FlightsListData();
        GetFlightsListResponse response = new GetFlightsListResponse();

        data.setAirlineCode(!TextUtils.isEmpty(flightsListData.getAirlineCode()) ? flightsListData.getAirlineCode() : "");
        data.setShifts(!TextUtils.isEmpty(flightsListData.getShifts()) ? flightsListData.getShifts() : "");
        data.setExpressDate(!TextUtils.isEmpty(flightsListData.getExpressDate()) ? flightsListData.getExpressDate() : "");
        data.setExpressTime(!TextUtils.isEmpty(flightsListData.getExpressTime()) ? flightsListData.getExpressTime() : "");

        response.setUploadData(data);
        final String json = response.getJson();
        if (TextUtils.isEmpty(json)) {
            mView.getFlightFailed(mContext.getString(R.string.data_error), NewApiConnect.TAG_DEFAULT);
            return;
        }

        mNewApiConnect.saveMyFlights(json, new NewApiConnect.MyCallback() {
            @Override
            public void onFailure(Call call, IOException e, int status) {
                mView.saveMyFlightFailed(e.toString(), status);
            }

            @Override
            public void onResponse(Call call, String response) throws IOException {
                mView.saveMyFlightSucceed(response, json);
            }
        });
    }
}
