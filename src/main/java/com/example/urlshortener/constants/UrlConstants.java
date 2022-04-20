package com.example.urlshortener.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class UrlConstants {
    public final static String http = "http://";
    public final static String https = "https://";
    public final static String www = "www.";
    public final static int httpLen = http.length();
    public final static int httpsLen = https.length();
    public final static int wwwLen = www.length();
    @Value("${url.end:an.ka}")
    private String urlInit;

    public static String initPath;
    public static int initPathLen;

    @Value("${shortUrl.length:7}")
    private Integer shortUrlLength;

    public static int shortUrlLen;

    @PostConstruct
    void init(){
        initPath = urlInit;
        shortUrlLen = shortUrlLength;
        initPathLen = initPath.length();
    }
}
