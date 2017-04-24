package com.odinuts.odistforxkcd;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.squareup.picasso.Picasso;
import java.util.Random;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedActivity extends AppCompatActivity {

  XkcdAPI xkcdAPI;
  volatile int index = 0;
  volatile String title = null;

  @BindView(R.id.progress_bar) ProgressBar progressBar;
  @BindView(R.id.image) ImageView image;
  @BindView(R.id.next) Button next;
  @BindView(R.id.previous) Button previous;
  @BindView(R.id.random) Button random;

  @OnClick(R.id.next) public void getNext() {
    getNextComic(index);
  }

  @OnClick(R.id.previous) public void getPrev() {
    getPreviousComic(index);
  }

  @OnClick(R.id.random) public void getRand() {
    getRandomComic(index);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_feed);
    ButterKnife.bind(this);
    progressBar.setVisibility(View.VISIBLE);
    getDefaultComic();
  }

  private void getDefaultComic() {
    xkcdAPI = XkcdAPI.retrofit.create(XkcdAPI.class);
    final Call<XkcdResponse> xkcdResponseCall = xkcdAPI.getDefaultComic();
    xkcdResponseCall.enqueue(new Callback<XkcdResponse>() {
      @Override public void onResponse(Call<XkcdResponse> call, Response<XkcdResponse> response) {
        if (response.isSuccessful()) {
          title = response.body().getTitle();
          index = response.body().getNum();
          initViews(response);
        }
      }

      @Override public void onFailure(Call<XkcdResponse> call, Throwable t) {
        t.printStackTrace();
      }
    });
  }

  private void getSpecificComic(int currentComic) {
    final XkcdAPI xkcdAPI = XkcdAPI.retrofit.create(XkcdAPI.class);
    final Call<XkcdResponse> xkcdResponseCall = xkcdAPI.getSpecificComic(currentComic);
    xkcdResponseCall.enqueue(new Callback<XkcdResponse>() {
      @Override public void onResponse(Call<XkcdResponse> call, Response<XkcdResponse> response) {
        if (response.isSuccessful()) {
          title = response.body().getTitle();
          index = response.body().getNum();
          initViews(response);
        }
      }

      @Override public void onFailure(Call<XkcdResponse> call, Throwable t) {

      }
    });
  }

  private void getNextComic(int currentIndex) {
    currentIndex++;
    getSpecificComic(currentIndex);
  }

  private void getPreviousComic(int currentIndex) {
    if (currentIndex != 0) {
      currentIndex--;
      getSpecificComic(currentIndex);
    } else {
      getDefaultComic();
    }
  }

  private void initViews(Response<XkcdResponse> response) {
    progressBar.setVisibility(View.INVISIBLE);
    getSupportActionBar().setTitle(
        "#" + response.body().getNum() + ": " + response.body().getTitle());
    Picasso.with(FeedActivity.this)
        .load(response.body().getImg())
        .placeholder(R.color.colorAccent)
        .into(image);
  }

  private void getRandomComic(int currentIndex) {
    Random random = new Random();
    currentIndex = random.nextInt(currentIndex) + 1;
    getSpecificComic(currentIndex);
  }
}