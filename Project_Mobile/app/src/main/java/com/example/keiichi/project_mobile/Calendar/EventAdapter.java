package com.example.keiichi.project_mobile.Calendar;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.keiichi.project_mobile.Contacts.ContactAdapter;
import com.example.keiichi.project_mobile.DAL.POJOs.Contact;
import com.example.keiichi.project_mobile.DAL.POJOs.Event;
import com.example.keiichi.project_mobile.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> implements Filterable {

    private final Context context;
    private Event event;
    private SparseBooleanArray animeationItemsIndex;
    private SparseBooleanArray selectedItems;
    private boolean reverseAllAnimations = false;

    // Ongefilterde list
    private List<Event> originalData = null;
    // Gefilterde list
    private List<Event> filteredData = null;

    private EventAdapter.CustomFilter mFilter = new EventAdapter.CustomFilter();

    public EventAdapter(Context context, List<Event> values) {
        this.context = context;
        this.originalData = values;
        this.filteredData = values;
        this.selectedItems = new SparseBooleanArray();
        this.animeationItemsIndex = new SparseBooleanArray();
    }

    public Event getItem(int i) {
        return filteredData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return filteredData.size();
    }

    public Event getItemAtPosition(int position){

        return filteredData.get(position);

    }

    @Override
    public EventAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_items, parent, false);
        return new EventAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EventAdapter.MyViewHolder holder, int position) {

        event = getItem(position);

        holder.header.setText(event.getSubject());

        //preview.setText(event.getStart().getDateTime());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date d = null;
        try {
            d = sdf.parse(event.getStart().getDateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.previewBody.setText(output.format(d));

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView header;
        TextView previewBody;

        MyViewHolder(View view) {
            super(view);
            header = view.findViewById(R.id.header);
            previewBody = view.findViewById(R.id.previewBody);

        }

    }

    public Filter getFilter() {
        // return mFilter;
        if (mFilter == null)
            mFilter = new EventAdapter.CustomFilter();
        return mFilter;
    }

    private class CustomFilter extends Filter {
        // called when adapter filter method is called
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();

            if (constraint != null && constraint.toString().length() > 0) {

                List<Event> filt = new ArrayList<Event>(); //filtered list
                for (int i = 0; i < originalData.size(); i++) {
                    Event e = originalData.get(i);
                    if (e.getSubject().toLowerCase().contains(constraint)) {
                        filt.add(e); //add only items which matches
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
                setList((List<Event>) results.values); // notify data set changed
            } else {
                setList((List<Event>) originalData);
            }
        }
    }

    public void setList(List<Event> data) {
        filteredData = data; // set the adapter list to data
        EventAdapter.this.notifyDataSetChanged(); // notify data set change
    }

}
