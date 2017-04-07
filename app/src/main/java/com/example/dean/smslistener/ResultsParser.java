package com.example.dean.smslistener;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Dean on 21/03/2017.
 */

public class ResultsParser
{
    String htmlSource;
    Vehicle vehicle;
    Pattern pattern;
    Matcher matcher;
    SimpleDateFormat sdf;

    public ResultsParser(String html)
    {
        this.htmlSource = html;
    }

    public boolean ConnectionsExceeded()
    {
        try
        {
            Pattern pattern = Pattern.compile("<h1 class=\"margin-small\">(.+?)requests.</h1>");
            Matcher matcher = pattern.matcher(this.htmlSource);
            matcher.find();

            if(matcher.group(1).toString().equals("We've temporarily restricted access to our service due to a high number of "))
            {
                return true;
            }
        }
        catch(Exception e1)
        {
            Log.wtf("The following error occurred - ",e1.toString());
        }

        return false;
    }

    public Vehicle ParseForCarCheck()
    {
        return null;
    }

    public Vehicle ParseVehicleFromMSuper(String html)
    {
        vehicle = new Vehicle();

        vehicle.Model = GetModelFromReg(html);


        return vehicle;
    }



    public String getMOTFromGov(String html)
    {
        try
        {
            pattern = Pattern.compile("Expires: (.+?)</p>");
            matcher = pattern.matcher(html);
            matcher.find();
//
            return matcher.group(1).toString();
        }
        catch(Exception e1)
        {
            return "No Mot";
        }
    }

    public String getTaxFromGov(String html)
    {
        try
        {
            Pattern pattern = Pattern.compile("Tax due: (.+?)</p>");
            Matcher matcher = pattern.matcher(html);
            matcher.find();
//
            return matcher.group(1).toString();
        } catch (Exception e1)
        {
            return "No Tax";
        }
    }

    public String GetModelFromReg(String html)
    {
        String vehicleMake;
//        JsonObject f = new JsonObject();
//        vehicleMake = "";
//        //JSONObject jsonObject = new JSONObject(html);
//        JsonArray jsonArray = json.getAsJsonArray("vehicles");
//
//        for(int i=0;i<jsonArray.size();i++)
//        {
//            JSONObject curr = jsonArray.getAsString("makeName");
//
//            vehicleMake = curr.getString("makeName");
//
//        }
        //("<img src=\"([^\"]*)\" (alt=\"[^\"]*\")>")
        //makeName":"VOLKSWAGEN","
        try
        {
            pattern = Pattern.compile("modelName\":\"(.+?)\",\"");
            matcher = pattern.matcher(html);
            matcher.find();

            vehicleMake = matcher.group(1).toString();
        }
        catch(Exception e)
        {
            vehicleMake = "No Vehicle";
        }


        return vehicleMake;
    }

    public String GetMakeFromReg(String html)
    {
        String vehicleMake;
//        JsonObject f = new JsonObject();
//        vehicleMake = "";
//        //JSONObject jsonObject = new JSONObject(html);
//        JsonArray jsonArray = json.getAsJsonArray("vehicles");
//
//        for(int i=0;i<jsonArray.size();i++)
//        {
//            JSONObject curr = jsonArray.getAsString("makeName");
//
//            vehicleMake = curr.getString("makeName");
//
//        }
        //("<img src=\"([^\"]*)\" (alt=\"[^\"]*\")>")
                //makeName":"VOLKSWAGEN","
        try
        {
            Pattern pattern = Pattern.compile("makeName\":\"(.+?)\",\"");
            Matcher matcher = pattern.matcher(html);
            matcher.find();

            vehicleMake = matcher.group(1).toString();
        }
        catch(Exception e)
        {
            vehicleMake = "No Vehicle";
        }


        return vehicleMake;
    }

    public Vehicle ParseForTotalCarCheck()
    {
        vehicle = new Vehicle();
        String test;
        try
        {
            Pattern pattern = Pattern.compile("id=\"vrm\" value=\"(.+?)\"> <input");
            Matcher matcher = pattern.matcher(this.htmlSource);
            matcher.find();

            test = matcher.group(1).toString();
        }
        catch(Exception e1)
        {
            Log.wtf("","");
        }

        return vehicle;
    }
}
