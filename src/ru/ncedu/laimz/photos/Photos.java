package ru.ncedu.laimz.photos;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class Photos {

    /**
     * @param args
     */
    public static void main(String[] args) {
        /*List<User> users;// = new LinkedList<User>();
        List<Album> albums;// = new LinkedList<Album>();
        List<Photo> photos;*/

        Controller controller = new Controller(System.in, System.out);
        try {
            controller.connectDB(
                    "jdbc:oracle:thin:@//192.168.17.132:1521/ORADB10G", "USERA",
                    "USERA");
            System.out.println("Connected!");
        } catch (DBException e1) {
            // TODO Auto-generated catch block
            System.out.println("Failed to connect DB!");
            e1.printStackTrace();
        }

        try{
            while(true) {
                if ( controller.parseOneCommand() )
                    break;

            }
        } catch (SQLException | DBException e2) {
            e2.printStackTrace();
            controller.close();
        }
        //PhotosDAO.close();

    }

}
