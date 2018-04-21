package com.coband.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tgc on 17-4-12.
 */

public class MatchMailAddress {

    public static boolean match(String mailAddress){
        String str="^[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$";
        Pattern pattern=Pattern.compile(str);
        Matcher matcher = pattern.matcher(mailAddress);
        return matcher.matches();

    }
}
