package adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudmagictask.cloudmagictask.Homepage;
import com.cloudmagictask.cloudmagictask.Messageactivity;
import com.cloudmagictask.cloudmagictask.R;

import org.json.JSONObject;

import java.util.List;

import models.Message;
import network.ApiInterface;
import okhttp3.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by haseeb on 15/10/16.
 */
public class Homeadapter extends RecyclerView.Adapter<Homeadapter.ViewHolder> {
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_EMPTY = 1;

    LayoutInflater inflater;
    static Context context;
    List<Message> data;
    ApiInterface apiInterface;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View mView;
        int Holderid;
        LinearLayout linearLayout, deleteLayout;
        TextView participant, subject, preview, ts, delete;
        ImageView star;



        public ViewHolder(View itemView, int ViewType) {                 // Creating ViewHolder Constructor with View and viewType As a parameter
            super(itemView);

            if (ViewType == TYPE_ITEM) {
                Holderid = 0;
                mView = itemView;

                linearLayout = (LinearLayout) mView.findViewById(R.id.email_layout);
                deleteLayout = (LinearLayout) mView.findViewById(R.id.deletelayout);
                participant = (TextView) mView.findViewById(R.id.home_participant);
                subject = (TextView) mView.findViewById(R.id.home_subject);
                preview = (TextView) mView.findViewById(R.id.home_preview);
                ts = (TextView) mView.findViewById(R.id.ts);
                delete = (TextView) mView.findViewById(R.id.delete);
                star = (ImageView) mView.findViewById(R.id.star);


            } else  {
                Holderid = 1;
                mView = itemView;

            }
        }

    }


    public Homeadapter(List<Message> data, Context context,  ApiInterface apiInterface) { // MyAdapter Constructor with titles and icons parameter
        this.data = data;
        Homeadapter.context = context;
        this.apiInterface = apiInterface;
    }


    //Below first we ovverride the method onCreateViewHolder which is called when the ViewHolder is
    //Created, In this method we inflate the item_row.xml layout if the viewType is Type_ITEM or else we inflate header.xml
    // if the viewType is TYPE_HEADER
    // and pass it to the view holder

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
//        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_placeholder, parent, false); //Inflating the layout
            ViewHolder vhItem = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view

            return vhItem; // Returning the created object
//        } else {
//            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty, parent, false); //Inflating the layout
//            ViewHolder vhItem = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view
//
//            return vhItem;
//        }

    }


    //Next we override a method which is called when the item in a row is needed to be displayed, here the int position
    // Tells us item at which position is being constructed to be displayed and the holder id of the holder object tell us
    // which view type is being created 1 for item row
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Message item = data.get(position);
        if (holder.Holderid == 0){
            holder.participant.setText(item.getParticipants().get(0));
            holder.subject.setText(item.getSubject());
            holder.preview.setText(item.getPreview());
            if (item.getIsStarred()){
                holder.star.setImageResource(R.drawable.orange_star);
            }
            else {
                holder.star.setImageResource(R.drawable.star);
            }

            holder.ts.setText(Utils.getTimeAgo.getTime(item.getTs(), context));
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Messageactivity.class);
                    intent.putExtra("id", item.getId());
                    context.startActivity(intent);
                    if (!item.getIsRead()){
                        holder.linearLayout.setBackgroundColor(Color.parseColor("#E0E0E0"));
                        item.setIsRead(true);
                    }
                    ((Activity)context).finish();
                }
            });

            holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    holder.linearLayout.setVisibility(View.GONE);
                    holder.deleteLayout.setVisibility(View.VISIBLE);
                    return true;
                }
            });

            holder.deleteLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final rx.Observable<JSONObject> Data = apiInterface.deleteMessage(item.getId());
                    Data.subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread());

                    Homepage.subscription_delete = Data.subscribe(new Observer<JSONObject>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            System.out.println("RESPPPP : "+e);
                        }

                        @Override
                        public void onNext(JSONObject response) {
                            System.out.println("RESPPPP : "+response);
                            data.remove(position);
                            notifyDataSetChanged();
                        }
                    });
                }
            });


            holder.star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.getIsStarred()){
                        holder.star.setImageResource(R.drawable.star);
                        item.setIsStarred(false);
                    }
                    else {
                        holder.star.setImageResource(R.drawable.orange_star);
                        item.setIsStarred(true);
                    }
                }
            });


            if (item.getIsRead()){
                holder.linearLayout.setBackgroundColor(Color.parseColor("#E0E0E0"));
            }
            else {
                holder.linearLayout.setBackgroundColor(Color.WHITE);
            }
        }
    }



    // This method returns the number of items present in the list
    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (data.get(position) != null) {
            return TYPE_ITEM;
        } else {
            return TYPE_EMPTY;

        }
    }



}
