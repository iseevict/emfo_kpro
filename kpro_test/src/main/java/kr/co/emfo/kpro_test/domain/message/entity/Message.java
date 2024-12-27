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
@Table(name = "message")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String senderKey;

    @Column(nullable = false)
    private String tmplCd;

    @Column(nullable = false)
    private String phone;

    private LocalDateTime reqDate;

    @Column(nullable = false)
    private String sendMsg;

    private Character curState;

    @Column(nullable = false)
    private String smsType;

    private String subject;

    private String callback;

    @Column(nullable = true)
    private Integer attachmentType;

    @Column(nullable = true)
    private String attachmentName;

    @Column(nullable = true)
    private String attachmentUrl;

    @Column(nullable = true)
    private String imgUrl;

    @Column(nullable = true)
    private String imgLink;

    public void updateCurState(Character c) {

        this.curState = c;
    }
}
