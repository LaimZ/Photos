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
public class FakeConsoleViewJSF implements ConsoleView {

	private String value;
	
	public void setString(String s) {
		value = s;
	}
	
	public String getString() {
		return value;
	}
    
    public FakeConsoleViewJSF() {
    	super();
    }
    
    public String nextLine() throws ConsoleViewExcepton {
    	return value;
    }
    
    public void println(String s) throws ConsoleViewExcepton {
    	value = s;
    }

    public void println() throws ConsoleViewExcepton {
    	value = "";
    }
    
    public void print(String s) throws ConsoleViewExcepton {
    	value = s;
    }
}
