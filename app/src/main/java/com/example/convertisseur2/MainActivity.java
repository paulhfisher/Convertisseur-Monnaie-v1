package com.example.convertisseur2;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String TAG = "Myapp";
    private String theNumber;
    private float theNumberToDouble;
    private float toDollar;
    private EditText myEuroNumber;
    private TextView myResultInDollar;
    private String currencyTXTinit;
    private String currencyTXTDest;

    // hashMaps
    HashMap<String, String> myHashEuro = new HashMap<>();
    HashMap<String, String> myHashDollar = new HashMap<>();
    HashMap<String, String> myHashYen = new HashMap<>();
    HashMap<String, String> myHashPesos = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button switchActivity = (Button) this.findViewById(R.id.button4);
        switchActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                methodeToSwitch();
            }
        });
    }

    private void methodeToSwitch(){
        Intent switchActivityIntent = new Intent(this, MainActivity2.class);
        startActivity(switchActivityIntent);
        Log.d(TAG, "has switch to second activity");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "activity started");
        // buttons
        Button myButtonConvert = (Button) this.findViewById(R.id.button3);

        // texts
        this.myEuroNumber = (EditText) this.findViewById(R.id.myEnterNumber);
        this.myResultInDollar = (TextView) this.findViewById(R.id.textView);

        // Spinners
        Spinner mySpinnerMoneyInit = (Spinner) this.findViewById(R.id.mySpinnerCurrencyInit);
        Spinner mySpinnerMoneyFinal = (Spinner) this.findViewById(R.id.mySpinnerCurrencyDestination);

        ArrayAdapter<CharSequence> adapterInit = ArrayAdapter.createFromResource(this, R.array.currenceChoice, android.R.layout.simple_spinner_item);
        adapterInit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinnerMoneyInit.setAdapter(adapterInit);
        mySpinnerMoneyInit.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapterFinal = ArrayAdapter.createFromResource(this, R.array.currencySelection, android.R.layout.simple_spinner_item);
        adapterInit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinnerMoneyFinal.setAdapter(adapterInit);
        mySpinnerMoneyFinal.setOnItemSelectedListener(this);


        // lorsqu'on appui sur le bouton
        myButtonConvert.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                Log.d(TAG, "button pressed");
                TestBackground xmlBackground = new TestBackground();
                xmlBackground.execute();
                myResultInDollar.setText(String.valueOf(toDollar));
                xmlBackground.doInBackground("euro", "USD");

                if (Objects.equals((currencyTXTinit), "Choose the currency")) {
                    myResultInDollar.setText("veuillez choisir une currency de départ");
                } else if (Objects.equals((currencyTXTDest), "Choose the currency")) {
                    myResultInDollar.setText("veuillez choisir une currency d'arrivée");
                } else if (Objects.equals(currencyTXTinit, "EURO")) { // from euro to currency
                    theNumber = myEuroNumber.getText().toString(); // récupération du EditString
                    theNumberToDouble = Float.parseFloat(theNumber);// convert to double
                    String myHashValue = myHashEuro.get(mySpinnerMoneyFinal.getSelectedItem().toString()); // get value of Spinner
                    Float theHashToDouble = Float.parseFloat(myHashValue);// convert to double
                    myResultInDollar.setText(String.valueOf(theNumberToDouble * theHashToDouble));
                    Log.d(TAG, "test mySpinner get" + mySpinnerMoneyFinal.getSelectedItem().toString());
                    Log.d(TAG, myHashEuro.get("USD"));
                } else if(Objects.equals(currencyTXTDest, "EURO")){
                    theNumber = myEuroNumber.getText().toString(); // from currency to euro
                    theNumberToDouble = Float.parseFloat(theNumber);// convert to double
                    String myHashValue = myHashEuro.get(mySpinnerMoneyInit.getSelectedItem().toString()); // get value of Spinner
                    Float theHashToDouble = Float.parseFloat(myHashValue);// convert to double
                    myResultInDollar.setText(String.valueOf(theNumberToDouble / theHashToDouble));
                } else {
                    theNumber = myEuroNumber.getText().toString(); // récupération du EditString
                    theNumberToDouble = Float.parseFloat(theNumber);// convert to double
                    String myHashValueInit = myHashEuro.get(mySpinnerMoneyInit.getSelectedItem().toString()); // get value of Spinner
                    String myHashValueDest = myHashEuro.get(mySpinnerMoneyFinal.getSelectedItem().toString()); // get value of Spinner
                    Float theHashToDoubleInit = Float.parseFloat(myHashValueInit);// convert to double
                    Float theHashToDoubleDest = Float.parseFloat(myHashValueDest);// convert to double
                    toDollar = (float) ((theNumberToDouble * theHashToDoubleDest)/theHashToDoubleInit);
                    myResultInDollar.setText(String.valueOf(toDollar));
                    Log.d(TAG, "test");
                }
            }
        });
    }

    // GERE CYCLE DE VIE MAIN ACTIVITY
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "activity destroyed");
    /*    if(backgroundXML != null){
            backgroundXML.cancel(true);
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "activity resumed");
    }

    // app pause
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "activity on pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "activity stoped");
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == R.id.mySpinnerCurrencyInit) { // select the init spinner
            currencyTXTinit = adapterView.getItemAtPosition(i).toString();
        } else if (adapterView.getId() == R.id.mySpinnerCurrencyDestination) { // select the destination spinner
            currencyTXTDest = adapterView.getItemAtPosition(i).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Log.d(TAG,"no spinner selected");
    }

    private class TestBackground extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String rateFromXML = null;
            try {
                Log.d(TAG, "In the Background task to work on XML file");
                // parse XML files and put data in nodeLists
                DocumentBuilderFactory xmlFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder xmlBuilder = null;
                xmlBuilder = xmlFactory.newDocumentBuilder();
                assert xmlBuilder != null;
                Document xmlFile = xmlBuilder.parse("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");
                NodeList myCubes = xmlFile.getElementsByTagName("Cube"); // extract nodes "Cube"
                for (int i = 0; i < myCubes.getLength(); i++) {
                    Node data = myCubes.item(i);
                    Element currency = (Element) data;
                    Element currencyValue = (Element) data;
                    String currencyXML = currency.getAttribute("currency"); // get currency
                    String currencyRate = currencyValue.getAttribute("rate"); // get rate
                    myHashEuro.put(currencyXML, currencyRate); // put currency and rate in a hash map
                }

            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
            return rateFromXML;
        }
    }
}



