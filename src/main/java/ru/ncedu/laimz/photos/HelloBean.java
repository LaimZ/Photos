package ru.ncedu.laimz.photos;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ru.ncedu.laimz.photos.controller.Controller;
import ru.ncedu.laimz.photos.view.ConsoleView;
import ru.ncedu.laimz.photos.view.ConsoleViewExcepton;
import ru.ncedu.laimz.photos.view.FakeConsoleView;
import ru.ncedu.laimz.photos.view.FakeConsoleViewJSF;

@ManagedBean//(name = "helloBean")
@SessionScoped
public class HelloBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void doSmth() {
		
	}
	
	static ConsoleView cv2 = new FakeConsoleViewJSF();
	static FakeConsoleViewJSF cvjsf = new FakeConsoleViewJSF();
	static ConsoleView cv = new FakeConsoleView(System.in, System.out, cv2);
	static Controller controller = new Controller(cv);

	static {
	        try {
	            controller.connectDB(
	                    "jdbc:oracle:thin:@//192.168.17.132:1521/ORADB10G", "USERA",
	                    "USERA");
	            System.out.println("Connected!");
	            
	            
	        } catch (DBException e1) {
	            // TODO Auto-generated catch block
	            System.out.println("Failed to connect DB!");
	            e1.printStackTrace();
	        } catch (ConsoleViewExcepton e) {
	            e.printStackTrace();
	        }
		
		
	}
	
	private String line;
	
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public void setLine(String line) {
		cvjsf.setString(line);
	}

	public String getLine() {
		/*try{
			//cvjsf.setString(value);
			controller.parseOneCommand();
			return cvjsf.getString();
		} catch (Exception e) {
			return "Error" + e.toString();
		}*/
		return "Line";
	}
	
}


