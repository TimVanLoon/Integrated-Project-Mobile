package com.example.keiichi.project_mobile;


import android.content.Context;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import helper.FlipAnimator;

/**
 * Created by Keiichi on 6/12/2017.
 */

public class MailAdapter extends RecyclerView.Adapter<MailAdapter.MyViewHolder> {
    private JSONArray jsonArray;

    private Context mContext;
    private SparseBooleanArray selectedItems;

    private SparseBooleanArray animeationItemsIndex;
    private boolean reverseAllAnimations = false;

    private static int currentSelectedIndex = -1;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mail_items, parent, false);
        return new MailAdapter.MyViewHolder(itemView);
    }


    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {
            //Mail objecten ophalen
            JSONObject mailObject = jsonArray.getJSONObject(position);
            JSONObject sender = mailObject.getJSONObject("from");
            JSONObject emailAddress = sender.getJSONObject("emailAddress");

            //Data weergeven
            holder.timestamp.setText(mailObject.getString("receivedDateTime"));
            holder.from.setText(emailAddress.getString("name"));
            holder.message.setText(mailObject.getString("bodyPreview"));
            holder.subject.setText(mailObject.getString("subject"));

            //eerste letter van 'from' tonen
            holder.iconText.setText(emailAddress.getString("name").substring(0, 1));
            applyProfilePicture(holder);

            //Row state tot active zetten
            holder.itemView.setActivated(selectedItems.get(position, false));

            //set onlcik events

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    void applyProfilePicture(MyViewHolder holder) {
        holder.imgProfile.setImageResource(R.drawable.bg_circle);
        holder.imgProfile.setColorFilter(Color.CYAN);
        holder.iconText.setVisibility(View.VISIBLE);
    }

    private void applyIconAnimation(MyViewHolder holder, int position) {
        if (selectedItems.get(position, false)) {
            holder.iconFront.setVisibility(View.GONE);
            resetIconYAxis(holder.iconBack);
            holder.iconBack.setVisibility(View.VISIBLE);
            holder.iconBack.setAlpha(1);
            if (currentSelectedIndex == position) {
                FlipAnimator.flipView(mContext, holder.iconBack, holder.iconFront, true);
                resetCurrentIndex();
            }
        } else {
            holder.iconBack.setVisibility(View.GONE);
            resetIconYAxis(holder.iconFront);
            holder.iconFront.setVisibility(View.VISIBLE);
            holder.iconFront.setAlpha(1);
            if ((reverseAllAnimations && animeationItemsIndex.get(position, false)) || currentSelectedIndex == position) {
                FlipAnimator.flipView(mContext, holder.iconBack, holder.iconFront, false);
                resetCurrentIndex();
            }
        }
    }

    private void resetCurrentIndex() {
        currentSelectedIndex = -1;
    }

    private void resetIconYAxis(View view) {
        if (view.getRotation() != 0) {
            view.setRotationY(0);
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }



    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView from, subject, message, iconText, timestamp;
        ImageView iconImp, imgProfile;
        LinearLayout messageContainer;
        RelativeLayout iconContainer, iconBack, iconFront;

        MyViewHolder(View view) {
            super(view);
            from = view.findViewById(R.id.from);
            subject = view.findViewById(R.id.txt_primary);
            message = view.findViewById(R.id.txt_secondary);
            iconText = view.findViewById(R.id.icon_text);
            timestamp = view.findViewById(R.id.timestamp);
            iconBack = view.findViewById(R.id.icon_back);
            iconFront = view.findViewById(R.id.icon_front);
            iconImp = view.findViewById(R.id.icon_star);
            imgProfile = view.findViewById(R.id.icon_profile);
            messageContainer = view.findViewById(R.id.message_container);
            iconContainer = view.findViewById(R.id.icon_container);

        }


    }

    MailAdapter(Context context, JSONArray jsonArray) {
        this.jsonArray = jsonArray;
        this.mContext = context;
        this.selectedItems = new SparseBooleanArray();
        animeationItemsIndex = new SparseBooleanArray();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }


}
