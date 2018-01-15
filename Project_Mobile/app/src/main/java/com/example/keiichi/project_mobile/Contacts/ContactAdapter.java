package com.example.keiichi.project_mobile.Contacts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.widget.Filter;
import android.widget.Filterable;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.keiichi.project_mobile.DAL.POJOs.Contact;
import com.example.keiichi.project_mobile.DAL.POJOs.Message;
import com.example.keiichi.project_mobile.Mail.MailAdapter;
import com.example.keiichi.project_mobile.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> implements Filterable {

    private ImageView profilePicture;
    private final Context context;
    private Contact contact;
    private SparseBooleanArray animeationItemsIndex;
    private SparseBooleanArray selectedItems;
    private boolean reverseAllAnimations = false;

    // Ongefilterde list
    private List<Contact> originalData = null;
    // Gefilterde list
    private List<Contact> filteredData = null;

    private ContactAdapter.CustomFilter mFilter = new ContactAdapter.CustomFilter();
    private String accessToken;

    public ContactAdapter(Context context, List<Contact> values, String accessToken) {
        this.context = context;
        this.originalData = values;
        this.filteredData = values;
        this.accessToken = accessToken;
        this.selectedItems = new SparseBooleanArray();
        this.animeationItemsIndex = new SparseBooleanArray();
    }

    public Contact getItem(int i) {
        return filteredData.get(i);
    }

    @Override
    public ContactAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_items, parent, false);
        return new ContactAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ContactAdapter.MyViewHolder holder, int position) {

        contact = getItem(position);

        String contactName = contact.getDisplayName();

        holder.contactName.setText(contactName);

        ColorGenerator generator = ColorGenerator.MATERIAL;

        int color2 = generator.getColor(contact.getDisplayName().substring(0,1));

        TextDrawable drawable1 = TextDrawable.builder()
                .buildRoundRect(contact.getDisplayName().toUpperCase().substring(0,1), color2, 3); // radius in px

        holder.profilePicture.setImageDrawable(drawable1);

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return filteredData.size();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView contactName;
        ImageView profilePicture;

        MyViewHolder(View view) {
            super(view);
            contactName = view.findViewById(R.id.contactName);
            profilePicture = view.findViewById(R.id.profilePicture);

        }

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

    public Contact getItemAtPosition(int position){

        return filteredData.get(position);

    }

}

