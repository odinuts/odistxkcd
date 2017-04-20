package com.odinuts.odistforxkcd;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedActivity extends AppCompatActivity {
    private static final String TAG = "FeedActivity";
    XkcdAPI xkcdAPI;
    /* @BindView(R.id.image)
     ImageView image;
     @BindView(R.id.next)
     Button next;
     @BindView(R.id.previous)
     Button previous;
     @BindView(R.id.random)
     Button random;

     @OnClick(R.id.next)
     public void getNext() {
         getNextComic();
     }

     @OnClick(R.id.previous)
     public void getPrev() {
         getPreviousComic();
     }

     @OnClick(R.id.random)
     public void getRand() {
         getRandomComic();
     }*/
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    volatile List<XkcdResponse> responseList;
    volatile int currentComicNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        ButterKnife.bind(this);
        responseList = new ArrayList<>();
        getDefaultComic();
    }

    // TODO: implement click listeners for all the views.
    // TODO: implement Realm.

    private void getDefaultComic() {
        xkcdAPI = XkcdAPI.retrofit.create(XkcdAPI.class);
        final Call<XkcdResponse> xkcdResponseCall = xkcdAPI.getDefaultComic();
        xkcdResponseCall.enqueue(new Callback<XkcdResponse>() {
            @Override
            public void onResponse(Call<XkcdResponse> call, Response<XkcdResponse> response) {
                if (response.isSuccessful()) {
                    // initViews(response);
                    responseList.add(response.body());
                    currentComicNumber = response.body().getNum();
                    initRecyclerView();
                }
            }

            @Override
            public void onFailure(Call<XkcdResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void initRecyclerView() {
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(
                FeedActivity.this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(new FeedAdapter(responseList));
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                currentComicNumber--;
                getSpecificComic(currentComicNumber);
            }
        });
    }

    private void getSpecificComic(int currentComic) {
        final XkcdAPI xkcdAPI = XkcdAPI.retrofit.create(XkcdAPI.class);
        final Call<XkcdResponse> xkcdResponseCall = xkcdAPI.getSpecificComic(currentComic);
        xkcdResponseCall.enqueue(new Callback<XkcdResponse>() {
            @Override
            public void onResponse(Call<XkcdResponse> call, Response<XkcdResponse> response) {
                if (response.isSuccessful()) {
                    // initViews(response);
                    responseList.add(response.body());
                    currentComicNumber = response.body().getNum();
                    initRecyclerView();
                }
            }

            @Override
            public void onFailure(Call<XkcdResponse> call, Throwable t) {

            }
        });
    }


/*
    private void getNextComic() {
        int currentIndex = responseList.get(responseList.size() - 1).getNum();
        currentIndex++;
        getSpecificComic(currentIndex);
    }

    private void getPreviousComic() {
        int currentIndex = responseList.get(responseList.size() - 1).getNum();
        if (currentIndex != 0) {
            currentIndex--;
            getSpecificComic(currentIndex);
        } else {
            getDefaultComic();
        }
    }

    private void initViews(Response<XkcdResponse> response) {
        responseList.add(response.body());
        getSupportActionBar().setTitle("#" + response.body().getNum() + ": " +
                response.body().getTitle());
        Picasso.with(FeedActivity.this)
                .load(response.body().getImg())
                .placeholder(R.color.colorAccent)
                .into(image);
    }
*/

    private void getRandomComic() {
        int currentIndex = responseList.get(responseList.size() - 1).getNum();
        Random random = new Random();
        currentIndex = random.nextInt(currentIndex) + 1;
        getSpecificComic(currentIndex);
    }

    class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedHolder> {
        private List<XkcdResponse> adapterResponseList;
        private SimpleDateFormat simpleDateFormat;

        public FeedAdapter(List<XkcdResponse> list) {
            adapterResponseList = list;
            simpleDateFormat = new SimpleDateFormat("MMMM dd, yyyy (EEEE)", Locale.getDefault());
        }

        @Override
        public FeedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(FeedActivity.this).
                    inflate(R.layout.item_comic, parent, false);
            return new FeedHolder(v);
        }

        @Override
        public void onBindViewHolder(FeedHolder holder, int position) {
            XkcdResponse response = responseList.get(position);

            holder.title.setText("#" + response.getNum() + ": " + response.getTitle());
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, Integer.parseInt(response.getYear()));
            calendar.set(Calendar.MONTH, Integer.parseInt(response.getMonth()) - 1);
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(response.getDay()));
            holder.date.setText(simpleDateFormat.format(calendar.getTime()));
            Picasso.with(holder.image.getContext()).load(response.getImg()).into(holder.image);
        }

        @Override
        public int getItemCount() {
            if (responseList.size() != 0) {
                return responseList.size();
            } else {
                return 0;
            }
        }

        class FeedHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.title)
            TextView title;
            @BindView(R.id.date)
            TextView date;
            @BindView(R.id.image)
            ImageView image;
            @BindView(R.id.alt)
            TextView alt;
            @BindView(R.id.favorite)
            ImageView favorite;
            @BindView(R.id.transcript)
            ImageView transcript;
            @BindView(R.id.share)
            ImageView share;

            public FeedHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}