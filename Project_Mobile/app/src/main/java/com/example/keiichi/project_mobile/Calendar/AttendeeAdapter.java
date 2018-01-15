package com.example.keiichi.project_mobile.Calendar;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.keiichi.project_mobile.DAL.POJOs.Attendee;
import com.example.keiichi.project_mobile.DAL.POJOs.Event;
import com.example.keiichi.project_mobile.R;

import java.util.List;

public class AttendeeAdapter extends BaseAdapter implements ListAdapter {

    private final Context context;

    // Ongefilterde list
    private List<Attendee> originalData = null;

    public AttendeeAdapter(Context context, List<Attendee> values) {
        this.context = context;

        this.originalData = values;
    }

    @Override
    public int getCount() {
        return originalData.size();
    }

    @Override
    public Attendee getItem(int i) {
        return originalData.get(i);
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
        View rowView = layoutInflater.inflate(R.layout.attendee_items, parent, false);

        Attendee attendee = getItem(position);

        TextView attendeeEmail = rowView.findViewById(R.id.attendee);

        attendeeEmail.setText(attendee.getEmailAddress().getName());

        return rowView;
    }
}
