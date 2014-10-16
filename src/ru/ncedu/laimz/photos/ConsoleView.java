package ru.ncedu.laimz.photos;

import java.io.PrintStream;
import java.util.Scanner;

class ConsoleViewExcepton extends Exception {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    ConsoleViewExcepton() { super(); }
    ConsoleViewExcepton(String s) { super(s); }
    ConsoleViewExcepton(String s, Exception e) { super(s, e); }
}

/**
 * Class for communication with user via PrintStream and Scanner. Can be used
 * for CLI.
 * @author LaimZ
 *
 */
public class ConsoleView {
    private PrintStream ps;
    private Scanner sc;
    
    public ConsoleView(PrintStream ps, Scanner sc) {
        this.ps = ps;
        this.sc = sc;
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
}