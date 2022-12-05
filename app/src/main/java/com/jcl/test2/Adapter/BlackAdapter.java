package com.jcl.test2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jcl.test2.R;
import com.jcl.test2.pojo.BlackInfo;

import java.util.List;

public class BlackAdapter extends RecyclerView.Adapter<BlackAdapter.ViewHolder> {

    private List<BlackInfo> list;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView black_number;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            black_number = itemView.findViewById(R.id.black_number);


        }
    }
    public BlackAdapter(List<BlackInfo> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_black,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BlackInfo blackInfo =  list.get(position);
        holder.black_number.setText(blackInfo.getNumber());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
