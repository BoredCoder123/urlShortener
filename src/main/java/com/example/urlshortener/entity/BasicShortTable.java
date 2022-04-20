package com.example.urlshortener.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name="`basic-short-table`")
public class BasicShortTable {
    @Id
    @GeneratedValue
    @Column(name = "`pk-id`")
    private Integer pkId;

    @Column(name="`full-url`", length=4096)
    @NotNull
    private String fullUrl;

    @Column(name="`short-url`", length = 15)
    @NotNull
    private String shortUrl;

    @Column(name="`created-at`")
    @NotNull
    private Date createdAt;

    public BasicShortTable(String fullUrl, String shortUrl, Date createdAt, Date expiresAt) {
        this.fullUrl = fullUrl;
        this.shortUrl = shortUrl;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    @Column(name="`expires-at`")
    private Date expiresAt;
}
