package com.example.library;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.mylibrary.DropDownLibrary;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DropDownLibrary.SetDataOnItemSelection {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DropDownLibrary dropDownLibrary = new DropDownLibrary();
        List<IdName> tenderNumberList = new ArrayList<>();
        tenderNumberList.add(new IdName("ok","raksha"));

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dropDownLibrary.main(MainActivity.this,tenderNumberList,"tender_number",MainActivity.this);
            }
        },2000);
    }

    @Override
    public void onItemSelected(String idName, int position, String identifier) {
        Toast.makeText(this, ""+idName, Toast.LENGTH_SHORT).show();
    }
}