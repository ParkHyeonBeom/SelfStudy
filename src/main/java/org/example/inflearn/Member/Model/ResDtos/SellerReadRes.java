package org.example.inflearn.Member.Model.ResDtos;


import lombok.Builder;
import lombok.Data;
import org.example.inflearn.Common.Address;
import org.example.inflearn.Grade.Grade;

@Builder
@Data
public class SellerReadRes {

    private String sellerName;

    private String sellerEmail;

    private Address sellerAddress;

    private String sellerPNum;

    private Grade sellerGrade;

    private Boolean status;
}
