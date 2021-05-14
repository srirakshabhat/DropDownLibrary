package com.example.mylibrary;

import android.app.Activity;
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

public class DropDownLibrary<T> implements SearchAdapter.SetEmployeeName{

    private SetDataOnItemSelection setDataOnItemSelection;
    private CallApi callApi;
    private RecyclerView mListView;
    private boolean mIsLoading;
    private LinearLayoutManager linearLayoutManager;
    private List<T> idName;
    private SearchAdapter searchAdapter;
    private PopupWindow mPopupWindow;
    private SearchView searchView;
    private View popupView;

    public void main(Activity context, List<T> idName, String identifier, SetDataOnItemSelection setDataOnItemSelection,CallApi callApi,boolean mShowingListForFirstTime) {
        this.setDataOnItemSelection = setDataOnItemSelection;
        this.callApi = callApi;
        this.idName = idName;
        if(mShowingListForFirstTime)
            showList(context);
        searchView();
        setData(identifier);
        lazyLoading();
    }

    /**
     * Method Name : showList
     * Used For : to show all the list in popup
     *  @param context
     */
    private void showList(Activity context) {
        popupView = LayoutInflater.from(context).inflate(R.layout.popup_showlist, null);
        mPopupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);

        mListView = popupView.findViewById(R.id.items_list);
        linearLayoutManager = new LinearLayoutManager(context);
        mListView.setLayoutManager(linearLayoutManager);

        mPopupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
    }

    private void searchView(){
        searchView = popupView.findViewById(R.id.search);

        searchView.setActivated(true);
        searchView.setQueryHint("Search & Select");
        searchView.onActionViewExpanded();
        searchView.setIconified(false);
        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                callApi.callApi(query,0,true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                callApi.callApi(newText,0,true);
                return false;
            }
        });
    }

    private void setData(String identifier){
        if(searchAdapter == null) {
            searchAdapter = new SearchAdapter(idName, DropDownLibrary.this, identifier);
            mListView.setAdapter(searchAdapter);
        }else
            loadMore();
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
                        callApi.callApi("",idName.size(),false);
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
    public void setDataForUserSelectedItem(String idName, int position, String identifier) {
        setDataOnItemSelection.onItemSelected(idName,position,identifier);
        mPopupWindow.dismiss();
    }

    public interface SetDataOnItemSelection{
        void onItemSelected(String idName, int position, String identifier);
    }

    public interface CallApi{
        void callApi(String searchKey, int skip,boolean search);
    }
}
