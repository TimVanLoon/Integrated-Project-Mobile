package com.example.keiichi.project_mobile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ContactAdapter extends BaseAdapter implements ListAdapter {

    private final Context context;
    private final JSONArray values;

    public ContactAdapter(Context context, JSONArray values) {
        this.context = context;
        this.values = values;
    }


    @Override
    public int getCount() {
        return values.length();
    }

    @Override
    public JSONObject getItem(int i) {
        return values.optJSONObject(i);
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
}
