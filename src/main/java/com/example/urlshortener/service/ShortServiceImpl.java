package com.example.urlshortener.service;

import com.example.urlshortener.constants.UrlConstants;
import com.example.urlshortener.entity.BasicShortTable;
import com.example.urlshortener.repository.BasicShortTableRepository;
import com.example.urlshortener.request.BasicUrlRequest;
import com.example.urlshortener.response.ShortenUrlResponse;
import com.example.urlshortener.util.Base62Convertor;
import com.example.urlshortener.util.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.CompletableFuture;

@Service
public class ShortServiceImpl implements ShortService {

    @Autowired
    private BasicShortTableRepository basicRepo;

    @Override
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
}
