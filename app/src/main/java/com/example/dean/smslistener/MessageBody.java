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

        if(vehicle.Colour != null)
        {
            message.add("\n" + vehicle.Colour + " " + vehicle.Model);
        }
        else
        {
            message.add("\n" + vehicle.Model);
        }


        return message;
    }

    public ArrayList<String> TaxInvalid(Vehicle vehicle)
    {
        message = new ArrayList<String>();

        message.add(vehicle.Reg + " ");
        message.add("❌");

        if(vehicle.Colour != null)
        {
            message.add("\n" + vehicle.Colour + " " + vehicle.Model +"\nTAX: No Tax\nMOT: " + vehicle.MotDate);
        }
        else
        {
            message.add("\n" + vehicle.Model + "\nTAX: No Tax\nMOT: " + vehicle.MotDate);
        }




        return message;
    }

    public ArrayList<String> MOTInvalid(Vehicle vehicle)
    {
        message = new ArrayList<String>();

        message.add(vehicle.Reg + " ");
        message.add("❌");

        if(vehicle.Colour != null)
        {
            message.add("\n" + vehicle.Colour + " " + vehicle.Model + "\nTAX: " + vehicle.TaxDate + "\nMOT: No MOT");
        }
        else
        {
            message.add("\n" + vehicle.Model + "\nTAX: " + vehicle.TaxDate + "\nMOT: No MOT");
        }



        return message;
    }

    public ArrayList<String> TaxMOTInvalid(Vehicle vehicle)
    {
        message = new ArrayList<String>();

        message.add(vehicle.Reg + " ");
        message.add("⁉");

        if(vehicle.Colour != null)
        {
            message.add("\n" + vehicle.Colour + " " + vehicle.Model);
        }
        else
        {
            message.add("\n" + vehicle.Make);
        }



        return message;
    }
}
