package kr.co.emfo.kpro_test.domain.message.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@Table(name = "message_log")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
public class MessageLog {

    @Id
    private Long idx;

    @Column(nullable = false)
    private String tmplCd;

    @Column(nullable = false)
    private String senderKey;

    @Column(nullable = false)
    private String phone;

    private LocalDateTime reqDate;

    private LocalDateTime sendDate;

    private LocalDateTime rsltDate;

    @Column(nullable = false, length = 500)
    private String sendMsg;

    @Column(nullable = false)
    private Character curState;

    @Column(nullable = false)
    private String smsType;

    private String subject;

    private String callback;

    private Integer attachmentType;

    private String attachmentName;

    private String attachmentUrl;

    private String imgUrl;

    private String imgLink;

    private String rsltCode;

    private String rsltCodeSms;

    private String rsltNet;
}
