package com.example.convertisseur2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

public class ModeOffLine extends AppCompatActivity {



    public static final String TAG = "Myapp";
    DataBaseManagement myDBget;
    ListView itemsBDD;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mode_off_line);
        Button backToConversion = (Button) findViewById(R.id.buttonBack);
        itemsBDD = (ListView) findViewById(R.id.listItemsCurrency);
        myDBget = new DataBaseManagement(this);
        Cursor data = myDBget.getRateAndCurrency();

        ArrayList<String> listFromDataBaseColumnZero = new ArrayList();
        ArrayList<String> listFromDataBaseColumnOne = new ArrayList();
        ArrayList<String> listFromDataBaseAll = new ArrayList<>();

        ListView itemsBDD = (ListView) findViewById(R.id.listItemsCurrency);
        Log.d(TAG, "test0");

        Log.d(TAG, "data get count" + data.getCount());

        // the database is not empty
        if(data.getCount() != 0){
            while(data.moveToNext()){
                Log.d(TAG, "test1");
                listFromDataBaseColumnZero.add(data.getString(0)); // get column of currencys
                listFromDataBaseColumnOne.add(data.getString(1)); // get column of rates
            }
        }
      for(int i = 0; i<listFromDataBaseColumnZero.size(); i++){
          Log.d(TAG, "test2");
            listFromDataBaseAll.add("currency " + listFromDataBaseColumnZero.get(i) + " = " + "rate " + listFromDataBaseColumnOne.get(i)+"\n");
        }
        ListAdapter adapter = new ArrayAdapter(ModeOffLine.this, android.R.layout.simple_list_item_1, Collections.singletonList(listFromDataBaseAll));
        itemsBDD.setAdapter(adapter);

        backToConversion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToConversionMethod();
            }
        });
    }


    private void backToConversionMethod() {
        Intent switchActivityIntent = new Intent(this, ModeOnline.class);
        startActivity(switchActivityIntent);
    }

}