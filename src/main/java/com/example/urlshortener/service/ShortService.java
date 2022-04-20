package com.example.urlshortener.service;

import com.example.urlshortener.request.BasicUrlRequest;
import com.example.urlshortener.response.FullUrlResponse;
import com.example.urlshortener.response.ShortenUrlResponse;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public interface ShortService {
    CompletableFuture<ShortenUrlResponse> shortenUrl(BasicUrlRequest urlRequest) throws Exception;

    CompletableFuture<FullUrlResponse> expandBasicUrl(BasicUrlRequest basicUrlRequest) throws Exception;
}
