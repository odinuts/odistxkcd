package com.odinuts.odistforxkcd.data.manager;

import android.support.annotation.NonNull;

import com.odinuts.odistforxkcd.data.model.XkcdResponse;
import com.odinuts.odistforxkcd.data.network.XkcdAPI;
import com.odinuts.odistforxkcd.ui.home.HomeContract;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APIPresenter implements HomeContract.UserActionsListener {
    @NonNull
    private final HomeContract.View mainView;

    public APIPresenter(@NonNull HomeContract.View mainView) {
        this.mainView = mainView;
    }

    @Override
    public void getDefaultComic() {
        final XkcdAPI xkcdAPI = XkcdAPI.retrofit.create(XkcdAPI.class);
        final Call<XkcdResponse> xkcdResponseCall = xkcdAPI.getDefaultComic();
        xkcdResponseCall.enqueue(new Callback<XkcdResponse>() {
            @Override
            public void onResponse(Call<XkcdResponse> call, Response<XkcdResponse> response) {
                if (response.isSuccessful()) {
                    XkcdResponse xkcdResponse = response.body();
                    mainView.updateUI(xkcdResponse);
                }
            }

            @Override
            public void onFailure(Call<XkcdResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void getSpecificComic(int currentComic) {
        final XkcdAPI xkcdAPI = XkcdAPI.retrofit.create(XkcdAPI.class);
        final Call<XkcdResponse> xkcdResponseCall = xkcdAPI.getSpecificComic(currentComic);
        xkcdResponseCall.enqueue(new Callback<XkcdResponse>() {
            @Override
            public void onResponse(Call<XkcdResponse> call, Response<XkcdResponse> response) {
                if (response.isSuccessful()) {
                    XkcdResponse xkcdResponse = response.body();
                    mainView.updateUI(xkcdResponse);
                }
            }

            @Override
            public void onFailure(Call<XkcdResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void getNextComic(int currentComic) {
        currentComic++;
        getSpecificComic(currentComic);

    }

    @Override
    public void getPreviousComic(int currentComic) {
        if (currentComic != 0) {
            currentComic--;
            getSpecificComic(currentComic);
        } else {
            getDefaultComic();
        }
    }
}