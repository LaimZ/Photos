package ru.ncedu.laimz.commons;

public class SimplestSplitter {
    //private String string;
    private SimplestSplitter() {
    }
    public static String[] split(String string) {
        String[] array2 = string.split(" ");
        for (String s : array2) {
            s = s.trim();
        }
        return array2;
    }
    
}
