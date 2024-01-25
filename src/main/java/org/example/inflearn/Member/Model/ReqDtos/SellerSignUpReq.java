package org.example.inflearn.Member.Model.ReqDtos;

import lombok.Builder;
import lombok.Data;
import org.example.inflearn.Common.Address;

@Builder
@Data
public class SellerSignUpReq {
    private String sellerName;

    private String sellerEmail;

    private String sellerPassword;

    private Address sellerAddress;

    private String sellerPNum;

    private Boolean socialLogin;
}
