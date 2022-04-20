package com.example.urlshortener.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlValidator {
    public static void validateUrl(String url) throws Exception {
        String regex = "((http|https)://)?(www.)?"
                + "[a-zA-Z0-9@:%._\\+~#?&//=]"
                + "{2,256}\\.[a-z]"
                + "{2,6}\\b([-a-zA-Z0-9@:%"
                + "._\\+~#?&//=]*)";

        Pattern p = Pattern.compile(regex);

        if(url == null)
            throw new Exception("Url is not present");

        Matcher m = p.matcher(url);

        if(m.matches()){
            return;
        }
        throw new Exception("Url is not valid");
    }
}
