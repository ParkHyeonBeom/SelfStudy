package org.example.inflearn.Member.Model.ReqDtos;

import lombok.Builder;
import lombok.Data;
import org.example.inflearn.Common.Address;

import javax.persistence.Embedded;

@Data
@Builder
public class CustomerUpdateReq {

    private String customerPassword;

    @Embedded
    private Address customerAddress;

    private String customerPNum;

}
