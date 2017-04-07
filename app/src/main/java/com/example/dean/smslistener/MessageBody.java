package com.example.dean.smslistener;

import java.util.ArrayList;

/**
 * Created by Dean on 06/04/2017.
 */

public class MessageBody
{
    ArrayList<String> message;
    public MessageBody()
    {

    }

    public ArrayList<String> TaxMotValid(Vehicle vehicle)
    {
        message = new ArrayList<String>();

        message.add(vehicle.Reg + " ");
        message.add("✅");
        message.add("\n");
        message.add(vehicle.Make + " ");

        return message;
    }

    public ArrayList<String> TaxInvalid(Vehicle vehicle)
    {
        message = new ArrayList<String>();

        message.add(vehicle.Reg + " ");
        message.add("❌");
        message.add("\n");
        message.add(vehicle.Model + " ");
        message.add("\n");
        message.add("TAX: No Tax");
        message.add("\nMOT: " + vehicle.MotDate);


        return message;
    }

    public ArrayList<String> MOTInvalid(Vehicle vehicle)
    {
        message = new ArrayList<String>();

        message.add(vehicle.Reg + " ");
        message.add("❌");
        message.add("\n");
        message.add(vehicle.Model + " ");
        message.add("\n");
        message.add("TAX: " + vehicle.TaxDate);
        message.add("\nMOT: No MOT");

        return message;
    }

    public ArrayList<String> TaxMOTInvalid(Vehicle vehicle)
    {
        message = new ArrayList<String>();

        message.add(vehicle.Reg + " ");
        message.add("⁉️");
        message.add("\n");
        message.add(vehicle.Make + " ");

        return message;
    }
}
