package ru.ncedu.laimz.photos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import ru.ncedu.laimz.photos.model.Album;
import ru.ncedu.laimz.photos.model.Photo;
import ru.ncedu.laimz.photos.model.User;


public class PhotosDAO {

    private static Connection connection = null;
    private static PreparedStatement getAllUsersStatement = null;
    private static PreparedStatement getUserByIdStatement = null;
    private static PreparedStatement getUserByNameStatement = null;
    private static PreparedStatement getAllAlbumsStatement = null;
    private static PreparedStatement getAllUserAlbumsStatement = null;
    private static PreparedStatement getAllPhotosInAlbumStatement = null;

    private PhotosDAO() {}


    private static int prepareStatements() {
        if (connection != null) {
            try {
                // getAllUsersStatement = connection.createStatement();
                getAllUsersStatement = connection.prepareStatement(
                        "SELECT ID, NAME FROM Users");
                getUserByNameStatement = connection.prepareStatement(
                        "SELECT ID, NAME FROM Users WHERE NAME = ?");
                getUserByIdStatement = connection.prepareStatement(
                        "SELECT ID, NAME FROM Users WHERE ID = ?");
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
                return 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return -1;
            }
        }
        return -1;
    }

    public static void connectDB(String address, String user, String password) throws DBException {
        //System.out.println("-------- Oracle JDBC Connection Testing ------");
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new DBException("JDBC Driver not registered!");
        }
        connection = null;
        try {
            connection = DriverManager.getConnection(address, user, password);
            //                    "jdbc:oracle:thin:@//192.168.17.132:1521/ORADB10G", "USERA",
            //                    "USERA");
        } catch (SQLException e) {
            throw new DBException("Connection Failed!");
        }
        if (connection != null) {
            prepareStatements();
            //return true;
        } else {
            throw new DBException("Connection Failed!");
        }
        //return false;

    }

    public static void close() {
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

    public static List<Album> getAllAlbums() throws SQLException {
        List<Album> list = new LinkedList<Album>();
        if (connection != null && getAllAlbumsStatement != null) {
            ResultSet rs = getAllAlbumsStatement.executeQuery();
            while (rs.next()) {
                list.add( new Album(rs.getLong(1), rs.getString(2), rs.getLong(3)) );
            }
        }
        return list;
    }

    public static List<User> getAllUsers() throws SQLException {
        List<User> list = new LinkedList<User>();
        if (connection != null && getAllUsersStatement != null) {
            ResultSet rs = getAllUsersStatement.executeQuery();
            while (rs.next()) {
                list.add( new User(rs.getLong(1), rs.getString(2)) );
            }
        }
        return list;
    }

    public static List<User> getUserByName(String user_name) throws SQLException {
        List<User> list = new LinkedList<User>();
        if (connection != null && getUserByNameStatement != null) {
            getUserByNameStatement.setString(1, user_name);
            ResultSet rs = getUserByNameStatement.executeQuery();
            while (rs.next()) {
                list.add( new User(rs.getLong(1), rs.getString(2)) );
            }
        }
        return list;
    }
    
    public static User getUserById(long id) throws DBException {
        if (connection != null && getUserByIdStatement != null) {
            try {
                getUserByIdStatement.setLong(1, id);
                ResultSet rs = getUserByIdStatement.executeQuery();
                if (rs.next()) {
                    return new User(rs.getLong(1), rs.getString(2)) ;
                } else {
                    return null;
                }

            } catch (SQLException e) {
                throw new DBException("Exception while getting user by id", e);
            }
        } else {
            throw new DBException("Connection is null or statement is not initialized in DAO");
        }
    }
    
    public static List<Album> getAllUserAlbums(long user_id) throws SQLException {
        List<Album> albums = new LinkedList<Album>();
        if (connection != null && getAllUserAlbumsStatement != null) {
            getAllUserAlbumsStatement.setLong(1, user_id);
            ResultSet rs = getAllUserAlbumsStatement.executeQuery();
            while (rs.next()) {
                albums.add( new Album(rs.getLong(1), rs.getString(2), rs.getLong(3)) );
            }
        } else {
            throw new NullPointerException();
        }
        return albums;
    }

    public static List<Photo> getAllPhotosInAlbum(long album_id) throws SQLException {
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

    public static Photo getPhoto(BigInteger id) {

        return new Photo();
    }

    public static List<Photo> getAllPhotos(BigInteger maxcount) {

        return new LinkedList<Photo>();
    }

    public static User getUser(BigInteger id) {
        return new User();
    }

    public static Album getAlbum (BigInteger id) {
        return new Album();
    }

}
