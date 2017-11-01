package com.example.ankurkhandelwal.datahungry.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.ankurkhandelwal.datahungry.R;
import com.example.ankurkhandelwal.datahungry.Listeners.ContactFilterListener;
import com.example.ankurkhandelwal.datahungry.Models.Contact;
import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends Adapter<ContactAdapter.ViewHolder> implements Filterable {
    private String TAG = "ContactAdapter";
    private Activity activity;
    private List<Contact> contactArrayList;
    private ContactFilterListener contactFilterListener;
    private ArrayFilter mFilter;
    private ArrayList<Contact> newcontactList;

    private class ArrayFilter extends Filter {
        private final Object lock;

        private ArrayFilter() {
            this.lock = new Object();
        }

        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (ContactAdapter.this.newcontactList == null) {
                synchronized (this.lock) {
                    ContactAdapter.this.newcontactList = new ArrayList(ContactAdapter.this.contactArrayList);
                }
            }
            if (constraint == null || constraint.length() == 0) {
                synchronized (this.lock) {
                    ArrayList<Contact> list = new ArrayList(ContactAdapter.this.newcontactList);
                    results.values = list;
                    results.count = list.size();
                }
            } else {
                String prefixString = constraint.toString().toLowerCase();
                ArrayList<Contact> values = ContactAdapter.this.newcontactList;
                int count = values.size();
                ArrayList<Contact> newValues = new ArrayList(count);
                for (int i = 0; i < count; i++) {
                    String phone = ((Contact) values.get(i)).mobile;
                    String name = ((Contact) values.get(i)).name;
                    if (phone.toLowerCase().contains(prefixString) || name.toLowerCase().contains(prefixString)) {
                        newValues.add(new Contact(((Contact) values.get(i)).id, ((Contact) values.get(i)).name, ((Contact) values.get(i)).mobile, ((Contact) values.get(i)).contact_id));
                    }
                }
                results.values = newValues;
                results.count = newValues.size();
            }
            return results;
        }

        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                ContactAdapter.this.contactArrayList = (ArrayList) results.values;
            } else {
                ContactAdapter.this.contactArrayList = new ArrayList();
            }
            if (results.count > 0) {
                ContactAdapter.this.notifyDataSetChanged();
                ContactAdapter.this.contactFilterListener.showLayout(1);
                return;
            }
            ContactAdapter.this.notifyDataSetChanged();
            ContactAdapter.this.contactFilterListener.showLayout(0);
        }
    }

    class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        TextView sms_content;
        TextView sms_sender;
        TextView sms_timestamp;
        RelativeLayout top_layout;

        ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            this.sms_sender = (TextView) itemLayoutView.findViewById(R.id.sms_sender);
            this.sms_content = (TextView) itemLayoutView.findViewById(R.id.sms_content);
            this.sms_timestamp = (TextView) itemLayoutView.findViewById(R.id.sms_timestamp);
            this.top_layout = (RelativeLayout) itemLayoutView.findViewById(R.id.top_layout);
        }
    }

    public ContactAdapter(Activity activity, List<Contact> data, ContactFilterListener contactFilterListener) {
        this.activity = activity;
        this.contactArrayList = data;
        this.newcontactList = new ArrayList(this.contactArrayList);
        this.contactFilterListener = contactFilterListener;
    }

    public ContactAdapter(Activity activity, List<Contact> data) {
        this.activity = activity;
        this.contactArrayList = data;
        this.newcontactList = new ArrayList(this.contactArrayList);
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(this.activity.getApplicationContext()).inflate(R.layout.single_sms_layout, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.sms_content.setText("contact : " + ((Contact) this.contactArrayList.get(position)).mobile);
        holder.sms_timestamp.setText("");
        holder.sms_sender.setText("name : " + ((Contact) this.contactArrayList.get(position)).name);
        if (this.activity.getLocalClassName().equals("ContactDetailActivity")) {
            holder.top_layout.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    ContactAdapter.this.contactFilterListener.onContactClick((Contact) ContactAdapter.this.contactArrayList.get(position));
                }
            });
        }
    }

    public int getItemCount() {
        return this.contactArrayList.size();
    }

    public Filter getFilter() {
        if (this.mFilter == null) {
            this.mFilter = new ArrayFilter();
        }
        return this.mFilter;
    }
}
