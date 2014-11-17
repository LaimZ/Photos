package ru.ncedu.laimz.photos.view;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;



/**
 * Class for communication with user via PrintStream and Scanner. Can be used
 * for CLI.
 * @author LaimZ
 *
 */
public class ConsoleViewFirst implements ConsoleView {
    private PrintStream ps = null;
    private Scanner sc = null;
    
    public ConsoleViewFirst (InputStream is, PrintStream ps) {
        this.ps = ps;
        this.sc = new Scanner(is);
    }
    
    public String nextLine() throws ConsoleViewExcepton {
        if (sc != null) {
            return sc.nextLine();
        } else {
            throw new ConsoleViewExcepton( "Scanner not initialized!" );
        }
    }
    
    public void println(String s) throws ConsoleViewExcepton {
        if (ps != null) {
            ps.println(s);
        } else {
            throw new ConsoleViewExcepton( "PrintStream not initialized!" );
        }
    }

    public void println() throws ConsoleViewExcepton {
        if (ps != null) {
            ps.println();
        } else {
            throw new ConsoleViewExcepton( "PrintStream not initialized!" );
        }
    }
    
    public void print(String s) throws ConsoleViewExcepton {
        if (ps != null) {
            ps.print(s);
        } else {
            throw new ConsoleViewExcepton( "PrintStream not initialized!" );
        }
    }
}
