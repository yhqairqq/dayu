package com.caicai.ottx.common.utils;

import java.util.Random;

/**
 * 随机生成一个指定长度的字符串
 * Created by oneape<oneape15@163.com>
 * Created 2019-05-09 14:12.
 * Modify:
 */
public final class CodeBuilderTool {
    private static char[] ALL_CHAR     = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };
    private static int    ALL_CHAR_LEN = ALL_CHAR.length;

    private static Random random = new Random();

    /**
     * 获取指定长度的随机字符串
     *
     * @param len int
     * @return String
     */
    public static String RadmonStr(int len) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; i++) {
            sb.append(ALL_CHAR[random.nextInt(ALL_CHAR_LEN)]);
        }
        return sb.toString();
    }
}
