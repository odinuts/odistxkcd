package com.odinuts.odistforxkcd.util;

import com.odinuts.odistforxkcd.model.XkcdResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface XkcdAPI {
    @GET("http://xkcd.com/info.0.json")
    Call<XkcdResponse> getDefaultComic();

    @GET("http://xkcd.com/{number}/info.0.json")
    Call<XkcdResponse> getSpecificComic(@Path("number") int number);

    Retrofit retrofit = new Retrofit.Builder().baseUrl("http://xkcd.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
