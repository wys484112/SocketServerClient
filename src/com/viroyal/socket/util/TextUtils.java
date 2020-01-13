package com.viroyal.socket.util;

import java.util.Locale;

import io.netty.buffer.ByteBuf;

public class TextUtils {
    private final static char[] mChars = "0123456789ABCDEF".toCharArray();

    public static boolean isEmpty(String value) {
        return (value == null || value.length() == 0) ? true : false;
    }

    public static String byte2HexStr(byte[] b) {
        StringBuilder sb = new StringBuilder();
        int iLen = b.length;
        for (int n = 0; n < iLen; n++) {
            sb.append(mChars[(b[n] & 0xFF) >> 4]);
            sb.append(mChars[b[n] & 0x0F]);
            sb.append(' ');
        }
        return sb.toString().trim().toUpperCase(Locale.US);
    }

    public static String byte2Str(byte[] b) {
        return new String(b);
    }
    
    public static byte[] str2Byte(String src) {
        return src.getBytes();
    }
    
    public static String byte2HexStr(ByteBuf buf) {
        StringBuilder sb = new StringBuilder();
        int iLen = buf.readableBytes();
        for (int n = 0; n < iLen; n++) {
            sb.append(mChars[(buf.getByte(n) & 0xFF) >> 4]);
            sb.append(mChars[buf.getByte(n) & 0x0F]);
            sb.append(' ');
        }
        return sb.toString().trim().toUpperCase(Locale.US);
    }

    public static String byte2HexStrNoSpace(byte[] b) {
        StringBuilder sb = new StringBuilder();
        int iLen = b.length;
        for (int n = 0; n < iLen; n++) {
            sb.append(mChars[(b[n] & 0xFF) >> 4]);
            sb.append(mChars[b[n] & 0x0F]);
        }
        return sb.toString().trim().toUpperCase(Locale.US);
    }

    public static String byte2HexStrNoSpace(byte[] b, int start, int length) {
        StringBuilder sb = new StringBuilder();
        for (int n = start; n < start + length; n++) {
            sb.append(mChars[(b[n] & 0xFF) >> 4]);
            sb.append(mChars[b[n] & 0x0F]);
        }
        return sb.toString().trim().toUpperCase(Locale.US);
    }

    public static byte[] hexStr2Bytes(String src) {
        /* ������ֵ���й淶������ */
        src = src.trim().replace(" ", "").toUpperCase(Locale.US);
        // ����ֵ��ʼ��
        int m = 0, n = 0;
        int iLen = src.length() / 2; // ���㳤��
        byte[] ret = new byte[iLen]; // ����洢�ռ�

        for (int i = 0; i < iLen; i++) {
            m = i * 2 + 1;
            n = m + 1;
            ret[i] = (byte) (Integer.decode("0x" + src.substring(i * 2, m) + src.substring(m, n)) & 0xFF);
        }
        return ret;
    }
}

