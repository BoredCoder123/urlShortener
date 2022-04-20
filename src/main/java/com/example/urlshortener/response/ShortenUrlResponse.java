package com.example.urlshortener.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShortenUrlResponse {
    private String fullUrl;
    private String shortUrl;
    private Date insertedAt;
    private Date expiresAt;
}
