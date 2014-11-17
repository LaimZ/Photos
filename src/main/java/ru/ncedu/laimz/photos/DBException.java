package ru.ncedu.laimz.photos;

public class DBException extends Exception {
    private static final long serialVersionUID = 1L;
    public DBException() {super();}
    public DBException(String string) {super(string);}
    public DBException(String string, Exception e) {super(string, e);}

}
