package com.example.mylibrary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;


public class SearchAdapter<T> extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder>{

    private final List<IdName> userList;
    private final SetEmployeeName  setEmployeeName;
    private final String identifier;

    /**
     * Method Name : SearchAdapter
     * Used For : constructor to pass list data and initialize interface
     */
    public SearchAdapter(List<IdName> userList, SetEmployeeName setEmployeeName, String identifier) {
        this.userList = userList;
        this.setEmployeeName = setEmployeeName;
        this.identifier = identifier;
    }

    /**
     * Method Name : onCreateViewHolder
     * Used For : to inflate the item layout
     *
     * @param parent
     * @param viewType
     */
    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        return new SearchViewHolder(view);
    }

    /**
     * Method Name : onBindViewHolder
     * Used For : to handle necessary logics and set list data to recyclerview
     *
     * @param position
     * @param holder
     */
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

    /**
     * Method Name : getItemCount
     * Used For : to get the size of list
     */
    @Override
    public int getItemCount() {
        return userList.size();
    }

    /**
     * Method Name : SearchViewHolder
     * Used For : to declare & initialize fields in item layout
     */
    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.stringName);
        }
    }

    /**
     * Method Name : SetEmployeeName
     * Used For : interface to pass data to whichever class it is implemented
     */
    public interface SetEmployeeName{
        void setDataForUserSelectedItem(IdName idName, int position, String identifier);
    }
}

