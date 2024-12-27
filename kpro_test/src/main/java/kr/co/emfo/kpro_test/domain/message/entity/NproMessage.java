package kr.co.emfo.kpro_test.domain.message.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "npro_message")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
public class NproMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mIdx;

    @Column(nullable = false)
    private String mId;

    @Column(nullable = false)
    private String mPwd;

    @Column(nullable = false)
    private String callTo;

    @Column(nullable = false)
    private String callFrom;

    private String mSubject;

    @Column(nullable = false)
    private String mMessage;

    @Column(nullable = false)
    private String mType;

    @Column(nullable = false)
    private String mSendType;

    private String mFileName;

    private String mYyyy;

    private String mMm;

    private String mDd;

    private String mHh;

    private String mMi;

    @Column(nullable = false)
    private String urlSuccess;

    @Column(nullable = false)
    private String urlFail;

    @Column(nullable = false)
    private String flagTest;

    @Column(nullable = false)
    private String flagDeny;

    @Column(nullable = false)
    private String flagMerge;

    private String state;

    public void updateState(String s) {

        this.state = s;
    }
}
