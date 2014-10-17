package ru.ncedu.laimz.photos.view;

public class ConsoleViewExcepton extends Exception {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    ConsoleViewExcepton() { super(); }
    ConsoleViewExcepton(String s) { super(s); }
    ConsoleViewExcepton(String s, Exception e) { super(s, e); }
}