package com.whatmedia.ttia.connect;

import android.text.TextUtils;

/**
 * Created by neo_mac on 2017/9/11.
 */

public class MyResponse {
    private ResponseBody myResponseBody;
    private int code;
    private String message;

    public ResponseBody body() {
        return myResponseBody != null ? myResponseBody : new ResponseBody();
    }


    public void setMyResponseBody(String myResponseBody) {
        ResponseBody responseBody = new ResponseBody();
        responseBody.setBody(myResponseBody);
        this.myResponseBody = responseBody;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }

    public int code() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public class ResponseBody {
        private String body;

        public String string() {
            return !TextUtils.isEmpty(body) ? body : "";
        }

        public void setBody(String mybody) {
            this.body = mybody;
        }
    }
}
