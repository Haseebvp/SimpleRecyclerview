package com.cloudmagictask.cloudmagictask;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import DesignDecorators.CharacterDrawable;

import models.MessageContent;
import network.ApiClient;
import network.ApiInterface;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Messageactivity extends AppCompatActivity {

    TextView message_content;
    Integer id = 0;
    Subscription subscription;
    Subscription subscription_delete = null;
    ImageView star, profile;

    TextView subject, participants,participant, ts;
    CollapsingToolbarLayout collapsingToolbarLayout;
    MessageContent messageContent;
    ApiInterface apiInterface;
    CoordinatorLayout main_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        message_content = (TextView) findViewById(R.id.message_content);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        View view =getSupportActionBar().getCustomView();

        ImageView back = (ImageView) view.findViewById(R.id.back);
        ImageView trash = (ImageView) view.findViewById(R.id.trash);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Delete();
            }
        });


        main_layout = (CoordinatorLayout) findViewById(R.id.main_layout);
        subject = (TextView) findViewById(R.id.subject);
        participants = (TextView) findViewById(R.id.participants);
        participant = (TextView) findViewById(R.id.participant);
        ts = (TextView) findViewById(R.id.ts);
        profile = (ImageView) findViewById(R.id.profile);
        star = (ImageView) findViewById(R.id.star);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.colllapse);
        collapsingToolbarLayout.setTitle("My Title");
        collapsingToolbarLayout.setContentScrimColor(Color.WHITE);

        id = getIntent().getIntExtra("id",0);

//        Network calls
//        -----------------------

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        rx.Observable<MessageContent> data = apiInterface.getMessageContent(id);
        data.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        subscription = data.subscribe(new Observer<MessageContent>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                System.out.println("MESSAGEQQQ error: "+e);
            }

            @Override
            public void onNext(MessageContent messageContent) {
                messageContent = messageContent;
                message_content.setText(messageContent.getBody());
                subject.setText(messageContent.getSubject());
                participants.setText(messageContent.getParticipants().get(0).getName());
                participant.setText(messageContent.getParticipants().get(0).getName());
                String time = Utils.getTimeAgo.getTimeinLocal(messageContent.getTs()) + "(" + Utils.getTimeAgo.getTime(messageContent.getTs(),Messageactivity.this) + ")";
                ts.setText(time);

                if (messageContent.getIsStarred()){
                    star.setImageResource(R.drawable.orange_star);
                }
                else {
                    star.setImageResource(R.drawable.star);
                }

                final MessageContent finalMessageContent = messageContent;
                star.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (finalMessageContent.getIsStarred()){
                            star.setImageResource(R.drawable.star);
                            finalMessageContent.setIsStarred(false);
                        }
                        else {
                            star.setImageResource(R.drawable.orange_star);
                            finalMessageContent.setIsStarred(true);
                        }
                    }
                });

                CharacterDrawable drawable = new CharacterDrawable(messageContent.getParticipants().get(0).getName().charAt(0), 0xFF805781);
                profile.setImageDrawable(drawable);

            }
        });

    }



    public void Delete(){
        final rx.Observable<JSONObject> Data = apiInterface.deleteMessage(id);
        Data.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        subscription_delete = Data.subscribe(new Observer<JSONObject>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Snackbar snackbar = Snackbar
                        .make(main_layout, "Oops. Something went wrong!", Snackbar.LENGTH_LONG);

                snackbar.show();
            }

            @Override
            public void onNext(JSONObject response) {
                Intent intent = new Intent(Messageactivity.this, Homepage.class);
                startActivity(intent);
                finish();
            }
        });


    }


    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Messageactivity.this, Homepage.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        subscription.unsubscribe();
        if (subscription_delete != null) {
            subscription_delete.unsubscribe();
        }
        super.onDestroy();
    }
}
