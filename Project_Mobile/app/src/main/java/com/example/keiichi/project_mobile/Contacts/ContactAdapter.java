package com.example.keiichi.project_mobile.Contacts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import android.widget.Filter;
import android.widget.Filterable;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.keiichi.project_mobile.DAL.POJOs.Contact;
import com.example.keiichi.project_mobile.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContactAdapter extends BaseAdapter implements ListAdapter, Filterable {

    private TextDrawable drawable;
    private ImageView profilePicture;
    private final Context context;
    //private final JSONArray values;

    // Ongefilterde list
    private List<Contact> originalData = null;
    // Gefilterde list
    private List<Contact> filteredData = null;

    private CustomFilter mFilter = new CustomFilter();

    public ContactAdapter(Context context, List<Contact> values) {
        this.context = context;

        this.originalData = values;
        this.filteredData = values;
    }


    @Override
    public int getCount() {
        return filteredData.size();
    }

    @Override
    public Contact getItem(int i) {
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
        View rowView = layoutInflater.inflate(R.layout.contact_items, parent, false);
        TextView name = rowView.findViewById(R.id.contactName);

        profilePicture = (ImageView)rowView.findViewById(R.id.profilePicture);

        Contact contact = getItem(position);

        name.setText(contact.getDisplayName());

        ColorGenerator generator = ColorGenerator.MATERIAL;

        int color2 = generator.getColor(contact.getDisplayName().substring(0,1));

        TextDrawable drawable1 = TextDrawable.builder()
                .buildRoundRect(contact.getDisplayName().substring(0,1), color2, 3); // radius in px

        profilePicture.setImageDrawable(drawable1);

        return rowView;

    }

    public Filter getFilter() {
        // return mFilter;
        if (mFilter == null)
            mFilter = new CustomFilter();
        return mFilter;
    }

    private class CustomFilter extends Filter {
        // called when adapter filter method is called
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (constraint != null && constraint.toString().length() > 0) {
                List<Contact> filt = new ArrayList<Contact>(); //filtered list
                for (int i = 0; i < originalData.size(); i++) {
                    Contact c = originalData.get(i);
                    if (c.getDisplayName().toLowerCase().contains(constraint)) {
                        filt.add(c); //add only items which matches
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
                setList((List<Contact>) results.values); // notify data set changed
            } else {
                setList((List<Contact>) originalData);
            }
        }
    }

    public void setList(List<Contact> data) {
        filteredData = data; // set the adapter list to data
        ContactAdapter.this.notifyDataSetChanged(); // notify data set change
    }


}

