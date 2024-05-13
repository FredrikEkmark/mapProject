package com.fredrik.mapProject;

public class Utility {

    public static String padZero(Integer integer) {
        String string = integer.toString();
        if (string.length() == 1) {
            char[] chars = string.toCharArray();
            char[] newString = new char[] {'0', chars[0]};
            string = new String(newString);
        }
        return string;
    }
}
