package com.example.keiichi.project_mobile.Contacts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.TextView;

import android.widget.Filter;
import android.widget.Filterable;

import com.example.keiichi.project_mobile.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ContactAdapter extends BaseAdapter implements ListAdapter, Filterable{

    private final Context context;
    //private final JSONArray values;

    // Ongefilterde JSONArray
    private JSONArray originalData = null;
    // Gefilterde JSONArray
    private JSONArray filteredData = null;

    private ItemFilter mFilter = new ItemFilter();


    public ContactAdapter(Context context, JSONArray values) {
        this.context = context;
        this.originalData = values;
        this.filteredData = values;
    }


    @Override
    public int getCount() {
        return filteredData.length();
    }

    @Override
    public JSONObject getItem(int i) {
        return filteredData.optJSONObject(i);
    }

    @Override
    public long getItemId(int i) {
        JSONObject jsonObject = getItem(i);
        return jsonObject.optLong("id");
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = layoutInflater.inflate(R.layout.contact_items, parent, false);
        TextView name = rowView.findViewById(R.id.contactName);

        JSONObject json_data = getItem(position);

        try {
            name.setText(json_data.getString("givenName"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return rowView;

    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final JSONArray list = originalData;

            int count = list.length();
            final ArrayList<String> nlist = new ArrayList<String>(count);

            String filterableString ;

            for (int i = 0; i < count; i++) {
                try {
                    filterableString = list.getJSONObject(i).toString();

                    if (filterableString.toLowerCase().contains(filterString)) {
                        nlist.add(filterableString);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            filteredData =  (JSONArray) results.values;
            notifyDataSetChanged();
        }

    }


}

