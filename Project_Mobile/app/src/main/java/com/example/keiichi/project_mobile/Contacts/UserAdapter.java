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
import com.example.keiichi.project_mobile.DAL.POJOs.User;
import com.example.keiichi.project_mobile.R;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> implements Filterable {

    private final Context context;
    private User user;
    private SparseBooleanArray animeationItemsIndex;
    private SparseBooleanArray selectedItems;
    private boolean reverseAllAnimations = false;

    // Ongefilterde list
    private List<User> originalData = null;
    // Gefilterde list
    private List<User> filteredData = null;

    private UserAdapter.CustomFilter mFilter = new UserAdapter.CustomFilter();

    public UserAdapter(Context context, List<User> values) {
        this.context = context;
        this.originalData = values;
        this.filteredData = values;
        this.selectedItems = new SparseBooleanArray();
        this.animeationItemsIndex = new SparseBooleanArray();
    }

    public User getItem(int i) {
        return filteredData.get(i);
    }


    @Override
    public UserAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_items, parent, false);
        return new UserAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserAdapter.MyViewHolder holder, int position) {

        user = getItem(position);

        String userName = user.getDisplayName();

        holder.userName.setText(userName);

        ColorGenerator generator = ColorGenerator.MATERIAL;

        int color2 = generator.getColor(user.getDisplayName().substring(0,1));

        TextDrawable drawable1 = TextDrawable.builder()
                .buildRoundRect(user.getDisplayName().toUpperCase().substring(0,1), color2, 3); // radius in px

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
        TextView userName;
        ImageView profilePicture;

        MyViewHolder(View view) {
            super(view);
            userName = view.findViewById(R.id.contactName);
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
                List<User> filt = new ArrayList<User>(); //filtered list
                for (int i = 0; i < originalData.size(); i++) {
                    User u = originalData.get(i);
                    if (u.getDisplayName().toLowerCase().contains(constraint)) {
                        filt.add(u); //add only items which matches
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
                setList((List<User>) results.values); // notify data set changed
            } else {
                setList((List<User>) originalData);
            }
        }
    }

    public void setList(List<User> data) {
        filteredData = data; // set the adapter list to data
        UserAdapter.this.notifyDataSetChanged(); // notify data set change
    }

    public User getItemAtPosition(int position){

        return filteredData.get(position);

    }
}

