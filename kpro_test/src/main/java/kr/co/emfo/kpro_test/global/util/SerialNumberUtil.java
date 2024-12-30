package kr.co.emfo.kpro_test.global.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class SerialNumberUtil {

    /**
     *  현재 시간 기준으로 17Byte 랜덤 일련번호 생성 메서드
     */
    public static String generateSerialNumber() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String dateTime = dateFormat.format(new Date());

        Random random = new Random();
        int randomNumber = random.nextInt(100);

        return dateTime.substring(0, 15) + String.format("%02d", randomNumber);
    }
}
