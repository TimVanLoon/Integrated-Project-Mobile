package com.example.keiichi.project_mobile.Mail;


import android.content.Context;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.keiichi.project_mobile.DAL.POJOs.Message;
import com.example.keiichi.project_mobile.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import helper.FlipAnimator;

public class MailAdapter extends RecyclerView.Adapter<MailAdapter.MyViewHolder> implements Filterable {

    private Context mContext;
    private SparseBooleanArray selectedItems;

    private SparseBooleanArray animeationItemsIndex;
    private boolean reverseAllAnimations = false;

    private static int currentSelectedIndex = -1;

    // Ongefilterde list
    private List<Message> originalData = null;
    // Gefilterde list
    private List<Message> filteredData = null;

    private MailAdapter.CustomFilter mFilter = new MailAdapter.CustomFilter();

    MailAdapter(Context context,  List<Message> values) {

        this.mContext = context;
        this.selectedItems = new SparseBooleanArray();
        animeationItemsIndex = new SparseBooleanArray();

        this.originalData = values;
        this.filteredData = values;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mail_items, parent, false);
        return new MailAdapter.MyViewHolder(itemView);
    }


    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {
            //Mail objecten ophalen

            Message message = getItem(position);

            String from = message.getFrom().getEmailAddress().getName();
            String email = message.getFrom().getEmailAddress().getAddress();
            String receivedDateTime = message.getReceivedDateTime();
            String bodyPreview = message.getBodyPreview();
            String subject = message.getSubject();
            Boolean isRead = message.isRead();

            //Data weergeven
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat output = new SimpleDateFormat("HH:mm");
            Date d = sdf.parse(receivedDateTime);
            holder.timestamp.setText(output.format(d));
            holder.from.setText(from);
            holder.message.setText(bodyPreview);
            holder.subject.setText(subject);



            //Row state tot active zetten
            holder.itemView.setActivated(selectedItems.get(position, false));

            ColorGenerator generator = ColorGenerator.MATERIAL;

            int color2 = generator.getColor(message.getSender().getEmailAddress().getName().substring(0,1));

            TextDrawable drawable1 = TextDrawable.builder()
                    .buildRoundRect(message.getSender().getEmailAddress().getName().substring(0,1), color2, 3); // radius in px

            holder.profilePicture.setImageDrawable(drawable1);

            if (!isRead){
                holder.subject.setTextColor(Color.CYAN);
            }


        } catch (ParseException e) {
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
        return filteredData.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView from, subject, message, iconText, timestamp;
        ImageView iconImp, imgProfile, profilePicture;
        LinearLayout messageContainer;
        RelativeLayout iconContainer, iconBack, iconFront;

        MyViewHolder(View view) {
            super(view);
            from = view.findViewById(R.id.from);
            subject = view.findViewById(R.id.txt_primary);
            message = view.findViewById(R.id.txt_secondary);
            timestamp = view.findViewById(R.id.timestamp);
            iconBack = view.findViewById(R.id.icon_back);
            iconFront = view.findViewById(R.id.icon_front);
            iconImp = view.findViewById(R.id.icon_star);
            messageContainer = view.findViewById(R.id.message_container);
            iconContainer = view.findViewById(R.id.icon_container);
            profilePicture = view.findViewById(R.id.profilePicture);



        }


    }



    public Message getItem(int position) {

      return filteredData.get(position);

    }



    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }


    public Filter getFilter() {
        // return mFilter;
        if (mFilter == null)
            mFilter = new MailAdapter.CustomFilter();
        return mFilter;
    }

    private class CustomFilter extends Filter {
        // called when adapter filter method is called
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (constraint != null && constraint.toString().length() > 0) {
                List<Message> filt = new ArrayList<Message>(); //filtered list
                for (int i = 0; i < originalData.size(); i++) {
                    Message m = originalData.get(i);
                    if (m.getSender().getEmailAddress().getName().toLowerCase().contains(constraint)) {
                        filt.add(m); //add only items which matches
                    }
                }
                result.count = filt.size();
                result.values = filt;
            } else { // return original list
                synchronized (this) {
                    result.values = originalData;
                    result.count = originalData.size();
                }
            }
            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            if (results != null) {
                setList((List<Message>) results.values); // notify data set changed
            } else {
                setList((List<Message>) originalData);
            }
        }
    }

    public void setList(List<Message> data) {
        filteredData = data; // set the adapter list to data
        MailAdapter.this.notifyDataSetChanged(); // notify data set change
    }

}
