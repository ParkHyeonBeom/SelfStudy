package org.example.inflearn.Member.Model.ReqDtos;

import lombok.Builder;
import lombok.Data;
import org.example.inflearn.Common.Address;
import org.example.inflearn.Grade.Grade;

import javax.persistence.Embedded;

@Data
@Builder
public class UpdateReq {

    private String customerPassword;

    @Embedded
    private Address customerAddress;

    private String customerPNum;

}
