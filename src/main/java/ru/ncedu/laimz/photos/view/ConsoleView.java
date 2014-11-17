package ru.ncedu.laimz.photos.view;

/**
 * Class for communication with user via PrintStream and Scanner. Can be used
 * for CLI.
 * @author LaimZ
 *
 */
public interface ConsoleView {
    
    public String nextLine() throws ConsoleViewExcepton ;
    
    public void println(String s) throws ConsoleViewExcepton ;

    public void println() throws ConsoleViewExcepton ;
    
    public void print(String s) throws ConsoleViewExcepton ;
}
