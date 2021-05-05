package com.adisalagic.journal;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class EntryListAdapter extends RecyclerView.Adapter<EntryListAdapter.EntryViewHolder> {


    private ArrayList<Entry> entries = new ArrayList<>();
    private ChangeListener   changeListener;

    @NonNull
    @Override
    public EntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_viewholder, parent, false);
        return new EntryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EntryViewHolder holder, int position) {
        Entry entry = entries.get(position);
        holder.service.setText(entry.getService());
        holder.smallInfo.setText(entry.getExtra());
        holder.price.setText(entry.getPrice());
        holder.date.setText(entry.getDayOfVisit());
        holder.id = entry.getId();
        holder.idPeople = entry.getIdPeople();
        holder.changeListener = changeListener;
        if (entry.getExtra().isEmpty()) {
            holder.smallInfo.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    static class EntryViewHolder extends RecyclerView.ViewHolder {
        TextView price, date, smallInfo, service;
        int idPeople, id;
        ChangeListener changeListener;

        public EntryViewHolder(@NonNull View itemView) {
            super(itemView);
            price = itemView.findViewById(R.id.price);
            date = itemView.findViewById(R.id.date);
            smallInfo = itemView.findViewById(R.id.small_info);
            service = itemView.findViewById(R.id.service);
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), ViewActivity.class);
                intent.putExtra("idPeople", idPeople);
                intent.putExtra("id", id);
                v.getContext().startActivity(intent);
            });
            itemView.setOnLongClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                builder
                        .setMessage("Что вы хотите сделать?")
                        .setPositiveButton("Удалить", (dialog, which) -> {
                            changeListener.onDeleteListener(id);
                        })
                        .setNegativeButton("Изменить", (dialog, which) -> {
                            changeListener.onChangeListener(id);
                        })
                        .show();
                return false;
            });
        }
    }

    public void changeArray(ArrayList<Entry> entries) {
        this.entries.clear();
        this.entries.addAll(entries);
        notifyDataSetChanged();
    }

    public final void addEntry(Entry entry) {
        entries.add(entry);
        notifyDataSetChanged();
    }


    public void addChangeListener(ChangeListener changeListener){
        this.changeListener = changeListener;
    }

    public EntryListAdapter(ArrayList<Entry> entries, ChangeListener changeListener) {
        this.entries = entries;
        this.changeListener = changeListener;
    }

    public EntryListAdapter(ArrayList<Entry> entries) {
        this.entries = entries;
        Collections.reverse(this.entries);
        changeListener = new ChangeListener() {
        };
    }
}
