package com.example.mylibrary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder>{

    private List<IdName> userList;
    private SetEmployeeName  setEmployeeName;
    private String identifier;

    public SearchAdapter(List<IdName> userList, SetEmployeeName setEmployeeName, String identifier) {
        this.userList = userList;
        this.setEmployeeName = setEmployeeName;
        this.identifier = identifier;
    }



    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.textView.setText(userList.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEmployeeName.setDataForUserSelectedItem(userList.get(position), position,identifier);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.stringName);
        }
    }

    public interface SetEmployeeName{
        void setDataForUserSelectedItem(IdName idName, int position, String identifier);
    }
}

