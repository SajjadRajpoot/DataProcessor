package com.example.spytech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SubscriberAdapter extends RecyclerView.Adapter<SubscriberAdapter.ViewHolder>{
    private List<Subscriber> subscriberList;

    public SubscriberAdapter(List<Subscriber> subscriberList) {
        this.subscriberList = subscriberList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Subscriber subscriber = subscriberList.get(position);
        holder.textId.setText(subscriber.getMsisdn());
        holder.textName.setText(subscriber.getName());
        holder.textcnic.setText(subscriber.getCnic());
        holder.textAddress.setText(subscriber.getAddress());
        holder.textCompany.setText(subscriber.getCompany());

    }

    @Override
    public int getItemCount() {
        return subscriberList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textId, textName,textcnic,textAddress,textCompany;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textId = itemView.findViewById(R.id.txtMsisdn);
            textName = itemView.findViewById(R.id.txtName);
            textcnic = itemView.findViewById(R.id.txtCnic);
            textAddress = itemView.findViewById(R.id.txtAddress);
            textCompany = itemView.findViewById(R.id.txtCompany);
        }
    }
}
