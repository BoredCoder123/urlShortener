package com.example.urlshortener.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name="`company-table`")
public class CompanyTable {
    @Id
    @GeneratedValue
    @Column(name = "`pk-id`")
    private Integer pkId;

    @Column(name="`company-name`", unique = true)
    private String companyName;

    @Column(name="`special-short-url`", unique = true, length = 15)
    private String specialShortUrl;

    @Column(name="`short-url-len")
    private Integer shortUrlLen;

    @Column(name="`expires-in", length = 3)
    private Integer expiresIn;
}
