package org.example.inflearn.Member.Model.ResDtos;


import lombok.Builder;
import lombok.Data;
import org.example.inflearn.Common.Address;
import org.example.inflearn.Grade.Grade;

@Builder
@Data
public class CustomerReadRes {

    private String customerName;

    private String customerEmail;

    private Address customerAddress;

    private String customerPNum;

    private Grade customerGrade;

    private Boolean status;
}
