package com.coband.common.utils;

/**
 * Created by tgc on 17-5-17.
 */

public class HideMailAddressUtil {

    public static String hideAddress(String mailAddress){
        int atPosInteger = mailAddress.lastIndexOf("@");
        String sectionOne = mailAddress.substring(0, atPosInteger-3);
        String sectionTwo = mailAddress.substring(atPosInteger, mailAddress.length());
        return sectionOne+"****"+sectionTwo;
    }
}
