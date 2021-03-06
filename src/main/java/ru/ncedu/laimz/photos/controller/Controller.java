package ru.ncedu.laimz.photos.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.naming.InvalidNameException;

import org.apache.commons.cli.GnuParser;

import org.apache.commons.cli.*;

import ru.ncedu.laimz.photos.AlbumHelper;
import ru.ncedu.laimz.photos.DBException;
import ru.ncedu.laimz.photos.PhotoHelper;
import ru.ncedu.laimz.photos.PhotosDAO;
import ru.ncedu.laimz.photos.UserHelper;
import ru.ncedu.laimz.photos.model.Album;
import ru.ncedu.laimz.photos.model.Photo;
import ru.ncedu.laimz.photos.model.User;
import ru.ncedu.laimz.photos.view.ConsoleView;
import ru.ncedu.laimz.photos.view.ConsoleViewExcepton;



public class Controller {
    //private  static final Logger log = Logger.getLogger(Controller.class.getName());
    private Options options2 = new Options();
    //private cvanner cv;
    //private PrintStream cv;
    private ConsoleView cv = null;
    //private PhotosDAO pDAO;
    private User currentUser = null;
    private Album currentAlbum = null;

    public void close() {
        try {
            //cv.println("commiting and closing...");
            if (PhotosDAO.commit()) {
                cv.println("Successfully commited");
            } else {
                cv.println("Committing fail");
            }
            if (PhotosDAO.close()) {
                cv.println("Closed correctly");
            } else {
                cv.println("Closed with errors");
            }
        } catch (ConsoleViewExcepton e) {
            //e.printStackTrace();
        }
    }

    public Controller(ConsoleView cv/*, PhotosDAO pDAO*/) {
        if (cv != null) {
            this.cv = cv;
        } else {
            throw new NullPointerException("Trying to set ConsoleView as null");
        }
        options2.addOption("setuser",   "setuser", false, "Get some info about user");
        options2.addOption("getusers", "getusers", false, "Get some info (users, albums, photos)");
        options2.addOption("getalbums",  "getalbums", false, "User name");
        options2.addOption("setalbum", "setalbum", false, "Album name");
        //options2.addOption("getphoto", "getphoto", false, "Photo name");
        options2.addOption("getphotos", "getphotos", false, "Photo name");
        options2.addOption("quit", "quit", false, "Exit program");
        options2.addOption("download", "download", false, "Download album");
        options2.addOption("upload", "upload", false, "Upload photo");
        
        options2.addOption("file", "file", true, "Upload photo from file");
        options2.getOption("file").setValueSeparator('=');
        options2.addOption("name", "name", true, "Upload photo name");
        options2.getOption("name").setValueSeparator('=');
        options2.addOption("user", "user", true, "Specify user");
        options2.getOption("user").setValueSeparator('=');
        options2.addOption("id", "id", true, "Specify user or album id");
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


            if (cl.hasOption("setuser")) {
                //System.out.println("setuser");
                //name or id?
                currentUser = null;
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
                                currentUser = user;
                                cv.println("User = " + user.toString());
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
                        cv.println("Try setuser id=<user_id>");
                    } else if (users.size() == 1) {
                        User user = users.iterator().next();
                        currentUser = user;
                        cv.println(user.toString());
                    } else {
                        cv.println("There is no user " + userName + ".");
                    }
                } else {
                    cv.println("Spicify user name (user=<user_name>) or id (id=<id>)");
                }
                if (currentUser == null) {
                    currentAlbum = null;
                    cv.println("User and album unset");
                }
            }


            if (cl.hasOption("setalbum")) {
                if (currentUser == null) {
                    cv.println("Firstly set user by command setuser user=<user_name>");
                } else {
                    currentAlbum = null;
                    if (cl.hasOption("id")) {
                        try{
                            cv.println("id=" + cl.getOptionValue("id"));
                            long id = Long.parseLong(cl.getOptionValue("id"));
                            cv.println("id=" + id);
                            Album album = AlbumHelper.getUserAlbumById(currentUser.getId(), id);
                            if (album == null) {
                                cv.println("User " + currentUser.getName() + " has no album with id " + id + ".");
                            } else {
                                currentAlbum = album;
                                cv.println("Album set");
                            }
                        } catch (NumberFormatException nfe) {
                            cv.println("Incorrect number format");
                        }
                    } else if (cl.hasOption("album")) {
                        String albumName = cl.getOptionValue("album");
                        //System.out.println(userName);
                        List<Album> albums = AlbumHelper.getUserAlbumByName(currentUser.getId(), albumName);
                        if (albums.size() > 1) {
                            cv.println("User " + currentUser.getName() + 
                                    " has more than one album with name " + albumName + ".");
                            //TODO What next? May be, disallow to add users with same names?
                            cv.println("Try setalbum id=<album_id>");
                        } else if (albums.size() == 1) {
                            Album album = albums.iterator().next();
                            currentAlbum = album;
                            cv.println("Album set");
                        } else {
                            cv.println("User " + currentUser.getName() + 
                                    " has not got album with name " + albumName + ".");
                        }
                    } else {
                        cv.println("Spicify album name (album=<user_name>) or album id (id=<id>)");
                    }
                }

                if (currentAlbum == null) {
                    cv.println("Album unset");
                }
            }

            if (cl.hasOption("getalbums")) {
                if (currentUser == null) {
                    cv.println("Firstly set user by command setuser user=<user_name>");
                } else {
                    List<Album> albums = AlbumHelper.getUserAlbums(currentUser.getId());//PhotosDAO.getAllUserAlbums();
                    cv.print("User " + currentUser.getName() + " has albums: ");
                    for (Album a:albums) {
                        cv.print(a.getName());
                        cv.print(", ");
                    }
                    cv.println();
                }
            }

            if (cl.hasOption("getphotos")) {
                if (currentUser == null || currentAlbum == null) {
                    cv.println("Firstly specify user (setuser user=<user_name>) " +
                            "and album (setalbum album=<album_name>).");
                } else {
                    List<Photo> photos = PhotoHelper.getAllPhotosInAlbum(currentAlbum.getId());//PhotosDAO.getAllPhotosInAlbum();
                    cv.print("User " + currentUser.getName() + " has photos in album " +
                            currentAlbum.getName() + " : ");
                    for (Photo a:photos) {
                        cv.print(a.getName());
                        cv.print(", ");
                    }
                    cv.println();

                }
            }


            if (cl.hasOption("getphoto")) {
                if (currentUser == null || currentAlbum == null) {
                    cv.println("Firstly specify user (setuser user=<user_name>) " +
                            "and album (setalbum album=<album_name>).");
                } else {
                    List<Photo> photos = PhotoHelper.getAllPhotosInAlbum(currentAlbum.getId());//PhotosDAO.getAllPhotosInAlbum();
                    cv.print("User " + currentUser.getName() + " has photos in album " +
                            currentAlbum.getName() + " : ");
                    for (Photo a:photos) {
                        cv.print(a.getName());
                        cv.print(", ");
                    }
                    cv.println();

                }
            }
