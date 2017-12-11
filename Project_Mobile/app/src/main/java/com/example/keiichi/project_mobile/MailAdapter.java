package com.example.keiichi.project_mobile;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Keiichi on 6/12/2017.
 */

public class MailAdapter extends RecyclerView.Adapter<MailAdapter.MyViewHolder> {
    private JSONArray jsonArray;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mail_items, parent,false);
        return new MailAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MailAdapter.MyViewHolder holder, int position) {
        try {
            JSONObject mailObject = jsonArray.getJSONObject(position);
            holder.bodyPreview.setText(mailObject.getString("bodyPreview"));
            holder.subject.setText(mailObject.getString("subject"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView subject,bodyPreview;
        public MyViewHolder(View view){
            super(view);
            this.subject = view.findViewById(R.id.previewBody);
            this.bodyPreview = view.findViewById(R.id.header);
        }

    }

    public MailAdapter(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

}
