package org.example.inflearn.Member.Model.ReqDtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KakaoEmailReq {
    private String customerName;
    private String email;
}
