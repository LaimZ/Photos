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
public class FakeConsoleView extends ConsoleView {
    private PrintStream ps = null;
    private Scanner sc = null;
    private static String[] input = {"setuser user=Lena",
                                        "setalbum album=\"Lena's_Summer\"",
                                        "download"
    };
    int index = 0;
    private ConsoleView cv2;
    
    public FakeConsoleView(InputStream is, PrintStream ps, ConsoleView cv2) {
        super( is,  ps);
        this.ps = ps;
        this.sc = new Scanner(is);
        this.cv2 = cv2;
    }
    
    public String nextLine() throws ConsoleViewExcepton {
        if (index < input.length) {
            return input[index++];
        } else {
            return cv2.nextLine();
            //throw new ConsoleViewExcepton( "Scanner not initialized!" );
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
