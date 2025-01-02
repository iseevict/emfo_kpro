package kr.co.emfo.kpro_test.domain.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

public class KproApiRequest {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SendKproMessageRequestDto {

        @NotNull(message = "phone cannot be null")
        @Pattern(regexp =  "^[0-9]{10,11}$", message = "Phone must be 10 or 11 digits and contain only numbers")
        private String phone;
        @NotNull(message = "senderKey cannot be null")
        private String senderKey;
        @NotNull(message = "tmplCd cannot be null")
        private String tmplCd;
        @NotNull(message = "sendMsg cannot be null")
        private String sendMsg;
        @NotNull(message = "smsType cannot be null")
        private String smsType;
        private String callback;
        private String subject;
        private Integer attachmentType;
        private String attachmentName;
        private String attachmentUrl;
        private String imgUrl;
        private String imgLink;
    }
}
