package com.example.myapp.ui.home;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.myapp.R;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Mis_Adapter extends RecyclerView.Adapter<Mis_Adapter.ViewHolder> {
    private List<User_Mis> dataList;

    Mis_Adapter(List<User_Mis> dataList) {
        this.dataList = dataList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_show_misson, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        String content = dataList.get(position);
        User_Mis um = dataList.get(position);
        holder.u_name.setText(um.getU_name());
        holder.u_title.setText(um.getU_title());
        holder.u_text.setText(um.getU_text());
        holder.u_date.setText(um.getU_date());

    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView u_name;
        private TextView u_title;
        private TextView u_text;
        private TextView u_date;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            u_name = itemView.findViewById(R.id.mis_user);
            u_title = itemView.findViewById(R.id.mis_tit);
            u_text = itemView.findViewById(R.id.mis_text);
            u_date = itemView.findViewById(R.id.mis_date);
        }
    }
}
