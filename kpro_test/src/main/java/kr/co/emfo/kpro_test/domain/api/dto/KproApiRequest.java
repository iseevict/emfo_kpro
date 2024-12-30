package kr.co.emfo.kpro_test.domain.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class KproApiRequest {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SendKproMessageRequestDto {
        private String callback;
        private String phone;
        private String senderKey;
        private String tmplCd;
        private String sendMsg;
        private String smsType;
        private String subject;
        private Integer attachmentType;
        private String attachmentName;
        private String attachmentUrl;
        private String imgUrl;
        private String imgLink;
        private Character curState;
    }
}
