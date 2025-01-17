package kr.co.emfo.kpro_test.domain.message.converter;

import com.fasterxml.jackson.databind.JsonNode;
import kr.co.emfo.kpro_test.domain.api.dto.KproApiRequest;
import kr.co.emfo.kpro_test.domain.api.dto.NproApiRequest;
import kr.co.emfo.kpro_test.domain.message.entity.Message;
import kr.co.emfo.kpro_test.domain.message.entity.MessageLog;
import kr.co.emfo.kpro_test.domain.message.entity.NproMessage;
import kr.co.emfo.kpro_test.domain.message.entity.NproMessageLog;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MessageConverter {

    public static NproApiRequest.SendNproMessageRequestDto toSendNproMessageRequestDto(NproMessage nproMessage) {

        return NproApiRequest.SendNproMessageRequestDto.builder()
                .mIdx(nproMessage.getMIdx())
                .mId(nproMessage.getMId())
                .mPwd(nproMessage.getMPwd())
                .callTo(nproMessage.getCallTo())
                .callFrom(nproMessage.getCallFrom())
                .mSubject(nproMessage.getMSubject())
                .mMessage(nproMessage.getMMessage())
                .mType(nproMessage.getMType())
                .mSendType(nproMessage.getMSendType())
                .mFileName(nproMessage.getMFileName())
                .mYyyy(nproMessage.getMYyyy())
                .mMm(nproMessage.getMMm())
                .mDd(nproMessage.getMDd())
                .mHh(nproMessage.getMHh())
                .mMi(nproMessage.getMMi())
                .urlSuccess(nproMessage.getUrlSuccess())
                .urlFail(nproMessage.getUrlFail())
                .flagTest(nproMessage.getFlagTest())
                .flagDeny(nproMessage.getFlagDeny())
                .flagMerge(nproMessage.getFlagMerge())
                .build();
    }

    public static KproApiRequest.SendKproMessageRequestDto toSendKproMessageDto(Message message) {

        return KproApiRequest.SendKproMessageRequestDto.builder()
                .callback(message.getCallback())
                .phone(message.getPhone())
                .senderKey(message.getSenderKey())
                .tmplCd(message.getTmplCd())
                .sendMsg(message.getSendMsg())
                .smsType(message.getSmsType())
                .subject(message.getSubject())
                .attachmentType(message.getAttachmentType())
                .attachmentName(message.getAttachmentName())
                .attachmentUrl(message.getAttachmentUrl())
                .imgUrl(message.getImgUrl())
                .imgLink(message.getImgLink())
                .build();
    }

    public static MessageLog toMessageLog(JsonNode logNode) {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime tempReqDate = LocalDateTime.parse(logNode.get("req_date").asText(), dateTimeFormatter);
        LocalDateTime tempSendDate = LocalDateTime.parse(logNode.get("send_date").asText(), dateTimeFormatter);
        LocalDateTime tempRsltDate = LocalDateTime.parse(logNode.get("rslt_date").asText(), dateTimeFormatter);

        return MessageLog.builder()
                .idx(logNode.get("idx").asLong())
                .tmplCd(logNode.get("tmpl_cd").asText())
                .senderKey(logNode.get("sender_key").asText())
                .phone(logNode.get("phone").asText())
                .reqDate(tempReqDate)
                .sendDate(tempSendDate)
                .rsltDate(tempRsltDate)
                .rsltCode(logNode.get("rslt_code").asText())
                .rsltCodeSms(logNode.get("rslt_code_sms").asText())
                .rsltNet(logNode.get("rslt_net").asText())
                .sendMsg(logNode.get("send_msg").asText())
                .smsType(logNode.get("sms_type").asText())
                .subject(logNode.get("subject").asText())
                .callback(logNode.get("callback").asText())
                .attachmentType(logNode.get("attachment_type").asInt())
                .attachmentName(logNode.get("attachment_name").asText())
                .attachmentUrl(logNode.get("attachment_url").asText())
                .imgUrl(logNode.get("img_url").asText())
                .imgLink(logNode.get("img_link").asText())
                .curState(logNode.get("cur_state").asText().charAt(0))
                .build();
    }

    public static NproMessageLog toNproMessageLog(Long idx, JsonNode logNode) {

        return NproMessageLog.builder()
                .idx(idx)
                .reqDate(logNode.get("REQ_DATE").asText())
                .phone(logNode.get("PHONE").asText())
                .callback(logNode.get("CALLBACK").asText())
                .code(logNode.get("CODE").asText())
                .net(logNode.get("NET").asText())
                .type(logNode.get("TYPE").asText())
                .build();
    }
}
