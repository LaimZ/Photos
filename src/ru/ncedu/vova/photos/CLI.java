package ru.ncedu.vova.photos;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.BasicParser;

import org.apache.commons.cli.*;

public class CLI {
    private  static final Logger log = Logger.getLogger(CLI.class.getName());
    private String[] args = null;
    private Options options1 = new Options();
    private Options options2 = new Options();
    private Scanner sc;
    private PhotosDAO pDAO;

    public CLI(InputStream is, PhotosDAO pDAO) {
        sc = new Scanner(is);
        options2.addOption("get",   "get", false, "Get some info (user, album, photo)");
        options2.addOption("getall", "getall", false, "Get some info (users, albums, photos)");
        options2.addOption("user",  "user", true, "User name");
        options2.addOption("album", "album", true, "Album name");
        options2.addOption("photo", "photo", true, "Photo name");
        options2.addOption("quit", "quit", true, "Exit program");
        this.pDAO = pDAO;
        //options.addOption("v", "var", true, "Here you can set parameter .");

    }
    
    //public void parseOneCommand(InputStream is, PhotosDAO pDAO) {
    public boolean parseOneCommand() throws SQLException  {
        //boolean quit = false;
        //Pattern p1 = Pattern.compile("(\\w+)");
        List<User> users;// = new LinkedList<User>();
        List<Album> albums;// = new LinkedList<Album>();
        List<Photo> photos;
        
        CommandLineParser clp = new BasicParser();
        CommandLine cl = null;
        

        try {
            String[] str = new String[1];
            str[0] = sc.nextLine();
            System.out.println("str[0]="+str[0]);
                cl = clp.parse(options2, str);
                if (cl.hasOption("quit"))
                    return true;
                if (cl.hasOption("get")) {
                    if (cl.hasOption("user")) { //bring user info
                        String s2 = cl.getOptionValue("user");
                        User user = null;
                        //if (("users").equals(m.group(1))) {
                        users = pDAO.getAllUsers();
                        for (User a:users) {
                            if (a.getName().equals(s2)) {
                                user = a;
                                break;
                            }
                        }
                        if (user != null) {
                            System.out.println(user.toString());
                        } else {
                            System.out.println("There is no such user");
                        }
                    }//bring user info

                    
                }
                    
                if (cl.hasOption("getall")) {
                    if (cl.hasOption("users")) { //bring all users
                        //if (("users").equals(m.group(1))) {
                        System.out.println("Users:");
                        users = pDAO.getAllUsers();
                        for (User a:users) {
                            if (a.getName().equals(s2)) {
                                System.out.println(a.getName());
                                break;
                            }
                        }
                    }//bring all users
                }
                
            if (cl.hasOption("getall")) {
                if (cl.hasOption("album") && (cl.hasOption("user"))) { //bring all users
                    //if (("users").equals(m.group(1))) {
                    System.out.println("Users:");
                    users = pDAO.getAllUsers();
                    for (User a:users) {
                        if (a.getName().equals(s2)) {
                            System.out.println(a.getName());
                            break;
                        }
                    }
                }//bring all users
            }
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
       return true;
    }
}