//
//            if (cl.hasOption("upload")) {
//                PhotosDAO.addPhoto(5, "Lena1", 4, 24, "Summer", "Lena, Summer", new File("Lena1.jpg"));
//                cv.println("Uploaded");
//            }
            
            if (cl.hasOption("download")) {
                if (currentUser!=null && currentAlbum!=null) {
                    List<Photo> photos = PhotoHelper.getAllPhotosInAlbum(currentAlbum.getId());
                    for (Photo p : photos) {
                        File userPath = new File (currentUser.getName());
                        //+ File.pathSeparator + currentAlbum.getName() + File.pathSeparator + p.getName()+ ".jpg");
                        if (userPath.exists()) {
                            if (!userPath.isDirectory()) {
                                //delete file and create directory
                                userPath.delete();
                                userPath.mkdir();
                            }
                        } else {
                            userPath.mkdir();
                        }
                        File albumPath = new File (currentUser.getName()+ File.separator+ currentAlbum.getName());
                        if (albumPath.exists()) {
                            if (!albumPath.isDirectory()) {
                                //delete file and create directory
                                albumPath.delete();
                                albumPath.mkdir();
                            }
                        } else {
                            albumPath.mkdir();
                        }
                        File ofile = new File(currentUser.getName()+ File.separator +
                                currentAlbum.getName() + File.separator +
                                p.getName()+ ".jpg");
                        if (ofile.exists()) {
                            ofile.delete();
                        }
                        try {
                            FileOutputStream ostream = new FileOutputStream(ofile);
                                ostream.write(p.getData());
                                ostream.close();
                        } catch (FileNotFoundException e) {
                            cv.println("Error while writing files!");
                        } catch (IOException e) {
                            cv.println("Error while writing files!");
                        }
                    }
                } else {
                    cv.println("Please, specify user and album!!!");
                }
            }

            if (cl.hasOption("upload")) {
                if (currentUser!=null && currentAlbum!=null) {
                    if (cl.hasOption("file") && cl.hasOption("name")) {
                        try{
                            PhotoHelper.UploadPhoto(currentUser,currentAlbum,
                                cl.getOptionValue("file"),
                                cl.getOptionValue("name"),
                                cl.getOptionValue("Description"),
                                cl.getOptionValue("tags")
                                );

                            PhotosDAO.commit();
                            cv.println("Uploaded");
                        } catch (FileNotFoundException fnfe) {
                            cv.println("File " + cl.getOptionValue("file") +
                                    " not found!");
                        }
                    } else {
                        cv.println("Not uploaded");
                    }
                    
                } else {
                    cv.println("Please, specify user and album!!!");
                }
            }

            if (cl.hasOption("quit")) {
                close();
                return true;
            }
        } catch (ParseException e) {
            cv.println("Incorrect syntax!");
        }
        return false;
    }
}
