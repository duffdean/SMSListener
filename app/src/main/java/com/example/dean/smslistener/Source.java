package com.example.dean.smslistener;

/**
 * Created by Dean on 25/03/2017.
 */

public class Source
{
    public static final String MSUPER = "https://www.moneysupermarket.com/bin/proxy/gb/reference-data/v0/vehicles?registrationNumber="; //Sometiems cannot find vehicle ie.v640grg
    public static final String GOVUK = "https://vehicleenquiry.service.gov.uk/ViewVehicle?Vrm="; //Requires &Make= after ?Vrm
    public static final String VEHICLEIS = "https://api.vehicleis.uk/Data/TCCFID?vrm="; //Not sure if has max daily connections?

    public Source()
    {
    }
}
