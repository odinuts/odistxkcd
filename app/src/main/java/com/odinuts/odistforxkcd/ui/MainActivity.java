package com.odinuts.odistforxkcd.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageView;

import com.odinuts.odistforxkcd.R;
import com.odinuts.odistforxkcd.model.XkcdResponse;
import com.odinuts.odistforxkcd.util.XkcdAPI;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    volatile int currentComic;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.xkcd_comic)
    ImageView comicImage;
    @BindView(R.id.next_button)
    Button nextButton;
    @BindView(R.id.previous_button)
    Button previousButton;

    @OnClick(R.id.next_button)
    public void next() {
        getNextComic();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getDefaultComic();
    }

    private void getDefaultComic() {
        final XkcdAPI xkcdAPI = XkcdAPI.retrofit.create(XkcdAPI.class);
        final Call<XkcdResponse> xkcdResponseCall = xkcdAPI.getDefaultComic();
        xkcdResponseCall.enqueue(new Callback<XkcdResponse>() {
            @Override
            public void onResponse(Call<XkcdResponse> call, Response<XkcdResponse> response) {
                if (response.isSuccessful()) {
                    XkcdResponse xkcdResponse = response.body();
                    updateUI(xkcdResponse);
                }
            }

            @Override
            public void onFailure(Call<XkcdResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void updateUI(XkcdResponse xkcdResponse) {
        currentComic = xkcdResponse.getNum();
        getSupportActionBar().setTitle("Comic #" + xkcdResponse.getNum() +
                ": " + xkcdResponse.getTitle());
        Picasso.with(MainActivity.this).load(xkcdResponse.getImg()).into(comicImage);
    }

    private void getNextComic() {
        if (currentComic != 0) {
            currentComic++;
            getSpecificComic(currentComic);
        }
    }

    private void getSpecificComic(int currentComic) {
        final XkcdAPI xkcdAPI = XkcdAPI.retrofit.create(XkcdAPI.class);
        final Call<XkcdResponse> xkcdResponseCall = xkcdAPI.getSpecificComic(currentComic);
        xkcdResponseCall.enqueue(new Callback<XkcdResponse>() {
            @Override
            public void onResponse(Call<XkcdResponse> call, Response<XkcdResponse> response) {
                if (response.isSuccessful()) {
                    XkcdResponse xkcdResponse = response.body();
                    updateUI(xkcdResponse);
                }
            }

            @Override
            public void onFailure(Call<XkcdResponse> call, Throwable t) {

            }
        });
    }
}
