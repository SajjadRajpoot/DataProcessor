package com.example.spytech;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;
    Context context;

    public UserAdapter(List<User> userList, Context context) {

        this.userList = userList;
        this.context=context;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.tvMsisdn.setText(user.getMsisdn());
        holder.tvUserName.setText(user.getUsername());
        holder.tvCnic.setText( user.getCnic());
        holder.tvAddress.setText(user.getAddress());
        holder.tvComany.setText(user.getCompany());
        holder.tvCnic.setOnClickListener(view ->  {
            Intent intent = new Intent(context,UserCNICActivity.class);
            intent.putExtra("userCnic",user.getCnic());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void updateList(List<User> newList) {
        userList = new ArrayList<>();
        userList.addAll(newList);
        notifyDataSetChanged();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{
    //implements View.OnClickListener{
        public TextView tvMsisdn, tvUserName, tvCnic, tvAddress, tvComany;

        public UserViewHolder(View view) {
            super(view);
            tvMsisdn = view.findViewById(R.id.txtMsisdn);
            tvUserName = view.findViewById(R.id.txtName);
            tvCnic = view.findViewById(R.id.txtCnic);
            tvAddress = view.findViewById(R.id.txtAddress);
            tvComany = view.findViewById(R.id.txtCompany);
            //view.setOnClickListener(this);
        }

        /*@Override
        public void onClick(View v) {
            *//*Toast.makeText(v.getContext(),
                    String.format("Clicked on position %d",getAdapterPosition()),
                    Toast.LENGTH_SHORT).show();*//*
            Intent intent = new Intent(context,UserCNICActivity.class);
            intent.putExtra("userCnic",tvCnic.getText());
            context.startActivity(intent);
        }*/

    }
}

