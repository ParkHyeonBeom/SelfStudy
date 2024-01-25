package org.example.inflearn.Member.Model.ReqDtos;

import lombok.Builder;
import lombok.Data;
import org.example.inflearn.Common.Address;

import javax.persistence.Embedded;

@Data
@Builder
public class SellerUpdateReq {

    private String sellerPassword;

    @Embedded
    private Address sellerAddress;

    private String sellerPNum;

}
