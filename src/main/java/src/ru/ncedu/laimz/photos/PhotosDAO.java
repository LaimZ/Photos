package ru.ncedu.laimz.photos;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.beans.Statement;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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
    private static PreparedStatement getUserAlbumByNameStatement = null;
    private static PreparedStatement getUserAlbumByIdStatement = null;

    private static PreparedStatement getAllPhotosInAlbumStatement = null;
    private static PreparedStatement getPhotoStatement = null;
    
    private static Random random;

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
                getAllAlbumsStatement = connection.prepareStatement(
                        "SELECT ID, NAME, AUTHOR_ID FROM Albums");
                getAllUserAlbumsStatement = connection.prepareStatement(
                        "SELECT ID, NAME, AUTHOR_ID FROM Albums " +
                        "Where AUTHOR_ID = ?");
                getUserAlbumByNameStatement = connection.prepareStatement(
                        "SELECT ID, NAME, AUTHOR_ID FROM Albums " +
                        "Where AUTHOR_ID = ? AND NAME = ?");
                getUserAlbumByIdStatement = connection.prepareStatement(
                        "SELECT ID, NAME, AUTHOR_ID FROM Albums " +
                        "Where AUTHOR_ID = ? AND ID = ?");
                // for you not to select album of another user

                getAllPhotosInAlbumStatement = connection.prepareStatement(
                        "SELECT * FROM Photos " +
                        "Where album_id = ?");
                getPhotoStatement = connection.prepareStatement(
                        "SELECT * FROM Photos " +
                        "Where id = ?");
                return 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return -1;
            }
        }
        return -1;
    }

    public static void addPhoto(long id, String name, long album_id, long author_id, String description, 
            String tags,  File file) throws DBException {
        if (connection != null && getAllUsersStatement != null) {
            try {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO Photos (ID, NAME, ALBUM_ID, AUTHOR_ID, DESCRIPTION, TAGS, DATA) " +
                        "VALUES (?,?,?,?,?,?,?)");
                ps.setLong  (1, id);
                ps.setString(2, name);
                ps.setLong  (3, album_id);
                ps.setLong  (4, author_id);
                ps.setString(5, description);
                ps.setString(6, tags);
                //java.io.InputStream bodyIn = new java.io.FileInputStream("1.txt");
                //java.io.File fileIn = new java.io.File("1.txt");
                java.io.InputStream is = new java.io.FileInputStream(file);
                ps.setBinaryStream(7, is, (int) file.length());
                int count = ps.executeUpdate();
                //System.out.println(count);
                is.close();
                ps.close();
            } catch (FileNotFoundException e1) {
                throw new DBException("File "+ file.getName()+" not found!", e1);
            } catch (IOException e2) {
                throw new DBException("IO Exception while working with "+ file.getName()+" .", e2);
            } catch (SQLException e3) {
                throw new DBException("SQLException while ", e3);
            }
        } else {
            throw new DBException("Connection is null or statement is not initialized in DAO");
        }
    }
    
    public static void addPhoto(Photo photo, File file) throws DBException {
        if (connection != null && getAllUsersStatement != null) {
            if (photo.getId() == 0) {
                if (random == null) {
                    random = new Random();
                }
                photo.setId(random.nextLong());
            }
            addPhoto(photo.getId(),
                    photo.getName(),
                    photo.getAlbumId(),
                    photo.getAuthorId(),
                    photo.getDescription(),
                    photo.getTags(),
                    file);
        } else {
            throw new DBException("Connection is null or statement is not initialized in DAO");
        }
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
            /*        
            /*        
            try {
                java.io.File file = new java.io.File("1.txt");
                java.io.PrintStream prstr;
                prstr = new java.io.PrintStream(file);
                prstr.print("This is 1.txt!");
                prstr.close();


                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO Photos (ID, NAME, DATA) VALUES (3,?,?)");
                ps.setString(1, "Name");
                java.io.InputStream bodyIn = 
                        new java.io.FileInputStream("1.txt");

                java.io.File fileIn = new java.io.File("1.txt");
                ps.setBinaryStream(2, bodyIn, (int) fileIn.length());
                int count = ps.executeUpdate();
                System.out.println(count);
                bodyIn.close();
                ps.close();




            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }*/

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

    public static boolean commit() {
        //PreparedStatement ps = connection.prepareStatement("commit;");
        try {
            connection.commit();
            return true;
        } catch (SQLException e1) {
            return false;
        }
    }

    public static boolean close() {
        //System.out.println("Closing...");
        if (connection != null) {
            try {
                connection.close();
                return true;
                //System.out.println("Closed correctly");
            } catch (SQLException e) {
                return false;
            }
        } else {
            return false;
        }
        //System.out.println("Closed");
    }

    public static List<Album> getAllAlbums() throws SQLException, DBException {
        List<Album> list = new LinkedList<Album>();
        if (connection != null && getAllAlbumsStatement != null) {
            ResultSet rs = getAllAlbumsStatement.executeQuery();
            while (rs.next()) {
                list.add( new Album(rs.getLong(1), rs.getString(2), rs.getLong(3)) );
            }
        } else {
            throw new DBException("Connection is null or statement is not initialized in DAO");
        }
        return list;
    }

    public static List<User> getAllUsers() throws SQLException, DBException {
        List<User> list = new LinkedList<User>();
        if (connection != null && getAllUsersStatement != null) {
            ResultSet rs = getAllUsersStatement.executeQuery();
            while (rs.next()) {
                list.add( new User(rs.getLong(1), rs.getString(2)) );
            }
        } else {
            throw new DBException("Connection is null or statement is not initialized in DAO");
        }
        return list;
    }

    public static List<User> getUserByName(String user_name) throws SQLException, DBException {
        List<User> list = new LinkedList<User>();
        if (connection != null && getUserByNameStatement != null) {
            getUserByNameStatement.setString(1, user_name);
            ResultSet rs = getUserByNameStatement.executeQuery();
            while (rs.next()) {
                list.add( new User(rs.getLong(1), rs.getString(2)) );
            }
        } else {
            throw new DBException("Connection is null or statement is not initialized in DAO");
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

    public static List<Album> getUserAlbums(long user_id) throws DBException {
        List<Album> albums = new LinkedList<Album>();
        if (connection != null && getAllUserAlbumsStatement != null) {
            try {
                getAllUserAlbumsStatement.setLong(1, user_id);
                ResultSet rs = getAllUserAlbumsStatement.executeQuery();
                while (rs.next()) {
                    albums.add( new Album(rs.getLong(1), rs.getString(2), rs.getLong(3)) );
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                throw new DBException("Exception while getting user albums");
            }
        } else {
            throw new DBException("Connection is null or statement is not initialized in DAO");
        }
        return albums;
    }

    public static List<Album> getUserAlbumByName(long user_id, String name) throws DBException {
        List<Album> albums = new LinkedList<Album>();
        if (connection != null && getUserAlbumByNameStatement != null) {
            try {
                getUserAlbumByNameStatement.setLong(1, user_id);
                getUserAlbumByNameStatement.setString(2, name);
                ResultSet rs = getUserAlbumByNameStatement.executeQuery();
                while (rs.next()) {
                    albums.add( new Album(rs.getLong(1), rs.getString(2), rs.getLong(3)) );
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                throw new DBException("Exception while getting user albums");
            }
        } else {
            throw new DBException("Connection is null or statement is not initialized in DAO");
        }
        return albums;
    }

    public static Album getUserAlbumById(long user_id, long id) throws DBException {
        if (connection != null && getUserAlbumByIdStatement != null) {
            try {
                getUserAlbumByIdStatement.setLong(1, user_id);
                getUserAlbumByIdStatement.setLong(2, id);
                ResultSet rs = getUserAlbumByIdStatement.executeQuery();
                if (rs.next()) {
                    return new Album(rs.getLong(1), rs.getString(2), rs.getLong(3));
                } else {
                    return null;
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                throw new DBException("Exception while getting user albums");
            }
        } else {
            throw new DBException("Connection is null or statement is not initialized in DAO");
        }
    }

    public static List<Photo> getAllPhotosInAlbum(long album_id) throws DBException {
        List<Photo> photos = new LinkedList<Photo>();
        if (connection != null && getAllPhotosInAlbumStatement != null) {
            try {
                getAllPhotosInAlbumStatement.setLong(1, album_id);
                ResultSet rs = getAllPhotosInAlbumStatement.executeQuery();
                while (rs.next()) {
                    Blob blob = rs.getBlob(7);
                    try {
                        if (blob.length() < 1024*1024*200) {
                            photos.add( new Photo(
                                    rs.getLong(1),//ID
                                    rs.getString(2),//Name
                                    rs.getLong(3),//ALBUM_ID
                                    rs.getLong(4),//AUTHOR_ID
                                    rs.getString(5),//DESCRIPTION
                                    rs.getString(6),//TAGS
                                    blob.getBytes(1, (int) blob.length())
                                    )
                                    );
                        }
                    } catch (NullPointerException e) {
                        //There is no blob
                    }
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                throw new DBException("Exception while getting user albums");
            }
        } else {
            throw new DBException("Connection is null or statement is not initialized in DAO");
        }
        return photos;
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
