package com.whatmedia.ttia.page.main.home;

import android.content.Context;

import com.whatmedia.ttia.connect.MyResponse;
import com.whatmedia.ttia.connect.NewApiConnect;
import com.whatmedia.ttia.newresponse.GetLanguageListResponse;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.Call;

/**
 * Created by neo_mac on 2017/6/17.
 */

public class HomePresenter implements HomeContract.Presenter {

    private static WeakReference<NewApiConnect> mNewApiConnect;
    private Context mContext;
    private HomeContract.View mView;


    HomePresenter(Context context, HomeContract.View view) {
        mNewApiConnect = new WeakReference<NewApiConnect>(NewApiConnect.getInstance(context));
        mView = view;
        mContext = context;
    }

    @Override
    public void getLanguageList() {
        mNewApiConnect.get().getLangList(new NewApiConnect.MyCallback() {
            @Override
            public void onFailure(Call call, IOException e, boolean timeout) {
                mView.getLanguageListFailed(e.toString(), timeout);
            }

            @Override
            public void onResponse(Call call, MyResponse response) throws IOException {
                GetLanguageListResponse result = GetLanguageListResponse.getInstance(mContext, response.body().string());
                if (result.getData() != null)
                    mView.getLanguageListSuccess(result);
                else
                    onFailure(call, new IOException("Data is empty"), false);
            }
        });
    }
}
