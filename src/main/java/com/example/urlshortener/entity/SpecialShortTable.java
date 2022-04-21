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
@Table(name="`special-short-table`")
public class SpecialShortTable {
    @Id
    @GeneratedValue
    @Column(name = "`pk-id`")
    private Integer pkId;

    @Column(name="`full-url`", length=4096)
    @NotNull
    private String fullUrl;

    @ManyToOne
    @JoinColumn(name = "`company-id`")
    private CompanyTable companyTable;

    @Column(name="`short-url`", length = 15)
    @NotNull
    private String shortUrl;

    @Column(name="`created-at`")
    @NotNull
    private Date createdAt;

    @Column(name="`expires-at`")
    private Date expiresAt;
}
