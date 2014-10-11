package ru.ncedu.vova.photos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

public class PhotosDAO {
    
    private Connection connection = null;
    private PreparedStatement getAllUsersStatement = null;
    private PreparedStatement getAllAlbumsStatement = null;
    private PreparedStatement getAllUserAlbumsStatement = null;
    private PreparedStatement getAllPhotosInAlbumStatement = null;
  
    private int prepareStatements() {
        if (connection != null) {
            try {
                // getAllUsersStatement = connection.createStatement();
                getAllUsersStatement = connection.prepareStatement(
                        "SELECT ID, NAME FROM Users");
                /*ResultSet rs; 
                rs = getAllUsersStatement.executeQuery();
                System.out.println("The ResultSet contains:");
                while (rs.next()) {
                    String name;
                    name = rs.getString(2);
                    //System.out.println(resultSet.getString("NAME"));
                    System.out.println(name);
                }*/
                getAllAlbumsStatement = connection.prepareStatement(
                        "SELECT ID, NAME, AUTHOR_ID FROM Albums");
                getAllUserAlbumsStatement = connection.prepareStatement(
                        "SELECT ID, NAME, AUTHOR_ID FROM Albums " +
                        "Where AUTHOR_ID = ?");
                getAllPhotosInAlbumStatement = connection.prepareStatement(
                        "SELECT * FROM Photos " +
                        "Where album_id = ?");
                return 1;
            } catch (SQLException e) {
                e.printStackTrace();
                return -1;
            }
        }
        return -1;
    }
    
    public boolean connectDB() {
        System.out.println("-------- Oracle JDBC Connection Testing ------");
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your Oracle JDBC Driver?");
            e.printStackTrace();
        }
        System.out.println("Oracle JDBC Driver Registered!");
        connection = null;
        try {
            connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@//192.168.17.132:1521/ORADB10G", "USERA",
                    "USERA");
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
        if (connection != null) {
            System.out.println("You made it, take control your database now!");
            prepareStatements();
            return true;
        } else {
            System.out.println("Failed to make connection!");
        }
        return false;
        
    }
    
    public void close() {
        System.out.println("Closing...");
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Closed correctly");
            } catch (SQLException e) {
                
            }
        }
        //System.out.println("Closed");
    }
    
    public List<Album> getAllAlbums() throws SQLException {
        List<Album> list = new LinkedList<Album>();
        if (connection != null && getAllAlbumsStatement != null) {
            ResultSet rs = getAllAlbumsStatement.executeQuery();
            while (rs.next()) {
                list.add( new Album(rs.getLong(1), rs.getLong(2), rs.getString(3)) );
            }
        }
        return list;
    }
    
    public List<User> getAllUsers() throws SQLException {
        List<User> list = new LinkedList<User>();
        if (connection != null && getAllUsersStatement != null) {
            ResultSet rs = getAllUsersStatement.executeQuery();
            while (rs.next()) {
                list.add( new User(rs.getLong(1), rs.getString(2)) );
            }
        }
        return list;
    }
    
    public List<Album> getAllUserAlbums(long user_id) throws SQLException {
        List<Album> albums = new LinkedList<Album>();
        if (connection != null && getAllUserAlbumsStatement != null) {
            getAllUserAlbumsStatement.setLong(1, user_id);
            ResultSet rs = getAllUserAlbumsStatement.executeQuery();
            while (rs.next()) {
                albums.add( new Album(rs.getLong(1), rs.getLong(2), rs.getString(3)) );
            }
        } else {
            throw new NullPointerException();
        }
        return albums;
    }
    
    public List<Photo> getAllPhotosInAlbum(long album_id) throws SQLException {
        List<Photo> photos = new LinkedList<Photo>();
        if (connection != null && getAllUserAlbumsStatement != null) {
            getAllUserAlbumsStatement.setLong(1, album_id);
            ResultSet rs = getAllPhotosInAlbumStatement.executeQuery();
            while (rs.next()) {
                photos.add( new Photo(
                                            rs.getString(1),
                                            rs.getLong(2),
                                            rs.getLong(3),
                                            rs.getLong(4),
                                            rs.getString(5),
                                            rs.getString(6),
                                            new byte[1]
                                        )
                            );
            }
        } else {
            throw new NullPointerException();
        }
        return photos;
    }
    
    public Photo getPhoto(BigInteger id) {
        
        return new Photo();
    }
    
    public List<Photo> getAllPhotos(BigInteger maxcount) {
        
        return new LinkedList<Photo>();
    }
    
    public User getUser(BigInteger id) {
        return new User();
    }
    
    public Album getAlbum (BigInteger id) {
        return new Album();
    }

}
