package com.example.benjaminxerri.currencyconverter.utilities;

import android.util.Log;

import org.json.JSONObject;

import java.text.DecimalFormat;

/**
 * Created by benjaminxerri on 9/17/17.
 */

public class Currency {
    private double amount;
    private double rate;
    private double total;

    DecimalFormat df = new DecimalFormat("###.##");

    public Currency() {
        this.amount = 1;
        this.rate = 0;
        this.total = 0;
    }


    public void setRate(double newRate){
        this.rate = newRate;
    }

    public void setAmount(double newAmount) {
        this.amount = newAmount;
    }

    public double getTotal () {
        return total;
    }

    public double getRate () {
        return rate;
    }

    public double getAmount(){
        return amount;
    }

    public String calculateExchange(double rate){
        total = amount * rate;
        String formattedTotal = df.format(total);
        return formattedTotal;
    }

    public String getCurrencyValue(String jsonString, String currencyValue){
        String newRate = "";
        try {
            //get the json value for the rate of the spinner
            JSONObject amount = new JSONObject(jsonString);
            JSONObject rates = amount.getJSONObject("rates");
            String value = rates.getString(currencyValue);
            Log.d("Value from json is ", value);
            double valueAsDouble = Double.parseDouble(value);
            newRate = calculateExchange(valueAsDouble);

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

        return newRate;
    }
    /*
    String formattedNumber = "";
        try {
        //get the json value for the rate of the spinner
        JSONObject amount = new JSONObject(jsonString);
        JSONObject rates = amount.getJSONObject("rates");
        String value = rates.getString(currencyValue);
        Log.d("Value from json is ", value);
        double valueAsDouble = Double.parseDouble(value);

        String newRate = String.valueOf(calculateExchange(valueAsDouble));
        formattedNumber = df.format(newRate);
    }catch (Exception e){
        e.printStackTrace();
        return null;
    }

        return formattedNumber;

    */

}
