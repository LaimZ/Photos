package ru.ncedu.laimz.photos;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Photos {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        boolean quit = false;
        Pattern p1 = Pattern.compile("(\\w+)");

        //PhotosDAO pDAO = new PhotosDAO();
        List<User> users;// = new LinkedList<User>();
        List<Album> albums;// = new LinkedList<Album>();
        List<Photo> photos;
        if (PhotosDAO.connectDB()) {
            try {
                users = PhotosDAO.getAllUsers();
                for (User a:users) {
                    System.out.println("User " + a.getName());
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                System.out.println("There is no connection!");
                e.printStackTrace();
            }
            CLI cli = new CLI(System.in, System.out);
            try{
                while(true) {
                    if ( cli.parseOneCommand() )
                        break;
                    
                }
            } catch (SQLException e) {
                
            }

            PhotosDAO.close();
        }
    }

}
