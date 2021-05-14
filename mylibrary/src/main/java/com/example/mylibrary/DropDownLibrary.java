package com.example.mylibrary;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DropDownLibrary implements SearchAdapter.SetEmployeeName{

    private SetDataOnItemSelection setDataOnItemSelection;

    public void main(Context context, List<IdName> idName, String identifier, SetDataOnItemSelection setDataOnItemSelection) {
        this.setDataOnItemSelection = setDataOnItemSelection;
        showList(context,idName,identifier);
    }

    /**
     * Method Name : showList
     * Used For : to show all the list in popup
     *
     * @param context
     * @param idName
     * @param identifier
     */
    private void showList(Context context,List<IdName> idName,String identifier) {
        View popupView = LayoutInflater.from(context).inflate(R.layout.popup_showlist, null);
        PopupWindow mPopupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);

        RecyclerView mListView = popupView.findViewById(R.id.items_list);
        SearchView searchView = popupView.findViewById(R.id.search);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        mListView.setLayoutManager(linearLayoutManager);

        searchView.setActivated(true);
        searchView.setQueryHint("Search & Select");
        searchView.onActionViewExpanded();
        searchView.setIconified(false);
        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        SearchAdapter searchAdapter = new SearchAdapter(idName,DropDownLibrary.this, identifier);
        mListView.setAdapter(searchAdapter);

        mPopupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
    }

    @Override
    public void setDataForUserSelectedItem(IdName idName, int position, String identifier) {
        setDataOnItemSelection.onItemSelected(idName,position,identifier);
    }

    public interface SetDataOnItemSelection{
        void onItemSelected(IdName idName, int position,String identifier);
    }
}
