package com.ydl.logintest;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

/**
 * Created by qweenhool on 2017/8/22.
 */

public abstract class ResultJsonCallback extends Callback<ResultJson> {

    @Override
    public ResultJson parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        return new Gson().fromJson(string, ResultJson.class);
    }
}
