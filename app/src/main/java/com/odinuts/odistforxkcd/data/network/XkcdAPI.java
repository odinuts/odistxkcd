package com.odinuts.odistforxkcd.data.network;

import com.odinuts.odistforxkcd.data.model.XkcdResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface XkcdAPI {
    Retrofit retrofit = new Retrofit.Builder().baseUrl("http://xkcd.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET("http://xkcd.com/info.0.json")
    Call<XkcdResponse> getDefaultComic();

    @GET("http://xkcd.com/{number}/info.0.json")
    Call<XkcdResponse> getSpecificComic(@Path("number") int number);
}