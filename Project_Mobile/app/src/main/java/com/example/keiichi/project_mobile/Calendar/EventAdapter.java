package com.example.keiichi.project_mobile.Calendar;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.keiichi.project_mobile.Contacts.ContactAdapter;
import com.example.keiichi.project_mobile.DAL.POJOs.Contact;
import com.example.keiichi.project_mobile.DAL.POJOs.Event;
import com.example.keiichi.project_mobile.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventAdapter extends BaseAdapter implements ListAdapter, Filterable {

    private final Context context;

    // Ongefilterde list
    private List<Event> originalData = null;
    // Gefilterde list
    private List<Event> filteredData = null;

    private EventAdapter.CustomFilter mFilter = new EventAdapter.CustomFilter();

    public EventAdapter(Context context, List<Event> values) {
        this.context = context;

        this.originalData = values;
        this.filteredData = values;
    }


    @Override
    public int getCount() {
        return filteredData.size();
    }

    @Override
    public Event getItem(int i) {
        return filteredData.get(i);
    }


    @Override
    public long getItemId(int i) {
        return i;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = layoutInflater.inflate(R.layout.event_items, parent, false);
        TextView preview = rowView.findViewById(R.id.previewBody);
        TextView header = rowView.findViewById(R.id.header);

        Event event = getItem(position);

        header.setText(event.getSubject());

        preview.setText(event.getStart().getDateTime());


        return rowView;

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
                    if (e.getBodyPreview().toLowerCase().contains(constraint)) {
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
