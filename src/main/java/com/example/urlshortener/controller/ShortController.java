package com.example.urlshortener.controller;

import com.example.urlshortener.request.BasicUrlRequest;
import com.example.urlshortener.request.SpecialUrlRequest;
import com.example.urlshortener.response.FullUrlResponse;
import com.example.urlshortener.response.ShortenUrlResponse;
import com.example.urlshortener.service.ShortService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.concurrent.CompletableFuture;

@RestController
@Log4j2
public class ShortController {

    @Autowired
    private ShortService shortService;

    @GetMapping("/")
    public Date heartbeat(){
        return new Date(System.currentTimeMillis());
    }

    @PostMapping("/basic-short")
    public ResponseEntity shortenBasicUrl(@RequestBody BasicUrlRequest basicUrlRequest){
        try{
            CompletableFuture<ShortenUrlResponse> resp = shortService.shortenUrl(basicUrlRequest);
            return new ResponseEntity<ShortenUrlResponse>(resp.get(), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            log.info(e);
            return new ResponseEntity<String>("Unable to get shortened url", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/basic-expand")
    public ResponseEntity expandBasicUrl(@RequestBody BasicUrlRequest basicUrlRequest){
        try{
            CompletableFuture<FullUrlResponse> resp = shortService.expandBasicUrl(basicUrlRequest);
            return new ResponseEntity<FullUrlResponse>(resp.get(), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            log.info(e);
            return new ResponseEntity<String>("Unable to get shortened url", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/special-short")
    public ResponseEntity shortenSpecialUrl(@RequestBody SpecialUrlRequest specialUrlRequest){
        try{
            CompletableFuture<ShortenUrlResponse> resp = shortService.shortenSpecialUrl(specialUrlRequest);
            return new ResponseEntity<>(resp.get(), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            log.info(e);
            return new ResponseEntity<String>("Unable to get shortened url", HttpStatus.BAD_REQUEST);
        }
    }
}
