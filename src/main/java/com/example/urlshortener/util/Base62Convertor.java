package com.example.urlshortener.util;

import com.example.urlshortener.constants.UrlConstants;

public class Base62Convertor {
    public static String base62Convertor(Integer input) throws IllegalArgumentException{
        if(input==null || input < 0)
            throw new IllegalArgumentException();

        String s = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder res = new StringBuilder();

        while(input > 0){
            res.insert(0, s.charAt(input % 62));
            input /= 62;
        }

        int remainingLength = UrlConstants.shortUrlLen -res.length();

        for(int i=0;i<remainingLength;i++){
            res.insert(0, 0);
        }

        return res.toString();
    }
}

