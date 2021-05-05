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
import java.util.Comparator;

public class PeopleListAdapter extends RecyclerView.Adapter<PeopleListAdapter.PeopleHolder> {
    ArrayList<Customer> customers;
    private ChangeListener onChangeListener;

    PeopleListAdapter(@NonNull ArrayList<Customer> customers, ChangeListener listener){
        this.customers = customers;
        onChangeListener = listener;
    }

    @NonNull
    @Override
    public PeopleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.people_viewholder, parent, false);
        return new PeopleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleHolder holder, int position) {
        sort();
        holder.textView.setText(customers.get(position).getFullName());
        holder.id = customers.get(position).getId();
        holder.changeListener = onChangeListener;
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }

    public void addCustomer(Customer customer){
        customers.add(customer);
        notifyDataSetChanged();
    }

    public void sort(){
        Comparator<Customer> customerComparator = (o1, o2) -> {
            String fs = o1.getSurName().toLowerCase();
            String ss = o2.getSurName().toLowerCase();
            if (fs.equals("") && fs.equals("")){
                return 0;
            }
            if (fs.equals("") && !ss.equals("")){
                return ss.charAt(0);
            }
            if (!fs.equals("") && ss.equals("")){
                return fs.charAt(0);
            }
            int f = (int) fs.charAt(0);
            int s = (int) ss.charAt(0);
            int i = 0;
            int res = f - s;
            while (f == s){
                i++;
                if (i < Math.min(o1.getSurName().length(), o2.getSurName().length())){
                    f = (int) o1.getSurName().toLowerCase().charAt(i);
                    s = (int) o2.getSurName().toLowerCase().charAt(i);
                    res = f - s;
                }else {
                    break;
                }
            }
            return res;
        };
        Customer[] cstmrs = new Customer[customers.size()];
        cstmrs = customers.toArray(cstmrs);
        Arrays.sort(cstmrs, customerComparator);
        customers.clear();
        customers.addAll(Arrays.asList(cstmrs));
    }

    public void changeArray(ArrayList<Customer> customers){
        this.customers.clear();
        this.customers.addAll(customers);
        notifyDataSetChanged();
    }

    static class PeopleHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ChangeListener changeListener;
        int id;
        public PeopleHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.name);
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
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), CustomerActivity.class);
                intent.putExtra("name", textView.getText());
                intent.putExtra("id", id);
                itemView.getContext().startActivity(intent);
            });
        }
    }
}
