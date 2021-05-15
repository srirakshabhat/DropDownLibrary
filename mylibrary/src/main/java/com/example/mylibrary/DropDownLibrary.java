package com.example.mylibrary;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
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

    private PopupWindow mPopupWindow;
    private SearchView searchView;
    private View popupView;

    private LinearLayoutManager linearLayoutManager;
    private SearchAdapter searchAdapter;
    private RecyclerView mListView;

    private List<IdName> idName;
    private String mSearchKey;
    private boolean mIsLoading = false;

    /**
     * Method Name : main
     * Used For : method gets called in the beggining
     *
     * @param context
     * @param identifier
     * @param callApi
     * @param setDataOnItemSelection
     */
    public void main(Activity context, String identifier,SetDataOnItemSelection setDataOnItemSelection, CallApi callApi) {
        this.setDataOnItemSelection = setDataOnItemSelection;
        this.callApi = callApi;

        initializePopup(context);
        showList();
        searchView();
        lazyLoading();
    }

    /**
     * Method Name : updateList
     * Used For : to update list at first, on lazy loading and on search
     *
     * @param idName
     *      Model class which contains id and name. Id to send for BE and name for displaying to user.
     */
    public void updateList(List<IdName> idName){
        mIsLoading = true;
        this.idName = idName;
        if(searchAdapter == null) {
            searchAdapter = new SearchAdapter(idName, DropDownLibrary.this);
            mListView.setAdapter(searchAdapter);
            mIsLoading = false;
        }else {
            loadMore();
        }
    }

    /**
     * Method Name : showList
     * Used For : to show popup
     */
    private void showList() {
        mPopupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
        dimBehind(mPopupWindow);
    }

    /**
     * Method Name : dimBehind
     * Used For : to dim behind thepopup
     */
    public static void dimBehind(PopupWindow popupWindow) {
        View container;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            container = (View) popupWindow.getContentView().getParent();
        } else {
            container = popupWindow.getContentView();
        }
        if (popupWindow.getBackground() != null) {
            container = (View) container.getParent();
        }
        Context context = popupWindow.getContentView().getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND; // add a flag here instead of clear others
        p.dimAmount = 0.6f;
        wm.updateViewLayout(container, p);
    }

    /**
     * Method Name : initializePopup
     * Used For : to initializePopup popup
     *
     * @param context
     */
    private void initializePopup(Activity context){

        popupView = LayoutInflater.from(context).inflate(R.layout.popup_showlist, null);

        mPopupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);

        mListView = popupView.findViewById(R.id.items_list);
        linearLayoutManager = new LinearLayoutManager(context);
        mListView.setLayoutManager(linearLayoutManager);

        searchView = popupView.findViewById(R.id.search);

    }

    /**
     * Method Name : searchView
     * Used For : to search items in list
     */
    private void searchView(){
        searchView.setActivated(true);
        searchView.setQueryHint("Search & Select");
        searchView.onActionViewExpanded();
        searchView.setIconified(false);
        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchKey = query;
                callApi.callApi(query,0,true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mSearchKey = newText;
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
                        callApi.callApi(mSearchKey,idName.size(),false);
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


    /**
     * Method Name : setDataForUserSelectedItem
     * Used For : interface to update adapter data to this class
     *
     * @param idName
     * @param position
     */
    @Override
    public void setDataForUserSelectedItem(IdName idName, int position) {
        setDataOnItemSelection.onItemSelected(idName,position);
        mPopupWindow.dismiss();
    }

    /**
     * Method Name : SetDataOnItemSelection
     * Used For : this imterface is used in the class which uses the library
     * to update data in that particular class (class which uses the library)
     */
    public interface SetDataOnItemSelection{
        void onItemSelected(IdName idName, int position);
    }

    /**
     * Method Name : CallApi
     * Used For : to call api during search and lazyloading
     */
    public interface CallApi{
        void callApi(String searchKey, int skip,boolean search);
    }
}
