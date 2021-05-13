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
    private static Context mContext;
    private static IdName idName;
    private static int position;
    private static String identifier;
    private static RecyclerView mListView;
    private static PopupWindow mPopupWindow;

    public static void main(Context context,String searchKey, List<IdName> idName, String identifier, SearchAdapter.SetEmployeeName activity) {
        mContext = context;
        SearchAdapter searchAdapter = new SearchAdapter(idName, activity, identifier);
        mListView.setAdapter(searchAdapter);
    }

    /**
     * Method Name : showList
     * Used For : to show all the list in popup
     *
     * @param view
     */
    public static void showList(View view, String api) {
        View popupView = LayoutInflater.from(mContext).inflate(R.layout.popup_showlist, null);
        mPopupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);

        mListView = popupView.findViewById(R.id.items_list);
        SearchView searchView = popupView.findViewById(R.id.search);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
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

        mPopupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
    }

    @Override
    public void setDataForUserSelectedItem(IdName idName, int position, String identifier) {
        this.idName = idName;
        this.position = position;
        this.identifier = identifier;
    }

    public interface SetDataOnItemSelection{
        void onItemSelectedd(IdName idName, int position,String identifier);
    }
}
