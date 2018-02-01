package com.varunjoshi.notesy.activity.Util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Notesy
 * Created by Varun Joshi on Thu, {1/2/18}.
 */

public class Util {

    public static String ConvertDateIntoSimpleFormat(String dateTime) {
        String result="";

        String date=dateTime;//"2013-09-06'T'14:15:11.557'Z'";
        DateFormat df=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date d = null;
        try {
            d = df.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        df=new SimpleDateFormat("dd/MM/yyyy, hh:mm aa");

        result=df.format(d);

        return result;
    }

}
