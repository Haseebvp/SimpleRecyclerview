package com.cloudmagictask.cloudmagictask;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import DesignDecorators.DividerItemDecoration;
import adapters.Homeadapter;
import models.Message;
import network.ApiClient;
import network.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Homepage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Subscription subscription;
    public static Subscription subscription_delete=null;
    RecyclerView recyclerView;
    Homeadapter adapter;
    List<Message> Data = new ArrayList<Message>();
    ApiInterface apiInterface;
    LinearLayout emptylayout;
    DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

//        progressbar = (ProgressBar) findViewById(R.id.progressbar);
//        progressbar.setVisibility(View.VISIBLE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        emptylayout = (LinearLayout) findViewById(R.id.emptylayout);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));


//        Network calls
//        -----------------------

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        rx.Observable<List<Message>> data = apiInterface.getMessages();
        data.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        subscription = data.subscribe(new Observer<List<Message>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Snackbar snackbar = Snackbar
                                .make(drawer, "Oops. Something went wrong!", Snackbar.LENGTH_LONG);

                        snackbar.show();
                    }

                    @Override
                    public void onNext(List<Message> messages) {
                        Data = messages;
                        if (messages.size() > 0) {
                            setData(0);
                            emptylayout.setVisibility(View.GONE);
                        }
                        else {
                            emptylayout.setVisibility(View.VISIBLE);
                        }

                    }
                });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_inbox) {
            // Handle the camera action
            setData(0);
        } else if (id == R.id.nav_starred) {
            setData(1);
        } else if (id == R.id.nav_unread) {
            setData(2);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onDestroy() {
        subscription.unsubscribe();
        if (subscription_delete != null) {
            subscription_delete.unsubscribe();
        }
        super.onDestroy();
    };




    public void setData(Integer param){
            List<Message> fData = new ArrayList<Message>();

            for (int i=0; i < Data.size();i++){
                if (param == 1){
                    if (Data.get(i).getIsStarred()){
                        fData.add(Data.get(i));
                    }

                    recyclerView.removeAllViews();
                    adapter = new Homeadapter(fData, Homepage.this, apiInterface);
                    recyclerView.setAdapter(adapter);
                }
                else if (param == 2){

                    if (!Data.get(i).getIsRead()){
                        fData.add(Data.get(i));
                    }

                    recyclerView.removeAllViews();
                    adapter = new Homeadapter(fData, Homepage.this, apiInterface);
                    recyclerView.setAdapter(adapter);
                }
                else {
                    recyclerView.removeAllViews();
                    adapter = new Homeadapter(Data, Homepage.this, apiInterface);
                    recyclerView.setAdapter(adapter);
                }
            }
        };

    };
