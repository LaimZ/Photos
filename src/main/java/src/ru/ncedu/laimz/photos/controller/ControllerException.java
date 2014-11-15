package ru.ncedu.laimz.photos.controller;

public class ControllerException extends Exception {
    private static final long serialVersionUID = 1L;
    ControllerException() { super(); }
    public ControllerException(String string) {super(string);}
    public ControllerException(String string, Exception e) {super(string, e);}
}