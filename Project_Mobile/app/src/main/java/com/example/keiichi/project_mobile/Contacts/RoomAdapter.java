package com.example.keiichi.project_mobile.Contacts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.keiichi.project_mobile.DAL.POJOs.Contact;
import com.example.keiichi.project_mobile.DAL.POJOs.EmailAddress;
import com.example.keiichi.project_mobile.R;

import java.util.ArrayList;
import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.MyViewHolder> implements Filterable {

    private final Context context;
    private EmailAddress room;
    private SparseBooleanArray animeationItemsIndex;
    private SparseBooleanArray selectedItems;
    private boolean reverseAllAnimations = false;

    // Ongefilterde list
    private List<EmailAddress> originalData = null;
    // Gefilterde list
    private List<EmailAddress> filteredData = null;

    private RoomAdapter.CustomFilter mFilter = new RoomAdapter.CustomFilter();
    private String accessToken;

    public RoomAdapter(Context context, List<EmailAddress> values) {
        this.context = context;
        this.originalData = values;
        this.filteredData = values;
        this.selectedItems = new SparseBooleanArray();
        this.animeationItemsIndex = new SparseBooleanArray();
    }



    public EmailAddress getItem(int i) {
        return filteredData.get(i);
    }


    @Override
    public RoomAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_items, parent, false);
        return new RoomAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RoomAdapter.MyViewHolder holder, int position) {

        room = getItem(position);

        String roomName = room.getName();

        holder.roomName.setText(roomName);

        ColorGenerator generator = ColorGenerator.MATERIAL;

        int color2 = generator.getColor(room.getName().substring(0,1));

        TextDrawable drawable1 = TextDrawable.builder()
                .buildRoundRect(room.getName().toUpperCase().substring(0,1), color2, 3); // radius in px

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
        TextView roomName;
        ImageView profilePicture;

        MyViewHolder(View view) {
            super(view);
            roomName = view.findViewById(R.id.contactName);
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
                List<EmailAddress> filt = new ArrayList<EmailAddress>(); //filtered list
                for (int i = 0; i < originalData.size(); i++) {
                    EmailAddress c = originalData.get(i);
                    if (c.getName().toLowerCase().contains(constraint) || c.getAddress().toLowerCase().contains(constraint)) {
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
                setList((List<EmailAddress>) results.values); // notify data set changed
            } else {
                setList((List<EmailAddress>) originalData);
            }
        }
    }

    public void setList(List<EmailAddress> data) {
        filteredData = data; // set the adapter list to data
        RoomAdapter.this.notifyDataSetChanged(); // notify data set change
    }

    public EmailAddress getItemAtPosition(int position){

        return filteredData.get(position);

    }
}
