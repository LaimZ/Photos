package ru.ncedu.laimz.photos;

import java.io.InputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.GnuParser;

import org.apache.commons.cli.*;

public class CLI {
    private  static final Logger log = Logger.getLogger(CLI.class.getName());
    private Options options2 = new Options();
    private Scanner sc;
    private PrintStream ps;
    //private PhotosDAO pDAO;

    public CLI(InputStream is, PrintStream ps/*, PhotosDAO pDAO*/) {
        sc = new Scanner(is);
        options2.addOption("getuser",   "getuser", false, "Get some info about user");
        options2.addOption("getusers", "getusers", false, "Get some info (users, albums, photos)");
        options2.addOption("getalbums",  "getalbums", false, "User name");
        options2.addOption("getalbum", "getalbum", false, "Album name");
        options2.addOption("getphoto", "getphoto", false, "Photo name");
        options2.addOption("getphotos", "getphotos", false, "Photo name");
        options2.addOption("quit", "quit", false, "Exit program");
        options2.addOption("user", "user", true, "Specify user");
        options2.getOption("user").setValueSeparator('=');
        options2.addOption("album", "album", true, "Specify album");
        options2.getOption("album").setValueSeparator('=');
        options2.addOption("photo", "photo", true, "Specify photo");
        options2.getOption("photo").setValueSeparator('=');
        //this.pDAO = pDAO;
        this.ps = ps;
    }
    
    //public void parseOneCommand(InputStream is, PhotosDAO pDAO) {
    public boolean parseOneCommand() throws SQLException  {
        //boolean quit = false;
        //Pattern p1 = Pattern.compile("(\\w+)");
        List<User> users;// = new LinkedList<User>();
        List<Album> albums;// = new LinkedList<Album>();
        List<Photo> photos;
        
        CommandLineParser clp = new GnuParser();
        CommandLine cl = null;
        
        try {
            String[] s = sc.nextLine().split(" ");
            for (int i = 0; i< s.length; i++) {
                s[i] = "-" + s[i];
            }
            for (String s2 : s) {
                ps.println("\"" + s2 + "\"");
            }
            cl = clp.parse(options2, s, true);
            
            //System.out.println(cl.getOptionValue("user"));
            
            if (cl.hasOption("getusers")) {
                users = PhotosDAO.getAllUsers();
                ps.print("Users: ");
                for (User a:users) {
                    ps.print(a.getName());
                    ps.print(", ");
                }
                ps.println();
            }
            
            if (cl.hasOption("getuser")) {
                //System.out.println("getuser");
                String userName = cl.getOptionValue("user");
                //System.out.println(userName);
                users = PhotosDAO.getAllUsers();
                User user = null;
                for (User a:users) {
                    if (a.getName().equals(userName)) {
                        user = a;
                        break;
                    }
                }
                if (user != null) {
                    ps.println(user.toString());
                } else {
                    ps.println("There is no user " + userName);
                }
            }
            
            if (cl.hasOption("quit")) {
                return true;
            }
        } catch (ParseException e) {
            
        }
        return false;

        /*
         * try {
            String[] str = new String[1];
            str[0] = sc.nextLine();
            System.out.println("str[0]="+str[0]);
                cl = clp.parse(options2, str);
                if (cl.hasOption("quit"))
                    return true;
                if (cl.hasOption("get")) {
                    System.out.println("get!");
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
                   if (cl.getOptions().length > 0) {
                       for (Option o:cl.getOptions()) {
                           System.out.println(o.getOpt());
                       }
                   }
                if (cl.hasOption("getall")) {
                    System.out.println(cl.getOptionValue("getall"));
                    if (cl.getOptionValue("getall").equals("users")) { //bring all users
                        //if (("users").equals(m.group(1))) {
                        System.out.println("Users:");
                        users = pDAO.getAllUsers();
                        for (User a:users) {
                                System.out.println(a.getName());
                        }
                    }//bring all users
                }*/
         
                
            
    }
}
