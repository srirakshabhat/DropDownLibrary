package com.example.library;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.mylibrary.DropDownLibrary;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private boolean mCallingApiForFirstTime = true;
    private List<IdName> tenderNumberList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

 /*   *//**
     * Method Name : apiTenderNumber
     * Used For : api to get tender number
     *
     * @param searchKey - search key to filter list
     *//*
    private void apiTenderNumber(String searchKey, int skip) {
        JSONObject payloadObject = new JSONObject();
        try {
            payloadObject.put("limit", 10);
            payloadObject.put("skip", skip);
            payloadObject.put("search_key", searchKey);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        poCreateVM.apiGetTenderNumber(payloadObject).observe(getViewLifecycleOwner(), new Observer<JSONObject>() {
            @Override
            public void onChanged(JSONObject jsonObject) {
                try {
                    switch (jsonObject.getInt("status_id")) {
                        case 1:
                            populateTenderNameData(jsonObject);
                            break;
                    }
                } catch (JSONException ignored) {
                }
            }
        });
    }

    *//**
     * Method Name : populateTenderNameData
     * Used For : populating tender number list
     *
     * @param jsonObject - JSON obj for tender number from api
     *//*
    private void populateTenderNameData(JSONObject jsonObject) {
        try {
            for (int i = 0; i < jsonObject.getJSONArray("response").length(); i++)
                tenderNumberList.add(new IdName(jsonObject.getJSONArray("response").getJSONObject(i).getString("_id"),
                        jsonObject.getJSONArray("response").getJSONObject(i).getString("pt_number")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DropDownLibrary dropDownLibrary = new DropDownLibrary();
        dropDownLibrary.main(MainActivity.this,tenderNumberList,"tender_number",POCreateInfo.this,POCreateInfo.this,mCallingApiForFirstTime);
        mCallingApiForFirstTime = false;
        //   setAdapterForList(tenderNumberList, "tender_number");
    }*/

}