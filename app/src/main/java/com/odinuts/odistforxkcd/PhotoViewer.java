package com.odinuts.odistforxkcd;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhotoViewer extends AppCompatActivity {
    public static final String EXTRA_TITLE = "photoviewer.title";
    public static final String EXTRA_NUMBER = "photoviewer.number";

    @BindView(R.id.close)
    ImageView close;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.number)
    TextView number;
    @BindView(R.id.image)
    ImageView image;

    @OnClick(R.id.close)
    public void returnToParent() {
        onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewer);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        title.setText(getIntent().getExtras().getString(EXTRA_TITLE));
        number.setText(getIntent().getExtras().getString(EXTRA_NUMBER));

//        Todo: implement a Realm db where you save the comics so you can retrieve the image here
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
