package com.example.urlshortener.service;

import com.example.urlshortener.constants.UrlConstants;
import com.example.urlshortener.entity.BasicShortTable;
import com.example.urlshortener.repository.BasicShortTableRepository;
import com.example.urlshortener.request.BasicUrlRequest;
import com.example.urlshortener.response.FullUrlResponse;
import com.example.urlshortener.response.ShortenUrlResponse;
import com.example.urlshortener.util.Base62Convertor;
import com.example.urlshortener.util.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.CompletableFuture;

@Service
public class ShortServiceImpl implements ShortService {

    @Autowired
    private BasicShortTableRepository basicRepo;

    @Override
    @Async
    public CompletableFuture<ShortenUrlResponse> shortenUrl(BasicUrlRequest urlRequest) throws Exception {
        UrlValidator.validateUrl(urlRequest.getUrl());
        Integer currMax = basicRepo.getMaxValue();
        if(currMax == null){
            currMax=0;
        }else{
            currMax++;
        }
        String path = Base62Convertor.base62Convertor(currMax);
        String fullPath = UrlConstants.https+UrlConstants.www+UrlConstants.initPath+"/"+path;

        BasicShortTable shortUrl = new BasicShortTable(urlRequest.getUrl(), path, new Date(System.currentTimeMillis()),
                new Date(System.currentTimeMillis()+ 1000L *60*60*24*365*4));

        BasicShortTable savedUrl = basicRepo.save(shortUrl);

        return CompletableFuture.completedFuture(new ShortenUrlResponse(savedUrl.getFullUrl(), fullPath, savedUrl.getCreatedAt(), savedUrl.getExpiresAt()));
    }

    @Override
    @Async
    public CompletableFuture<FullUrlResponse> expandBasicUrl(BasicUrlRequest basicUrlRequest) throws Exception {
        String url = basicUrlRequest.getUrl();
        String sevenChar = "";
        String remaining = "";
        char endpoint = '\0';
        UrlValidator.validateUrl(url);
        if(url.length() < UrlConstants.initPathLen+UrlConstants.shortUrlLen+1) {
            throw new Exception("Url not valid");
        }else if(url.substring(0, UrlConstants.initPathLen).equals(UrlConstants.initPath)){
            sevenChar = url.substring(UrlConstants.initPathLen+1, UrlConstants.initPathLen+1+UrlConstants.shortUrlLen);
            if(url.length()>UrlConstants.initPathLen+1+UrlConstants.shortUrlLen) {
                endpoint = url.charAt(UrlConstants.initPathLen+1+UrlConstants.shortUrlLen);
                if(!(endpoint == '/' || endpoint=='?'))
                    throw new Exception("Invalid url");
                remaining = url.substring(UrlConstants.initPathLen+1+UrlConstants.shortUrlLen+1);
            }
        }else if(url.length() >=(UrlConstants.wwwLen+UrlConstants.initPathLen+1+UrlConstants.shortUrlLen)
                && url.substring(0, UrlConstants.wwwLen+UrlConstants.initPathLen).equals(UrlConstants.www+UrlConstants.initPath)) {
            sevenChar = url.substring(UrlConstants.wwwLen + UrlConstants.initPathLen + 1, UrlConstants.wwwLen + UrlConstants.initPathLen + 1
                    + UrlConstants.shortUrlLen);
            if (url.length() > UrlConstants.wwwLen + UrlConstants.initPathLen + 1 + UrlConstants.shortUrlLen) {
                endpoint = url.charAt(UrlConstants.wwwLen + UrlConstants.initPathLen + 1 + UrlConstants.shortUrlLen);
                if (!(endpoint == '/' || endpoint == '?'))
                    throw new Exception("Invalid url");
                remaining = url.substring(UrlConstants.wwwLen + UrlConstants.initPathLen + 1 + UrlConstants.shortUrlLen + 1);
            }
//        }else if(url.length() >=24 && url.substring(0, 17).equals("http://www.an.ka/")){
        }else if(url.length() >=(UrlConstants.httpLen+UrlConstants.wwwLen+UrlConstants.initPathLen+1+UrlConstants.shortUrlLen)
                && url.substring(0, UrlConstants.httpLen+UrlConstants.wwwLen+UrlConstants.initPathLen)
                .equals(UrlConstants.http+UrlConstants.www+UrlConstants.initPath)){
            sevenChar = url.substring(UrlConstants.httpLen+UrlConstants.wwwLen+UrlConstants.initPathLen+1,
                    UrlConstants.httpLen+UrlConstants.wwwLen+UrlConstants.initPathLen+1+UrlConstants.shortUrlLen);
            if(url.length()>UrlConstants.httpLen+UrlConstants.wwwLen + UrlConstants.initPathLen + 1 + UrlConstants.shortUrlLen) {
                endpoint = url.charAt(UrlConstants.httpLen+UrlConstants.wwwLen + UrlConstants.initPathLen + 1 + UrlConstants.shortUrlLen);
                if(!(endpoint == '/' || endpoint == '?'))
                    throw new Exception("Invalid url");
                remaining = url.substring(UrlConstants.httpLen+UrlConstants.wwwLen + UrlConstants.initPathLen + 1 + UrlConstants.shortUrlLen+1);
            }
        }else if(url.length() >=(UrlConstants.httpsLen+UrlConstants.wwwLen+UrlConstants.initPathLen+1+UrlConstants.shortUrlLen)
                && url.substring(0, UrlConstants.httpsLen+UrlConstants.wwwLen+UrlConstants.initPathLen)
                .equals(UrlConstants.https+UrlConstants.www+UrlConstants.initPath)){
            sevenChar = url.substring(UrlConstants.httpsLen+UrlConstants.wwwLen+UrlConstants.initPathLen+1,
                    UrlConstants.httpsLen+UrlConstants.wwwLen+UrlConstants.initPathLen+1+UrlConstants.shortUrlLen);
            if(url.length()>UrlConstants.httpsLen+UrlConstants.wwwLen + UrlConstants.initPathLen + 1 + UrlConstants.shortUrlLen) {
                endpoint = url.charAt(UrlConstants.httpsLen+UrlConstants.wwwLen + UrlConstants.initPathLen + 1 + UrlConstants.shortUrlLen);
                if(!(endpoint == '/' || endpoint == '?'))
                    throw new Exception("Invalid url");
                remaining = url.substring(UrlConstants.httpsLen+UrlConstants.wwwLen + UrlConstants.initPathLen + 1 + UrlConstants.shortUrlLen+1);
            }
        }
        else{
            throw new Exception("Url not valid");
        }
        String respFromDb = basicRepo.getFullUrl(sevenChar);
        if(respFromDb == null)
            throw new Exception("Unable to find in database");
        if(endpoint=='\0')
            return CompletableFuture.completedFuture(new FullUrlResponse(respFromDb));
        else
            return CompletableFuture.completedFuture(new FullUrlResponse(respFromDb+endpoint+remaining));
    }
}
