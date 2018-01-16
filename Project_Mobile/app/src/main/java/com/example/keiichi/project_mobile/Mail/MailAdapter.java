package com.example.keiichi.project_mobile.Mail;


import android.content.Context;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.keiichi.project_mobile.DAL.POJOs.Message;
import com.example.keiichi.project_mobile.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import helper.FlipAnimator;

public class MailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    public static final int TYPE_DATE = 0;
    public static final int TYPE_GENERAL = 1;

    private Context mContext;
    private SparseBooleanArray selectedItems;

    private SparseBooleanArray animeationItemsIndex;
    private boolean reverseAllAnimations = false;

    private static int currentSelectedIndex = -1;
    private Message message;
    // Ongefilterde list
    private List<Message> originalData = null;
    // Gefilterde list
    private List<Message> filteredData = null;

    private MailAdapter.CustomFilter mFilter = new MailAdapter.CustomFilter();

    private List<ListItem> consolidatedList = new ArrayList<>();

    MailAdapter(Context context, HashMap<String, List<Message>> values) {

        this.mContext = context;
        this.selectedItems = new SparseBooleanArray();
        animeationItemsIndex = new SparseBooleanArray();

        for (String date : values.keySet()){
            DateItem dateItem = new DateItem();
            dateItem.setDate(date);
            consolidatedList.add(dateItem);
            for (Message message : values.get(date)) {
                GeneralItem generalItem = new GeneralItem();
                generalItem.setPojoOfJsonArray(message);
                consolidatedList.add(generalItem);
            }
        }

        /*this.originalData = values;
        this.filteredData = values;*/
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
       /* View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mail_items, parent, false);
        return new MailAdapter.MessageViewHolder(itemView);*/
       LayoutInflater inflater = LayoutInflater.from(parent.getContext());

       switch (viewType){
           case ListItem.TYPE_GENERAL:
               View v1 = inflater.inflate(R.layout.mail_items, parent,false);
               viewHolder = new MessageViewHolder(v1);
               break;
           case ListItem.TYPE_DATE:
               View v2 = inflater.inflate(R.layout.date_items, parent,false);
               viewHolder = new DateViewHolder(v2);
               break;
       }

       return  viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        try {
            //Mail objecten ophalen

             //message = getItem(position);

             switch (holder.getItemViewType()){
                 case TYPE_DATE:
                     DateItem dateItem = (DateItem) consolidatedList.get(position);
                     DateViewHolder dateViewHolder = (DateViewHolder) holder;

                     DateFormat format = new SimpleDateFormat("yyyy/MM/dd");

                     Date date = new GregorianCalendar().getTime();
                     Date yesterday = new Date(date.getTime() - 24 * 3600 * 1000);



                     String formatted = format.format(date);
                     String formattedyesterday = format.format(yesterday);

                     if (dateItem.getDate().equals(formatted.replace("/","-"))){
                         dateViewHolder.date.setText("Today");
                     } else if (dateItem.getDate().equals(formattedyesterday.replace("/","-"))){
                         dateViewHolder.date.setText("Yesterday");
                     }else{
                         dateViewHolder.date.setText(dateItem.getDate());

                     }


                     break;
                 case TYPE_GENERAL:
                     GeneralItem generalItem1 = (GeneralItem) consolidatedList.get(position);
                     MessageViewHolder messageViewHolder = (MessageViewHolder) holder;
                     Message message = generalItem1.message();
                     String from = message.getFrom().getEmailAddress().getName();
                     String email = message.getFrom().getEmailAddress().getAddress();
                     String receivedDateTime = message.getReceivedDateTime();
                     String bodyPreview = message.getBodyPreview();
                     String subject = message.getSubject();
                     Boolean isRead = message.isRead();
                     Boolean hasAttachment = message.isHasAttachments();

                     //Data weergeven
                     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                     SimpleDateFormat output = new SimpleDateFormat("HH:mm");
                     Date d = sdf.parse(receivedDateTime);
                     messageViewHolder.timestamp.setText(output.format(d));
                     messageViewHolder.from.setText(from);
                     messageViewHolder.message.setText(bodyPreview);
                     messageViewHolder.subject.setText(subject);
                     //Row state tot active zetten
                     messageViewHolder.itemView.setActivated(selectedItems.get(position, false));

                     RequestQueue queue = Volley.newRequestQueue(mContext);

                     ColorGenerator generator = ColorGenerator.MATERIAL;

                     int color2 = generator.getColor(message.getSender().getEmailAddress().getName().substring(0,1));

                     TextDrawable drawable1 = TextDrawable.builder()
                             .buildRoundRect(message.getSender().getEmailAddress().getName().substring(0,1), color2, 3); // radius in px

                     messageViewHolder.profilePicture.setImageDrawable(drawable1);



                     if (message.isHasAttachments()){
                         messageViewHolder.attachmentView.setVisibility(View.VISIBLE);
                     }

                     if (message.isRead()) {

                         messageViewHolder.from.setTypeface(null, Typeface.NORMAL);
                         messageViewHolder.subject.setTypeface(null, Typeface.NORMAL);
                         messageViewHolder.from.setTextColor(ContextCompat.getColor(mContext, R.color.subject));
                         messageViewHolder.subject.setTextColor(ContextCompat.getColor(mContext, R.color.message));

                     } else {

                         messageViewHolder.from.setTypeface(null, Typeface.BOLD);
                         messageViewHolder.subject.setTypeface(null, Typeface.BOLD);
                         messageViewHolder.from.setTextColor(ContextCompat.getColor(mContext, R.color.from));
                         messageViewHolder.subject.setTextColor(ContextCompat.getColor(mContext, R.color.subject));

                     }
                     break;
             }




        } catch (ParseException e) {
            e.printStackTrace();
        }


    }


    void applyProfilePicture(MessageViewHolder holder) {
        holder.imgProfile.setImageResource(R.drawable.bg_circle);
        holder.imgProfile.setColorFilter(Color.CYAN);
        holder.iconText.setVisibility(View.VISIBLE);
    }

    private void applyIconAnimation(MessageViewHolder holder, int position) {
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

        return consolidatedList != null ? consolidatedList.size() : 0;

    }

    @Override
    public int getItemViewType(int position) {
        // here your custom logic to choose the view type
        return consolidatedList.get(position).getType();
    }



    class DateViewHolder extends RecyclerView.ViewHolder{
        TextView date;

        public DateViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.dateTxtView);
        }


        public int getViewType() {
            return TYPE_DATE;
        }
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView from, subject, message, iconText, timestamp;
        ImageView iconImp, imgProfile, profilePicture, attachmentView;
        LinearLayout messageContainer;
        RelativeLayout iconContainer, iconBack, iconFront;

        MessageViewHolder(View view) {
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
            attachmentView = view.findViewById(R.id.AttachmentImage);

        }

        public int getViewType() {
            return TYPE_GENERAL;
        }

    }

    public Message getItem(int i) {
        return filteredData.get(i);
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

    /*public Message getItemAtPosition(int position){

        return filteredData.get(position);

    }*/

    public Message getItemAtPosition(int position){

        GeneralItem generalItem = (GeneralItem) consolidatedList.get(position);
        return  generalItem.message();


    }

    public abstract class ListItem {

        public static final int TYPE_DATE = 0;
        public static final int TYPE_GENERAL = 1;

        abstract public int getType();
    }

    public class GeneralItem extends ListItem {

        private Message message;

        public Message message() {
            return message;
        }

        public void setPojoOfJsonArray(Message message) {
            this.message = message;
        }

        @Override
        public int getType() {
            return TYPE_GENERAL;
        }
    }

    public class DateItem extends ListItem {

        private String date;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        @Override
        public int getType() {
            return TYPE_DATE;
        }


    }







}
