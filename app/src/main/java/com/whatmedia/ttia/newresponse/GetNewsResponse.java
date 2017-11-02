package com.whatmedia.ttia.newresponse;


import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.whatmedia.ttia.newresponse.data.UserNewsData;

import java.util.List;

public class GetNewsResponse {

    @SerializedName("newsList")
    private List<UserNewsData> userNewsDataList;

    public static GetNewsResponse getGson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, GetNewsResponse.class);
    }

    public List<UserNewsData> getUserNewsDataList() {
        return userNewsDataList;
    }

    public void setUserNewsDataList(List<UserNewsData> userNewsDataList) {
        this.userNewsDataList = userNewsDataList;
    }
}
