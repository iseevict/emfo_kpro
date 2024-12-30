package kr.co.emfo.kpro_test.global.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class SerialNumberUtil {

    public static String generateSerialNumber() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String dateTime = dateFormat.format(new Date());

        Random random = new Random();
        int randomNumber = random.nextInt(100);

        return dateTime.substring(0, 15) + String.format("%02d", randomNumber);
    }
}
