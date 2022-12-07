package com.opzpy123.util;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CopilotTest {
    public static void main(String[] args) {
        System.out.println(toTwentyHex(0, true));
        System.out.println(toTwentyHex(1, true));
        System.out.println(toTwentyHex(11, true));
        System.out.println(toTwentyHex(26, true));
        System.out.println(toTwentyHex(999, false));
        System.out.println(toTwentyHex(25, false));
    }

    public static String toTwentyHex(int num, boolean useUppercase) {
        num+=26;
        char startChar = useUppercase ? 'A' : 'a';
        StringBuilder sb = new StringBuilder();
        if (num == 0) {
            sb.append(startChar);
            return sb.toString();
        }
        int divide = num / 26;
        if (divide > 0) {
            sb.append(String.valueOf(startChar).repeat(divide));
        }
        int m = num % 26;
        char c = (char) (startChar + m);
        sb.append(c);
        return sb.toString();
    }


}
