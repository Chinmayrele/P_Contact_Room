package com.codingproject.contactroom.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.codingproject.contactroom.R;
import com.codingproject.contactroom.model.Contact;

import java.util.List;
import java.util.Objects;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private onContactClickListener contactClickListener;
    // 1) DATA-SOURCE
    private final List<Contact> contactList;
    private final Context context;

    public RecyclerViewAdapter(List<Contact> contactList, Context context, onContactClickListener onContactClickListener) {
        this.contactList = contactList;
        this.context = context;
        this.contactClickListener = onContactClickListener;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_row, parent, false);
        return new ViewHolder(view, contactClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        final Contact contact = Objects.requireNonNull(contactList).get(position);
        holder.name.setText(contact.getName());
        holder.occupation.setText(contact.getOccupation());

    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(contactList).size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        onContactClickListener onContactClickListener;
        public TextView name;
        public TextView occupation;
        public ViewHolder(@NonNull View itemView, onContactClickListener onContactClickListener) {
            super(itemView);
            name = itemView.findViewById(R.id.row_name_textView);
            occupation = itemView.findViewById(R.id.row_occupation_textView);
            this.onContactClickListener = onContactClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onContactClickListener.onContactClick(getAdapterPosition());
//            Log.d("TAG", "onClick: inside");
        }
    }

    public interface onContactClickListener {
        void onContactClick(int position);
    }
}
