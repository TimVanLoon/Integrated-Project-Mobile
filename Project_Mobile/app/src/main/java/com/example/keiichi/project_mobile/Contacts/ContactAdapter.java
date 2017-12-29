package com.example.keiichi.project_mobile.Contacts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
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

import com.example.keiichi.project_mobile.DAL.POJOs.Contact;
import com.example.keiichi.project_mobile.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContactAdapter extends BaseAdapter implements ListAdapter, Filterable {

    private final Context context;
    //private final JSONArray values;

    // Ongefilterde JSONArray
    private List<Contact> originalData = null;
    // Gefilterde JSONArray
    private List<Contact> filteredData = null;

    private ItemFilter mFilter = new ItemFilter();

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

        Contact contact = getItem(position);

        name.setText(contact.getDisplayName());


        return rowView;

    }

    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Contact> list = originalData;

            int count = list.size();
            final List<Contact> nlist = new ArrayList<Contact>(count);

            Contact filterableContact ;

            for (int i = 0; i < count; i++) {
                filterableContact = list.get(i);
                if (filterableContact.getDisplayName().contains(filterString) || filterableContact.getDisplayName().startsWith(filterString )) {
                    nlist.add(filterableContact);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults)
        {
            filteredData = (List<Contact>) filterResults.values;
            notifyDataSetChanged();
        }

    }



}

