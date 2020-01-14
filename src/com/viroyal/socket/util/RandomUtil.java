package com.viroyal.socket.util;

import java.util.Random;
import java.util.UUID;

public class RandomUtil {
    private final static int MAX_INT = 2147483647;

    public static final String MDC_KEY = "invokeNo";

    //生成n位的随机数 
    public static String RandomCode(int n) {
        StringBuffer sb = new StringBuffer();
        String str = "0123456789";
        Random r = new Random();
        for(int i=0;i<n;i++){
            int num = r.nextInt(str.length());
            sb.append(str.charAt(num));
//            str = str.replace((str.charAt(num)+""), "");
        }
        return sb.toString();
    }

    public static Integer getMySqlKey() {
        Long result;

        for (;;) {
            Integer n = (int) (Math.random() * 6 + 5);
            result = Long.valueOf(RandomCode(n));

            if (result <= MAX_INT)
                break;
        }

        return result.intValue();
    }

    public static String getMDCValue() {
        return UUID.randomUUID().toString();
    }
}