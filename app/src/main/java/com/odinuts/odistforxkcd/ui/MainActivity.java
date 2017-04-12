package com.odinuts.odistforxkcd.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.odinuts.odistforxkcd.R;
import com.odinuts.odistforxkcd.data.model.XkcdResponse;
import com.odinuts.odistforxkcd.ui.home.HomeContract;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements HomeContract.View {
    private HomeContract.UserActionsListener userActionsListener;
    volatile int currentComic;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.xkcd_comic)
    ImageView comicImage;
    @BindView(R.id.next_button)
    Button nextButton;
    @BindView(R.id.previous_button)
    Button previousButton;

    @OnClick(R.id.next_button)
    public void next() {
        userActionsListener.getNextComic(currentComic);
    }

    @OnClick(R.id.previous_button)
    public void previous() {
        userActionsListener.getPreviousComic(currentComic);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
    }

    @Override
    public void updateUI(XkcdResponse xkcdResponse) {
        getSupportActionBar().setTitle("Comic #" + xkcdResponse.getNum() +
                " " + xkcdResponse.getTitle());
        Picasso.with(this).load(xkcdResponse.getImg()).into(comicImage);
    }

    @Override
    public void showProgress() {
        // implement a progress bar
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}