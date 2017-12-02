package com.bwie.mockapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.greenrobot.greendao.database.Database;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://gank.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final GankAPI gankAPI = retrofit.create(GankAPI.class);

        Call<GankData<GankFeed>> gankDataCall = gankAPI.list(10, 1);

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db");
        Database db = helper.getWritableDb();
        final DaoSession daoSession = new DaoMaster(db).newSession();

        gankDataCall.enqueue(new Callback<GankData<GankFeed>>() {
            @Override
            public void onResponse(Call<GankData<GankFeed>> call, Response<GankData<GankFeed>> response) {
                Log.d(getLocalClassName(), response.body().toString());

                GankFeedDao gankFeedDao = daoSession.getGankFeedDao();

                for (GankFeed feed :
                        response.body().results) {
                    gankFeedDao.insert(feed);
                }

                Log.d(getLocalClassName(), "数量：" + gankFeedDao.count());
                Log.d(getLocalClassName(), gankFeedDao.queryBuilder().build().list().toString());

            }

            @Override
            public void onFailure(Call<GankData<GankFeed>> call, Throwable t) {

            }
        });
    }
}
