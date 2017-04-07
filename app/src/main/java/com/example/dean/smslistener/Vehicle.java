package com.example.dean.smslistener;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Dean on 21/03/2017.
 */

public class Vehicle
{
    String Make, TaxDate, MotDate, Model, Colour, FirstRegistered, Reg;
    //Boolean HasTax, HasMOT;
    SimpleDateFormat sdf;

    public Vehicle()
    {

    }

    public boolean hasTax()
    {
        try
        {
            sdf = new SimpleDateFormat("dd-MMM-yy");
            Date taxDate = sdf.parse(this.TaxDate);
            this.TaxDate = taxDate.toString();

            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    public boolean hasMOT()
    {
        try
        {
            sdf = new SimpleDateFormat("dd-MMM-yy");
            Date taxDate = sdf.parse(this.MotDate);
            this.MotDate = taxDate.toString();

            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

}
