package org.example.inflearn.Member.Model.ResDtos;


import lombok.Builder;
import lombok.Data;
import org.example.inflearn.Common.Address;

import javax.persistence.Embedded;

@Data
@Builder
public class SellerUpdateRes {
    private String sellerName;

    private String sellerEmail;

    private String sellerPassword;

    @Embedded
    private Address sellerAddress;

    private String sellerPNum;

}
