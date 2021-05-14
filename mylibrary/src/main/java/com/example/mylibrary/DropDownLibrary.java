package com.example.mylibrary;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class DropDownLibrary implements SearchAdapter.SetEmployeeName{


    private SetDataOnItemSelection setDataOnItemSelection;
    private CallApi callApi;
    private RecyclerView mListView;
    private boolean mIsLoading = false;
    private LinearLayoutManager linearLayoutManager;
    private List<IdName> idName;
    private SearchAdapter searchAdapter;
    private PopupWindow mPopupWindow;
    private SearchView searchView;
    private View popupView;
    private String identifier;
    private String searckKey;

    public void main(Activity context, String identifier,SetDataOnItemSelection setDataOnItemSelection, CallApi callApi) {
        this.setDataOnItemSelection = setDataOnItemSelection;
        this.callApi = callApi;
        this.identifier = identifier;
        initializePopup(context);
        showList();
        searchView();
        lazyLoading();
    }

    public void updateList(List<IdName> idName){
        mIsLoading = true;
        this.idName = idName;
        if(searchAdapter == null) {
            searchAdapter = new SearchAdapter(idName, DropDownLibrary.this, identifier);
            mListView.setAdapter(searchAdapter);
            mIsLoading = false;
        }else {
            loadMore();
        }
    }

    /**
     * Method Name : showList
     * Used For : to show all the list in popup
     */
    private void showList() {
        mPopupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
    }

    private void initializePopup(Activity context){

        popupView = LayoutInflater.from(context).inflate(R.layout.popup_showlist, null);

        mPopupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);

        mListView = popupView.findViewById(R.id.items_list);
        linearLayoutManager = new LinearLayoutManager(context);
        mListView.setLayoutManager(linearLayoutManager);

        searchView = popupView.findViewById(R.id.search);

    }

    private void searchView(){
        searchView.setActivated(true);
        searchView.setQueryHint("Search & Select");
        searchView.onActionViewExpanded();
        searchView.setIconified(false);
        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searckKey = query;
                callApi.callApi(query,0,true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searckKey = newText;
                callApi.callApi(newText,0,true);
                return false;
            }
        });
    }

    /*Method to lazy loading*/
    private void lazyLoading() {
        mListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (!mIsLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == idName.size() - 1) {
                        callApi.callApi(searckKey,idName.size(),false);
                        mIsLoading = true;
                    }
                }

            }
        });
    }

    /*Method to load more data
     *@param 1 : response(JSONObject)
     */
    private void loadMore() {
        ViewCompat.setNestedScrollingEnabled(mListView, true);
        int current_pos = idName.size();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                searchAdapter.notifyDataSetChanged();

                mIsLoading = false;
                ViewCompat.setNestedScrollingEnabled(mListView, false);

                searchAdapter.notifyItemInserted(current_pos + 1);
            }
        },2000);
    }


    @Override
    public void setDataForUserSelectedItem(IdName idName, int position, String identifier) {
        setDataOnItemSelection.onItemSelected(idName,position,identifier);
        mPopupWindow.dismiss();
    }

    public interface SetDataOnItemSelection{
        void onItemSelected(IdName idName, int position, String identifier);
    }

    public interface CallApi{
        void callApi(String searchKey, int skip,boolean search);
    }
}
