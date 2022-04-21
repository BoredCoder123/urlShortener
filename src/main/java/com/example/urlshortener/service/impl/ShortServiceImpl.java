package com.example.urlshortener.service.impl;

import com.example.urlshortener.constants.UrlConstants;
import com.example.urlshortener.entity.BasicShortTable;
import com.example.urlshortener.entity.CompanyTable;
import com.example.urlshortener.entity.SpecialShortTable;
import com.example.urlshortener.repository.BasicShortTableRepository;
import com.example.urlshortener.repository.CompanyTableRepository;
import com.example.urlshortener.repository.SpecialShortTableRepository;
import com.example.urlshortener.request.BasicUrlRequest;
import com.example.urlshortener.request.SpecialUrlRequest;
import com.example.urlshortener.response.FullUrlResponse;
import com.example.urlshortener.response.ShortenUrlResponse;
import com.example.urlshortener.service.ShortService;
import com.example.urlshortener.util.Base62Convertor;
import com.example.urlshortener.util.UrlValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

@Service
@Log4j2
public class ShortServiceImpl implements ShortService {

    @Autowired
    private BasicShortTableRepository basicRepo;

    @Autowired
    private CompanyTableRepository companyRepo;

    @Autowired
    private SpecialShortTableRepository specialRepo;

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
        if(url.startsWith(UrlConstants.initPath) || url.startsWith(UrlConstants.www+UrlConstants.initPath)
                || url.startsWith(UrlConstants.http+UrlConstants.www+UrlConstants.initPath) ||
                url.startsWith(UrlConstants.https+UrlConstants.www+UrlConstants.initPath)) {
            if (url.length() < UrlConstants.initPathLen + UrlConstants.shortUrlLen + 1) {
                throw new Exception("Url not valid");
            } else if (url.substring(0, UrlConstants.initPathLen).equals(UrlConstants.initPath)) {
                sevenChar = url.substring(UrlConstants.initPathLen + 1, UrlConstants.initPathLen + 1 + UrlConstants.shortUrlLen);
                if (url.length() > UrlConstants.initPathLen + 1 + UrlConstants.shortUrlLen) {
                    endpoint = url.charAt(UrlConstants.initPathLen + 1 + UrlConstants.shortUrlLen);
                    if (!(endpoint == '/' || endpoint == '?'))
                        throw new Exception("Invalid url");
                    remaining = url.substring(UrlConstants.initPathLen + 1 + UrlConstants.shortUrlLen + 1);
                }
            } else if (url.length() >= (UrlConstants.wwwLen + UrlConstants.initPathLen + 1 + UrlConstants.shortUrlLen)
                    && url.substring(0, UrlConstants.wwwLen + UrlConstants.initPathLen).equals(UrlConstants.www + UrlConstants.initPath)) {
                sevenChar = url.substring(UrlConstants.wwwLen + UrlConstants.initPathLen + 1, UrlConstants.wwwLen + UrlConstants.initPathLen + 1
                        + UrlConstants.shortUrlLen);
                if (url.length() > UrlConstants.wwwLen + UrlConstants.initPathLen + 1 + UrlConstants.shortUrlLen) {
                    endpoint = url.charAt(UrlConstants.wwwLen + UrlConstants.initPathLen + 1 + UrlConstants.shortUrlLen);
                    if (!(endpoint == '/' || endpoint == '?'))
                        throw new Exception("Invalid url");
                    remaining = url.substring(UrlConstants.wwwLen + UrlConstants.initPathLen + 1 + UrlConstants.shortUrlLen + 1);
                }
            } else if (url.length() >= (UrlConstants.httpLen + UrlConstants.wwwLen + UrlConstants.initPathLen + 1 + UrlConstants.shortUrlLen)
                    && url.substring(0, UrlConstants.httpLen + UrlConstants.wwwLen + UrlConstants.initPathLen)
                    .equals(UrlConstants.http + UrlConstants.www + UrlConstants.initPath)) {
                sevenChar = url.substring(UrlConstants.httpLen + UrlConstants.wwwLen + UrlConstants.initPathLen + 1,
                        UrlConstants.httpLen + UrlConstants.wwwLen + UrlConstants.initPathLen + 1 + UrlConstants.shortUrlLen);
                if (url.length() > UrlConstants.httpLen + UrlConstants.wwwLen + UrlConstants.initPathLen + 1 + UrlConstants.shortUrlLen) {
                    endpoint = url.charAt(UrlConstants.httpLen + UrlConstants.wwwLen + UrlConstants.initPathLen + 1 + UrlConstants.shortUrlLen);
                    if (!(endpoint == '/' || endpoint == '?'))
                        throw new Exception("Invalid url");
                    remaining = url.substring(UrlConstants.httpLen + UrlConstants.wwwLen + UrlConstants.initPathLen + 1 + UrlConstants.shortUrlLen + 1);
                }
            } else if (url.length() >= (UrlConstants.httpsLen + UrlConstants.wwwLen + UrlConstants.initPathLen + 1 + UrlConstants.shortUrlLen)
                    && url.substring(0, UrlConstants.httpsLen + UrlConstants.wwwLen + UrlConstants.initPathLen)
                    .equals(UrlConstants.https + UrlConstants.www + UrlConstants.initPath)) {
                sevenChar = url.substring(UrlConstants.httpsLen + UrlConstants.wwwLen + UrlConstants.initPathLen + 1,
                        UrlConstants.httpsLen + UrlConstants.wwwLen + UrlConstants.initPathLen + 1 + UrlConstants.shortUrlLen);
                if (url.length() > UrlConstants.httpsLen + UrlConstants.wwwLen + UrlConstants.initPathLen + 1 + UrlConstants.shortUrlLen) {
                    endpoint = url.charAt(UrlConstants.httpsLen + UrlConstants.wwwLen + UrlConstants.initPathLen + 1 + UrlConstants.shortUrlLen);
                    if (!(endpoint == '/' || endpoint == '?'))
                        throw new Exception("Invalid url");
                    remaining = url.substring(UrlConstants.httpsLen + UrlConstants.wwwLen + UrlConstants.initPathLen + 1 + UrlConstants.shortUrlLen + 1);
                }
            } else {
                throw new Exception("Url not valid");
            }
            String respFromDb = basicRepo.getFullUrl(sevenChar);
            if (respFromDb == null)
                throw new Exception("Unable to find in database");
            if (endpoint == '\0')
                return CompletableFuture.completedFuture(new FullUrlResponse(respFromDb));
            else
                return CompletableFuture.completedFuture(new FullUrlResponse(respFromDb + endpoint + remaining));
        }else{
            if(url.startsWith(UrlConstants.https)){
                url = url.substring(UrlConstants.httpsLen+UrlConstants.wwwLen);
            }else if(url.startsWith(UrlConstants.http))
                url = url.substring(UrlConstants.httpLen+UrlConstants.wwwLen);
            else if(url.startsWith(UrlConstants.www))
                url=url.substring(UrlConstants.wwwLen);
            char breakChar = '\0';
            int breakPoint = 0;
            for(int i=0;i<=UrlConstants.companyLenInit;i++){
                if(url.charAt(i)=='/'){
                    breakChar = url.charAt(i);
                    breakPoint = i;
                    break;
                }
            }
            if(breakChar == '\0')
                throw new Exception("Invalid url");
            String initUrl = url.substring(0, breakPoint);
            String restUrl = url.substring(breakPoint+1);
            CompanyTable company = companyRepo.findBySpecialShortUrl(initUrl);
            if(company == null)
                throw new Exception("Invalid special url");
            breakPoint = restUrl.charAt(company.getShortUrlLen());
            log.info((char)breakPoint);
            if(!(breakPoint == '/' || breakPoint == '?'))
                throw new Exception("Invalid special url");
            String base62 = restUrl.substring(0, company.getShortUrlLen());
            String rest = restUrl.substring(company.getShortUrlLen()+1);
            SpecialShortTable specialShort = specialRepo.findByCompanyIdAndShortUrl(company.getPkId(), base62);
            if(specialShort ==null)
                throw new Exception("Invalid special url");
            return CompletableFuture.completedFuture(new FullUrlResponse(specialShort.getFullUrl()+breakChar+rest));
        }
    }

    @Override
    @Async
    public CompletableFuture<ShortenUrlResponse> shortenSpecialUrl(SpecialUrlRequest specialUrlRequest) throws Exception {
        UrlValidator.validateUrl(specialUrlRequest.getUrl());
        CompanyTable companyTable = companyRepo.findCompanyByCompanyName(specialUrlRequest.getCompanyName().toLowerCase());
        if(companyTable == null)
            throw new Exception("Company doesn't exist");
        Integer currCounter = specialRepo.findCountOfCompany(companyTable.getPkId());
        currCounter++;
        SpecialShortTable newSpecial = new SpecialShortTable();
        newSpecial.setCreatedAt(new Date());
        newSpecial.setFullUrl(specialUrlRequest.getUrl());
        newSpecial.setShortUrl(Base62Convertor.base62Convertor(currCounter));
        newSpecial.setCompanyTable(companyTable);
        newSpecial.setExpiresAt(new Date(System.currentTimeMillis()+ 1000L *60*60*24*365* companyTable.getExpiresIn()));
        SpecialShortTable savedSpecial = specialRepo.save(newSpecial);
        return CompletableFuture.completedFuture(new ShortenUrlResponse(savedSpecial.getFullUrl(),
                UrlConstants.https+UrlConstants.www+companyTable.getSpecialShortUrl()+"/"+savedSpecial.getShortUrl(),
                savedSpecial.getCreatedAt(),
                savedSpecial.getExpiresAt()));
    }
}
