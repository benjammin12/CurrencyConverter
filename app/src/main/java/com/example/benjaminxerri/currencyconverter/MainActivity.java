package com.example.benjaminxerri.currencyconverter;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.benjaminxerri.currencyconverter.utilities.Currency;
import com.example.benjaminxerri.currencyconverter.utilities.Network;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    //the users money from the text field
    private double userMoney;
    private Button convertButton;
    private Spinner spinner;
    private Spinner convert_to_spinner;
    private ProgressBar progressBar;

    //current 3 letter country codes
    private String [] countrys ={ "JPY", "CNY",  "RON", "MXN", "CAD",
            "ZAR", "AUD", "NOK", "ILS", "THB", "IDR", "HRK",
            "DKK", "MYR", "SEK", "RSD", "BGN",  "KRW",
            "CZK", "VND", "NZD",
            "GBP", "CHF", "RUB", "INR",
            "TRY", "SGD", "HKD",
            "BRL", "EUR", "HUF",   "PHP", "PLN",
            "USD" };

    private int usd = countrys.length-1;
    private String currencyValueSelected;
    private String baseCurrencyValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        convertButton = (Button) findViewById(R.id.convert_button);

        spinner = (Spinner) findViewById(R.id.base_currency);
        convert_to_spinner = (Spinner) findViewById(R.id.convert_currency_to);
        progressBar = (ProgressBar) findViewById(R.id.loading_indicator);

        //TODO::Customize spinner creating new xml file and passing as second parameter.
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, countrys);
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.countries, android.R.layout.simple_spinner_item);

        //set spinners
        spinner.setAdapter(adapter);
        convert_to_spinner.setAdapter(adapter);
        spinner.setSelection(usd); // make spinner start at USD by default

        //give this button an onclick lister of the converterListener anonymous function called below.
        convertButton.setOnClickListener(converterListener);
        //Completed: Prevent the user from selecting currency twice, as this throws an error, because the same rate is not in api
        //pulled this out to lighten up onCreate method
        /*
        convertButton.setOnClickListener(new View.OnClickListener(){public void onClick(View v){}});
        */
    }
    public class AsyncSearch extends AsyncTask<URL, Void, String> {
        private TextView result;
        Currency money = new Currency();

        /**
         * This method is executed in the background
         * @param urls
         * @return currency value after exchange
         */
        @Override
        protected String doInBackground(URL... urls) {
            URL searchURL = urls[0];
            String searchResults = null;
            try {
                //can use methods from same package
                //open a stream and search the url when we call execute
                searchResults = Network.getResponseFromHttpUrl(searchURL); //json object, could return this
                Log.d("JSON Object is", searchResults);
                //set the amount in the Currency class instance to match what the user put in
                money.setAmount(userMoney);
                String currencyValue = money.getCurrencyValue(searchResults,currencyValueSelected);
                return currencyValue;

            }catch(IOException e){
                e.printStackTrace();
            }
            return searchResults;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        /**
         * After execute is completed, set the text field
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.INVISIBLE);
            if (s != null && !s.equals("")) {
                result = (TextView) findViewById(R.id.results);
                result.setText("Currency value in " + currencyValueSelected + " is: " + s.toString());
            }


        }
    }

    //Completed make 2 scrollers full of 3 letter country codes
    //Completed: After that make the base the first scroller, and parse the json for the second scroller value in the currency converter
    //Completed: Do the math based off the value in a text box.
    //Completed: Display the results

    public URL getUserCurrencyValue(String currencyBase){

        //build a URL from that string spinner value
        URL searchURL = Network.buildURL(currencyBase);

        return searchURL;
    }

    //anonymous implementation of onclick listener
    public View.OnClickListener converterListener = new View.OnClickListener() {
        public void onClick(View v){
            TextView errMessage = (TextView) findViewById(R.id.results);
            //get both spinner values on click incase the user changes multiple times
            baseCurrencyValue = spinner.getSelectedItem().toString();
            currencyValueSelected = convert_to_spinner.getSelectedItem().toString();

            if (baseCurrencyValue.equals(currencyValueSelected)){
                errMessage.setText("Can't convert to same currency type.");
                return;
            }

            //build api request with first spinner value on screen
            URL urlQUery = getUserCurrencyValue(baseCurrencyValue);

            //get the amount of money the user entered in the edittext field
            EditText txt = (EditText) findViewById(R.id.editText);
            if (txt.getText().toString().equals("")){
                errMessage.setText("You can't convert 0.");
                return;
            }
            userMoney = Double.parseDouble(txt.getText().toString());

            //make AsyncSearch instance and execute the search with correct query
            System.out.println("URL is: " + urlQUery);
            new AsyncSearch().execute(urlQUery);

        }
    };




}
