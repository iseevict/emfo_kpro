package kr.co.emfo.kpro_test.domain.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class emfoRequest {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class SendMessageDto {

        private Long mIdx;
        private String mId;
        private String mPwd;
        private String callTo;
        private String callFrom;
        private String mSubject;
        private String mMessage;
        private String mType;
        private String mSendType;
        private String mFileName;
        private String mYyyy;
        private String mMm;
        private String mDd;
        private String mHh;
        private String mMi;
        private String urlSuccess;
        private String urlFail;
        private String flagTest;
        private String flagDeny;
        private String flagMerge;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class LogDto {

        private Long mIdx;
        private String mId;
    }
}
