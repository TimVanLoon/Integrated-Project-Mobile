package com.example.keiichi.project_mobile.Mail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.keiichi.project_mobile.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Keiichi on 6/12/2017.
 */

public class MailAdapter extends BaseAdapter implements ListAdapter {
    private final Context context;
    private final JSONArray values;

    public MailAdapter(Context context, JSONArray values) {
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
        View rowView = layoutInflater.inflate(R.layout.mail_items, parent, false);
        TextView header = rowView.findViewById(R.id.previewBody);
        TextView preview = rowView.findViewById(R.id.header);

        JSONObject json_data = getItem(position);

        try {
            header.setText(json_data.getString("bodyPreview"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            preview.setText(json_data.getString("subject"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rowView;

    }
}
