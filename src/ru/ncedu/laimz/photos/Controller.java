package ru.ncedu.laimz.photos;

import java.sql.SQLException;
import java.util.List;
import org.apache.commons.cli.GnuParser;

import org.apache.commons.cli.*;

class ControllerException extends Exception {
    private static final long serialVersionUID = 1L;
    ControllerException() { super(); }
    public ControllerException(String string) {super(string);}
    public ControllerException(String string, Exception e) {super(string, e);}
}

public class Controller {
    //private  static final Logger log = Logger.getLogger(Controller.class.getName());
    private Options options2 = new Options();
    //private cvanner cv;
    //private PrintStream cv;
    private ConsoleView cv = null;
    //private PhotosDAO pDAO;

    public Controller(ConsoleView cv/*, PhotosDAO pDAO*/) {
        if (cv != null) {
            this.cv = cv;
        } else {
            throw new NullPointerException("Trying to set ConsoleView as null");
        }
        options2.addOption("getuser",   "getuser", false, "Get some info about user");
        options2.addOption("getusers", "getusers", false, "Get some info (users, albums, photos)");
        options2.addOption("getalbums",  "getalbums", false, "User name");
        options2.addOption("getalbum", "getalbum", false, "Album name");
        options2.addOption("getphoto", "getphoto", false, "Photo name");
        options2.addOption("getphotos", "getphotos", false, "Photo name");
        options2.addOption("quit", "quit", false, "Exit program");
        options2.addOption("user", "user", true, "Specify user");
        options2.getOption("user").setValueSeparator('=');
        options2.addOption("id", "id", true, "Specify user id");
        options2.getOption("id").setValueSeparator('=');
        options2.addOption("album", "album", true, "Specify album");
        options2.getOption("album").setValueSeparator('=');
        options2.addOption("photo", "photo", true, "Specify photo");
        options2.getOption("photo").setValueSeparator('=');
    }
    
    public void connectDB(String address, String user, String password) throws DBException, ConsoleViewExcepton {
        cv.println("Connecting Database ...");
        PhotosDAO.connectDB(address, user, password);
    }
    
    public void close() {
        PhotosDAO.close();
    }
    
    //public void parseOneCommand(InputStream is, PhotosDAO pDAO) {
    /**
     * Parses one command from InputStream. When command was quit closes
     * connection PhotosDAO.close()) and returns true. Else returns false.
     * @return
     * @throws SQLException will be removed soon.
     * @throws DBException if connection was close suddenly or other error
     * occured.
     * @throws ConsoleViewExcepton 
     */
    public boolean parseOneCommand() throws SQLException, DBException, ConsoleViewExcepton  {
//        List<User> users;// = new LinkedList<User>();
//        List<Album> albums;// = new LinkedList<Album>();
//        List<Photo> photos;
        
        CommandLineParser clp = new GnuParser();
        CommandLine cl = null;
        
        try {
            String[] s = cv.nextLine().split(" ");
            for (int i = 0; i< s.length; i++) {
                s[i] = "-" + s[i];
            }
            /*for (String s2 : s) {
                cv.println("\"" + s2 + "\"");
            }*/
            cl = clp.parse(options2, s, true);
            
            //System.out.println(cl.getOptionValue("user"));
            
            if (cl.hasOption("getusers")) {
                List<User> users = UserHelper.getUsers();//PhotosDAO.getAllUsers();
                cv.print("Users: ");
                for (User a:users) {
                    cv.print(a.getName());
                    cv.print(", ");
                }
                cv.println();
            }
            
            
            if (cl.hasOption("getuser")) {
                //System.out.println("getuser");
                //name or id?
                if (cl.hasOption("id")) {
                    try{
                        cv.println("id=" + cl.getOptionValue("id"));
                        long id = Long.parseLong(cl.getOptionValue("id"));
                        //System.out.println(userName);
                        cv.println("id=" + id);
                        User user = UserHelper.getUserById(id);
                        if (user == null) {
                            cv.println("There is no user with id " + id + ".");
                        } else {
                            if (cl.hasOption("user") &&
                                    !user.getName().equals(cl.getOptionValue("user")))
                            {
                                cv.println("There is user with id " + id + 
                                        ", but his/her name is " + user.getName());
                            } else {
                                cv.println(user.toString());
                            }
                        }
                    } catch (NumberFormatException nfe) {
                        cv.println("Incorrect number format");
                    }
                } else if (cl.hasOption("user")) {
                    String userName = cl.getOptionValue("user");
                    //System.out.println(userName);
                    List<User> users = UserHelper.getUserByName(userName);
                    if (users.size() > 1) {
                        cv.println("There are more than one user with name " + userName + ".");
                        //TODO What next? May be, disallow to add users with same names?
                        cv.println("Try getuser id=<user_id>");
                    } else if (users.size() == 1) {
                        User user = users.iterator().next();
                        cv.println(user.toString());
                    } else {
                        cv.println("There is no user " + userName + ".");
                    }
                } else {
                    cv.println("Spicify user name (user=<user_name>) or id (id=<id>)");
                }
            }
            
            if (cl.hasOption("quit")) {
                close();
                return true;
            }
        } catch (ParseException e) {
            
        }
        return false;

        /*
         * try {
            String[] str = new String[1];
            str[0] = cv.nextLine();
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
